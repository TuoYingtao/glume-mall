<template>
  <layout-container>
    <el-card class="box-card min-h8">
      <el-table v-loading="loading" :data="feedbackList" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="用户ID" prop="user_id"/>
        <el-table-column label="联系方式">
          <template slot-scope="scope">
            <span>{{scope.row.mobile ? scope.row.mobile : "未绑定"}}</span>
          </template>
        </el-table-column>
        <el-table-column label="反馈内容" prop="content"/>
        <el-table-column label="创建时间">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.create_time) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status == 0" type="primary" size="mini" @click="consultHandler(scope.row)">查阅</el-button>
            <el-button v-else type="info" size="mini" disabled>已查阅</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :page-size="[20,40,60,80,100]" v-show="total > 0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
    </el-card>
  </layout-container>
</template>

<script>
import { getFeedback,consultFeedback } from "@/api/feedback"
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
export default {
  name: 'index',
  components: { LayoutContainer },
  data() {
    return {
      loading: false,
      feedbackList: [],
      total: null,
      queryParams: {
        page: 1,
        limit: 20
      }
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      getFeedback().then(response => {
        this.loading = false;
        this.feedbackList = response.data;
        this.total = response.total;
      })
    },
    consultHandler(row) {
      consultFeedback({id: row.id}).then(response => {
        if (response.code == 200) {
          this.getList();
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
