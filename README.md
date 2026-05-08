# 起飞助手 Android Kotlin 版本

纯 Kotlin 原生开发的 Android 应用，使用 Jetpack Compose 构建 UI。

## 功能特性

- 计时器模式：启动计时，记录活动时长
- 手动补录：直接输入时长补录记录
- 记录管理：查看、编辑、删除历史记录
- 统计分析：次数、频率、时长分布等统计
- 图表展示：饼图、折线图可视化
- 主题切换：亮色/暗色模式
- 中英文切换
- 媒介和性癖管理
- 数据导入导出

## 技术栈

- Kotlin
- Jetpack Compose
- Room 数据库
- Hilt 依赖注入
- MVVM 架构
- Material Design 3

## 打包方法

### 方法一：使用在线打包服务（推荐）

1. **PWABuilder**
   - 访问 https://www.pwabuilder.com/
   - 输入您的网站地址或上传打包好的 Web 文件
   - 选择 Android 平台
   - 点击 "Package for stores"
   - 下载生成的 APK

2. **AppsGeyser**
   - 访问 https://appsgeyser.com/
   - 选择 "Create App"
   - 输入 Web URL 或上传 HTML 文件
   - 自定义应用名称和图标
   - 点击 "Create"
   - 下载 APK

### 方法二：使用 Gradle 命令行打包

1. 确保已安装 Android SDK
2. 安装 Gradle（如果还没有）
3. 在项目根目录执行：

```bash
cd android-kotlin
gradlew assembleRelease
```

APK 文件将生成在 `app/build/outputs/apk/release/` 目录

### 方法三：使用 Docker 打包

```bash
docker run --rm -v $(pwd):/app -w /app gradle:8.2 gradle assembleRelease
```

### 方法四：使用在线 Gradle 构建服务

1. 将代码推送到 GitHub
2. 使用 GitHub Actions 自动构建
3. 下载构建产出的 APK

## 项目结构

```
android-kotlin/
├── app/
│   ├── src/main/
│   │   ├── java/com/selfcare/tracker/
│   │   │   ├── data/           # 数据层
│   │   │   │   ├── local/      # Room 数据库
│   │   │   │   ├── model/      # 数据模型
│   │   │   │   └── repository/ # 仓储
│   │   │   ├── di/             # 依赖注入
│   │   │   └── ui/             # 界面层
│   │   │       ├── components/ # UI 组件
│   │   │       ├── screens/    # 界面
│   │   │       ├── theme/      # 主题
│   │   │       ├── viewmodel/  # ViewModel
│   │   │       └── I18n.kt     # 国际化
│   │   ├── res/                # 资源文件
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## 版本信息

- 版本号：1.0.0
- 最低 Android 版本：Android 5.0 (API 21)
- 目标 Android 版本：Android 14 (API 34)
