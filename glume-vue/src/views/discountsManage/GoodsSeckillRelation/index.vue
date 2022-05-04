<template>
  <layout-container>
    <!--    搜索框-->
    <search-box v-show="showSearch" :is-select="true" :param-accept="queryParams" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
    <!--    全局按钮-->
    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-button icon="el-icon-plus" size="mini" @click="addData">新增</el-button>
        <el-button icon="el-icon-plus" type="danger" size="mini" @click="batchDelete">批量删除</el-button>
        <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="getList" />
      </el-row>
      <!--    内容展示-->
      <el-table v-loading="loading" :data="dataList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="活动名称" prop="promotionName"/>
        <el-table-column label="活动场次名称" prop="promotionSessionName"/>
        <el-table-column label="商品标题" prop="skuTitle"/>
        <el-table-column label="秒杀价格" prop="seckillPrice"/>
        <el-table-column label="秒杀总量" prop="seckillCount"/>
        <el-table-column label="每人限购数量" prop="seckillLimit"/>
        <el-table-column label="排序" prop="seckillSort"/>
        <el-table-column label="操作" prop="sort">
          <template slot-scope="scope">
            <el-button icon="el-icon-edit" type="warning" size="mini" @click="amendData(scope.row)">修改</el-button>
            <el-button icon="el-icon-delete" type="danger" size="mini" @click="deleteData(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
    <!-- 弹出层 -->
    <my-dialog ref="dialog" :title="title" :form="form"
               :productList="productList"
               :brandTreeList="productCategoryList"
               :promotionAndSession="promotionAndSession"
               @onCategory="onCategory"
               @submitForm="submitForm"/>
  </layout-container>
</template>

<script>
  import SearchBox from '@/components/searchBox/index'
  import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
  import {MessageBox} from "element-ui";
  import {getData,save,info,update,delData,getPrductData,listProductCategory,promotionAndSession} from "@/api/DiscountsManage/GoodsSeckillRelation";
  import MyDialog from "@/views/discountsManage/GoodsSeckillRelation/component/MyDialog";
  export default {
    name: 'index',
    components: {MyDialog, LayoutContainer, SearchBox },
    data() {
      return {
        ids: null,
        total: 0,
        showSearch: true,
        loading: false,
        queryParams: {
          page: 1,
          limit: 20,
        },
        queryDataModel: [
          {type: "select",label: "活动主体",prop: "promotionId",data: []},
          {type: "select",label: "活动场次",prop: "sessionId",data: []},
          {type: "default",label: "秒杀价格",prop: "seckillPrice"},
          {type: "default",label: "限购数量",prop: "seckillLimit"}],
        title: "",
        form: {
          status: 1,
        },
        dataList: [],
        productList: [],
        productCategoryList: [],
        promotionAndSession: {},
      }
    },
    created() {
      this.getList();
      this.getListProductCategory();
      this.getProductList();
      this.getPromotionAndSession();
    },
    methods: {
      getList() {
        this.loading = true;
        getData(this.queryParams).then(response => {
          this.dataList = response.data.list;
          this.total = response.data.totalCount;
          this.loading = false;
        })
      },
      getListProductCategory() {
        listProductCategory().then(response => {
          this.productCategoryList = response.data;
          this.handlerBrandTreeList(this.productCategoryList)
        })
      },
      handlerBrandTreeList(brandTreeList) {
        brandTreeList.forEach(item => {
          item.level = 1;
          if (item.children != undefined || item.children != null) {
            this.levelChildren(item.children, 2)
          }
        })
      },
      levelChildren(children, level) {
        children.forEach(child => {
          child.level = level;
        })
      },
      getPromotionAndSession() {
        promotionAndSession().then(response => {
          this.promotionAndSession = response.data;
          if (this.promotionAndSession.promotions && this.promotionAndSession.promotions.length > 0) {
            let arr = [];
            this.promotionAndSession.promotions.forEach(item => {
              arr.push({id: item.id, name: item.title});
            })
            this.queryDataModel[0].data = arr;
          }
          if (this.promotionAndSession.sessions && this.promotionAndSession.sessions.length > 0) {
            let arr = [];
            this.promotionAndSession.sessions.forEach(item => {
              arr.push({id: item.id, name: item.name});
            })
            this.queryDataModel[1].data = arr;
          }
        })
      },
      getProductList() {
        getPrductData().then(response => {
          this.productList = response.data;
        })
      },
      onCategory(data) {
        console.log(data)
      },
      addData() {
        this.reset();
        this.title = "添加秒杀活动商品关系";
        this.$refs["dialog"].open();
      },
      amendData(row) {
        this.reset();
        this.form.id = row.id;
        info(row.id).then(response => {
          this.form = response.data;
          if (response.data.startTime && response.data.endTime) {
            this.form.dateTime = [response.data.startTime, response.data.endTime];
          }
          this.title = "修改秒杀活动商品关系";
          this.$refs["dialog"].open();
        });
      },
      /** 提交按钮 */
      submitForm: function () {
        if (this.form.id != undefined) {
          update(this.form).then(response => {
            this.notSuccess("修改成功");
            this.$refs["dialog"].close();
            this.getList();
          });
        } else {
          save(this.form).then(response => {
            this.notSuccess("新增成功");
            this.$refs["dialog"].close();
            this.getList();
          });
        }
      },
      batchDelete() {
        if (this.ids && this.ids.length > 0) {
          let ids = this.ids;
          MessageBox.confirm('是否确认批量删除数据项？').then(function () {
            return delData(ids);
          }).then(() => {
            this.getList();
            this.notSuccess("删除成功");
          }).catch((err) => {
            console.log(err)
          });
        } else {
          this.notWarning("请选择需要删除的数据");
        }
      },
      deleteData(row) {
        MessageBox.confirm('是否确认删除名称为"' + row.name + '"的数据项？').then(function () {
          return delData(row.id);
        }).then(() => {
          this.getList();
          this.notSuccess("删除成功");
        }).catch(() => {
        });
      },
      handleSelectionChange(e) {
        let arr = []
        e.forEach(item => {
          arr.push(item.id);
        })
        this.ids = arr;
      },
      handleQuery(param) {
        this.getList();
      },
      resetQuery() {
        this.reset();
        this.getList();
      },
      reset() {
        this.title = "";
        this.form = {
          status: 1
        };
        this.queryParams = {
          page: 1,
          limit: 20,
        };
      }
    }
  }
</script>

<style scoped>
</style>
