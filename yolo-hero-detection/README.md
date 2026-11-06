# 王者荣耀英雄目标检测（YOLOv8）

自建王者荣耀英雄图像数据集，训练 YOLOv8 目标检测模型，识别 6 类英雄：牛魔、刘禅、吕布、马可波罗、小乔、元流之子。

## 技术栈

- PyTorch + Ultralytics YOLOv8（yolov8n）
- CUDA 12.1（RTX 4060）

## 数据集

- 自建 312 张标注图像：train 249 / val 63
- YOLO 格式标注（`datasets/BVN/labels/`）
- 类别配置见 `yolo_bvn.yaml`（6 类，中文名）

## 训练

```bash
pip install ultralytics
python train.py   # 训练 + 验证，输出 mAP
```

## 结果（`results/bvn_train_stable/`）

- `weights/best.pt` — 训练好的权重
- PR 曲线、混淆矩阵、`results.csv` 等评估产物
- **mAP50 ≈ 65.5%**（课程级 baseline）

## 数据准备脚本

- `extract_frames.py` 视频抽帧 → `split.py` 划分 train/val → `fix_labels.py` 修正标注
- `detect_video.py` / `visualize_val.py` 推理与可视化
