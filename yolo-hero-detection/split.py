import os
import random
import shutil

root_dir = "D:/ultralytics-main/datasets/BVN"
train_ratio = 0.8
random.seed(42)

images_train = os.path.join(root_dir, "images", "train")
images_val = os.path.join(root_dir, "images", "val")
labels_train = os.path.join(root_dir, "labels", "train")
labels_val = os.path.join(root_dir, "labels", "val")

os.makedirs(images_val, exist_ok=True)
os.makedirs(labels_val, exist_ok=True)

all_imgs = [f for f in os.listdir(images_train) if f.endswith((".jpg", ".png", ".jpeg"))]
random.shuffle(all_imgs)
train_cnt = int(len(all_imgs) * train_ratio)
val_imgs = all_imgs[train_cnt:]

for img in val_imgs:
    src_img = os.path.join(images_train, img)
    dst_img = os.path.join(images_val, img)
    shutil.move(src_img, dst_img)

    lbl = os.path.splitext(img)[0] + ".txt"
    src_lbl = os.path.join(labels_train, lbl)
    dst_lbl = os.path.join(labels_val, lbl)
    if os.path.exists(src_lbl):
        shutil.move(src_lbl, dst_lbl)

print(f"✅ 拆分完成！训练集：{train_cnt}张，验证集：{len(val_imgs)}张")