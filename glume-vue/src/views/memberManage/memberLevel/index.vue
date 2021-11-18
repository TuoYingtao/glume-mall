<template>
  <layout-container>
    <!--    搜索框-->
    <search-box v-show="showSearch" :is-select="true" :param-accept="queryParams" :search-data="queryDataModel" @queryParams="handleQuery" @resetData="resetQuery" />
    <!--    全局按钮-->
    <el-card class="box-card min-h8">
      <el-row :gutter="10" class="mb8">
        <el-button icon="el-icon-plus" size="mini" @click="addData">新增</el-button>
        <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="getList" />
      </el-row>
      <!--    内容展示-->
      <table-box :loading="loading" :data="memberLevelList" :tableColumn="tableColumn" @amendMemberLevel="amendMemberLevel" @deleteMemberLevel="deleteMemberLevel" @selection-change="handleSelectionChange"/>
      <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
    <!-- 弹出层 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="35%" :before-close="handleClose">
      <el-form ref="elForm" :model="form" :rules="rules" size="medium" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="会员等级名称：" prop="name">
              <el-input placeholder="输入会员等级名称" v-model="form.name"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="等级成长值：" prop="growthPoint">
              <el-input-number placeholder="输入等级成长值" style="width: 100%;" v-model="form.growthPoint" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="免运费标准：" prop="freeFreightPoint">
              <el-input-number placeholder="输入免运费标准" :precision="2" v-model="form.freeFreightPoint" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="评价成长值：" prop="commentGrowthPoint">
              <el-input-number placeholder="输入评价成长值" v-model="form.commentGrowthPoint" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="默认等级：" prop="defaultStatus">
              <el-radio-group v-model="form.defaultStatus">
                <el-radio :label="0">不是</el-radio>
                <el-radio :label="1">是</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="免邮特权：" prop="priviledgeFreeFreight">
              <el-radio-group v-model="form.priviledgeFreeFreight">
                <el-radio :label="0">没有</el-radio>
                <el-radio :label="1">有</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="会员价格特权：" prop="priviledgeMemberPrice">
              <el-radio-group v-model="form.priviledgeMemberPrice">
                <el-radio :label="0">没有</el-radio>
                <el-radio :label="1">有</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="生日特权：" prop="priviledgeBirthday">
              <el-radio-group v-model="form.priviledgeBirthday">
                <el-radio :label="0">没有</el-radio>
                <el-radio :label="1">有</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注：">
              <el-input type="textarea" :rows="3"placeholder="请输入内容" v-model="form.note"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <span class="fr">
            <el-button @click="handleClose">取消</el-button>
            <el-button type="cyan" @click="submitForm">添加</el-button>
          </span>
        </el-form-item>
      </el-form>
    </el-dialog>
  </layout-container>
</template>

<script>
import SearchBox from '@/components/searchBox/index'
import TableBox from '@/components/TableBox'
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
import {MessageBox} from "element-ui";
import {addMemberLevel, delMemberLevel, memberLevelList, queryMemberLevel, updateMemberLevel} from "@/api/memberManage/memberLevel";
export default {
  name: 'memberLevelList',
  components: { LayoutContainer, SearchBox,TableBox },
  data() {
    return {
      tableColumn: [
        {label: '会员等级ID',prop: 'id'},
        {label: '会员等级名称',prop: 'name'},
        {label: '等级成长值',prop: 'growthPoint'},
        {label: '默认等级',prop: 'defaultStatus'},
        {label: '免运费标准',prop: 'freeFreightPoint'},
        {label: '评价成长值',prop: 'commentGrowthPoint'},
        {label: '免邮特权',prop: 'priviledgeFreeFreight'},
        {label: '会员价格特权',prop: 'priviledgeMemberPrice'},
        {label: '生日特权',prop: 'priviledgeBirthday'},
        {label: '备注',prop: 'note'},
        {label: '操作',size: 'mini',width: 200,model: [
            {name: '修改',color: 'warning',onClick: 'amendMemberLevel',icon: "el-icon-edit"},
            {name: '删除',color: 'danger',onClick: 'deleteMemberLevel',icon: "el-icon-delete"}],type: 'bottom'},
      ],
      total: 0,
      showSearch: true,
      loading: false,
      queryParams: {
        page: 1,
        limit: 20,
      },
      queryDataModel: [{type: "default",label: "会员等级ID",prop: "memberLevelId"},
        {type: "default",label: "会员等级名称",prop: "memberLevelName"},
        {type: "datetime",label: "日期范围",prop: "date_time"}],
      title: "",
      dialogVisible: false,
      form: {},
      rules: {
        memberLevelName: [{ required: true, message: "菜单名称不能为空", trigger: "blur" }],
        memberLevelTag: [{ required: true, message: "菜单顺序不能为空", trigger: "blur" }],
      },
      memberLevelList: [],
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      memberLevelList(this.queryParams).then(response => {
        this.memberLevelList = response.data.list;
        this.total = response.data.totalCount;
        this.loading = false;
      })
    },
    addData() {
      this.reset();
      this.title = "添加会员等级";
      this.dialogVisible = true;
    },
    amendMemberLevel(row) {
      this.reset();
      this.form.id = row.id;
      queryMemberLevel(row.id).then(response => {
        this.form = response.data;
        this.dialogVisible = true;
        this.title = "修改会员等级";
      });
    },
    /** 提交按钮 */
    submitForm: function() {
      this.$refs["elForm"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateMemberLevel(this.form).then(response => {
              this.notSuccess("修改成功");
              this.dialogVisible = false;
              this.getList();
            });
          } else {
            addMemberLevel(this.form).then(response => {
              this.notSuccess("新增成功");
              this.dialogVisible = false;
              this.getList();
            });
          }
        }
      });
    },
    deleteMemberLevel(row) {
      MessageBox.confirm('是否确认删除名称为"' + row.name + '"的数据项？').then(function() {
        return delMemberLevel({ids: row.id});
      }).then(() => {
        this.getList();
        this.notSuccess("删除成功");
      }).catch(() => {});
    },
    handleSelectionChange(e) {
      console.log(e)
    },
    handleQuery(param) {
      this.getList();
    },
    resetQuery() {
      this.reset();
      this.getList();
    },
    handleClose() {
      this.dialogVisible = false;
      this.reset();
    },
    reset() {
      this.title = "";
      this.form = {
        defaultStatus: 0,
        priviledgeFreeFreight: 0,
        priviledgeMemberPrice: 0,
        priviledgeBirthday: 0,
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
