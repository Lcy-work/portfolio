import os
os.environ["ULTRALYTICS_OFFLINE"] = "1"
os.environ["ULTRALYTICS_DOWNLOAD_FONTS"] = "0"

from ultralytics import YOLO

if __name__ == '__main__':
    model = YOLO("yolov8n.pt")
    results = model.train(
        data="yolo_bvn.yaml",
        epochs=100,
        imgsz=640,
        batch=8,          # 从16降到8，降低GPU负载，数据队列不会空
        device="0",
        workers=4,        # 从8降到4，减少多进程冲突
        project="runs/train",
        name="bvn_train_4060_fix",
        exist_ok=True
    )
    metrics = model.val()
    print(f"✅ GPU训练完成！验证集mAP@0.5: {metrics.box.map:.4f}")