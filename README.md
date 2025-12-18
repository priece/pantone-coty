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
   git clone https://github.com/priece/pantone-coty.git
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

## Language Support
- English: `README.md` (this file)
- Chinese: `README.zh-CN.md`