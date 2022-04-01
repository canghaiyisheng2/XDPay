<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="120px" label-position="left">
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
      <el-form-item label="交易请求流水号" prop="requestTxnJnl">
        <el-input
          v-model="queryParams.requestTxnJnl"
          placeholder="请输入交易请求流水号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易响应流水号" prop="responseTxnJnl">
        <el-input
          v-model="queryParams.responseTxnJnl"
          placeholder="请输入交易响应流水号"
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
      <el-form-item label="数量" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="折合金额" prop="amount">
        <el-input
          v-model="queryParams.amount"
          placeholder="请输入折合金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="交易类型" prop="txnType">
        <el-select v-model="queryParams.txnType" placeholder="请选择交易类型" clearable>
          <el-option
            v-for="dict in dict.type.txn_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="交易状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择交易状态" clearable>
          <el-option
            v-for="dict in dict.type.txn_status"
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
          v-hasPermi="['payTxnJnl:payTxnJnl:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="payTxnJnlList" @selection-change="handleSelectionChange">
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="支付订单编号" align="center" prop="payOrderId" />
      <el-table-column label="交易请求流水号" align="center" prop="requestTxnJnl" />
      <el-table-column label="交易响应流水号" align="center" prop="responseTxnJnl" />
      <el-table-column label="请求订单编号" align="center" prop="requestOrderId" />
      <el-table-column label="订单类型" align="center" prop="requestOrderType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.request_order_type" :value="scope.row.requestOrderType"/>
        </template>
      </el-table-column>
      <el-table-column label="支付方式" align="center" prop="payMethod">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.pay_method" :value="scope.row.payMethod"/>
        </template>
      </el-table-column>
      <el-table-column label="数量" align="center" prop="number" />
      <el-table-column label="折合金额" align="center" prop="amount" />
      <el-table-column label="交易类型" align="center" prop="txnType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.txn_type" :value="scope.row.txnType"/>
        </template>
      </el-table-column>
      <el-table-column label="交易状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.txn_status" :value="scope.row.status"/>
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

    <!-- 添加或修改支付流水对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="支付订单编号" prop="payOrderId">
          <el-input v-model="form.payOrderId" placeholder="请输入支付订单编号" />
        </el-form-item>
        <el-form-item label="交易流水号" prop="requestTxnJnl">
          <el-input v-model="form.requestTxnJnl" placeholder="请输入交易流水号" />
        </el-form-item>
        <el-form-item label="交易流水号" prop="responseTxnJnl">
          <el-input v-model="form.responseTxnJnl" placeholder="请输入交易流水号" />
        </el-form-item>
        <el-form-item label="请求流水" prop="requestOrderId">
          <el-input v-model="form.requestOrderId" placeholder="请输入请求流水" />
        </el-form-item>
        <el-form-item label="订单类型  0-用品订单  1-服务订单" prop="requestOrderType">
          <el-select v-model="form.requestOrderType" placeholder="请选择订单类型  0-用品订单  1-服务订单">
            <el-option
              v-for="dict in dict.type.request_order_type"
              :key="dict.value"
              :label="dict.label"
:value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="支付方式 0现金 1积分  2代金券" prop="payMethod">
          <el-select v-model="form.payMethod" placeholder="请选择支付方式 0现金 1积分  2代金券">
            <el-option
              v-for="dict in dict.type.pay_method"
              :key="dict.value"
              :label="dict.label"
:value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数量" prop="number">
          <el-input v-model="form.number" placeholder="请输入数量" />
        </el-form-item>
        <el-form-item label="折合金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入折合金额" />
        </el-form-item>
        <el-form-item label="交易类型  0冻结   1记账   2解冻   3解冻支取  4预下单" prop="txnType">
          <el-select v-model="form.txnType" placeholder="请选择交易类型  0冻结   1记账   2解冻   3解冻支取  4预下单">
            <el-option
              v-for="dict in dict.type.txn_type"
              :key="dict.value"
              :label="dict.label"
:value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="交易状态  S成功  F失败  U超时  " prop="status">
          <el-select v-model="form.status" placeholder="请选择交易状态  S成功  F失败  U超时  ">
            <el-option
              v-for="dict in dict.type.txn_status"
              :key="dict.value"
              :label="dict.label"
:value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间戳" prop="tmpSmp">
          <el-input v-model="form.tmpSmp" placeholder="请输入时间戳" />
        </el-form-item>
        <el-form-item label="节点ID" prop="nodeId">
          <el-input v-model="form.nodeId" placeholder="请输入节点ID" />
        </el-form-item>
        <el-form-item label="集群ID" prop="regionId">
          <el-input v-model="form.regionId" placeholder="请输入集群ID" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPayTxnJnl, getPayTxnJnl, delPayTxnJnl, addPayTxnJnl, updatePayTxnJnl } from "@/api/payTxnJnl/payTxnJnl";

export default {
  name: "PayTxnJnl",
  dicts: ['request_order_type', 'txn_type', 'pay_method', 'txn_status'],
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
      // 支付流水表格数据
      payTxnJnlList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        payOrderId: null,
        requestTxnJnl: null,
        responseTxnJnl: null,
        requestOrderId: null,
        requestOrderType: null,
        payMethod: null,
        number: null,
        amount: null,
        txnType: null,
        status: null,
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
        requestTxnJnl: [
          { required: true, message: "交易流水号不能为空", trigger: "blur" }
        ],
        responseTxnJnl: [
          { required: true, message: "交易流水号不能为空", trigger: "blur" }
        ],
        requestOrderId: [
          { required: true, message: "请求流水不能为空", trigger: "blur" }
        ],
        requestOrderType: [
          { required: true, message: "订单类型  0-用品订单  1-服务订单不能为空", trigger: "change" }
        ],
        payMethod: [
          { required: true, message: "支付方式 0现金 1积分  2代金券不能为空", trigger: "change" }
        ],
        status: [
          { required: true, message: "交易状态  S成功  F失败  U超时  不能为空", trigger: "change" }
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
    /** 查询支付流水列表 */
    getList() {
      this.loading = true;
      listPayTxnJnl(this.queryParams).then(response => {
        this.payTxnJnlList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        userId: null,
        payOrderId: null,
        requestTxnJnl: null,
        responseTxnJnl: null,
        requestOrderId: null,
        requestOrderType: null,
        payMethod: null,
        number: null,
        amount: null,
        txnType: null,
        status: null,
        tmpSmp: null,
        nodeId: null,
        regionId: null
      };
      this.resetForm("form");
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
      this.title = "添加支付流水";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getPayTxnJnl(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改支付流水";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updatePayTxnJnl(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPayTxnJnl(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除支付流水编号为"' + ids + '"的数据项？').then(function() {
        return delPayTxnJnl(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('payTxnJnl/payTxnJnl/export', {
        ...this.queryParams
      }, `payTxnJnl_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
