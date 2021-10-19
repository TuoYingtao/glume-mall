<template>
  <layout-container>
    <!-- 搜索 -->
    <search-box :param-accept="queryParams" :search-data="searchData" @queryParams="queryData" @resetData="resetQuery" />

    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button icon="el-icon-plus" size="mini">新增</el-button>
        </el-col>
        <right-toolbar :show-search.sync="showSearch" :is-flag-show="$route.meta.search" @queryTable="getList"/>
      </el-row>
    </el-card>
  </layout-container>
</template>

<script>
  import SearchBox from '@/components/searchBox/index'
  import RightToolbar from '@/components/RightToolbar/index'
  import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
  export default {
    name: 'index',
    components: { LayoutContainer, RightToolbar, SearchBox },
    data() {
      return {
        showSearch: true,
        searchData: [{type: "default",label: "手机号",prop: "phone"},
          {type: "default",label: "负责人",prop: "principals"},
          {type: "default",label: "店面名称",prop: "shop"},
          {type: "select",label: "状态",prop: "status",data:[{id: 7,name:"全部"},{id: 1,name: "启用"},{id: 2,name: "关闭"}]}],
        queryParams: {
          page: 1,
          page_size: 10,
          status: 7
        }
      }
    },
    methods: {
      getList() {

      },
      queryData(args) {
        console.log(args);
      },
      resetQuery() {
        console.log(this.queryParams);
      }
    }
  }
</script>

<style scoped>

</style>
