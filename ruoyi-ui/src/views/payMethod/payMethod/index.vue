<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px" label-position="left">
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
      <el-form-item label="支付方式" prop="payMethod">
        <el-select v-model="queryParams.payMethod" placeholder="请选择支付方式" clearable>
          <el-option
            v-for="dict in dict.type.pay_method"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="支付凭证" prop="payVoucher">
        <el-input
          v-model="queryParams.payVoucher"
          placeholder="请输入支付凭证"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择交易状态" clearable>
          <el-option
            v-for="dict in dict.type.pay_method_status"
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
          v-hasPermi="['payMethod:payMethod:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="payMethodList" @selection-change="handleSelectionChange">
      <el-table-column label="支付订单编号" align="center" prop="payOrderId" width="200" />
      <el-table-column label="请求订单编号" align="center" prop="requestOrderId" />
      <el-table-column label="支付方式" align="center" prop="payMethod">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.pay_method" :value="scope.row.payMethod"/>
        </template>
      </el-table-column>
      <el-table-column label="支付凭证" align="center" prop="payVoucher" />
      <el-table-column label="数量" align="center" prop="number" />
      <el-table-column label="折合金额" align="center" prop="amount" />
      <el-table-column label="交易状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.pay_method_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
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
import { listPayMethod, getPayMethod, delPayMethod, addPayMethod, updatePayMethod } from "@/api/payMethod/payMethod";

export default {
  name: "PayMethod",
  dicts: ['pay_method', 'pay_method_status'],
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
      // 订单支付方式信息表格数据
      payMethodList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        payOrderId: null,
        requestOrderId: null,
        payMethod: null,
        payVoucher: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        payOrderId: [
          { required: true, message: "支付订单编号不能为空", trigger: "blur" }
        ],
        requestOrderId: [
          { required: true, message: "请求订单编号不能为空", trigger: "blur" }
        ],
        payMethod: [
          { required: true, message: "支付方式 0现金 1积分  2代金券不能为空", trigger: "change" }
        ],
        payVoucher: [
          { required: true, message: "支付凭证   积分填冻结编号   代金券填代金券编号不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "交易状态  0初始登记  1冻结成功   2支付成功  3失败    不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询订单支付方式信息列表 */
    getList() {
      this.loading = true;
      listPayMethod(this.queryParams).then(response => {
        this.payMethodList = response.rows;
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
      this.title = "添加订单支付方式信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPayMethod(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改订单支付方式信息";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除订单支付方式信息编号为"' + ids + '"的数据项？').then(function() {
        return delPayMethod(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('payMethod/payMethod/export', {
        ...this.queryParams
      }, `payMethod_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
