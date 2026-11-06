from ultralytics import YOLO

if __name__ == '__main__':
    model = YOLO("runs/train/bvn_train_4060/weights/best.pt")

    # 自动检测视频并保存
    model.predict(
        source="BVN.mp4",
        save=True,
        device=0
    )