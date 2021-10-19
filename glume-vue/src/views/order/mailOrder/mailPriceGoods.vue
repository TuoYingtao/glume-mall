<template>
  <layout-container>
    <!--    搜索框-->
    <search-box v-show="showSearch" :is-select="true" :param-accept="queryParams" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
    <!--    全局按钮-->
    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="getList" />
      </el-row>
      <!--    内容展示-->
      <table-box :loading="loading" :data="orderData" :tableColumn="tableColumn" @navigatorToOrderInfo="navigatorToOrderInfo" @selection-change="handleSelectionChange"/>
      <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
  </layout-container>
</template>

<script>
  import { getOrderList } from "@/api/orderList"
  import SearchBox from '@/components/searchBox/index'
  import TableBox from '@/components/TableBox'
  import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
  export default {
    name: 'mailPriceGoods',
    components: { LayoutContainer, SearchBox,TableBox },
    data() {
      return {
        tableColumn: [
          {label: '订单编号',prop: 'id'},
          {label: '图片',prop: 'model_image',plugin: true,className: "img_box",type: 'img',},
          {label: '品牌名称',prop: 'model_name'},
          {label: '订单所属',children: [{label: '分公司',prop: 'branch_id'},{label: '用户ID',prop: 'user_id'},],type: 'table'},
          {label: '创建日期',prop: 'create_time',type: 'time'},
          {label: '汇款日期',prop: 'remit_time',type: 'time'},
          {label: '状态',prop: 'status',filter: [
              {id: 1,name: '已预约'},
              {id: 2,name: '已受理',type: 'warning'},
              {id: 3,name: '已取消',type: 'info'},
              {id: 4,name: '已完成',type: 'success'},
              {id: 5,name: '已取件',type: 'warning'},
              {id: 6,name: '已出价',className: 'out-price'},
              {id: 7,name: '待打款',className: 'remit'},
              {id: 8,name: '被退回',type: 'danger'},
              {id: 9,name: '已签收',type: 'warning'},
              {id: 10,name: '已退回',type: 'danger'},
            ],type: 'tag'},
          {label: '操作',size: 'mini',model: [{name: '订单详情',color: 'primary',onClick: 'navigatorToOrderInfo'}],type: 'bottom'},
        ],
        total: 0,
        showSearch: true,
        loading: false,
        queryParams: {
          type: 2,
          page: 1,
          limit: 20,
          status: 6
        },
        queryDataModel: [{type: "default",label: "用户ID",prop: "user_id"},
          {type: "default",label: "订单编号",prop: "id"},
          {type: "select",label: "分公司",prop: "branch_id",input: true,data: []},
          {type: "datetime",label: "日期范围",prop: "date_time"}],
        orderData: []
      }
    },
    created() {
      this.getList();
    },
    methods: {
      getList() {
        this.loading = true;
        getOrderList(this.queryParams).then(response => {
          this.orderData = response.data;
          this.total = response.total;
          this.queryDataModel[2].data = response.company;
          this.loading = false;
        })
      },
      navigatorToOrderInfo(row) {
        this.$router.push({
          path: "/mailOrder/mailGoodsInfo",
          query: { info: encodeURIComponent(JSON.stringify(row)) }
        })
      },
      handleSelectionChange(e) {
        console.log(e)
      },
      handleQuery(param) {
        console.log(param)
        this.getList();
      },
      resetQuery() {
        this.reset();
        this.getList();
      },
      reset() {
        this.queryParams = {
          type: 2,
          page: 1,
          limit: 20,
          status: 6
        };
      }
    }
  }
</script>

<style scoped>
</style>
