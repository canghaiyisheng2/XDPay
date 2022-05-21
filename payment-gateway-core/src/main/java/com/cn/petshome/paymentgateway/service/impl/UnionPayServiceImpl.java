package com.cn.petshome.paymentgateway.service.impl;

import com.alipay.api.internal.util.WebUtils;
import com.cn.petshome.paymentgateway.common.config.UnionPayResource;
import com.cn.petshome.paymentgateway.bo.NotifyInfo;
import com.cn.petshome.paymentgateway.common.exception.ApiException;
import com.cn.petshome.paymentgateway.common.util.RequestUtil;
import com.cn.petshome.paymentgateway.common.util.enums.PaymentTypeEnum;
import com.cn.petshome.paymentgateway.sdk.union.AcpService;
import com.cn.petshome.paymentgateway.service.UnionPayService;
import com.cn.petshome.paymentgateway.common.exception.NotifyException;
import com.cn.petshome.paymentgateway.sdk.union.SdkConfig;
import com.cn.petshome.paymentgateway.sdk.union.SdkConstants;
import com.cn.petshome.paymentgateway.po.PayOrderPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * 对接银联相关支付接口(手机网页支付)
 * @date 2022/1/17 14:53
 */
@Service("mobileH5")
@Slf4j
public class UnionPayServiceImpl implements UnionPayService {

    private final static String VERSION = "version";
    private final static String ENCODING = "encoding";
    private final static String SIGN_METHOD = "signMethod";
    private final static String TXN_TYPE = "txnType";
    private final static String TXN_SUB_TYPE = "txnSubType";
    private final static String BIZ_TYPE = "bizType";
    private final static String CHANNEL_TYPE = "channelType";
    private final static String MER_ID = "merId";
    private final static String ACCESS_TYPE = "accessType";
    private final static String ORDER_ID = "orderId";
    private final static String ORDER_DESC = "orderDesc";
    private final static String TXN_TIME = "txnTime";
    private final static String CURRENCY_CODE = "currencyCode";
    private final static String TXN_AMT = "txnAmt";
    private final static String FRONT_URL = "frontUrl";
    private final static String BACK_URL = "backUrl";

    @Autowired
    private UnionPayResource unionPayResource;

    static {
        //在SDK中载入银联配置文件acp_sdk.properties
        SdkConfig.getConfig().loadPropertiesFromSrc();
    }

    /**
     *
     * 跳转至支付宝支付网关
     * @param order 支付订单po
     * @return 支付网页表单
     * @author hjr
     * @date 2022/1/17 16:08
     */
    @Override
    public String goPay(PayOrderPO order) throws ApiException{
        log.info("进入银联下单方法，入参：{}", order);

        PaymentTypeEnum typeEnum = PaymentTypeEnum.getPaymentTypeByCode(order.getPaymentType());
        if (ObjectUtils.isEmpty(typeEnum)) {
            throw new ApiException("错误的支付方式参数");
        }
        String returnForm = "";
        String channelType = "";
        WebUtils.setNeedCheckServerTrusted(false);
        switch (typeEnum){
            case PAY_TYPE_PC: channelType = "07";break;
            case PAY_TYPE_MOBILE: channelType = "08";break;
            case PAY_TYPE_NATIVE: return goNative(order);
            default: throw new ApiException("错误的支付方式参数");
        }

        Map<String, String> requestData = new HashMap<>(20);
        //版本号，全渠道默认值
        requestData.put(VERSION, unionPayResource.getVersion());
        //字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put(ENCODING, UnionPayResource.encoding_UTF8);
        //签名方法
        requestData.put(SIGN_METHOD, SdkConfig.getConfig().getSignMethod());
        //交易类型 ，01：消费
        requestData.put(TXN_TYPE, "01");
        //交易子类型， 01：自助消费
        requestData.put(TXN_SUB_TYPE, "01");
        //业务类型，B2C网关支付，手机wap支付
        requestData.put(BIZ_TYPE, "000201");
        //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
        requestData.put(CHANNEL_TYPE, channelType);
        //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
        requestData.put(MER_ID, unionPayResource.getMerchantId());
        //接入类型，0：直连商户
        requestData.put(ACCESS_TYPE, "0");
        //商户订单号，8-40位数字字母，不能含“-”或“_”，因此订单号去掉“PAYORDER_”
        requestData.put(ORDER_ID, order.getPayOrderId().split("_")[1]);
        //订单描述
        requestData.put(ORDER_DESC, order.getSubject());
        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        requestData.put(TXN_TIME, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        //交易币种（境内商户一般是156 人民币）
        requestData.put(CURRENCY_CODE, "156");
        //交易金额，单位分，不要带小数点
        requestData.put(TXN_AMT, order.getActualPayAmt().toString());
        //前台通知地址
        requestData.put(FRONT_URL, unionPayResource.getFrontUrl());
        //后台通知地址
        requestData.put(BACK_URL, unionPayResource.getBackUrl());
        //加签，报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> submitFromData = AcpService.sign(requestData,UnionPayResource.encoding_UTF8);
        //获取请求银联的前台地址
        String requestFrontUrl = unionPayResource.getFrontTransUrl();

        //生成自动跳转的Html表单
        String htmlForm = AcpService.createAutoFormHtml(requestFrontUrl, submitFromData,UnionPayResource.encoding_UTF8);

        if (StringUtils.hasLength(htmlForm)){
            log.info("银联下单完成，返回：{}", htmlForm);
        }else {
            log.error("银联下单失败，返回空字符串");
        }

        return htmlForm;
    }

    /**
     * 扫码支付
     * @param order 支付订单
     * @return 返回表单
     * @author hjr
     * @date 2022/5/20 18:45
     */
    private String goNative(PayOrderPO order) {
        Map<String, String> requestData = new HashMap<>(20);
        //版本号，全渠道默认值
        requestData.put(VERSION, unionPayResource.getVersion());
        //字符集编码，可以使用UTF-8,GBK两种方式
        requestData.put(ENCODING, UnionPayResource.encoding_UTF8);
        //签名方法
        requestData.put(SIGN_METHOD, SdkConfig.getConfig().getSignMethod());
        //交易类型 ，01：消费
        requestData.put(TXN_TYPE, "01");
        //交易子类型， 07：申请消费二维码
        requestData.put(TXN_SUB_TYPE, "07");
        //业务类型，B2C网关支付，手机wap支付
        requestData.put(BIZ_TYPE, "000000");
        //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机
        requestData.put(CHANNEL_TYPE, "08");
        //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
        requestData.put(MER_ID, unionPayResource.getMerchantId());
        //接入类型，0：直连商户
        requestData.put(ACCESS_TYPE, "0");
        //商户订单号，8-40位数字字母，不能含“-”或“_”，因此订单号去掉“PAYORDER_”
        requestData.put(ORDER_ID, order.getPayOrderId().split("_")[1]);
        //订单描述
        requestData.put(ORDER_DESC, order.getSubject());
        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
        requestData.put(TXN_TIME, new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        //交易币种（境内商户一般是156 人民币）
        requestData.put(CURRENCY_CODE, "156");
        //交易金额，单位分，不要带小数点
        requestData.put(TXN_AMT, order.getActualPayAmt().toString());
        //前台通知地址
        requestData.put(FRONT_URL, unionPayResource.getFrontUrl());
        //后台通知地址
        requestData.put(BACK_URL, unionPayResource.getBackUrl());
        //加签，报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
        Map<String, String> submitFromData = AcpService.sign(requestData,UnionPayResource.encoding_UTF8);
        //获取请求银联的前台地址
        String requestFrontUrl = unionPayResource.getAppTransUrl();

        //接收返回参数
        Map<String, String> rspData = AcpService.post(submitFromData,requestFrontUrl,UnionPayResource.encoding_UTF8);
        String qrCode = rspData.get("qrCode");
        if (!StringUtils.hasLength(qrCode)){
            throw new ApiException("银联下单失败，返回空字符串");
        }
        String htmlForm = qrCode;

        return htmlForm;
    }

    /**
     *
     * 处理银联的异步通知
     * @param request 来自银联的异步通知
     * @return {@link NotifyInfo}
     * @author hjr
     * @date 2022/1/17 16:09
     */
    @Override
    public NotifyInfo notifyHandle(HttpServletRequest request) {
        log.info("进入银联异步通知处理方法，入参：{}", request);

        Map<String, String> params = RequestUtil.getAllRequestParams(request);
        String encoding = params.get(SdkConstants.PARAM_ENCODING);
        if (!AcpService.validate(params, encoding)) {
            throw new NotifyException("验证签名结果[失败].");
        } else {
            log.info("验证签名结果[成功].");
        }
        //此处转化回系统订单号，加上“PAYORDER_”
        String payOrderId = "PAYORDER_" + params.get("orderId");
        String respCode = params.get("respCode");
        return new NotifyInfo(payOrderId,
                "00".equals(respCode)? NotifyInfo.TRADE_SUCCESS: NotifyInfo.TRADE_FAIL);
    }
}
