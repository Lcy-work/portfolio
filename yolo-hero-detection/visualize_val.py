from ultralytics import YOLO
import os

if __name__ == '__main__':
    # 加载你训练好的模型
    model = YOLO("runs/train/bvn_train_4060/weights/best.pt")

    # 自动对验证集图片做可视化
    # 它会自动读取你 yaml 里的 val 图片路径，自动保存结果
    model.val(
        data="yolo_bvn.yaml",
        save_json=True,     # 保存结果json
        save_hybrid=True,   # 保存带标签的图
        save_conf=True,     # 显示置信度
        plots=True,         # 自动画 PR 曲线、混淆矩阵、验证效果图
        device=0            # 用你的4060
    )

print("✅ 可视化全部完成！去 runs/val/ 文件夹里看效果图")