# 瑞雪物语 (MerrySnow)

一个以雪为核心的 Minecraft Fabric 机制调整模组，让你对原版的降雪、积雪、结冰和刷怪机制拥有细粒度控制。

## 功能

### ❄️ 冻寂魔国（全群系天气覆盖）

让所有群系都能下雪，就算是沙漠也不例外：

- 配置文件中设置 `enforceSnowWeather = "true"`，再使用 `/weather rain` 指令，即可让整个维度飘雪。
- 这只是**客户端**的天气形态变化，不会强制产生积雪或结冰（见下方）。
- 支持按群系细调，通过 `weatherModify` 列表指定。

### 🧊 冰天雪地（积雪与结冰控制）

控制积雪覆盖和水面结冰，可全局或按群系设置：

- `enforceSnowCover = "true"` — 强制全群系积雪（需要天气开启）。
- `enforceIceFreeze = "true"` — 强制全群系结冰。
- 按群系覆盖：`snowModify` 和 `iceModify` 列表。

### ☀️ 晴雪

让日月与雨雪共存。通过调整**晴雪系数**，在完全晴朗和完全天气之间自由过渡，可简单理解为小雪、中雪、大雪：

- 配置文件：`sunnySnow`（0.1–1.0，默认 0.5）。
- 游戏内：按住热键（默认 `M`）+ 鼠标滚轮实时调整。
- 开启 `randomSunnySnow` 可让每次天气切换时使用随机系数。
- 纯客户端功能，也可用于临时关闭天气效果。

效果：
![img](https://resource-api.xyeidc.com/client/pics/87a23ce2)

光影下效果：
![img](https://resource-api.xyeidc.com/client/pics/438665ad)

### ⛄ 瑞雪

雪天环境下的便利优化：

- 细雪具有台阶碰撞箱，且无冷冻伤害（`merryPowderSnow`）
- 下雪区块不刷怪（`merrySnowWeather`）
- 雪层无碰撞体积，不再挡路（`merrySnowLayer`）

### 🌱 静谧四季兼容

安装 [Serene Seasons](https://github.com/Glitchfiend/SereneSeasons) 或 [Serene Seasons Plus](https://github.com/xGabou/SereneSeasonsPlus) 后：

- 保持 `sereneSeasonsCompatibility = true`（默认），并将强制选项设为 `"default"` — MerrySnow 会遵循静谧四季的季节逻辑来决定降水、积雪和结冰。
- 按群系的显式覆盖仍然优先生效。将强制选项设为 `"true"` / `"false"` 会覆盖一切。
- 寒冷季节（深秋至早春）期间，刷怪阻止范围会自动扩大到所有可降水的群系。

## 指令

| 指令 | 说明 |
|------|------|
| `/biomeid current` | 获取当前所在群系 ID |
| `/biomeid all` | 获取所有已注册群系 ID 的 JSON 数组 |

## 配置

配置文件位于 `config/merrysnow.toml`（基于 ForgeConfigAPI Port）。可通过 [Mod Menu](https://modrinth.com/mod/modmenu) + [Cloth Config](https://modrinth.com/mod/cloth-config) 打开游戏内 GUI。

### 瑞雪功能

| 键 | 类型 | 默认值 | 说明 |
|----|------|--------|------|
| `merrySnowLayer` | 布尔 | `true` | 雪层无碰撞 |
| `merryPowderSnow` | 布尔 | `true` | 细雪台阶碰撞且无冷冻伤害 |
| `merrySnowWeather` | 布尔 | `true` | 下雪区块不刷怪 |

### 晴雪效果

| 键 | 类型 | 默认值 | 说明 |
|----|------|--------|------|
| `sunnySnow` | 小数 | `0.5` | 晴雪系数（0.1–1.0） |
| `randomSunnySnow` | 布尔 | `false` | 随机晴雪效果 |

### 群系降雪

| 键 | 类型 | 默认值 | 说明 |
|----|------|--------|------|
| `enforceSnowWeather` | 字符串 | `"true"` | 全群系下雪：`"true"`、`"false"`、`"default"` |
| `enforceSnowCover` | 字符串 | `"false"` | 全群系积雪 |
| `enforceIceFreeze` | 字符串 | `"default"` | 全群系结冰 |
| `weatherModify` | 字符串列表 | `[]` | 按群系覆盖天气：`"命名空间:群系=snow\|rain\|none"` |
| `snowModify` | 字符串列表 | `[]` | 按群系覆盖积雪：`"命名空间:群系=true\|false"` |
| `iceModify` | 字符串列表 | `[]` | 按群系覆盖结冰：`"命名空间:群系=true\|false"` |

### 静谧四季兼容

| 键 | 类型 | 默认值 | 说明 |
|----|------|--------|------|
| `sereneSeasonsCompatibility` | 布尔 | `true` | 启用后，未设置显式覆盖时将遵循静谧四季的季节逻辑 |

### 按群系覆盖示例

```toml
weatherModify = ["minecraft:desert=snow", "minecraft:jungle=rain", "minecraft:ocean=none"]
snowModify = ["minecraft:forest=true", "minecraft:plains=false"]
iceModify = ["minecraft:frozen_ocean=false"]
```

- 天气可选值：`snow`（雪）、`rain`（雨）、`none`（无）
- 积雪/结冰可选值：`true`、`false`

## 前置依赖

- **Minecraft** 26.1.2
- **Fabric Loader** 0.19.3+
- **Fabric API** 0.155.2+
- **ForgeConfigAPI Port** 26.1.5+

### 可选依赖

- [Cloth Config](https://modrinth.com/mod/cloth-config) — 游戏内配置界面
- [Mod Menu](https://modrinth.com/mod/modmenu) — 配置入口集成
- [Serene Seasons](https://modrinth.com/mod/serene-seasons) — 季节天气联动
- [Serene Seasons Plus](https://modrinth.com/mod/serene-seasons-plus) — 扩展季节机制

## 构建

```bash
./gradlew build
```

输出 JAR 位于 `build/libs/MerrySnow-fabric-26.1.2-1.0.0.jar`。

环境要求：
- JDK 25+
- Gradle 9.5.1（项目已包含 wrapper）

## 未来计划

- 生物踩过的积雪层数 -1，在积雪中受到轻微减速，层数越高减速越多
- 与下雪、积雪相关的道具，如雪天娃娃
- 滑冰玩法

## 开源协议

GPL-3.0

## 致谢

- **Flechazo** — 原作者
- **MerrySnow** — 开发团队
- **Tower of Sighs** — 维护者
