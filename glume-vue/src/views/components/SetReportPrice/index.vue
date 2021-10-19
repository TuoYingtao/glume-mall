<template>
  <div>
    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.8">
          <el-button icon="el-icon-plus" size="mini" @click="addDate">新增</el-button>
        </el-col>
        <right-toolbar :is-flag-show="$route.meta.search" @queryTable="queryTable"/>
      </el-row>
      <el-table v-loading="loading" :data="data" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}">
        <el-table-column label="报价范围取值">
          <el-table-column label="起始价" prop="start_price" />
          <el-table-column label="最终价" prop="end_price" />
        </el-table-column>
        <el-table-column label="类型">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.type === 1">百分比价格</el-tag>
            <el-tag type="success" v-else>固定价格</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="加价金额" prop="earnings"/>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleAmend(scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 弹出层 -->
    <el-dialog :title="dialog_title" :visible.sync="showDialog" width="25%" :before-close="handleClose" :modal="modal">
      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
        <el-form-item label="最低回收价" prop="start_price">
          <el-col :span="20"><el-input type="number" v-model="formData.start_price"/></el-col>
        </el-form-item>
        <el-form-item label="最高回收价" prop="end_price">
          <el-col :span="20"><el-input type="number" v-model="formData.end_price"/></el-col>
        </el-form-item>
        <el-form-item label="加价金额" prop="earnings">
          <el-col :span="20"><el-input type="number" v-model="formData.earnings"/></el-col>
        </el-form-item>
        <el-form-item label="类型">
          <el-col :span="20">
            <el-radio-group v-model="formData.type">
              <el-radio :label="2">固定价格</el-radio>
              <el-radio :label="1">百分比</el-radio>
            </el-radio-group>
          </el-col>
        </el-form-item>
        <el-form-item>
          <span class="fr">
            <el-button @click="handleClose">取消</el-button>
            <el-button type="cyan" @click="handleAdd">确认</el-button>
          </span>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
let start_price = null;
const validatorStartPrice = function(rules,value,callback) {
  if (!value) return callback(new Error("请输入最低价格！"));
  if (Number(value) < 0) return callback(new Error("最低价格不能为0或小于0！"));
  start_price = value;
  callback();
}
const validatorEndPrice = function(rule, value, callback) {
  if (!value) return callback(new Error("请输入最高价格！"));
  if (Number(value) < 0) return callback(new Error("最低价格不能为0或小于0！"));
  if (Number(value) < Number(start_price)) return callback(new Error("输入最高价不能低于最低价，请重新输入!"));
  if (Number(start_price) === Number(value)) return callback(new Error("输入最高价不能与最低价相同，请重新输入!"));
  callback();
}

export default {
  name: 'index',
  props: {
    queryTable: Function,
    priceDataList: Array,
    title: String,
    form: Object,
    modal: {
      type: Boolean,
      default: () => true
    },
    loading: {
      type: Boolean,
      default: () => false
    },
    dialogWrapper: {
      type: Boolean,
      default: () => false
    },
  },
  watch: {
    title: {
      handler(val) {
        this.dialog_title = val;
      }
    },
    priceDataList: {
      handler(val) {
        this.data = val;
      }
    },
    dialogWrapper: {
      handler(val) {
        this.showDialog = val
      }
    },
    form: {
      handler(val) {
        this.formData = val
      }
    }
  },
  data() {
    return {
      dialog_title: "",
      showDialog: false,
      data: [],
      formData: {
        start_price: null,
        end_price: null,
        earnings: null,
        type: 2
      },
      rules: {
        start_price: { required: true, validator: validatorStartPrice,trigger: 'blur' },
        end_price: { required: true, validator: validatorEndPrice,trigger: 'blur' },
        earnings: { required: true, message: "请输入加价金额！",trigger: 'blur' }
      }
    }
  },
  methods: {
    rest() {
      this.formData = {
        start_price: null,
        end_price: null,
        earnings: null,
        type: 2
      };
    },
    open() {
      this.showDialog = true;
    },
    handleClose() {
      this.$refs['elForm'].resetFields();
      this.rest();
      this.showDialog = false;
    },
    addDate() {
      this.rest()
      this.dialog_title = "新增报价范围"
      this.showDialog = true;
    },
    handleAmend(row) {
      this.$emit("handleAmend",row);
    },
    handleDelete(row) {
      this.$emit("handleDelete",row);
    },
    handleAdd() {
      this.$emit("handleAdd",this.formData);
    },
  }
}
</script>
