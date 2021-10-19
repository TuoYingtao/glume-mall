<template>
  <layout-container>
    <el-card class="box-card min-h8">
      <el-tabs class="el-tabs" value="first" type="card">
        <el-tab-pane label="设备报价" name="first">
          <set-report-price class="set-report-price" ref="setReportPrice"
                            :query-table="getList"
                            :price-data-list="priceDataList"
                            :title="title"
                            :loading="loading"
                            :form="form"
                            @handleAmend="handleAmend" @handleDelete="handleDelete" @handleAdd="handleAdd" />
        </el-tab-pane>
        <el-tab-pane label="黄金报价" name="second">
          <table-box class="table-box" :data="goldList" @inputBlur="HandlerInputBlur" :tableColumn="tableColumn"/>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </layout-container>
</template>

<script>
  import SetReportPrice from "@/views/components/SetReportPrice/index"
  import TableBox from "@/components/TableBox/index"
  import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
  import { getPriceList,addPriceItem,selectPriceItem,amendPriceItem,deletePriceItem,getGoldList,amendGoldItem } from "@/api/price"
  import { arrayFun } from '@/utils/tuo'
  import { MessageBox } from "element-ui"
  export default {
    name: 'index',
    components: {
      SetReportPrice,
      TableBox,
      LayoutContainer
    },
    data() {
      return {
        tableColumn: [{label: '名称',prop: 'name'},
          {label: '回购价',prop: 'repo'},
          {label: '更新时间',prop: 'update_time',type: 'time'},
          {label: '调价',prop: 'adjust',input:{type: 'number',onBlur: 'inputBlur'},type: 'input'}],
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
        goldList: [],
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        this.loading = true;
        getPriceList().then(response => {
          this.priceDataList = response.data;
          this.total = response.total;
          this.loading = false;
        })
        getGoldList().then(response => {
          this.goldList = response.data;
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
      HandlerInputBlur(e) {
        console.log(e)
        amendGoldItem(e.id,{adjust: e.adjust}).then(response => {
          if (response.code == 200) {
            this.notSuccess("修改成功！")
          }
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
<style lang="scss" scoped>
.el-tabs /deep/ .el-tabs__item.is-active {
  color: #C03639;
}
.el-tabs /deep/ .el-tabs__item.is-active:hover {
  color: #C03639;
}
.el-tabs /deep/ .el-tabs__item:hover {
  color: #C03639;
}
.el-tabs /deep/ .el-tabs__header {
  margin-bottom: 0;
}
.el-tabs /deep/ .el-tabs--card > .el-tabs__header {
  border-bottom: 0;
}
.el-tabs .set-report-price /deep/ .el-card {
  border: 0;
}
.el-tabs .set-report-price /deep/ .el-card.is-always-shadow,
.el-tabs .set-report-price /deep/ .box-card {
  -webkit-box-shadow: none;
  box-shadow: none;
  margin: 0;
}
.el-tabs /deep/ .el-tabs__content {
  border-left: 1px solid #dfe4ed;
  border-bottom: 1px solid #dfe4ed;
  border-right: 1px solid #dfe4ed;
}
</style>
