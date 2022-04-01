<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px" label-position="left">
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付订单编号" prop="payOrderId">
        <el-input
          v-model="queryParams.payOrderId"
          placeholder="请输入支付订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="请求订单编号" prop="requestOrderId">
        <el-input
          v-model="queryParams.requestOrderId"
          placeholder="请输入请求订单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="订单类型" prop="requestOrderType">
        <el-select v-model="queryParams.requestOrderType" placeholder="请选择订单类型" clearable>
          <el-option
            v-for="dict in dict.type.request_order_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="支付渠道" prop="channelType">
        <el-select v-model="queryParams.channelType" placeholder="请选择支付渠道" clearable>
          <el-option
            v-for="dict in dict.type.channel_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="支付类型" prop="paymentType">
        <el-select v-model="queryParams.paymentType" placeholder="请选择支付类型" clearable>
          <el-option
            v-for="dict in dict.type.payment_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="请求支付时间" prop="requestDate">
        <el-date-picker
          v-model="dateTimeRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          clearable
          @change="changeTimeRange">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="交易状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择交易状态" clearable>
          <el-option
            v-for="dict in dict.type.pay_order_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <br/>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['payOrder:payOrder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="payOrderList" @selection-change="handleSelectionChange">
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="支付订单编号" align="center" prop="payOrderId" />
      <el-table-column label="请求订单编号" align="center" prop="requestOrderId" />
      <el-table-column label="订单类型" align="center" prop="requestOrderType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.request_order_type" :value="scope.row.requestOrderType"/>
        </template>
      </el-table-column>
      <el-table-column label="订单总金额" align="center" prop="totalAmount" />
      <el-table-column label="现金支付总金额" align="center" prop="actualPayAmt" />
      <el-table-column label="其他方式支付金额" align="center" prop="otherPayAmt" />
      <el-table-column label="附加信息" align="center" prop="orderAppendix" />
      <el-table-column label="订单标题" align="center" prop="subject" />
      <el-table-column label="失效时间" align="center" prop="expireTime" />
      <el-table-column label="支付渠道" align="center" prop="channelType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.channel_type" :value="scope.row.channelType"/>
        </template>
      </el-table-column>
      <el-table-column label="支付类型" align="center" prop="paymentType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.payment_type" :value="scope.row.paymentType"/>
        </template>
      </el-table-column>
      <el-table-column label="交易状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.pay_order_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="请求支付日期" align="center" prop="requestDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.requestDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请求支付时间" align="center" prop="requestTime" />
      <el-table-column label="结束日期" align="center" prop="finishDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.finishDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="finishTime" />
      <el-table-column label="请求方异步通知地址" align="center" prop="notifyUrl" />
      <el-table-column label="请求方异步通知日期" align="center" prop="notifyDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.notifyDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="请求方异步通知时间" align="center" prop="notifyTime" />
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listPayOrder, getPayOrder, delPayOrder, addPayOrder, updatePayOrder } from "@/api/payOrder/payOrder";

export default {
  name: "PayOrder",
  dicts: ['request_order_type', 'pay_order_status', 'channel_type', 'payment_type'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 支付订单表格数据
      payOrderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //选择的请求时间范围
      dateTimeRange:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        payOrderId: null,
        requestOrderId: null,
        requestOrderType: null,
        totalAmount: null,
        actualPayAmt: null,
        otherPayAmt: null,
        authCode: null,
        payData: null,
        payImageUrl: null,
        orderAppendix: null,
        subject: null,
        expireTime: null,
        channelType: null,
        paymentType: null,
        status: null,
        requestDate: null,
        requestTime: null,
        finishDate: null,
        finishTime: null,
        notifyUrl: null,
        notifyDate: null,
        notifyTime: null,
        chkSts: null,
        chkBatNo: null,
        settleDate: null,
        tmpSmp: null,
        nodeId: null,
        regionId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        payOrderId: [
          { required: true, message: "支付订单编号不能为空", trigger: "blur" }
        ],
        requestOrderId: [
          { required: true, message: "请求流水不能为空", trigger: "blur" }
        ],
        requestOrderType: [
          { required: true, message: "订单类型  0-用品订单  1-服务订单不能为空", trigger: "change" }
        ],
        authCode: [
          { required: true, message: "授权码，条码支付的授权码不能为空", trigger: "blur" }
        ],
        payData: [
          { required: true, message: "支付数据域：1.扫码支付时，返回的支付URL数据域；2.JSAPI支付时为支付机构的payInfo字段不能为空", trigger: "blur" }
        ],
        payImageUrl: [
          { required: true, message: "支付图片URL：条码支付时，包含支付链接二维码图片的链接，考虑兼容银联支付不能为空", trigger: "blur" }
        ],
        orderAppendix: [
          { required: true, message: "附加信息,请求上送后,需要原样返回的信息不能为空", trigger: "blur" }
        ],
        subject: [
          { required: true, message: "订单标题不能为空", trigger: "blur" }
        ],
        expireTime: [
          { required: true, message: "失效时间不能为空", trigger: "blur" }
        ],
        channelType: [
          { required: true, message: "支付渠道  0微信  1支付宝不能为空", trigger: "change" }
        ],
        paymentType: [
          { required: true, message: "支付类型不能为空", trigger: "change" }
        ],
        status: [
          { required: true, message: "交易状态  0初始登记  1下单成功  2支付成功    不能为空", trigger: "blur" }
        ],
        requestTime: [
          { required: true, message: "请求支付时间不能为空", trigger: "blur" }
        ],
        finishTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
        notifyUrl: [
          { required: true, message: "请求方异步通知地址不能为空", trigger: "blur" }
        ],
        notifyTime: [
          { required: true, message: "请求方异步通知时间不能为空", trigger: "blur" }
        ],
        chkSts: [
          { required: true, message: "与账务方对账状态  0未对账不能为空", trigger: "blur" }
        ],
        chkBatNo: [
          { required: true, message: "对账批次号不能为空", trigger: "blur" }
        ],
        settleDate: [
          { required: true, message: "清算日期不能为空", trigger: "blur" }
        ],
        tmpSmp: [
          { required: true, message: "时间戳不能为空", trigger: "blur" }
        ],
        nodeId: [
          { required: true, message: "节点ID不能为空", trigger: "blur" }
        ],
        regionId: [
          { required: true, message: "集群ID不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询支付订单列表 */
    getList() {
      this.loading = true;
      listPayOrder(this.queryParams).then(response => {
        this.payOrderList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.dateTimeRange = [];
      this.queryParams.params = {};
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加支付订单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPayOrder(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改支付订单";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除支付订单编号为"' + ids + '"的数据项？').then(function() {
        return delPayOrder(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('payOrder/payOrder/export', {
        ...this.queryParams
      }, `payOrder_${new Date().getTime()}.xlsx`)
    },
    /**  改变时间范围 */
    changeTimeRange(){
      this.queryParams.params = {};
      if (null != this.dateTimeRange && 0 != this.dateTimeRange.length){
        let beginTime = this.dateTimeRange[0].split(' ');
        let endTime = this.dateTimeRange[1].split(' ');
        this.queryParams.params["beginRequestDate"] = beginTime[0]; 
        this.queryParams.params["beginRequestTime"] = beginTime[1]; 
        this.queryParams.params["endRequestDate"] = endTime[0];
        this.queryParams.params["endRequestTime"] = endTime[1]; 
      }
      this.handleQuery()
    }
  }
};
</script>
