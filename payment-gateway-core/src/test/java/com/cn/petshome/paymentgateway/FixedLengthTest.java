package com.cn.petshome.paymentgateway;

import com.cn.petshome.paymentgateway.common.request.PayOrderRequest;
import com.cn.petshome.paymentgateway.common.response.PrePlacePayOrderResponse;
import com.cn.petshome.paymentgateway.common.response.ResponseBean;
import com.cn.petshome.paymentgateway.po.OrderPayMethodPO;
import com.cn.xidian.fixedLength.Resolver;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @date 2022/4/9 15:39
 */
public class FixedLengthTest {

    @Test
    public void fixedLengthTest() throws ClassNotFoundException, InvocationTargetException, IntrospectionException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException, InstantiationException {
        PayOrderRequest payOrderRequest = new PayOrderRequest();
        payOrderRequest.setRequestOrderId("RequestOrderId123456");
        payOrderRequest.setRequestOrderType("0");
        payOrderRequest.setOrderAppendix("OrderAppendix");
        payOrderRequest.setChannelType("1");
        payOrderRequest.setPaymentType("PaymentType");
        payOrderRequest.setSubject("Subject");
        payOrderRequest.setTotalAmount(1200L);
        payOrderRequest.setUserId("UserId123456");
        payOrderRequest.setNotifyUrl("notify");

        List<OrderPayMethodPO> list = new ArrayList<>();
        OrderPayMethodPO methodPo1 = new OrderPayMethodPO();
        methodPo1.setPayMethod("0");
        methodPo1.setPayVoucher("123456");
        methodPo1.setNumber(2);
        methodPo1.setAmount(1200L);
        list.add(methodPo1);

        OrderPayMethodPO methodPo2 = new OrderPayMethodPO();
        methodPo2.setPayMethod("1");
        methodPo2.setPayVoucher("123456");
        methodPo2.setNumber(2);
        methodPo2.setAmount(1200L);
        list.add(methodPo2);

        OrderPayMethodPO methodPo3 = new OrderPayMethodPO();
        methodPo3.setPayMethod("0");
        methodPo3.setPayVoucher("123456");
        methodPo3.setNumber(2);
        methodPo3.setAmount(1200L);
        list.add(methodPo3);

        PrePlacePayOrderResponse response = new PrePlacePayOrderResponse("PAYORDER_93120220409194435857", "<form name=\"punchout_form\" method=\"post\" action=\"https://openapi.alipaydev.com/gateway.do?charset=utf-8&method=alipay.trade.wap.pay&sign=BZ%2FnQ%2FXwt9sd2UbIAI2BpxJmnWs%2FseW1ub9rUtljgpzujIgNKM6JHbO7iQJcaZ4U1tXpe8Jdt7ROgU2luxhl4suXftJ4GgsBxqsWK7kV4%2BNq5eVL5l4Zcjs9wEmPHb7AQvwKEgpBL9dUNUyBlmqAtiNN8QWjD41n%2B%2BCmzLM2x%2BTdKPTbfn82aWIBSc31%2FMXDL2pNNe9GunnifzWrRWj5V2%2BzAHgWfST6npaub2e41bFkS4Rzzk400%2BizdQfgiYEw3wyDd0BxAEjej73wlqtHMPs6jUeANMOszi4DHq%2BR06WnBT5VxV8aA269gfqK97wbI4Cqfu8j5LjqWl5OTDTMxA%3D%3D&return_url=http%3A%2F%2Flocalhost%3A8080%2Fnotify%2Freturn&notify_url=http%3A%2F%2F124.90.39.130%3A8080%2Fnotify%2Falipay&version=1.0&app_id=2021000118650359&sign_type=RSA2&timestamp=2022-04-09+19%3A44%3A35&alipay_sdk=alipay-sdk-java-dynamicVersionNo&format=json\">\n" +
                "<input type=\"hidden\" name=\"biz_content\" value=\"{&quot;out_trade_no&quot;:&quot;PAYORDER_93120220409194435857&quot;,&quot;total_amount&quot;:&quot;12&quot;,&quot;subject&quot;:&quot;Subject&quot;,&quot;product_code&quot;:&quot;QUICK_WAP_PAY&quot;}\">\n" +
                "<input type=\"submit\" value=\"立即支付\" style=\"display:none\" >\n" +
                "</form>\n" +
                "<script>document.forms[0].submit();</script>");

        ResponseBean<PrePlacePayOrderResponse> bean = new ResponseBean<>();
        bean.setStatus(200);
        bean.setCode(0);
        bean.setData(response);
        bean.setMsg("msg");

        payOrderRequest.setPayMethods(list);
        System.out.println(new Resolver<>(ResponseBean.class).
                stringify(bean));
        System.out.println(new Resolver<>(PrePlacePayOrderResponse.class).
                stringify(response));
        System.out.println(new Resolver<>(PayOrderRequest.class).
                parse("UserId123456        RequestOrderId123456          01200     0PaymentType   notify                                                                                                                                                                                                                                                          Subject                                                         OrderAppendix                                                                                                                     30 123456              2        1200     1 123456              2        1200     0 123456              2        1200     "));
    }
}
