<template>
  <div class="sizeParams">
    <el-tabs tab-position="left" v-if="sizeParamData && sizeParamData.length > 0">
      <el-tab-pane v-for="(attrGroup,attrGroupIndex) in sizeParamData" :key="attrGroupIndex" :label="attrGroup.attrGroupName">
        <el-row>
          <el-col :span="24" v-for="(item,itemIndex) in attrGroup.attrs" :key="itemIndex">
            <el-form ref="form" label-width="100px">
              <el-form-item :label="item.attrName">
                <el-select style="width: 80%;" :multiple="item.valueType == 1" filterable allow-create default-first-option
                           :placeholder="`请选择或输入${item.attrName}值（${item.valueType == 1 ? '多选' : '单选'}）`"
                            v-model="item.baseAttrs.attrValues">
                  <el-option v-for="(val,index) in valueSelectHandler(item.valueSelect)" :key="index" :label="val" :value="val"/>
                </el-select>
                <el-checkbox style="padding-left: 20px" v-model="item.baseAttrs.showDesc" :true-label="1" :false-label="0">快速展示</el-checkbox>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-tab-pane>
    </el-tabs>
    <el-empty v-else description="当前分类没有可选的规格参数"/>
    <div class="button-box">
      <el-button class="nextButton" type="info" @click="back">上一步</el-button>
      <el-button class="nextButton" type="primary" @click="next">下一步</el-button>
    </div>
  </div>
</template>

<script>
import {getAttrGroupWithAttrs} from "@/api/commodityManage/issuedCommodity";
import {mapActions, mapGetters} from "vuex";

export default {
  name: "sizeParams",
  data() {
    return {
      sizeParamData: [],
    }
  },
  computed: {
    ...mapGetters(['baseInfoForm']),
  },
  methods: {
    ...mapActions(['setSizeParamData']),
    next() {
      this.setSizeParamData(this.sizeParamData);
      this.$emit("next")
    },
    back() {
      this.$emit("back")
    },
    attrGroupWithAttrs() {
      getAttrGroupWithAttrs(this.baseInfoForm.catalogId).then(response => {
        this.sizeParamData = response.data;
        this.sizeParamDataHandler();
      })
    },
    sizeParamDataHandler() {
      this.sizeParamData.forEach(item => {
        if (item.attrs.length > 0) {
          item.attrs.map(attrItem => {
            this.$set(attrItem, 'baseAttrs', {
              attrValues: [],
              showDesc: "",
            })
          })
        }
      })
    },
    valueSelectHandler(data) {
      return data.split(";")
    },
  }
}
</script>

<style scoped>
  .sizeParams {
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
