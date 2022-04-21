<template>
    <div class="app-container">
        
        <div class="hello-user">
            <el-avatar :size="90" :src="userAvatar"/>
            <div class="hello-msg">
                <div class="call-user">
                    {{timeSectionString}}好，管理员 {{userName}}
                </div>
            </div>
        </div>

        <!-- 订单请求记录图表 -->
        <div class="count-record">
            <div class="top-tool">
                <div class="title">
                    订单请求
                </div>
                <div class="date-picker">
                    <el-date-picker
                            v-model="selectDate"
                            type="date"
                            placeholder="选择日期"
                            value-format="yyyy-MM-dd"
                            />
                </div>
            </div>
            <div class="data-chart" v-loading="isLoading">
                <ve-line :data="chartData" v-if="chartData.rows.length>0"/>
                <el-empty :image-size="200" v-if="chartData.rows.length==0 && !isLoading"/>
            </div>
        </div>
        
    </div>
</template>

<script>
import store from '@/store'
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
            //订单请求数据
            chartData: {
                columns:['时间', '订单请求数'],
                rows:[]
            },
            isLoading:true,
            selectDate:null,
            selectDate1:null,

            //用户简略信息
            userAvatar: store.getters.avatar,
            userName: store.getters.name,
            timeSectionString: ''
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
        var hour = new Date().getHours();
        if(hour >= 0 && hour < 12){
            this.timeSectionString = "早上";
        }else if(hour >= 12 && hour < 18){
            this.timeSectionString = "下午";
        }else if(hour >= 18 && hour < 24){
            this.timeSectionString = "晚上";
        }
    }
}
</script>

<style lang="scss" scoped>
    .hello-user {
        display: flex;
        border: 1px solid #d8d8d8;
        padding: 5vh 2vw;
        border-radius: 10px;
        box-shadow: 5px 5px #d6d6d6;

        .hello-msg {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            margin: 0 1vw;

            .call-user {
                display: inherit;
                align-items: center;
                font-weight: bold;
                font-size: larger;
                height: 50%;
            }
        }
    }

    .count-record, .pay-channel {
        margin: 5vh 0;
        border: 1px solid #d8d8d8;
        padding: 3vh 2vw;
        border-radius: 10px;
        box-shadow: 5px 5px #d6d6d6;

        .top-tool {
            display: flex;
            flex-direction: row;

            .title {
                display: flex;
                width: 50%;
                justify-content: flex-start;
                align-items: center;
                font-size: large;
                font-weight: bold;
                margin-top: 2vh;
            }

            .date-picker {
                display: flex;
                width: 50%;
                justify-content: flex-end;
                align-items: center;
            }
        }
        
    }
</style>