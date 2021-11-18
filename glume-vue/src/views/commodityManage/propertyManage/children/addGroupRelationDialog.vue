<template>
  <div>
    <el-dialog title="新增分组属性关联" width="30%" :visible.sync="dialogTableVisible">
      <el-table :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" :data="relationData">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column property="attrId" label="属性ID"/>
        <el-table-column property="attrName" label="属性名"/>
        <el-table-column property="valueSelect" label="可选值">
          <template slot-scope="scope">
            <el-tooltip class="item" effect="dark" :content="scope.row.valueSelect" placement="top-start">
              <el-tag>{{ scope.row.valueSelect }}</el-tag>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button icon="el-icon-plus" type="success" size="mini" @click="deleteRelation(scope.row)">添加</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-size="[10,20,30,40,50]" v-show="total > 0" :total="total" :page.sync="queryParam.page" :limit.sync="queryParam.limit" @pagination="getList"/>
    </el-dialog>
  </div>
</template>

<script>
import {groupNotRelationList} from "@/api/commodityManage/attrGroup";

export default {
  name: "addGroupRelationDialog",
  props: {
    attrGroupId: Number
  },
  data() {
    return {
      queryParam: {
        page: 1,
        limit: 10
      },
      dialogTableVisible: false,
      relationData: [],
      total: null,
    }
  },
  methods: {
    getList() {
      groupNotRelationList(this.attrGroupId).then(response => {
        this.relationData = response.data.list;
        this.total = response.data.totalCount;
      })
    },
    open() {
      this.getList();
      this.dialogTableVisible = true;
    },
    close() {
      this.dialogTableVisible = false;
    }
  }
}
</script>

<style scoped>

</style>
