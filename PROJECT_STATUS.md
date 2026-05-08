# 起飞助手 Android Kotlin 版本 - 项目状态文档

## 项目概述

**项目名称：** 起飞助手 (Self-Care Tracker)
**项目类型：** Android 原生应用
**目标平台：** Android 5.0+ (API 21+)
**当前版本：** 1.0.0

这是一个从 Web 版本 (Vanilla JavaScript) 转写为 Kotlin 原生开发的 Android 应用，用于记录和分析个人习惯数据。

## 项目当前状态

### ✅ 已完成

- [x] 项目结构搭建
- [x] Gradle 构建配置
- [x] 数据模型定义
- [x] Room 数据库配置
- [x] Repository 仓储层
- [x] Hilt 依赖注入配置
- [x] 主题系统（亮色/暗色模式）
- [x] 国际化系统（中英文）
- [x] ViewModel 层实现
- [x] UI 组件库
- [x] 四个主要界面 Screen

### ⚠️ 待完善/可能存在的问题

- [ ] 需要真机或模拟器测试验证
- [ ] 部分 UI 细节可能需要调整
- [ ] 导入导出功能可能需要完善
- [ ] 编辑记录功能需要测试

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Kotlin | 1.9.20 | 开发语言 |
| Jetpack Compose | BOM 2023.10.01 | UI 框架 |
| Material Design 3 | - | 设计系统 |
| Room | 2.6.1 | 本地数据库 |
| Hilt | 2.48.1 | 依赖注入 |
| Kotlin Coroutines | 1.7.3 | 异步编程 |
| Kotlinx Serialization | 1.6.0 | JSON 序列化 |
| DataStore | 1.0.0 | 偏好设置存储 |
| Navigation Compose | 2.7.5 | 导航系统 |

## 项目结构

```
android-kotlin/
├── app/
│   ├── src/main/
│   │   ├── java/com/selfcare/tracker/
│   │   │   ├── MainActivity.kt          # 主入口 Activity
│   │   │   ├── SelfCareApp.kt           # Application 类 (Hilt)
│   │   │   │
│   │   │   ├── data/                    # 数据层
│   │   │   │   ├── local/              # Room 数据库
│   │   │   │   │   ├── AppDatabase.kt  # 数据库单例
│   │   │   │   │   ├── RecordDao.kt    # 记录 DAO
│   │   │   │   │   ├── MediumDao.kt    # 媒介 DAO
│   │   │   │   │   ├── FetishDao.kt    # 性癖 DAO
│   │   │   │   │   └── Converters.kt   # 类型转换器
│   │   │   │   ├── model/              # 数据模型
│   │   │   │   │   ├── Record.kt       # 记录实体
│   │   │   │   │   ├── Medium.kt       # 媒介实体
│   │   │   │   │   ├── Fetish.kt       # 性癖实体
│   │   │   │   │   ├── StatsConfig.kt  # 统计配置
│   │   │   │   │   ├── UIConfig.kt     # UI 配置
│   │   │   │   │   ├── AppSettings.kt  # 应用设置
│   │   │   │   │   ├── ExportData.kt  # 导出数据结构
│   │   │   │   │   └── StatsResult.kt  # 统计结果
│   │   │   │   └── repository/         # 仓储层
│   │   │   │       ├── RecordRepository.kt
│   │   │   │       ├── MediumRepository.kt
│   │   │   │       └── FetishRepository.kt
│   │   │   │
│   │   │   ├── di/                     # 依赖注入
│   │   │   │   └── AppModule.kt        # Hilt 模块
│   │   │   │
│   │   │   └── ui/                     # 界面层
│   │   │       ├── I18n.kt            # 国际化（中英文翻译）
│   │   │       ├── MainActivity.kt    # 主界面入口
│   │   │       ├── components/        # 可复用 UI 组件
│   │   │       │   ├── Charts.kt       # 饼图、柱状图、折线图
│   │   │       │   ├── CommonComponents.kt  # 统计卡片、记录项
│   │   │       │   └── SelectionComponents.kt # 选择器组件
│   │   │       ├── screens/           # 页面 Screen
│   │   │       │   ├── AddScreen.kt   # 添加记录界面
│   │   │       │   ├── RecordsScreen.kt    # 记录列表界面
│   │   │       │   ├── StatsScreen.kt      # 统计分析界面
│   │   │       │   └── SettingsScreen.kt   # 设置界面
│   │   │       ├── theme/             # 主题系统
│   │   │       │   ├── Theme.kt       # 颜色主题
│   │   │       │   └── Type.kt        # 字体样式
│   │   │       └── viewmodel/         # ViewModel
│   │   │           ├── TimerViewModel.kt   # 计时器 ViewModel
│   │   │           ├── RecordsViewModel.kt # 记录 ViewModel
│   │   │           ├── StatsViewModel.kt   # 统计 ViewModel
│   │   │           └── SettingsViewModel.kt # 设置 ViewModel
│   │   │
│   │   ├── res/                        # Android 资源
│   │   │   ├── drawable/             # 图标
│   │   │   ├── values/               # 字符串、颜色、主题
│   │   │   └── values-zh/            # 中文资源
│   │   │
│   │   └── AndroidManifest.xml
│   │
│   ├── build.gradle.kts               # App 模块构建配置
│   └── proguard-rules.pro             # 代码混淆规则
│
├── build.gradle.kts                   # 根项目构建配置
├── settings.gradle.kts                 # 项目设置
├── gradle.properties                  # Gradle 属性
└── README.md                         # 打包说明文档
```

## 功能模块

### 1. 添加记录 (AddScreen)

- 计时器模式：启动/停止计时
- 手动补录：输入分钟和秒
- 媒介单选
- 性癖多选
- 备注输入
- 满意度评分滑块 (0-10)

### 2. 记录列表 (RecordsScreen)

- 显示所有记录（按时间倒序）
- 显示时长与上一条的差异（红色=快/绿色=慢）
- 编辑记录
- 删除记录（带确认对话框）

### 3. 统计分析 (StatsScreen)

- 日期范围选择（本周/本月/上月/今年/自定义）
- 统计指标卡片：
  - 总次数
  - 平均时长
  - 最短时长
  - 最长时长
  - 频率（次/周）
  - 最长禁欲天数
- 对比上月百分比变化
- 最常用的媒介
- 媒介分布饼图
- 时长趋势折线图

### 4. 设置 (SettingsScreen)

- 语言切换（中文/英文）
- 主题切换（亮色/暗色）
- 媒介管理（添加/删除）
- 性癖管理（添加/删除）
- 关于页面

## 数据模型

### Record (记录)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键，时间戳 |
| startTime | Long | 开始时间（毫秒） |
| duration | Int | 时长（秒） |
| mediumId | String | 媒介 ID |
| fetishIds | String | 性癖 ID 列表（逗号分隔） |
| notes | String | 备注 |
| satisfaction | Int | 满意度 (0-10) |

### Medium (媒介)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| name | String | 名称 |

### Fetish (性癖)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键 |
| name | String | 名称 |

## 下一步工作

### 立即需要做

1. **环境搭建**
   - 安装 JDK 17+
   - 安装 Android SDK
   - 配置环境变量

2. **构建测试**
   - 运行 `gradlew assembleDebug` 验证项目能否编译
   - 修复任何编译错误
   - 在模拟器或真机上测试

3. **功能验证**
   - 测试计时器功能
   - 测试记录保存和显示
   - 测试统计分析计算
   - 测试主题切换
   - 测试语言切换

### 后续优化

1. **完善功能**
   - 实现完整的数据导入/导出
   - 添加编辑记录的详细表单
   - 完善日期选择器

2. **性能优化**
   - 优化图表渲染性能
   - 添加数据缓存机制

3. **发布准备**
   - 配置应用签名
   - 优化 APK 体积
   - 准备应用商店材料

## 构建指南

### 环境要求

- JDK 17 或更高版本
- Android SDK (API 21+)
- Windows/macOS/Linux

### 构建命令

```bash
# 进入项目目录
cd android-kotlin

# 调试版本构建
./gradlew assembleDebug

# 发布版本构建
./gradlew assembleRelease

# 清理并重新构建
./gradlew clean assembleDebug
```

### APK 输出位置

```
app/build/outputs/apk/debug/app-debug.apk      # 调试版本
app/build/outputs/apk/release/app-release.apk  # 发布版本
```

## 已知问题

1. TimerViewModel 中的计时器协程在某些情况下可能需要优化
2. RecordsScreen 的编辑功能需要完整测试
3. 导入/导出功能框架已搭建，具体实现可能需要调整

## 参考资源

- [Jetpack Compose 文档](https://developer.android.com/compose)
- [Room 数据库指南](https://developer.android.com/training/data-storage/room)
- [Hilt 依赖注入](https://developer.android.com/training/dependency-injection/hilt-android)
- [Material Design 3](https://m3.material.io/)

---

*文档生成时间：2026-05-08*
*基于原 Web 版本 v0.91.7 转写*
