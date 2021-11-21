<template>
  <div class="marketProperty">
    <div slot="header" class="clearfix" v-if="marketPropertyData && marketPropertyData.length > 0">
      <span>选择销售属性</span>
      <el-form ref="form">
        <el-form-item v-for="(attr,attrIndex) in marketPropertyData" :key="attr.attrId" :label="attr.attrName">
          <el-checkbox-group v-model="marketData[attrIndex].newTempSaleAttrs">
            <el-checkbox v-if="attr.valueSelect != ''" v-for="val in valueSelectHandler(attr.valueSelect)" :key="val" :label="val"/>
            <div style="margin-left:20px;display:inline">
              <el-button v-show="!marketData[attrIndex].inputVisible" class="button-new-tag" size="mini" @click="showInput(attr.attrId,attrIndex)">+自定义</el-button>
              <el-input v-show="marketData[attrIndex].inputVisible" v-model="marketData[attrIndex].newValueSelect" :ref="'saveTagInput'+attr.attrId" size="mini" style="width:150px"
                        @keyup.enter.native="handleInputConfirm(attr.attrId,attrIndex)"
                        @blur="handleInputConfirm(attr.attrId,attrIndex)"/>
            </div>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
    </div>
    <el-empty v-else description="当前分类没有可选的规格参数"/>
    <div class="button-box">
      <el-button class="nextButton" type="info" @click="back">上一步</el-button>
      <el-button class="nextButton" type="primary" @click="next">下一步</el-button>
    </div>
  </div>
</template>

<script>
import {mapActions, mapGetters} from "vuex";

export default {
  name: "issuedCommodity",
  data() {
    return {
      marketPropertyData: [],
      marketData: [],
    }
  },
  computed: {
    ...mapGetters(['sizeParamData'])
  },
  methods: {
    ...mapActions(['setMarketPropertyData']),
    next() {
      this.setMarketPropertyData(this.marketData)
      this.$emit("next")
    },
    back() {
      this.$emit("back")
    },
    showInput(attrId,attrIndex) {
      this.marketData[attrIndex].inputVisible = !this.marketData[attrIndex].inputVisible
      this.marketPropertyData.forEach(item => {
        if (item.attrId == attrId) {
          item.marketProperty.inputVisible = !item.marketProperty.inputVisible;
        }
      })
    },
    handleInputConfirm(attrId,attrIndex) {
      this.marketPropertyData.forEach(item => {
        if (item.attrId == attrId && this.marketData[attrIndex].newValueSelect != "") {
          item.valueSelect = item.valueSelect + `;${this.marketData[attrIndex].newValueSelect}`;
          this.marketData[attrIndex].newValueSelect = "";

        }
      })
      this.marketData[attrIndex].inputVisible = !this.marketData[attrIndex].inputVisible
    },
    sizeParamDataHandler() {
      if (this.sizeParamData && this.sizeParamData.length > 0) {
        let marketPropertyData = [];
        this.sizeParamData.forEach(item => {
          let attrs = this.filterAttrs(item.attrs)
          if (attrs && attrs.length > 0) {
            marketPropertyData.push(...attrs)
          }
        })
        let num = 0;
        marketPropertyData.forEach(item => {
          this.$set(this.marketData,num,{
            newTempSaleAttrs: [],
            newValueSelect: "",
            inputVisible: false,
          })
          num++;
        })
        this.marketPropertyData = marketPropertyData;
      }
    },
    filterAttrs(attrs) {
      return attrs.filter(attr => attr.attrType == 2)
    },
    valueSelectHandler(data) {
      return data.split(";")
    },
  }
}
</script>

<style scoped>
  .marketProperty {
    padding: 0 30%;
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
