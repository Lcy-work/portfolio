-- ============================================
-- 将编号44的反馈改为编号40，并设置为未指派状态
-- ============================================

-- 步骤1：先查询44号的数据（确认数据存在）
SELECT * FROM aqi_feedback WHERE af_id = 44;

-- 步骤2：检查40号是否已存在（如果存在需要先删除或跳过）
SELECT * FROM aqi_feedback WHERE af_id = 40;

-- 步骤3：如果40号不存在，复制44号的数据并插入为新记录（编号为40）
INSERT INTO aqi_feedback (af_id, supervisor_id, province_id, city_id, address, 
                          estimated_grade, information, tel_id, af_date, af_time, 
                          state, gm_id, create_date, create_time, update_date, update_time)
SELECT 40, supervisor_id, province_id, city_id, address, 
       estimated_grade, information, tel_id, af_date, af_time, 
       0 as state,        -- 改为未指派
       NULL as gm_id,     -- 清空网格员
       create_date, create_time, CURDATE(), CURTIME()
FROM aqi_feedback WHERE af_id = 44;

-- 步骤4：删除原来的44号记录
DELETE FROM aqi_feedback WHERE af_id = 44;

-- 步骤5：验证结果
SELECT af_id, state, gm_id FROM aqi_feedback WHERE af_id = 40;
