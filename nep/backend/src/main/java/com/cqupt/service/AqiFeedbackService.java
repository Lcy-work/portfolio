package com.cqupt.service;

import com.cqupt.dto.AfPageRequestDto;
import com.cqupt.dto.PageResponseDto;
import com.cqupt.pojo.AqiFeedback;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-05 14:55
 * @Description:
 */
public interface AqiFeedbackService {

    // ======== nepm ========
    // 分页反省反馈列表
    PageResponseDto<AqiFeedback> listAqiFeedbackPage(AfPageRequestDto request);
    // 分配指派任务
    int updateAqiFeedbackAssign(AqiFeedback aqiFeedback);

    // ======== nepg ========
    // 根据GmId和state状态查询网格员的反馈信息
    List<AqiFeedback> listAqiFeedbackByGmId(AqiFeedback aqiFeedback);

    // 根据id查询反馈信息
    List<AqiFeedback> getAqiFeedbackById(Integer afId);

    // 更新AQI反馈信息
    int updateAqiFeedbackState(AqiFeedback aqiFeedback);

    // ======== neps ========
    // 根据公众手机号查询反馈列表
    List<AqiFeedback> listAqiFeedbackByTelId(String id);

    // 保存AQI反馈信息
    int saveAqiFeedback(AqiFeedback aqiFeedback);
}