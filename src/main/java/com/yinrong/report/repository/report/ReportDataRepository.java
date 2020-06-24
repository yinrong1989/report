package com.yinrong.report.repository.report;

import com.yinrong.report.entity.ReportData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Classname ReportTargetRepository
 * @Description
 * @Date 2020/6/16 6:54 下午
 * @Created by yinrong
 */
public interface ReportDataRepository extends JpaRepository<ReportData,Long> {
    List<ReportData> queryByTaskIdAndIsValid(@Param("taskId")Long taskId, @Param("isValid")String isValid);
}
