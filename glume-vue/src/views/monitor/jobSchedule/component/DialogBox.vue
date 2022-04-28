<template>
  <div>
    <el-dialog :title="title" :visible.sync="dialogVisible" append-to-body :before-close="cancel">
      <el-form ref="form" :model="formData" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务名称" prop="jobName">
              <el-input v-model="formData.jobName" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="任务分组" prop="jobGroup">
              <el-select style="width: 100%" v-model="formData.jobGroup" placeholder="请选择">
                <el-option v-for="item in jobGroupType" :key="item.id" :label="item.name" :value="item.name"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Bean 名称"  prop="beanName">
              <el-input v-model="formData.beanName" placeholder="请输入Bean 名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="invokeTarget">
              <span slot="label">
                调用方法
                <el-tooltip placement="top">
                  <div slot="content">
                    Bean调用示例：jobTest.jobParams('ry')
                    <br />Class类调用示例：com.glume.glumemall.JobTest.jobParams('ry')
                    <br />参数说明：支持字符串（' '），布尔类型（true/false），长整型（L），浮点型（D），整型
                  </div>
                  <i class="el-icon-question"></i>
                </el-tooltip>
              </span>
              <el-input v-model="formData.invokeTarget" placeholder="请输入调用目标字符串" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="cron表达式" prop="cronExpression">
              <el-input v-model="formData.cronExpression" placeholder="请输入cron执行表达式">
                <template slot="append">
                  <el-button type="primary" @click="handleShowCron">生成表达式<i class="el-icon-time el-icon--right"></i></el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="错误策略" prop="misfirePolicy">
              <el-radio-group v-model="formData.misfirePolicy" size="small">
                <el-radio-button label="1">立即执行</el-radio-button>
                <el-radio-button label="2">执行一次</el-radio-button>
                <el-radio-button label="3">放弃执行</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否并发" prop="concurrent">
              <el-radio-group v-model="formData.concurrent" size="small">
                <el-radio-button label="0">允许</el-radio-button>
                <el-radio-button label="1">禁止</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="formData.status">
                <el-radio :label="0">正常</el-radio>
                <el-radio :label="1">暂停</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 4}" v-model="formData.remark" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Cron表达式生成器" :visible.sync="openCron" append-to-body destroy-on-close class="scrollbar">
      <crontab @hide="openCron=false" @fill="crontabFill" :expression="expression"></crontab>
    </el-dialog>
  </div>
</template>

<script>
import Crontab from '../component/Crontab'
export default {
  name: "DialogBox",
  components: { Crontab },
  props: {
    title: String,
    data: Object,
  },
  watch: {
    data(val) {
      this.formData = val;
      this.expression = val.cronExpression;
    }
  },
  data() {
    return {
      dialogVisible: false,
      openCron: false,
      formData: {
        misfirePolicy: 1,
        concurrent: 1,
        status: 1,
      },
      // 传入的表达式
      expression: "",
      jobGroupType: [{id: 1, name: "DEFAULT"},{id: 2, name: "SYSTEM"}],
      rules: {
        jobName: [{ required: true, message: "任务名称不能为空", trigger: "blur" }],
        jobGroup: [{ required: true, message: "任务分组不能为空", trigger: "blur" }],
        invokeTarget: [{ required: true, message: "调用方法不能为空", trigger: "blur" }],
        beanName: [{ required: true, message: "Bean 名称不能为空", trigger: "blur" }],
        cronExpression: [{ required: true, message: "cron表达式不能为空", trigger: "blur" }],
        misfirePolicy: [{ required: true, message: "错误策略不能为空", trigger: "blur" }],
        concurrent: [{ required: true, message: "并发不能为空", trigger: "blur" }],
        status: [{ required: true, message: "状态不能为空", trigger: "blur" }],
      },
    }
  },
  methods: {
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.$emit("submitForm",this.formData);
        }
      })
    },
    handleShowCron() {
      this.openCron = true;
    },
    /** 确定后回传值 */
    crontabFill(value) {
      this.formData.cronExpression = value;
    },
    cancel() {
      this.close();
    },
    open() {
      this.dialogVisible = true;
    },
    close() {
      this.dialogVisible = false;
    }
  }
}
</script>

<style scoped>

</style>
