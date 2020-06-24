package com.yinrong.report.repository.report;

import com.yinrong.report.entity.ReportTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Classname ReportTargetRepository
 * @Description
 * @Date 2020/6/16 6:54 下午
 * @Created by yinrong
 */
public interface ReportTargetRepository extends JpaRepository<ReportTarget,Long> {
    List<ReportTarget> queryByTaskIdAndIsValid(@Param("taskId")Long taskId, @Param("isValid")String isValid);
}
