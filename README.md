# DynamicHPP - Kotlin Multiplatform Payment Page

This is a Kotlin Multiplatform project demonstrating a cross-platform payment form with credit card validation, targeting Android and iOS.

## Features

- **Shared Logic**: Core validation and business logic shared between platforms
- **Platform-Specific UIs**: Native UI implementation for both Android (Compose) and iOS (SwiftUI)
- **Credit Card Validation**:
  - Luhn algorithm for card number validation
  - Expiry date validation
  - CVV validation
  - Cardholder name validation
- **Real-time Validation**: Immediate feedback as the user types
- **Responsive Design**: Works on various screen sizes
- **Cross-platform Architecture**: Demonstrates KMP best practices

## Project Structure

* `/composeApp` - Contains the Android Compose UI implementation
* `/iosApp` - Contains the iOS SwiftUI implementation
* `/shared` - Contains shared code between platforms:
  - `model` package - Data models and validation logic
  - `viewmodel` package - UI state management
  - `ui` package - Compose UI components

## Setup & Run

### Prerequisites

- Android Studio Arctic Fox or higher
- Xcode 14 or higher (for iOS)
- JDK 11+

### Android

1. Open the project in Android Studio
2. Select the 'composeApp' configuration from the run configurations dropdown
3. Click the Run button

### iOS

1. Open the `iosApp/iosApp.xcworkspace` file in Xcode
2. Select a simulator or device
3. Run the project

## Implementation Details

The project demonstrates the Kotlin Multiplatform approach by:

1. **Shared Models**: Using a common `CreditCard` data class with validation logic
2. **Platform-Specific Implementations**: Using expect/actual declarations for platform-specific date operations
3. **Shared UI State**: Using `CreditCardViewModel` to manage UI state consistently
4. **Native UI Implementations**: Leveraging Compose for Android and SwiftUI for iOS

The credit card form includes validations for:
- Card number (using Luhn algorithm)
- Cardholder name
- Expiry date
- CVV

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Learn More

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)