<template>
  <layout-container>
    <!--    搜索框-->
    <search-box v-show="showSearch" :is-select="true" :param-accept="queryParams" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
    <el-card class="box-card min-h8 box-card-content">
      <el-row :gutter="10" class="mb8">
        <el-col>
          <span class="title-box">SQL 监控：<span class="title">{{ title }}</span></span>
          <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="init" />
        </el-col>
      </el-row>
      <iframe ref="iframe" :src="`http://localhost:${queryParams.prod}/druid/index.html`"
              width="100%" height="1000px" scrolling="auto"
              frameborder="0" security="restricted"
              sandbox="allow-same-origin allow-top-navigation allow-forms allow-scripts allow-popups"/>
    </el-card>
  </layout-container>
</template>

<script>
export default {
  name: "druid",
  data() {
    return {
      title: "",
      showSearch: true,
      queryParams: {},
      queryDataModel: [
        {type: "select",label: "服务",prop: "prod",input: true,data: [
          {id: 8088,name: "后台服务"}, {id: 9985,name: "后台服务c"}
        ]}
        ],
    }
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      this.title = this.queryDataModel[0].data[0].name;
      this.queryParams.prod = this.queryDataModel[0].data[0].id;
    },
    handleQuery() {

    },
    resetQuery() {
      this.queryParams.prod = this.queryDataModel[0].data[0].id;
    }
  }
}
</script>

<style lang="scss" scoped>
  .title-box {
    font-size: 28px;
    font-weight: bold;
    .title {
      font-size: 24px;
      color: #999;
    }
  }
  .box-card-content {
    padding: 15px 0;
  }
</style>
