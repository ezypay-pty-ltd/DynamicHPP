# DynamicHPP - Kotlin Multiplatform Payment Page

This is a Kotlin Multiplatform (KMP) project that demonstrates how to build a cross-platform payment form with real-time credit card validation for both Android and iOS from a single codebase.

## What is Kotlin Multiplatform?

Kotlin Multiplatform is a technology that allows you to write shared business logic once in Kotlin and use it across multiple platforms (Android, iOS, web, desktop). With KMP:
- You write common code ONCE in Kotlin
- You create platform-specific UIs that connect to this common code
- You share models, business logic, and networking code across platforms

## How This App Works

This payment page app demonstrates the KMP approach by:

1. **Shared Business Logic**: All credit card validation rules are written once in Kotlin and used on both platforms
2. **Platform-Specific UIs**: Native UIs are implemented using Jetpack Compose for Android and SwiftUI for iOS
3. **Common ViewModel**: A shared ViewModel manages the app state and validation logic for both platforms

### App Flow

1. User enters credit card details (number, name, expiry date, CVV)
2. Real-time validation occurs as they type, with immediate feedback
3. On submission, all fields are validated together
4. A simulated payment process shows success or failure

## Project Structure Explained

The project follows a standard KMP structure:

```
DynamicHPP/
├── shared/                 👉 Shared code for both platforms
│   └── src/
│       ├── commonMain/     👉 Code shared between Android and iOS
│       │   └── kotlin/com/ezypay/dhpp/
│       │       ├── model/
│       │       │   └── CreditCard.kt          👉 Data model with validation logic
│       │       └── viewmodel/
│       │           └── CreditCardViewModel.kt 👉 Shared business logic
│       ├── androidMain/    👉 Android-specific implementations
│       └── iosMain/        👉 iOS-specific implementations
├── composeApp/             👉 Android app using Jetpack Compose
└── iosApp/                 👉 iOS app using SwiftUI
```

## How the Code is Structured

### 1. Shared Code (KMP Magic! 🪄)

The `shared` module contains code that works on both platforms:

#### Models (`CreditCard.kt`):
- Defines the credit card data structure
- Contains validation logic for card number, name, expiry date, and CVV
- Uses the Luhn algorithm to validate card numbers

#### ViewModel (`CreditCardViewModel.kt`):
- Manages the UI state using Kotlin flows
- Provides validation as users type
- Handles form submission and simulated payment processing

#### Platform-Specific Implementations:
- Some functions need platform-specific implementations (like getting the current date)
- These use Kotlin's `expect/actual` pattern - you declare what you need in common code with `expect` and implement it for each platform with `actual`

### 2. Platform UIs 

#### Android (`composeApp`):
- Uses Jetpack Compose for a modern, declarative UI
- Connects to the shared ViewModel to get state updates
- Provides a Material Design payment form experience

#### iOS (`iosApp`):
- Uses SwiftUI for a native iOS experience
- Connects to the same shared ViewModel (through KMP's interoperability)
- Follows iOS design guidelines

## Understanding KMP Concepts Used

### 1. Shared Code & Platform-Specific Code

In the `CreditCard.kt` file, notice:
```kotlin
// This tells KMP: "I need this function, but its implementation depends on the platform"
expect fun getCurrentYear(): Int
expect fun getCurrentMonth(): Int 
```

And then in platform-specific modules:
```kotlin
// Android implementation
actual fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

// iOS implementation
actual fun getCurrentYear(): Int = NSCalendar.currentCalendar.component(NSCalendarUnit.Year, fromDate: NSDate())
```

### 2. Dependency Injection

The ViewModel is shared, but how it's injected differs:
- Android: Using Android's ViewModel architecture
- iOS: Using a wrapper that exposes it to Swift

### 3. Coroutines & Flows

The app uses Kotlin coroutines and flows for reactive programming:
- `StateFlow` provides reactive UI state updates
- `viewModelScope` manages coroutine lifecycles

## Getting Started with This Code

### Prerequisites

- Android Studio Arctic Fox or higher
- Xcode 14 or higher (for iOS)
- JDK 11+

### Android

1. Open the project in Android Studio
2. Select the 'composeApp' configuration
3. Click Run

### iOS

1. Open `iosApp/iosApp.xcworkspace` in Xcode
2. Select a simulator or device
3. Run the project

## Testing the App

### Android Testing

1. **Run Unit Tests**:
   ```bash
   ./gradlew :shared:testDebugUnitTest       # Test shared code
   ./gradlew :composeApp:testDebugUnitTest   # Test Android-specific code
   ```

2. **Run Instrumented Tests**:
   ```bash
   ./gradlew :composeApp:connectedDebugAndroidTest
   ```

3. **Manual Testing**:
   - Use these test card numbers:
     - Valid: `4111 1111 1111 1111`
     - Invalid: `1234 5678 9012 3456`
   - Test validation by:
     - Entering invalid data in each field
     - Submitting with missing information
     - Submitting with all valid data

### iOS Testing

1. **Run Unit Tests via Xcode**:
   - Open `iosApp/iosApp.xcworkspace` in Xcode
   - Select `Product > Test` or press `⌘+U`

2. **Run Tests via Command Line**:
   ```bash
   xcodebuild test -workspace iosApp/iosApp.xcworkspace -scheme iosApp -destination 'platform=iOS Simulator,name=iPhone 15 Pro'
   ```
   (Replace "iPhone 15 Pro" with your available simulator)

3. **Manual Testing**:
   - Use the same test card numbers as Android
   - Verify UI behaves correctly in both portrait and landscape modes
   - Check that validation messages appear correctly
   - Test keyboard behavior and field focus

### Testing Shared Code

1. **Run All Tests**:
   ```bash
   ./gradlew check
   ```

2. **Test Credit Card Validation**:
   Run the app on both platforms and test these scenarios:
   - Card number passes Luhn check but is expired
   - Name validation with different characters
   - CVV with different lengths (3-4 digits)
   - Various expiry date combinations

## Building and Running on Physical Devices

### Android Device Setup

1. **Enable Developer Options on your Android device**:
   - Go to `Settings > About phone`
   - Tap `Build number` 7 times to enable Developer Options
   - Go back to Settings, you'll now see `Developer Options`
   - Enable `USB debugging`

2. **Connect your Android device**:
   - Connect your phone to your computer with a USB cable
   - If prompted on your phone, allow USB debugging
   - Verify connection by running `adb devices` in terminal

3. **Build and Install via Android Studio**:
   - Open the project in Android Studio
   - Select your connected device from the dropdown menu
   - Click the Run button (▶️)
   - The app will build and install on your device

4. **Build and Install via Command Line**:
   ```bash
   # Build the app
   ./gradlew :composeApp:assembleDebug
   
   # Install on connected device
   ./gradlew :composeApp:installDebug
   ```

### iOS Device Setup

1. **Xcode Developer Account Setup**:
   - Open Xcode and go to `Xcode > Preferences > Accounts`
   - Add your Apple ID and sign in
   - If you're part of a development team, it should appear here

2. **Prepare your iOS device**:
   - Connect your iPhone to your Mac with a USB cable
   - Trust the computer if prompted on your phone
   - Unlock your device and keep it unlocked during installation

3. **Configure the Project**:
   - Open `iosApp/iosApp.xcworkspace` in Xcode
   - Select the `iosApp` target
   - In the `Signing & Capabilities` tab:
     - Select your Team from the dropdown
     - Xcode should automatically manage signing
   - If you don't have a paid Apple Developer account, you can still run on your device but will need to:
     - Use a personal team profile
     - The app will expire after 7 days
     - The app will only run on your personal devices

4. **Build and Run**:
   - Select your device from the device dropdown at the top of Xcode
   - Click the Play button to build and run
   - First installation may require you to trust the developer profile:
     - On iOS go to `Settings > General > Device Management`
     - Select your developer profile and trust it

### Troubleshooting

#### Android Issues:
- If your device doesn't appear in Android Studio, try:
  - Restarting adb: `adb kill-server && adb start-server`
  - Changing USB modes on your phone
  - Using a different USB cable

#### iOS Issues:
- If you get a provisioning profile error:
  - Go to `Product > Destination` and ensure your device is selected
  - Try restarting Xcode and reconnecting your device
  - Verify your Apple ID has proper permissions

## Key KMP Concepts for Beginners

1. **expect/actual**: Define what you need in common code (`expect`) and provide platform implementations (`actual`)
2. **Common module**: Contains all shared code between platforms
3. **Platform modules**: Contain platform-specific implementations
4. **Kotlin/Native**: Technology that compiles Kotlin to native code for iOS
5. **Cocoapods integration**: Allows iOS to use Kotlin code through Cocoapods

## Learn More

- [Kotlin Multiplatform Documentation](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [KMP Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)

## License

This project is licensed under the MIT License.