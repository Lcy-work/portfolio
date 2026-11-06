# 计算机系统 Cache Lab（CS:APP）

CS:APP 经典实验：实现 CPU 缓存模拟器，并用分块技术优化矩阵转置的缓存性能。

## 内容

| 文件 | 说明 |
|------|------|
| `csim.c` | 缓存模拟器（S/E/B、LRU 替换、命中/缺失/驱逐统计） |
| `trans.c` | 矩阵转置（分块 32/64/61）降低缓存缺失率 |
| `《计算机系统(2)》项目作业二.docx` | 实验报告 |

## 编译运行

```bash
# csim：模拟缓存行为
gcc -o csim csim.c
./csim -s 5 -E 1 -b 5 -t traces/yi.trace

# trans：矩阵转置优化
gcc -o trans trans.c
./trans -M 32 -N 32
```

## 体现能力

- 底层计算机系统理解（内存层次、缓存一致性、LRU 替换策略）
- 性能优化思维（分块技术降低 miss rate）
- 读懂并复现经典系统级实验
