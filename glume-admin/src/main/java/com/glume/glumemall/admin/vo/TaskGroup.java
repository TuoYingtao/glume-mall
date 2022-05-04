package com.glume.glumemall.admin.vo;

import java.io.Serializable;
import java.util.Objects;

/**
 * 定时任务分组
 * @author tuoyingtao
 * @create 2022-05-03 11:25
 */
public class TaskGroup implements Serializable {
    public static final long serialVersionUID = 1L;

    /**
     * 分组ID
     */
    private Long id;
    /**
     * 分组名称
     */
    private String jobGroup;

    public TaskGroup() {
    }

    public TaskGroup(Long id, String jobGroup) {
        this.id = id;
        this.jobGroup = jobGroup;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskGroup taskGroup = (TaskGroup) o;
        return Objects.equals(id, taskGroup.id) && Objects.equals(jobGroup, taskGroup.jobGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jobGroup);
    }

    @Override
    public String toString() {
        return "TaskGroup{" +
                "id=" + id +
                ", jobGroup='" + jobGroup + '\'' +
                '}';
    }
}
