package com.glume.glumemall.admin.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * 定时任务日志
 * @author tuoyingtao
 * @create 2022-04-22 14:02
 */
@TableName("schedule_job_log")
public class ScheduleJobLogEntity implements Serializable {
    private static final long serialVersionUID = 1;

    /**
     * 任务日志id
     */
    @TableId
    private Long logId;
    /**
     * 任务id
     */
    private Long jobId;
    /**
     * 任务状态    0：成功    1：失败
     */
    private Integer status;
    /**
     * 日志信息
     */
    private String jobMessage;
    /**
     * 异常信息
     */
    private String exceptionInfo;
    /**
     * 耗时(单位：毫秒)
     */
    private Long times;
    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 停止时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date stopTime;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date createTime;

    public ScheduleJobLogEntity() {
    }

    public ScheduleJobLogEntity(Long logId, Long jobId, Integer status, String jobMessage, String exceptionInfo, Long times, Date stringTime, Date stopTime, Date createTime) {
        this.logId = logId;
        this.jobId = jobId;
        this.status = status;
        this.jobMessage = jobMessage;
        this.exceptionInfo = exceptionInfo;
        this.times = times;
        this.startTime = stringTime;
        this.stopTime = stopTime;
        this.createTime = createTime;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getJobMessage() {
        return jobMessage;
    }

    public void setJobMessage(String jobMessage) {
        this.jobMessage = jobMessage;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
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
        ScheduleJobLogEntity that = (ScheduleJobLogEntity) o;
        return logId.equals(that.logId) && Objects.equals(jobId, that.jobId) && Objects.equals(status, that.status) && Objects.equals(jobMessage, that.jobMessage) && Objects.equals(exceptionInfo, that.exceptionInfo) && Objects.equals(times, that.times) && Objects.equals(startTime, that.startTime) && Objects.equals(stopTime, that.stopTime) && Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logId, jobId, status, jobMessage, exceptionInfo, times, startTime, stopTime, createTime);
    }

    @Override
    public String toString() {
        return "ScheduleJobLogEntity{" +
                "logId=" + logId +
                ", jobId=" + jobId +
                ", status=" + status +
                ", jobMessage='" + jobMessage + '\'' +
                ", exceptionInfo='" + exceptionInfo + '\'' +
                ", times=" + times +
                ", startTime=" + startTime +
                ", stopTime=" + stopTime +
                ", createTime=" + createTime +
                '}';
    }
}
