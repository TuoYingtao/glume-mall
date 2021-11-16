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
      <table-box :loading="loading" :data="brandList" :tableColumn="tableColumn" @mapping="mapping" @amendbrand="amendbrand" @deletebrand="deletebrand" @selection-change="handleSelectionChange"/>
      <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
    <!-- 弹出层 -->
    <el-dialog :title="title" :visible.sync="dialogVisible" width="30%" :before-close="handleClose">
      <el-form ref="elForm" :model="form" :rules="rules" size="medium" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="品牌名称：" prop="name">
              <el-input v-model="form.name"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检索首字母：" prop="firstLetter">
              <el-input v-model="form.firstLetter"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序：" prop="sort">
              <el-input-number v-model="form.sort" controls-position="right" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="描述：" prop="descript">
              <el-input type="textarea" :rows="3"placeholder="请输入内容" v-model="form.descript"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="LOGO：" prop="logo">
              <el-upload :action="uploadParams.host"
                         :data="uploadParams"
                         :on-success="uploadSuccess"
                         list-type="picture-card"
                         :auto-upload="true">
                <i slot="default" class="el-icon-plus"></i>
                <div slot="file" slot-scope="{file}">
                  <img class="el-upload-list__item-thumbnail" :src="file.url" alt="">
                  <span class="el-upload-list__item-actions">
                      <span class="el-upload-list__item-preview"@click="handlePictureCardPreview(file)">
                        <i class="el-icon-zoom-in"></i>
                      </span>
                      <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleDownload(file)">
                        <i class="el-icon-download"></i>
                      </span>
                      <span v-if="!disabled" class="el-upload-list__item-delete" @click="handleRemove(file)">
                        <i class="el-icon-delete"></i>
                      </span>
                    </span>
                </div>
              </el-upload>
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

  <!-- 弹窗 -->
    <el-dialog title="品牌分类关联" :visible.sync="dialogTableVisible">
      <el-row :gutter="10" class="mb8">
        <el-cascader ref="cascader" size="mini" v-model="relationFrom.catelogId"
                     :options="classifyTreeList"
                     :props="props"
                     @change="handleClassifyChange"/>
        <el-button icon="el-icon-plus" size="mini" @click="addClassify">新增关系绑定</el-button>
        <right-toolbar :showSearch.sync="showSearch" :isFlagShow="$route.meta.search" @queryTable="getRelationList" />
      </el-row>
      <el-table :data="relationData">
        <el-table-column property="brandId" label="品牌ID" width="150"></el-table-column>
        <el-table-column property="brandName" label="品牌名称" width="200"></el-table-column>
        <el-table-column property="catelogId" label="分类ID" width="200"></el-table-column>
        <el-table-column property="catelogName" label="分类名称" width="200"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="deleteRelation(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-sizes="[20,40,60,80]" v-show="relationTotal>0" :total="relationTotal" :page.sync="relationQueryParams.page" :limit.sync="relationQueryParams.limit" @pagination="getRelationList"/>
    </el-dialog>
  </layout-container>
</template>

<script>
  import SearchBox from '@/components/searchBox/index'
  import TableBox from '@/components/TableBox'
  import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
  import {MessageBox} from "element-ui";
  import {
    addbrand, addBrandClassify,
    amendbrand,
    brandClassifyList,
    brandList,
    delbrand,
    deleteBrandClassify,
    querybrand
  } from "@/api/commodityManage/brand";
  import {getBrandTree, getOSSPolicy} from "@/api/commodityManage/classify";
  export default {
    name: 'brandList',
    components: { LayoutContainer, SearchBox,TableBox },
    data() {
      return {
        tableColumn: [
          {label: '品牌ID',prop: 'brandId'},
          {label: "LOGO",prop: "logo",plugin: true,type: "img"},
          {label: '品牌名称',prop: 'name'},
          {label: '描述',prop: 'descript'},
          {label: '检索首字母',prop: 'firstLetter'},
          {label: '排序',prop: 'sort'},
          {label: '操作',size: 'mini',width: "280",model: [
              {name: '分类匹配',color: 'primary',onClick: 'mapping',icon: "el-icon-connection"},
              {name: '修改',color: 'warning',onClick: 'amendbrand',icon: "el-icon-edit"},
              {name: '删除',color: 'danger',onClick: 'deletebrand',icon: "el-icon-delete"}],type: 'bottom'},
        ],
        total: 0,
        showSearch: true,
        loading: false,
        queryParams: {
          page: 1,
          limit: 20,
        },
        queryDataModel: [{type: "default",label: "模糊搜索",prop: "key"},
          {type: "default",label: "品牌名称",prop: "name"},
          {type: "datetime",label: "日期范围",prop: "date_time"}],
        title: "",
        dialogVisible: false,
        form: {},
        rules: {
          name: [{ required: true, message: "品牌名称不能为空", trigger: "blur" }],
          firstLetter: [{ required: true, message: "检索首字母不能为空", trigger: "blur" }],
          sort: [{ required: true, message: "排序不能为空", trigger: "blur" }],
          descript: [{ required: true, message: "描述不能为空", trigger: "blur" }],
        },
        uploadPath: "",
        uploadParams: {},
        dialogImageUrl: '',
        disabled: false,
        brandList: [],
        defaultProps: {
          children: "children",
          label: "label"
        },
        /* 关系列表 */
        classifyTreeList: [],
        props: {
          value: "catId",
          children: "children",
          label: "name",
        },
        dialogTableVisible: false,
        relationData: [],
        relationTotal: null,
        relationQueryParams: {
          page: 1,
          limit: 20,
        },
        relationFrom: {
          catelogId: null,

        }
      }
    },
    created() {
      this.getList();
      this.OSSPolicy();
      this.getClassifyTree();
    },
    methods: {
      getClassifyTree() {
        getBrandTree().then(response => {
          this.classifyTreeList = response.data;
        })
      },
      getList() {
        this.loading = true;
        brandList(this.queryParams).then(response => {
          this.brandList = response.data.list;
          this.total = response.data.totalCount;
          this.loading = false;
        })
      },
      getRelationList() {
        brandClassifyList(this.relationQueryParams).then(response => {
          this.relationData = response.data.list;
        })
      },
      mapping(row) {
        this.relationFrom.brandId = row.brandId;
        this.relationFrom.brandName = row.name;
        this.relationQueryParams.brandId = row.brandId;
        this.dialogTableVisible = true;
        this.getRelationList();
      },
      addData() {
        this.reset();
        this.title = "添加品牌";
        this.dialogVisible = true;
      },
      amendbrand(row) {
        this.reset();
        this.form.brandId = row.brandId;
        querybrand(row.brandId).then(response => {
          this.form = response.data.brand;
          this.dialogVisible = true;
          this.title = "修改品牌";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["elForm"].validate(valid => {
          if (valid) {
            if (this.form.brandId != undefined) {
              amendbrand(this.form).then(response => {
                this.notSuccess("修改成功");
                this.dialogVisible = false;
                this.getList();
              });
            } else {
              addbrand(this.form).then(response => {
                this.notSuccess("新增成功");
                this.dialogVisible = false;
                this.getList();
              });
            }
          }
        });
      },
      deletebrand(row) {
        MessageBox.confirm('是否确认删除名称为"' + row.name + '"的数据项？').then(function() {
          return delbrand(row.brandId);
        }).then(() => {
          this.getList();
          this.notSuccess("删除成功");
        }).catch(() => {});
      },
      handleClassifyChange(e) {
        let num = e.length - 1;
        this.classifyTreeHandler(e[num],this.classifyTreeList);
        this.relationFrom.catelogId = e[num];
      },
      classifyTreeHandler(id,all) {
        all.forEach(item => {
          if (item.catId == id) {
            this.relationFrom.catelogName = item.name;
          } else if (item.children && item.children.length > 0){
            this.classifyTreeHandler(id,item.children)
          }
        })
      },
      addClassify() {
        if (this.relationFrom.catelogId && this.relationFrom.catelogId != null) {
          addBrandClassify(this.relationFrom).then(response => {
            this.notSuccess("新增成功");
            this.getRelationList();
            this.relationFrom = {
              brandId: this.relationFrom.brandId,
              brandName: this.relationFrom.brandName,
              catelogId: null,
            }
          })
        }
      },
      deleteRelation(row) {
        console.log(row)
        MessageBox.confirm('是否确认删除分类名称为"' + row.catelogName + '"的数据项？').then(function() {
          return deleteBrandClassify({ids: row.id});
        }).then(() => {
          this.getRelationList();
          this.notSuccess("删除成功");
        }).catch(() => {});
      },
      OSSPolicy() {
        getOSSPolicy().then(response => {
          this.uploadPath = response.data.dir + "/" + new Date().getTime();
          this.uploadParams = {
            host: response.data.host,
            OSSAccessKeyId: response.data.accessid,
            key: this.uploadPath,
            signature: response.data.signature,
            policy: response.data.policy,
            // 设置服务端返回状态码为200，不设置则默认返回状态码204。
            success_action_status: 200,
          }
        })
      },
      uploadSuccess(response, file, fileList) {
        console.log("上传成功！")
        this.form.logo = "https://glume-mall.oss-cn-shenzhen.aliyuncs.com/" + this.uploadPath;
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      },
      handleDownload(file) {
        console.log(file);
      },
      handleRemove(file) {
        console.log(file);
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
        this.form = {};
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
