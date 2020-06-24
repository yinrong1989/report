package com.yinrong.report.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Classname ReportTarget
 * @Description
 * @Date 2020/6/16 6:51 下午
 * @Created by yinrong
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report_target")
public class ReportTarget {

    private long id;
    private Timestamp createDate;
    private Timestamp updateDate;
    private String isValid;
    private String targetType;
    private String targetAddress;
    private String message;
    private Long taskId;

    @Id
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "create_date", nullable = false)
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "update_date", nullable = false)
    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    @Basic
    @Column(name = "is_valid", nullable = false, length = 1)
    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    @Basic
    @Column(name = "target_type", nullable = false, length = 20)
    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @Basic
    @Column(name = "target_address", nullable = true, length = 20)
    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    @Basic
    @Column(name = "message", nullable = true, length = 20)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name = "task_id", nullable = false, length = 200)
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportTarget that = (ReportTarget) o;
        return id == that.id &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(updateDate, that.updateDate) &&
                Objects.equals(isValid, that.isValid) &&
                Objects.equals(targetType, that.targetType) &&
                Objects.equals(targetAddress, that.targetAddress) &&
                Objects.equals(message, that.message) &&
                Objects.equals(taskId, that.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createDate, updateDate, isValid, targetType, targetAddress, message, taskId);
    }
}
