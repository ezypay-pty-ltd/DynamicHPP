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