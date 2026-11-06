# 饥荒联机版（DST）Mod：荧光花种植精简版 glowberry-lite

在《饥荒联机版》真实游戏引擎上开发的玩法扩展 Mod：荧光花可种植、可铲除返还。

## 功能

- `lightbulb.lua`：给荧光果添加 `deployable` 组件，右键种植生成洞穴花 `flower_cave`
- `flower_cave.lua`：添加 `workable` 组件（`ACTIONS.DIG`），铲子铲掉返还荧光果（与草/树枝/浆果一致）

## 目录结构

```
dst-glowberry-mod/
├── modinfo.lua                 # mod 元信息（UTF-8 无 BOM）
├── modicon.tex / modicon.xml   # mod 图标
├── scripts/prefabs/
│   ├── lightbulb.lua
│   └── flower_cave.lua
└── 修改说明.md                   # 修改说明
```

## 安装

将本目录放入 `<DST>\mods\glowberry-lite\`，在 modoverrides.lua 中启用。

## 体现能力

- 游戏引擎源码阅读（components/deployable、workable、ACTIONS）
- Lua 脚本编写，遵循 DST 引擎 prefab 规范
