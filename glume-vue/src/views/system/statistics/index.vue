<template>
  <layout-container>
    <el-card class="box-card min-h8">
      <el-table v-loading="loading" :data="list" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}">
        <el-table-column type="selection" width="80" align="center"/>
        <el-table-column label="日期">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.create_date) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="已填地址人数" prop="address_total"/>
        <el-table-column label="未填地址人数" prop="not_address"/>
        <el-table-column label="已绑定手机号人数" prop="phone_total"/>
        <el-table-column label="未绑定手机号人数" prop="not_phone"/>
        <el-table-column label="总用户数" prop="total_num"/>
      </el-table>
    </el-card>
    <pagination :page-sizes="[20,40,60,80]" v-show="total>0" :total="total" :page.sync="queryParams.page" :limit.sync="queryParams.limit" @pagination="getList"/>
  </layout-container>
</template>

<script>
import { getStatistics } from "@/api/sataistics"
import LayoutContainer from '@/components/LayoutContainer/LayoutContainer'
export default {
  name: 'index',
  components: { LayoutContainer },
  data() {
    return {
      loading: false,
      list: [],
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
      getStatistics().then(response => {
        this.list = response.data;
      })
    }
  }
}
</script>

<style scoped>

</style>
