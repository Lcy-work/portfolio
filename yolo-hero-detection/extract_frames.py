import cv2
import os

# -------------------------- 可修改配置 --------------------------
# 1. 你的游戏/目标视频的完整路径（替换成你自己的！）
video_path = "D:/ultralytics-main/BVN.mp4"
# 2. 抽帧后图片的保存目录（会自动创建，不用手动建）
save_dir = "D:/ultralytics-main/datasets/BVN/images/train"
# 3. 抽帧步长：每隔多少帧保存一张（30=每秒1张，10=每秒3张，按需调整）
save_step = 30
# ----------------------------------------------------------------

# 创建保存目录
os.makedirs(save_dir, exist_ok=True)

# 打开视频文件
cap = cv2.VideoCapture(video_path)
if not cap.isOpened():
    print("❌ 错误：无法打开视频文件，请检查路径！")
    exit()

frame_count = 0
saved_count = 0

# 逐帧读取视频
while True:
    ret, frame = cap.read()
    # 视频读取完毕，退出循环
    if not ret:
        break
    
    # 按步长保存图片
    if frame_count % save_step == 0:
        # 图片命名：frame_000001.jpg 格式，方便排序
        img_name = f"frame_{saved_count:06d}.jpg"
        img_path = os.path.join(save_dir, img_name)
        cv2.imwrite(img_path, frame)
        saved_count += 1
    
    frame_count += 1

# 释放资源
cap.release()
print(f"✅ 抽帧完成！共保存 {saved_count} 张图片，路径：{save_dir}")