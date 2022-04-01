<template>
    <div class="app-container">
        <h3>订单请求记录</h3>
        <div class="data-chart" v-loading="isLoading">
            <el-date-picker
                v-model="selectDate"
                type="date"
                placeholder="选择日期"
                value-format="yyyy-MM-dd"
                />
            <ve-line :data="chartData" v-if="chartData.rows.length>0"/>
            <el-result icon="warning" title="暂无数据" v-if="chartData.rows.length==0 && !isLoading"/>
        </div>
    </div>
</template>

<script>
import {listPayOrderCount} from '@/api/payOrderCount/payOrderCount.js';
export default {
    data() {
        return {
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 20,
                recordDate: null,
                recordTime: null,
                count: null
            },
            chartData: {
                columns:['时间', '订单请求数'],
                rows:[]
            },
            isLoading:true,
            selectDate:null
        }
    },
    watch:{
        selectDate(val){
            this.requestPayOrderCount(val);
        }
    },
    methods: {
        requestPayOrderCount(date){
            this.isLoading = true;
            this.queryParams.recordDate = date;
            listPayOrderCount(this.queryParams).then(res=>{
                let recordData = [];
                for(let index = res.rows.length - 1; index >= 0; index--){
                    recordData.push({
                        '时间':res.rows[index].recordTime,
                        '订单请求数':res.rows[index].count
                    });
                };
                this.chartData.rows = recordData;
                this.isLoading = false;
            });
        },
        //获取当前格式化时间
        getNowTime() {
            var now = new Date();
            var year = now.getFullYear(); //得到年份
            var month = now.getMonth(); //得到月份
            var date = now.getDate(); //得到日期
            month = month + 1;
            month = month.toString().padStart(2, "0");
            date = date.toString().padStart(2, "0");
            var defaultDate = `${year}-${month}-${date}`;
            this.selectDate = defaultDate;
        }
    },
    created() {
        this.getNowTime();
        this.requestPayOrderCount(this.selectDate);
    }
}
</script>

<style scoped>
    
</style>