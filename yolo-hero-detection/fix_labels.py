import os
import shutil

# 你的数据集根路径
root = "datasets/BVN"

# 源路径：标签已经在labels/train里了
src_labels = os.path.join(root, "labels", "train")
# 目标路径：val标签要放到labels/val里
dst_val_labels = os.path.join(root, "labels", "val")

# 创建val标签文件夹
os.makedirs(dst_val_labels, exist_ok=True)

# 获取val文件夹里的所有图片（这些图片对应的标签要挪到val）
val_images = [f for f in os.listdir(os.path.join(root, "images", "val")) 
              if f.endswith(('.jpg', '.png', '.jpeg'))]

# 遍历val图片，把labels/train里对应的txt挪到labels/val
for img_name in val_images:
    txt_name = os.path.splitext(img_name)[0] + ".txt"
    src_path = os.path.join(src_labels, txt_name)
    dst_path = os.path.join(dst_val_labels, txt_name)
    
    if os.path.exists(src_path):
        shutil.move(src_path, dst_path)
        print(f"✅ 已移动标签：{txt_name} 到 val")
    else:
        print(f"⚠️  警告：{txt_name} 无对应标签（未标注，跳过）")

print("\n🎉 修复完成！labels/val 已同步val图片的标签，现在可以正常训练了")