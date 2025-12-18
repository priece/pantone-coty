# Pantone Color of the Year App

## Description
A beautiful Android application that showcases Pantone's Color of the Year (COTY) from 2000 to 2026, with detailed information and smooth swipe navigation.

## Features
- 🎨 **Color Display**: Shows Pantone Color of the Year in a elegant rectangular box
- 📋 **Detailed Information**: Displays year, English name, Chinese name, and Pantone number
- 🔄 **Swipe Navigation**: 
  - Swipe up to view previous years
  - Swipe down to view next years (loops to earliest year when reaching boundaries)
- 🎯 **Default Display**: Opens with 2025 Pantone Color "Mocha Mousse"
- 📊 **Data Driven**: All color data sourced from CSV file for easy updates

## Requirements
- **Platform**: Android
- **Minimum SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 33 (Android 13)
- **Compile SDK**: 34 (Android 14)
- **Development Language**: Kotlin
- **Build System**: Gradle

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/pantone-coty.git
   ```
2. Open the project in Android Studio
3. Build and run the app on an Android device or emulator

## Usage
1. Launch the app - you'll see the 2025 Pantone Color of the Year
2. Swipe **up** to view previous years (2024, 2023, etc.)
3. Swipe **down** to view future years (2026, then loops back to 2000)
4. Read the detailed information about each color in the bottom section

## Technical Details
- **Architecture**: Single Activity with ViewBinding
- **Gesture Handling**: Implemented using Android's GestureDetector
- **Animation**: Spring animation for smooth color transitions
- **Data Parsing**: CSV file reading from raw resources
- **UI Design**: ConstraintLayout with golden ratio proportions
- **Immersive Mode**: Fullscreen display with hidden navigation bars

## Data Source
The color data is stored in `app/src/main/res/raw/coty.csv` and includes:
- Year
- English name
- Chinese name
- Pantone number
- Hex color code

## License
This project is open source and available under the MIT License.

---

# 潘通年度色应用

## 项目描述
一个精美的Android应用，展示2000年至2026年的潘通年度色（COTY），包含详细信息和流畅的滑动导航。

## 功能特点
- 🎨 **颜色展示**：在优雅的矩形框中显示潘通年度色
- 📋 **详细信息**：显示年份、英文名、中文名和潘通色号
- 🔄 **滑动导航**：
  - 向上滑动查看上一年度
  - 向下滑动查看下一年度（到达边界时循环到最早年份）
- 🎯 **默认显示**：打开应用显示2025年潘通色「摩卡慕斯」
- 📊 **数据驱动**：所有颜色数据来自CSV文件，便于更新

## 系统要求
- **平台**：Android
- **最低SDK**：24（Android 7.0 Nougat）
- **目标SDK**：33（Android 13）
- **编译SDK**：34（Android 14）
- **开发语言**：Kotlin
- **构建系统**：Gradle

## 安装方法
1. 克隆仓库：
   ```bash
   git clone https://github.com/your-username/pantone-coty.git
   ```
2. 在Android Studio中打开项目
3. 在Android设备或模拟器上构建并运行应用

## 使用说明
1. 启动应用 - 您将看到2025年潘通年度色
2. **向上滑动**查看上一年度（2024年、2023年等）
3. **向下滑动**查看下一年度（2026年，然后循环回到2000年）
4. 在底部区域阅读每种颜色的详细信息

## 技术细节
- **架构**：单Activity + ViewBinding
- **手势处理**：使用Android的GestureDetector实现
- **动画效果**：弹簧动画实现流畅的颜色过渡
- **数据解析**：从raw资源中读取CSV文件
- **UI设计**：使用黄金比例的ConstraintLayout
- **沉浸式模式**：全屏显示，隐藏导航栏

## 数据来源
颜色数据存储在 `app/src/main/res/raw/coty.csv` 中，包含：
- 年份
- 英文名
- 中文名
- 潘通色号
- 十六进制颜色代码

## 许可证
本项目采用MIT许可证开源。