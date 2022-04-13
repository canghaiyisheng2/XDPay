package com.cn.xidian.fixedLength;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * 定长报文处理工具类
 * @date 2022/4/7 16:06
 */
public class FixedLengthUtil {

    public static final String[] IGNORED_FIELDS = {"serialVersionUID"};
    public static final int CYCLE_COUNT_LENGTH = 3;

    /**
     *
     * 向左填充0
     * @param originString 原字符串
     * @param paddingLength 填充后长度
     * @return 填充后字符串
     * @author hjr
     * @date 2022/4/7 16:12
     */
    public static String leftPadding(String originString, int paddingLength){

        if (paddingLength <= 0){
            return originString;
        }

        if (originString == null){
            originString = "";
        }

        paddingLength = paddingLength - originString.length();
        if (paddingLength < 0){
            throw new FixedLengthResolveException("报文长度错误");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paddingLength; i++) {
            sb.append(' ');
        }
        sb.append(originString);
        return sb.toString();
    }

    /**
     *
     * 向右填充0
     * @param originString 原字符串
     * @param paddingLength 填充后长度
     * @return 填充后字符串
     * @author hjr
     * @date 2022/4/7 16:12
     */
    public static String rightPadding(String originString, int paddingLength){

        if (paddingLength <= 0){
            return originString;
        }

        if (originString == null){
            originString = "";
        }

        paddingLength = paddingLength - originString.length();
        if (paddingLength < 0){
            throw new FixedLengthResolveException("报文：" + originString + "长度错误");
        }

        StringBuilder sb = new StringBuilder(originString);
        for (int i = 0; i < paddingLength; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }


    /**
     *
     * 根据类生成定长报文解析规则
     * @param clazz 待解析类
     * @return 解析完成后规则
     * @author hjr
     * @date 2022/4/7 16:38
     */
    public static List<FixedLengthPiece> resolveClass(Class<?> clazz){

        Field[] fields = clazz.getDeclaredFields();
        List<FixedLengthPiece> pieceList = new ArrayList<>();
        for (Field field : fields){
            FixedLengthPiece piece = new FixedLengthPiece();

            //判断字段是否忽略
            ResolveIgnore resolveIgnore = field.getAnnotation(ResolveIgnore.class);
            if (resolveIgnore != null && resolveIgnore.value()){
                continue;
            }

            piece.setCycle(field.getType().isAssignableFrom(List.class) && List.class.isAssignableFrom(field.getType()));

            if (piece.isCycle()){
                //循环字段处理
                ParameterizedType listType = (ParameterizedType)field.getGenericType();
                piece.setCycleFieldName(field.getName());
                piece.setCycleField(resolveClass((Class<?>) listType.getActualTypeArguments()[0]));
            }else {
                //非循环字段处理
                FixedLength fixedLengthAnno = field.getAnnotation(FixedLength.class);
                if (fixedLengthAnno == null){
                    throw new FixedLengthResolveException(field.getName() +
                            "字段固定长度未定义，请使用FixedLength注解定义");
                }
                int length = fixedLengthAnno.value();
                if (length == 0){
                    throw new FixedLengthResolveException(field.getName() +
                            "字段固定长度未定义，请使用FixedLength注解定义");
                }
                piece.setSinglePiece(new FixedLengthField(field.getName(),
                        length, field.getType().toString().split(" ")[1]));
            }

            pieceList.add(piece);
        }
        return pieceList;
    }

    /**
     *
     * 将字符串转换成指定类型
     * @param parseString 待转换字符串
     * @param clazz 转换后的类型
     * @return 转换后的值
     * @author hjr
     * @date 2022/4/9 12:33
     */
    public static <T> T parseToClass(String parseString, Class<T> clazz) {
        T value = null;
        try {
            Constructor<T> constructor = clazz.getConstructor(String.class);
            constructor.setAccessible(true);
            value = constructor.newInstance(parseString.trim());
        }catch (NoSuchMethodException | IllegalAccessException |
                InvocationTargetException | InstantiationException e){
            throw new FixedLengthResolveException("将字符串转换成指定类型时发生错误",e);
        }
        return value;
    }

    /**
     *
     * 根据定长解析片段规则解析对象
     * @param piece 解析规则片段
     * @param obj 待解析对象
     * @return 解析后得到的字符串
     * @author hjr
     * @date 2022/4/9 12:34
     */
    public static String stringifyPieceInObject(FixedLengthPiece piece, Object obj) {
        try {
            if (piece.isCycle()){
                List<FixedLengthPiece> pieceList = piece.getCycleField();
                PropertyDescriptor pd = new PropertyDescriptor(piece.getCycleFieldName(), obj.getClass());
                Method getter = pd.getReadMethod();
                List<?> cycleList = (List<?>) getter.invoke(obj);
                StringBuilder sb = new StringBuilder();
                sb.append(leftPadding(String.valueOf(cycleList.size()), CYCLE_COUNT_LENGTH));
                for (Object cycleObj : cycleList){
                    for (FixedLengthPiece cyclePiece : pieceList){
                        sb.append(stringifyPieceInObject(cyclePiece, cycleObj));
                    }
                }
                return sb.toString();
            }else {
                int length = piece.getSinglePiece().getFieldLength();
                PropertyDescriptor pd = new PropertyDescriptor(piece.getSinglePiece().getFieldName(), obj.getClass());
                Method getter = pd.getReadMethod();
                Object pieceObj = getter.invoke(obj);
                if (pieceObj == null){
                    Nullable nullableAnno = obj.getClass().getDeclaredField(piece.getSinglePiece().getFieldName())
                            .getAnnotation(Nullable.class);
                    if (nullableAnno.value()){
                        pieceObj = "";
                    }else {
                        throw new FixedLengthResolveException(piece.getSinglePiece().getFieldName()
                                + "字段不得为空");
                    }
                }
                String pieceString = null;
                if(pieceObj instanceof String || pieceObj instanceof Date ||
                        pieceObj instanceof Number){
                    pieceString = pieceObj.toString();
                }else {
                    try {
                        Method method = pieceObj.getClass().getMethod("toFixedLengthString");
                        pieceString = (String) method.invoke(pieceObj);
                    } catch (NoSuchMethodException e) {
                        throw new FixedLengthResolveException(piece.getSinglePiece().getFieldName() + "缺少toFixedLengthString");
                    }
                }
                return rightPadding(pieceString, length);
            }
        }catch (IntrospectionException | InvocationTargetException |
                IllegalAccessException | NoSuchFieldException e){
            throw new FixedLengthResolveException("实体转换为定长报文时发生错误", e);
        }
    }

    public static int index = 0;

    /**
     *
     * 将定长报文转换为对应的对象
     * @param fixedLengthString 定长报文
     * @param rules 定长报文规则
     * @param tClass T的Class
     * @return 定长报文转换后的T实例对象
     * @author hjr
     * @date 2022/4/12 11:10
     */
    public static <T> T parseToObject(String fixedLengthString, List<FixedLengthPiece> rules, Class<T> tClass)  {

        //实例化T
        T resultObject;
        try {
            resultObject = tClass.newInstance();

            for (FixedLengthPiece piece : rules){
                if (piece.isCycle()){
                    //循环片段处理
                    int count = Integer.parseInt(fixedLengthString.substring(index, index + CYCLE_COUNT_LENGTH).trim());
                    index += CYCLE_COUNT_LENGTH;

                    //获取循环列表范型类型
                    ParameterizedType cycleType = (ParameterizedType)tClass.getDeclaredField(piece.getCycleFieldName()).getGenericType();
                    Class<?> cycleClass = (Class<?>) cycleType.getActualTypeArguments()[0];
                    List<Object> list = new ArrayList<>();

                    for (int i = 0; i < count; i++) {
                        list.add(parseToObject(fixedLengthString, piece.getCycleField(), cycleClass));
                    }
                    PropertyDescriptor pd = new PropertyDescriptor(piece.getCycleFieldName(), tClass);
                    Method setter = pd.getWriteMethod();
                    setter.invoke(resultObject, list);
                }else {
                    //非循环片段处理
                    PropertyDescriptor pd = new PropertyDescriptor(piece.getSinglePiece().getFieldName(), tClass);
                    Method setter = pd.getWriteMethod();
                    Class<?> fieldClass = Class.forName(piece.getSinglePiece().getFieldType());
                    int length = piece.getSinglePiece().getFieldLength();
                    setter.invoke(resultObject,
                            FixedLengthUtil.parseToClass(fixedLengthString.substring(index, index + length), fieldClass));
                    index+=length;
                }
            }
        }catch (IllegalAccessException | InstantiationException |
                IntrospectionException | ClassNotFoundException |
                InvocationTargetException | NoSuchFieldException e){
            throw new FixedLengthResolveException("定长报文转换为对象时发生错误", e);
        }

        return resultObject;
    }

}
