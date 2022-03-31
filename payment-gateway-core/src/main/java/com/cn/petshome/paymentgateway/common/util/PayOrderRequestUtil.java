package com.cn.petshome.paymentgateway.common.util;

import com.cn.petshome.paymentgateway.common.request.PayOrderRequest;
import com.cn.petshome.paymentgateway.po.OrderPayMethodDO;
import com.cn.petshome.paymentgateway.po.PayOrderDO;
import com.cn.petshome.paymentgateway.po.PayTxnJnlDO;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * 处理PayOrderRequest参数
 * @date 2022/1/24 21:17
 */
@Slf4j
public class PayOrderRequestUtil {

    private static final Long EXPIRE_LIMIT = 60000L * 15;

    /**
     *
     * 检验请求参数合法性
     * @return boolean
     * @author hjr
     * @date 2022/1/19 13:29
     */
    public static boolean requestExamine(PayOrderRequest request){
        log.info("进入交易合法性检验方法， 入参：{}", request);

        //初步处理请求参数
        BigDecimal total = new BigDecimal(0);
        boolean response = true;
        for (OrderPayMethodDO method:request.getPayMethods()){
            total = total.add(new BigDecimal(method.getAmount()));
        }
        if (total.compareTo(new BigDecimal(request.getTotalAmount())) != 0){
            log.error("请求参数错误，请核对交易金额：{} ！= {}", total, request.getTotalAmount());
            response = false;
        }

        log.info("交易合法性检验方法正常返回：{}", response);
        return response;
    }

    /**
     *
     * 根据请求参数初始化订单类以及支付方式列表，返回订单类
     * @param request 预下单请求vo
     * @param payOrderId 申请得到的订单号
     * @return {@link PayOrderDO}
     * @author hjr
     * @date 2022/1/20 11:10
     */
    public static PayOrderDO initAndGetPayOrder(PayOrderRequest request, String payOrderId){
        log.info("订单初始化方法， 入参：{}, {}", request, payOrderId);

        PayOrderDO initOrder = new PayOrderDO();

        // 其他方式支付金额
        Long otherPayAmount = request.getTotalAmount();

        // 订单支付类型列表初始化
        for (OrderPayMethodDO method:request.getPayMethods()){
            method.setPayOrderId(payOrderId);
            method.setRequestOrderId(request.getRequestOrderId());
            method.setPayVoucher("");
            method.setStatus(StatusEnum.PAY_METHOD_STATUS_INITIAL.getCode());
            if (PaymentKeyEnum.PAY_METHOD_CASH.getKeyCode().equals(method.getPayMethod())) {
                otherPayAmount -= method.getAmount();
            }
        }

        // 订单类初始化
        initOrder.setPayOrderId(payOrderId);
        initOrder.setUserId(request.getUserId());
        initOrder.setRequestOrderId(request.getRequestOrderId());
        initOrder.setRequestOrderType(request.getRequestOrderType());
        initOrder.setTotalAmount(request.getTotalAmount());
        initOrder.setActualPayAmt(request.getTotalAmount() - otherPayAmount);
        initOrder.setChannelType(request.getChannelType());
        initOrder.setPaymentType(request.getPaymentType());
        initOrder.setNotifyUrl(request.getNotifyUrl());
        initOrder.setSubject(request.getSubject());
        initOrder.setOrderAppendix(request.getOrderAppendix());

        initOrder.setOtherPayAmt(otherPayAmount);
        initOrder.setAuthCode("");
        initOrder.setPayData("");
        initOrder.setPayImageUrl("");
        initOrder.setStatus(StatusEnum.PAY_ORDER_STATUS_INITIAL.getCode());
        initOrder.setFinishTime("");
        initOrder.setNotifyTime("");
        initOrder.setChkBatNo("");
        initOrder.setChkSts(StatusEnum.CHK_STATUS.getCode());
        initOrder.setSettleDate("");
        initOrder.setNodeId("");
        initOrder.setRegionId("");

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date requestDate = new Date();
        initOrder.setRequestDate(requestDate);
        initOrder.setRequestTime(timeFormat.format(requestDate));
        initOrder.setTmpSmp(new SimpleDateFormat("yyyyMMddHHmmss").format(requestDate));

        String expireTime = timeFormat.format(new Date(requestDate.getTime() + EXPIRE_LIMIT));
        initOrder.setExpireTime(expireTime);

        log.info("初始化订单方法正常返回：{}", initOrder);
        return initOrder;
    }

    /**
     *
     * 根据请求参数vo返回支付方式列表
     * @param request 预下单请求vo
     * @return null
     * @author hjr
     * @date 2022/1/25 13:42
     */
    public static List<OrderPayMethodDO> getMethods(PayOrderRequest request){
        List<OrderPayMethodDO> methodList = request.getPayMethods();
        log.info("获取到支付方式列表：{}", methodList);
        return methodList;
    }

    /**
     *
     * 根据请求参数生成的订单po以及对应的支付方式po生成支付流水po
     * @param order 订单po
     * @param method 支付流水对应的支付方式po
     * @return {@link PayTxnJnlDO}
     * @author hjr
     * @date 2022/1/20 13:21
     */
    public static PayTxnJnlDO getTxnJnl(PayOrderDO order, OrderPayMethodDO method){
        log.info("进入生成支付流水方法：{}, {}", order, method);

        PayTxnJnlDO payTxnJnlDO = new PayTxnJnlDO();
        payTxnJnlDO.setUserId(order.getUserId());
        payTxnJnlDO.setPayOrderId(order.getPayOrderId());
        payTxnJnlDO.setRequestOrderId(order.getRequestOrderId());
        payTxnJnlDO.setRequestTxnJnl(NumberGeneratorUtil.getTxnNumbere());
        payTxnJnlDO.setResponseTxnJnl("");
        payTxnJnlDO.setPayMethod(method.getPayMethod());
        payTxnJnlDO.setRequestOrderType(order.getRequestOrderType());
        payTxnJnlDO.setNumber(method.getNumber());
        payTxnJnlDO.setAmount(method.getAmount());
        String txnType = PaymentKeyEnum.PAY_METHOD_CASH.getKeyCode().equals(method.getPayMethod())
                ? PaymentKeyEnum.TXN_TYPE_PRE_PLACE_ORDER.getKeyCode()
                : PaymentKeyEnum.TXN_TYPE_FROZEN.getKeyCode();
        payTxnJnlDO.setTxnType(txnType);
        payTxnJnlDO.setStatus(StatusEnum.PAY_TXN_JNL_STATUS_TIMEOUT.getCode());
        payTxnJnlDO.setTmpSmp(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        payTxnJnlDO.setNodeId("");
        payTxnJnlDO.setRegionId("");

        log.info("生成支付流水：{}", payTxnJnlDO);
        return payTxnJnlDO;
    }

    /**
     *
     * 通过支付方式类型从支付方式列表中获取对应支付方式vo
     * @param methodList 支付方式列表
     * @param payMethodType 支付方式类型
     * @return {@link OrderPayMethodDO}
     * @author hjr
     * @date 2022/1/27 14:19
     */
    public static OrderPayMethodDO getOrderPayMethod(List<OrderPayMethodDO> methodList, String payMethodType){
       log.info("进入获取对应支付方式方法：{}, {}", methodList, payMethodType);

        Object[] filterResult = methodList.stream()
                .filter(method -> payMethodType.equals(method.getPayMethod()))
                .filter(method -> method.getNumber() > 0)
                .toArray();
        OrderPayMethodDO method = null;
        if (filterResult.length > 0) {
            method = (OrderPayMethodDO) filterResult[0];
        }

        log.info("获取对应类型支付方式：{}", method);
        return method;
    }
}
