package com.cqupt.service.Impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cqupt.dto.AfPageRequestDto;
import com.cqupt.dto.PageResponseDto;
import com.cqupt.mapper.AqiFeedbackMapper;
import com.cqupt.pojo.AqiFeedback;
import com.cqupt.service.AqiFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-05 14:55
 * @Description:
 */
@Service
public class AqiFeedbackServiceImpl implements AqiFeedbackService {

    @Autowired
    private AqiFeedbackMapper aqiFeedbackMapper;

    // ==================== nepm ====================
    @Override
    public PageResponseDto<AqiFeedback> listAqiFeedbackPage(AfPageRequestDto request) {
        // 完善防御：同时判空、小于1，彻底防止maxPageNum=0除零
        if (request.getPageNum() == null || request.getPageNum() < 1) {
            request.setPageNum(1);
        }
        if (request.getMaxPageNum() == null || request.getMaxPageNum() < 1) {
            request.setMaxPageNum(10);
        }

        PageResponseDto<AqiFeedback> response = new PageResponseDto<>();
        int totalRow = aqiFeedbackMapper.getAqiFeedbackCount(request);
        response.setTotalRow(totalRow);
        if (totalRow == 0) {
            return response;
        }

        int pageSize = request.getMaxPageNum();
        // 安全计算总页数，分母一定≥1
        int totalPageNum = totalRow / pageSize;
        if (totalRow % pageSize != 0) {
            totalPageNum = totalPageNum + 1;
        }
        response.setTotalPageNum(totalPageNum);

        int pageNum = request.getPageNum();
        response.setPageNum(pageNum);
        response.setPreNum(pageNum > 1 ? pageNum - 1 : pageNum);
        response.setNextNum(pageNum < totalPageNum ? pageNum + 1 : pageNum);

        request.setBeginNum((pageNum - 1) * pageSize);

        List<AqiFeedback> list = aqiFeedbackMapper.listAqiFeedbackPage(request);
        response.setMaxPageNum(pageSize);
        response.setList(list);

        return response;
    }

    @Override
    public int updateAqiFeedbackAssign(AqiFeedback aqiFeedback) {
        UpdateWrapper<AqiFeedback> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("af_id", aqiFeedback.getAfId());
        updateWrapper.set("state", 1);  // 1=已分配待处理
        if (aqiFeedback.getGmId() != null) {
            updateWrapper.set("gm_id", aqiFeedback.getGmId());
        }
        return aqiFeedbackMapper.update(null, updateWrapper);
    }

    // ==================== nepg ====================
    @Override
    public List<AqiFeedback> listAqiFeedbackByGmId(AqiFeedback aqiFeedback) {
        return aqiFeedbackMapper.listAqiFeedbackByGmId(aqiFeedback);
    }

    @Override
    public List<AqiFeedback> getAqiFeedbackById(Integer afId) {
        return aqiFeedbackMapper.getAqiFeedbackById(afId);
    }

    @Override
    public int updateAqiFeedbackState(AqiFeedback aqiFeedback) {
        UpdateWrapper<AqiFeedback> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("af_id", aqiFeedback.getAfId());
        updateWrapper.set("state", aqiFeedback.getState());
        return aqiFeedbackMapper.update(null, updateWrapper);
    }

    // ==================== neps ====================
    @Override
    public List<AqiFeedback> listAqiFeedbackByTelId(String id) {
        return aqiFeedbackMapper.listAqiFeedbackByTelId(id);
    }

    @Override
    public int saveAqiFeedback(AqiFeedback aqiFeedback) {
        return aqiFeedbackMapper.insert(aqiFeedback);
    }
}