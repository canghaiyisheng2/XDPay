<template>
    <div class="app-container">
        <el-form ref="form" :model="payOrder" label-width="120px" label-position="left">
            <el-form-item label="请求订单编号" prop="requestOrderId">
                <el-input
                    v-model="payOrder.requestOrderId"
                    placeholder="请输入请求订单编号"
                />
            </el-form-item>
            <el-form-item label="用户ID" prop="userId">
                <el-input
                    v-model="payOrder.userId"
                    placeholder="请输入用户ID"
                />
            </el-form-item>
            <el-form-item label="订单总金额" prop="totalAmount">
                <el-input
                    v-model="payOrder.totalAmount"
                    placeholder="请输入订单总金额"
                />
            </el-form-item>
            <el-form-item label="订单类型" prop="requestOrderType">
                <el-select v-model="payOrder.requestOrderType" placeholder="请选择订单类型" clearable>
                    <el-option
                        v-for="dict in dict.type.request_order_type"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="支付渠道" prop="channelType">
                <el-select v-model="payOrder.channelType" placeholder="请选择支付渠道" clearable>
                    <el-option
                        v-for="dict in dict.type.channel_type"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="异步通知地址" prop="notifyUrl">
                <el-input
                    v-model="payOrder.notifyUrl"
                    placeholder="请输入异步通知地址"
                />
            </el-form-item>
            <el-form-item label="附加信息" prop="orderAppendix">
                <el-input
                    v-model="payOrder.orderAppendix"
                    placeholder="请输入附加信息"
                />
            </el-form-item>
            <el-form-item label="订单标题" prop="subject">
                <el-input
                    v-model="payOrder.subject"
                    placeholder="请输入订单标题"
                />
            </el-form-item>
            <el-form-item label="支付类型" prop="paymentType">
                <el-select v-model="payOrder.paymentType" placeholder="请选择支付类型" clearable>
                    <el-option
                        v-for="dict in dict.type.payment_type"
                        :key="dict.value"
                        :label="dict.label"
                        :value="dict.value"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="支付方式">
                <el-checkbox-group v-model="checkedMethods">
                <el-checkbox label="0" checked disabled>现金方式</el-checkbox>
                <el-input
                    v-model="cashAmount"
                    placeholder="请输入对应金额"
                />
                <el-checkbox label="1" @change="onCheckBoxChange(1)">积分方式</el-checkbox>
                <el-input
                    v-model="payOrder.payMethods[1].amount"
                    placeholder="请输入对应金额"
                    :disabled="!usePayMethods[1]"
                />
                <el-checkbox label="2" @change="onCheckBoxChange(2)">代金券方式</el-checkbox>
                <el-input
                    v-model="payOrder.payMethods[2].amount"
                    placeholder="请输入对应金额"
                    :disabled="!usePayMethods[2]"
                />
                </el-checkbox-group>
            </el-form-item>
            <el-form-item>
                <el-button type="danger" @click="goPay">确认支付</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import axios from 'axios'
import request from '@/utils/gatewayRequest'
export default {
    dicts: ['request_order_type', 'channel_type', 'payment_type'],
    data() {
        return {
            payOrder:{
                "requestOrderId": "payOrder_1234567",
                "userId": "userId_12345",
                "totalAmount": 110,
                "requestOrderType": "0",
                "channelType": "2",
                "notifyUrl": "",
                "orderAppendix": "",
                "subject": "",
                "paymentType":"",
                "payMethods": [{
                    "payMethod": 0,
                    "number": 1,
                    "amount": 110
                },{
                    "payMethod": 1,
                    "number": 0,
                    "amount": 0
                },{
                    "payMethod": 2,
                    "number": 0,
                    "amount": 0
                }]
            },
            checkedMethods:[],
            usePayMethods:[true, false, false],
            pointAmount:0,
            couponAmount:0,
        }
    },
    computed: {
        cashAmount(){
            return this.payOrder.totalAmount - this.payOrder.payMethods[1].amount - this.payOrder.payMethods[2].amount;
        }
    },
    watch: {
        cashAmount(val){
            this.payOrder.payMethods[0].amount =val;
            console.log(this.payOrder.payMethods)
        }
    },
    methods: {
        //发起支付
        goPay(){
           let _this = this;
           // axios({
           //     method: 'post',
            //    url: process.env.VUE_APP_BASE_API + '/gateway/cashRegister/goPay',
            //    data: _this.payOrder
           // }).then(res => {
           //     let win = window.open();
           //     win.document.write(res.data.data.returnForm);
           // });
          request({
                      url: '/cashRegister/goPay',
                      method: 'post',
                      data: _this.payOrder
                    }).then(res => {
                                      let win = window.open();
                                      win.document.write(res.data.returnForm);
                                  });
        },
        //点击复选框
        onCheckBoxChange(payMethod){
            this.usePayMethods[payMethod] = !this.usePayMethods[payMethod];
            this.payOrder.payMethods[payMethod].number = this.usePayMethods[payMethod]?1:0;
        }
    },
}
</script>

<style scoped>
    .button-class{
        display: flex;
        height: 100px;
        width: 100px;
        margin: 100px;
    }
</style>
