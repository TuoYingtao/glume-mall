<template>
  <div class="skuinfo">
    <div v-if="tableData">
      <el-table :data="tableData" :header-cell-style="{'text-align':'center'}" :cell-style="{'text-align':'center'}" width="100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="商品名称">
                <span>{{ props.row.name }}</span>
              </el-form-item>
              <el-form-item label="所属店铺">
                <span>{{ props.row.shop }}</span>
              </el-form-item>
              <el-form-item label="商品 ID">
                <span>{{ props.row.id }}</span>
              </el-form-item>
              <el-form-item label="店铺 ID">
                <span>{{ props.row.shopId }}</span>
              </el-form-item>
              <el-form-item label="商品分类">
                <span>{{ props.row.category }}</span>
              </el-form-item>
              <el-form-item label="店铺地址">
                <span>{{ props.row.address }}</span>
              </el-form-item>
              <el-form-item label="商品描述">
                <span>{{ props.row.desc }}</span>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column label="品牌信息">
          <el-table-column label="sku名称" prop="skuName">
            <template slot-scope="scope">
              <el-input v-model="scope.row.skuName" placeholder="请输入内容"/>
            </template>
          </el-table-column>
          <el-table-column label="标题" prop="skuTitle">
            <template slot-scope="scope">
              <el-input v-model="scope.row.skuTitle" placeholder="请输入内容"/>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="副标题" prop="skuSubtitle">
          <template slot-scope="scope">
            <el-input v-model="scope.row.skuSubtitle" placeholder="请输入内容"/>
          </template>
        </el-table-column>
        <el-table-column label="价格" prop="price">
          <template slot-scope="scope">
            <el-input-number controls-position="right" v-model="scope.row.price" :precision="3" :step="0.1" />
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-empty v-else description="当前分类没有可选的规格参数"/>
    <div class="button-box">
      <el-button class="nextButton" type="info" @click="back">上一步</el-button>
      <el-button class="nextButton" type="primary" @click="next">下一步</el-button>
    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";

export default {
  name: "SKUInfo",
  data() {
    return {
      SKUInfoData: [],
      tableData: [],
    }
  },
  computed: {
    ...mapGetters(['baseInfoForm','sizeParamData','marketPropertyData'])
  },
  methods: {
    next() {
      this.$emit("next");
    },
    back() {
      this.$emit("back");
    },
    tableHandlerData() {
      console.log(this.baseInfoForm)
      let spuName = this.baseInfoForm.spuName;
      let SKUInfoData = [];
      SKUInfoData.push(Array(spuName))
      console.log(SKUInfoData)
      this.marketPropertyData.forEach(item => {
        SKUInfoData.push(item.newTempSaleAttrs);
      })
      let descartes = this.descartes(SKUInfoData)
      let data = [];
      descartes.forEach(item => {
        let obj = {};
        obj.skuName = item.join(" ");
        obj.skuTitle = item.join(" ");
        obj.skuSubtitle = null;
        obj.price = null;
        data.push(obj);
      })
      this.tableData = data;
      console.log(data)
      this.SKUInfoData = SKUInfoData;
    },
    //笛卡尔积运算
    descartes(list) {
      //parent上一级索引;count指针计数
      var point = {};

      var result = [];
      var pIndex = null;
      var tempCount = 0;
      var temp = [];

      //根据参数列生成指针对象
      for (var index in list) {
        if (typeof list[index] == "object") {
          point[index] = { parent: pIndex, count: 0 };
          pIndex = index;
        }
      }

      //单维度数据结构直接返回
      if (pIndex == null) {
        return list;
      }

      //动态生成笛卡尔积
      while (true) {
        for (var index in list) {
          tempCount = point[index]["count"];
          temp.push(list[index][tempCount]);
        }

        //压入结果数组
        result.push(temp);
        temp = [];

        //检查指针最大值问题
        while (true) {
          if (point[index]["count"] + 1 >= list[index].length) {
            point[index]["count"] = 0;
            pIndex = point[index]["parent"];
            if (pIndex == null) {
              return result;
            }

            //赋值parent进行再次检查
            index = pIndex;
          } else {
            point[index]["count"]++;
            break;
          }
        }
      }
    }
  }
}
</script>

<style scoped>
  .skuinfo {
    padding: 0 10%;
  }
  .button-box {
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .nextButton {
    width: 200px;
    margin-top: 12px;
  }
</style>
