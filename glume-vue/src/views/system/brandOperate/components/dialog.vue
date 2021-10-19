<template>
  <el-dialog class="set-report-price" title="价位设置" :visible.sync="showDialog" width="75%" :before-close="close">
    <set-report-price ref="setReportPrice"
                      :modal="false"
                      :query-table="getList"
                      :price-data-list="priceDataList"
                      :title="title"
                      :loading="loading"
                      :form="form"
                      @handleAmend="handleAmend" @handleDelete="handleDelete" @handleAdd="handleAdd" />
  </el-dialog>
</template>

<script>
import SetReportPrice from "@/views/components/SetReportPrice/index"
import { addPriceItem, amendPriceItem, deletePriceItem, getPriceList, selectPriceItem } from '@/api/brandOperate'
import { MessageBox } from 'element-ui'
import { arrayFun } from '@/utils/tuo'
export default {
  name: 'index',
  components: {
    SetReportPrice
  },
  props: {
    mobelId: Number
  },
  watch: {
    mobelId: {
      handler(val) {
        this.getList()
      }
    }
  },
  data() {
    return {
      showDialog: false,
      id: null,
      loading: false,
      total: 0,
      title: null,
      form: {
        start_price: null,
        end_price: null,
        earnings: null,
        type: 2
      },
      priceDataList: [],
    }
  },
  methods: {
    open:function() {
      this.showDialog = true;
    },
    close: function() {
      this.showDialog = false;
    },
    getList() {
      this.loading = true;
      getPriceList({model_id: this.mobelId}).then(response => {
        this.priceDataList = response.data;
        this.total = response.total;
        this.loading = false;
      })
    },
    handleAmend(row) {
      this.title = "修改报价范围"
      this.id = row.id;
      selectPriceItem({id: row.id}).then(response => {
        this.form = response.data;
        this.$refs['setReportPrice'].open();
      })
    },
    handleAdd: function(form) {
      this.$refs['setReportPrice'].$refs["elForm"].validate(valid => {
        if (!valid) return false;
        form.model_id = this.mobelId;
        if (this.id == null) {
          addPriceItem(form).then(() => {
            this.getList()
            this.reset()
            this.$refs['setReportPrice'].handleClose();
          })
        } else {
          form.id = this.id
          amendPriceItem(form).then(() => {
            this.getList()
            this.reset()
            this.$refs['setReportPrice'].handleClose();
          })
        }
      })
    },
    handleDelete(row) {
      MessageBox.confirm("确认删除《" + row.start_price + "~" + row.end_price + "》" + "范围的价格吗？","系统提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
      }).then(() => {
        this.form.id = row.id;
        deletePriceItem(this.form).then(() => {
          arrayFun.deleteList(this.priceDataList,"id",row.id);
        })
      })
    },
    handleClose() {
      this.reset();
    },
    reset() {
      this.id = null,
        this.title = "";
    }
  }
}
</script>

<style scoped>
</style>
