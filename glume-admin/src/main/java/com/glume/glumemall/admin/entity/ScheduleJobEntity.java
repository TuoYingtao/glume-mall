package com.glume.glumemall.admin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 定时任务
 * @author tuoyingtao
 * @create 2022-04-19 17:30
 */
@TableName("schedule_job")
public class ScheduleJobEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务id
     */
    @TableId
    private Long jobId;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务分组
     */
    private String jobGroup;
    /**
     * 调用目标字符串
     */
    private String invokeTarget;
    /**
     * spring bean名称
     */
    private String beanName;
    /**
     * cron表达式
     */
    private String cronExpression;
    /**
     * cron 计划策略：0-默认 、1-立即触发执行、2-触发一次执行、3-不触发立即执行
     */
    private Integer misfirePolicy;
    /**
     * 是否并发执行：0-允许、1-禁止
     */
    private Integer concurrent;
    /**
     * 任务状态  0：正常  1：暂停
     */
    private Integer status;
    /**
     * 参数
     */
    private String params;
    /**
     * 备注
     */
    private String remark;
    /**
     * 下一次执行时间
     */
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date nextValidTime;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date createTime;

    public ScheduleJobEntity() {
    }

    public ScheduleJobEntity(Long jobId, String jobName, String jobGroup, String invokeTarget, String beanName, String cronExpression, Integer misfirePolicy, Integer concurrent, Integer status, String params, String remark, Date nextValidTime, Date createTime) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.jobGroup = jobGroup;
        this.invokeTarget = invokeTarget;
        this.beanName = beanName;
        this.cronExpression = cronExpression;
        this.misfirePolicy = misfirePolicy;
        this.concurrent = concurrent;
        this.status = status;
        this.params = params;
        this.remark = remark;
        this.nextValidTime = nextValidTime;
        this.createTime = createTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getInvokeTarget() {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget) {
        this.invokeTarget = invokeTarget;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Integer getMisfirePolicy() {
        return misfirePolicy;
    }

    public void setMisfirePolicy(Integer misfirePolicy) {
        this.misfirePolicy = misfirePolicy;
    }

    public Integer getConcurrent() {
        return concurrent;
    }

    public void setConcurrent(Integer concurrent) {
        this.concurrent = concurrent;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getNextValidTime() {
        return nextValidTime;
    }

    public void setNextValidTime(Date nextValidTime) {
        this.nextValidTime = nextValidTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleJobEntity that = (ScheduleJobEntity) o;
        return jobId.equals(that.jobId) && Objects.equals(jobName, that.jobName) && Objects.equals(jobGroup, that.jobGroup) && Objects.equals(invokeTarget, that.invokeTarget) && Objects.equals(beanName, that.beanName) && Objects.equals(cronExpression, that.cronExpression) && Objects.equals(misfirePolicy, that.misfirePolicy) && Objects.equals(concurrent, that.concurrent) && Objects.equals(status, that.status) && Objects.equals(params, that.params) && Objects.equals(remark, that.remark) && Objects.equals(nextValidTime, that.nextValidTime) && Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, jobName, jobGroup, invokeTarget, beanName, cronExpression, misfirePolicy, concurrent, status, params, remark, nextValidTime, createTime);
    }

    @Override
    public String toString() {
        return "ScheduleJobEntity{" +
                "jobId=" + jobId +
                ", jobName='" + jobName + '\'' +
                ", jobGroup='" + jobGroup + '\'' +
                ", invokeTarget='" + invokeTarget + '\'' +
                ", beanName='" + beanName + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                ", misfirePolicy=" + misfirePolicy +
                ", concurrent=" + concurrent +
                ", status=" + status +
                ", params='" + params + '\'' +
                ", remark='" + remark + '\'' +
                ", nextValidTime=" + nextValidTime +
                ", createTime=" + createTime +
                '}';
    }
}
