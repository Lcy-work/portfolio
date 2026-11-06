package com.cqupt.mapper;

import com.cqupt.dto.AfPageRequestDto;
import com.cqupt.pojo.AqiFeedback;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-05 10:04
 * @Description:
 */
@Mapper
public interface AqiFeedbackMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<AqiFeedback> {

    // nepg
    List<AqiFeedback> listAqiFeedbackByGmId(AqiFeedback aqiFeedback);

    List<AqiFeedback> getAqiFeedbackById(Integer afId);

    // neps
    List<AqiFeedback> listAqiFeedbackByTelId(String id);

    // nepm
    List<AqiFeedback> listAqiFeedbackPage(AfPageRequestDto request);
    int getAqiFeedbackCount(AfPageRequestDto request);
}