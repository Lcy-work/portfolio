package com.cqupt.dto;

import lombok.Data;
import java.util.List;

/**
 * @author: fengye
 * @create: 2026-06-09 10:09
 * @Description: 通用分页查询对象
 */
@Data
public class PageResponseDto<T> {

    private Integer totalRow;       // 总行数
    private Integer totalPageNum;   // 总页数
    private Integer preNum;         // 上一页
    private Integer nextNum;        // 下一页
    private Integer pageNum;        // 当前页
    private Integer maxPageNum;     // 每页最大行数
    private Integer beginNum;       // 起始记录数

    // 业务数据
    private List<T> list;
}