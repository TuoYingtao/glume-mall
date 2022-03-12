<!-- 搜索框 -->
<template>
  <el-card class="box-card" v-show="showSearch">
    <el-form id="form" :model="paramAccept" ref="queryForm" :inline="true">
      <div v-for="(item,index) in searchData" :key="index">
        <el-form-item v-if="item.type === 'default'" :label="item.label" :prop="item.prop">
          <el-input
            v-model="paramAccept[item.prop]"
            :placeholder="'请输入' + item.label"
            clearable
            size="small"
            style="width: 240px"
            @clear="clearText(item.prop)"
            @keyup.enter.native="queryParamData" />
        </el-form-item>
        <el-form-item v-else-if="item.type === 'select'" :label="item.label||'状态'" :prop="item.prop">
          <el-select clearable v-if="!item.input" v-model="paramAccept[item.prop]" placeholder="请选择" @change="selectQuery">
            <el-option v-for="item in item.data" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
          <el-select clearable v-else v-model="paramAccept[item.prop]" filterable placeholder="请选择或手动输入" @change="selectQuery">
            <el-option v-for="item in item.data" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item v-else-if="item.type === 'datetime'" :label="item.label" :prop="item.prop">
          <el-date-picker
            v-model="dataTime"
            type="datetimerange"
            :format="item.format ? item.format : 'yyyy-MM-dd HH:mm:ss'"
            :value-format="item.format ? item.format : 'yyyy-MM-dd HH:mm:ss'"
            range-separator="至"
            start-placeholder="开始日期时间"
            end-placeholder="结束日期时间"
            @change="parseDate(item.prop)" />
        </el-form-item>
      </div>
      <el-form-item>
        <el-button style="margin-left: 10px" type="cyan" icon="el-icon-search" size="mini" @click="queryParamData">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQueryForm">重置</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
  export default {
    name: 'searchBox',
    props: {
      /* 是否显示 */
      showSearch: {
        type: Boolean,
        default: () => true
      },
      /* 选择查询状态  选择立即发起事务回调 */
      isSelect: {
        type: [Boolean,String],
        default: () => false
      },
      /* 模板数据 */
      searchData: {
        type: Array,
        default: () => [
          { type: "default", label: "title", prop: "title" },
          { type: "select", label: "状态",prop: "status",data: [{id: 1,name: "开启"},{id: 2,name: "关闭"}]},
          { type: "datetime", label: "时间", prop: "date_time" }]
      },
      /* 接收数据源 */
      paramAccept: {
        type: Object,
        default: () => new Object()
      },
    },
    data() {
      return {
        dataTime: []
      }
    },
    methods: {
      /* 日期格式化 */
      parseDate: function(prop) {
        if (this.dataTime && this.dataTime.length > 0) {
          this.paramAccept[prop] = this.dataTime.toString();
        }
      },
      /* 选择查询 */
      selectQuery(){
        const status = typeof this.isSelect === "string" ? Boolean(this.isSelect) : this.isSelect;
        if(status) this.queryParamData();
      },
      /* 清空输入框 */
      clearText(prop) {
        delete this.paramAccept[prop];
      },
      /* 查询 */
      queryParamData() {
        this.$emit("queryParams",this.paramAccept);
      },
      /* 重置 */
      resetQueryForm() {
        this.searchData.forEach(item => {
          if (this.paramAccept[item.prop]) {
            this.paramAccept[item.prop] = null;
            this.clearText(item.prop);
          }
        });
        this.$emit("resetData",true);
      },
    }
  }
</script>

<style scoped>
#form {
  display: flex;
  flex-wrap: wrap;
}
</style>
