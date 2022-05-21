<template>
    <div>
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <span>系统设置</span>
            </div>
            <el-form class="form-class">
                <el-form-item label="使用积分">
                    <el-tooltip :content="usePointLabel" placement="top">
                        <el-switch
                            v-model="usePoint"
                            active-color="#13ce66"
                            inactive-color="#ff4949"
                            @change="togglePoint">
                        </el-switch>
                    </el-tooltip>
                </el-form-item>
                <el-form-item label="使用代金券">
                    <el-tooltip :content="useCouponLabel" placement="top">
                        <el-switch
                            v-model="useCoupon"
                            active-color="#13ce66"
                            inactive-color="#ff4949"
                            @change="toggleCoupon">
                        </el-switch>
                    </el-tooltip>
                </el-form-item>
            </el-form>
        </el-card>
    </div>
</template>

<script>
import request from '@/utils/gatewayRequest'
export default {
    data() {
        return {
            usePoint: true,
            useCoupon: false
        }
    },
    computed:{
        usePointLabel(){
            return this.usePoint?'是':'否';
        },
        useCouponLabel(){
            return this.useCoupon?'是':'否';
        }
    },
    methods: {
        togglePoint(){
            request({
                url: '/common/togglePoint',
                method: 'get',
            }).then(res => {
                _this.usePoint = res.data;
            });
        },

        toggleCoupon(){
            request({
                url: '/common/toggleCoupon',
                method: 'get',
            }).then(res => {
                _this.useCoupon = res.data;
            });
        }
    },
    created(){
        let _this = this;
        request({
            url: '/common/isUsePoint',
            method: 'get',
        }).then(res => {
            _this.usePoint = res.data;
        })
        request({
            url: '/common/isUseCoupon',
            method: 'get',
        }).then(res => {
            _this.useCoupon = res.data;
        })
    }
}
</script>

<style lang="scss" scoped>
    .box-card{
        margin: 1% 2%;

        .form-class{
            padding: 1% 1%;
        }
    }
    
</style>