<template>
  <div>
    <el-dialog title="分组属性关联" width="30%" :visible.sync="dialogTableVisible">
      <el-row :gutter="10" class="mb8">
        <el-col :span="10">
          <el-button icon="el-icon-plus" size="mini" @click="addAttrGroupRelation()">添加</el-button>
        </el-col>
      </el-row>
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
            <el-button icon="el-icon-delete" type="danger" size="mini" @click="deleteRelation(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import {MessageBox} from "element-ui";
import {deleteGroupRelation, groupRelationList} from "@/api/commodityManage/attrGroup";

export default {
  name: "groupPropertyRelationDialog",
  props: {
    attrGroupIds: Number,
  },
  watch: {
    attrGroupIds: {
      handler(val) {
        this.attrGroupId = val
      }
    }
  },
  data() {
    return {
      dialogTableVisible: false,
      relationData: [],
      attrGroupId: null,
    }
  },
  methods: {
    open(attrGroupId) {
      if (attrGroupId) this.attrGroupId = attrGroupId;
      this.dialogTableVisible = true;
      this.getRelationList();
    },
    getRelationList() {
      console.log("BB",this.attrGroupId)
      groupRelationList(this.attrGroupId).then(response => {
        this.relationData = response.data;
      })
    },
    addAttrGroupRelation() {
      this.$emit('addAttrGroupRelation')
    },
    deleteRelation(row) {
      let param = {ids: [{attrId: row.attrId, attrGroupId: this.attrGroupId}]};
      MessageBox.confirm('是否确认删除名称为"' + row.attrName + '"的数据项？').then(function () {
        return deleteGroupRelation(param);
      }).then(() => {
        this.getRelationList();
        this.notSuccess("删除成功");
      }).catch((err) => {
        console.log(err)
      });
    },
  }
}
</script>

<style scoped>

</style>
