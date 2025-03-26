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

## Detailed Codebase Explanation for KMP Beginners

### The Shared Module: Heart of KMP Architecture

The `shared` module is the core of any KMP project, including this payment app. It contains all the code that will be used by both Android and iOS platforms. Think of it as the "write once, use everywhere" part of your app.

#### Structure of the Shared Module

```
shared/
├── src/
    ├── commonMain/       👉 Platform-independent code
    │   └── kotlin/
    │       └── com/ezypay/dhpp/
    │           ├── model/
    │           │   └── CreditCard.kt     👉 Data model shared across platforms
    │           └── viewmodel/
    │               └── CreditCardViewModel.kt   👉 Shared business logic
    ├── androidMain/      👉 Android-specific implementations
    │   └── kotlin/
    │       └── com/ezypay/dhpp/
    │           └── model/
    │               └── DateUtils.kt      👉 Android implementation of date functions
    └── iosMain/          👉 iOS-specific implementations
        └── kotlin/
            └── com/ezypay/dhpp/
                └── model/
                    └── DateUtils.kt      👉 iOS implementation of date functions
```

#### What Each Part Does

1. **commonMain**: Contains all the platform-independent code
   - `CreditCard.kt`: Defines the data structure and validation logic
   - `CreditCardViewModel.kt`: Handles UI state and business logic

2. **androidMain**: Contains Android-specific implementations
   - Implements the `actual` part of `expect/actual` declarations
   - Example: Using Android's Calendar API to get current date

3. **iosMain**: Contains iOS-specific implementations
   - Implements the `actual` part of `expect/actual` declarations for iOS
   - Example: Using iOS's NSCalendar to get current date

### Deep Dive: How Everything Connects

#### 1. The Model Layer (CreditCard.kt)

This defines our credit card data and validation logic:

```kotlin
// In shared/src/commonMain/.../model/CreditCard.kt
data class CreditCard(
    val number: String = "",
    val holderName: String = "",
    val expiryMonth: String = "",
    val expiryYear: String = "",
    val cvv: String = ""
) {
    companion object {
        fun validateNumber(number: String): Boolean {
            // Validation logic is written ONCE and used everywhere
            return CARD_NUMBER_PATTERN.matches(digitsOnly) && validateLuhn(digitsOnly)
        }
        
        // More validation functions...
    }
}

// This tells KMP: "I need these functions, but how they're implemented depends on the platform"
expect fun getCurrentYear(): Int
expect fun getCurrentMonth(): Int
```

#### 2. Platform-Specific Implementations

For Android:
```kotlin
// In shared/src/androidMain/.../model/DateUtils.kt
actual fun getCurrentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)
actual fun getCurrentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1
```

For iOS:
```kotlin
// In shared/src/iosMain/.../model/DateUtils.kt
actual fun getCurrentYear(): Int = NSCalendar.currentCalendar.component(NSCalendarUnit.Year, fromDate: NSDate())
actual fun getCurrentMonth(): Int = NSCalendar.currentCalendar.component(NSCalendarUnit.Month, fromDate: NSDate())
```

#### 3. The ViewModel Layer

The ViewModel serves as the bridge between your shared business logic and platform UIs:

```kotlin
// In shared/src/commonMain/.../viewmodel/CreditCardViewModel.kt
class CreditCardViewModel : ViewModel() {
    // This state is observed by BOTH Android and iOS UIs
    private val _uiState = MutableStateFlow(CreditCardUiState())
    val uiState: StateFlow<CreditCardUiState> = _uiState.asStateFlow()
    
    fun updateCardNumber(number: String) {
        _uiState.update { currentState ->
            // Shared validation logic from CreditCard model
            val isValid = CreditCard.validateNumber(number)
            currentState.copy(
                creditCard = currentState.creditCard.copy(number = number),
                numberError = if (number.isNotEmpty() && !isValid) "Invalid card number" else ""
            )
        }
    }
    
    // More functions that modify the shared state...
}

// This state class is also shared between platforms
data class CreditCardUiState(
    val creditCard: CreditCard = CreditCard(),
    val numberError: String = "",
    // More fields...
)
```

### How Platform UIs Consume the Shared Code

#### Android UI (Compose)

In your Android app, you use Jetpack Compose to create the UI and connect to the shared ViewModel:

```kotlin
// In composeApp/src/main/kotlin/.../MainActivity.kt (simplified example)
@Composable
fun PaymentScreen(viewModel: CreditCardViewModel = viewModel()) {
    // Collect the shared state as Compose state
    val uiState by viewModel.uiState.collectAsState()
    
    Column {
        // Text field for card number
        TextField(
            value = uiState.creditCard.number,
            onValueChange = { viewModel.updateCardNumber(it) },
            label = { Text("Card Number") },
            isError = uiState.numberError.isNotEmpty()
        )
        
        if (uiState.numberError.isNotEmpty()) {
            Text(uiState.numberError, color = Color.Red)
        }
        
        // More UI components for other fields...
        
        Button(
            onClick = { viewModel.processPayment() },
            enabled = !uiState.isSubmitting
        ) {
            Text("Pay Now")
        }
    }
}
```

#### iOS UI (SwiftUI)

In your iOS app, you use SwiftUI and connect to the same shared ViewModel through a wrapper:

```swift
// In iosApp/iosApp/ContentView.swift (simplified example)
struct ContentView: View {
    // This wrapper observes the shared ViewModel
    @ObservedObject private var viewModel = CreditCardViewModelWrapper()
    
    var body: some View {
        VStack {
            // Text field for card number
            TextField("Card Number", text: Binding(
                get: { viewModel.uiState.creditCard.number },
                set: { viewModel.updateCardNumber(number: $0) }
            ))
            .padding()
            .border(viewModel.uiState.numberError.isEmpty ? Color.gray : Color.red)
            
            if !viewModel.uiState.numberError.isEmpty {
                Text(viewModel.uiState.numberError)
                    .foregroundColor(.red)
            }
            
            // More UI components for other fields...
            
            Button("Pay Now") {
                viewModel.processPayment()
            }
            .disabled(viewModel.uiState.isSubmitting)
        }
    }
}

// This wrapper makes the Kotlin ViewModel observable in SwiftUI
class CreditCardViewModelWrapper: ObservableObject {
    private let viewModel = CreditCardViewModel()
    @Published var uiState = CreditCardUiState()
    
    init() {
        // Set up observation of the Kotlin Flow in Swift
        viewModel.uiState.watch { [weak self] state in
            self?.uiState = state
        }
    }
    
    func updateCardNumber(number: String) {
        viewModel.updateCardNumber(number: number)
    }
    
    // More wrapper functions...
}
```

### Data Flow in the App

Here's how data flows through the application:

1. **User Input**: The user enters data in platform UI (Android/iOS)
2. **UI to ViewModel**: Platform code calls shared ViewModel functions
3. **Validation**: ViewModel uses shared model logic to validate input
4. **State Update**: ViewModel updates state using Kotlin flows
5. **UI Update**: Platform UI observes state changes and updates accordingly

This pattern ensures:
- Business logic is written once in Kotlin
- Platform-specific code is only written for UI and platform APIs
- The app behaves consistently across platforms

### Benefits for a KMP Beginner

As a beginner working with KMP, you'll benefit from:

1. **Code Sharing**: Write core logic once, reducing bugs and maintenance
2. **Native UIs**: Still create fully native UIs with platform-specific tools
3. **Gradual Adoption**: Start small by sharing just models, then expand
4. **Type Safety**: Kotlin's type system helps prevent errors across platforms
5. **Modern Architecture**: The separation enforces clean architecture practices

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