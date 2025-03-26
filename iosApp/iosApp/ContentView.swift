import SwiftUI
import Shared

struct ContentView: View {
    @State private var cardNumber = ""
    @State private var cardholderName = ""
    @State private var expiryMonth = ""
    @State private var expiryYear = ""
    @State private var cvv = ""
    
    @State private var numberError = ""
    @State private var nameError = ""
    @State private var expiryError = ""
    @State private var cvvError = ""
    
    @State private var isSubmitting = false
    @State private var showSuccessAlert = false
    @State private var showErrorAlert = false
    @State private var errorMessage = ""
    
    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 20) {
                    // Card Number
                    VStack(alignment: .leading) {
                        Text("Card Number")
                            .font(.caption)
                            .foregroundColor(.gray)
                        
                        TextField("4111 1111 1111 1111", text: $cardNumber)
                            .keyboardType(.numberPad)
                            .padding()
                            .background(
                                RoundedRectangle(cornerRadius: 8)
                                    .stroke(numberError.isEmpty ? Color.gray.opacity(0.3) : Color.red, lineWidth: 1)
                            )
                            .onChange(of: cardNumber) { _ in
                                let result = validateCardNumber(cardNumber)
                                numberError = result ? "" : "Invalid card number"
                            }
                        
                        if !numberError.isEmpty {
                            Text(numberError)
                                .font(.caption)
                                .foregroundColor(.red)
                        }
                    }
                    
                    // Cardholder Name
                    VStack(alignment: .leading) {
                        Text("Cardholder Name")
                            .font(.caption)
                            .foregroundColor(.gray)
                        
                        TextField("John Doe", text: $cardholderName)
                            .padding()
                            .background(
                                RoundedRectangle(cornerRadius: 8)
                                    .stroke(nameError.isEmpty ? Color.gray.opacity(0.3) : Color.red, lineWidth: 1)
                            )
                            .onChange(of: cardholderName) { _ in
                                let result = validateCardholderName(cardholderName)
                                nameError = result ? "" : "Name must be at least 3 characters"
                            }
                        
                        if !nameError.isEmpty {
                            Text(nameError)
                                .font(.caption)
                                .foregroundColor(.red)
                        }
                    }
                    
                    // Expiry Date and CVV
                    HStack(spacing: 16) {
                        VStack(alignment: .leading) {
                            Text("Expiry Date")
                                .font(.caption)
                                .foregroundColor(.gray)
                            
                            HStack(spacing: 8) {
                                TextField("MM", text: $expiryMonth)
                                    .keyboardType(.numberPad)
                                    .padding()
                                    .frame(maxWidth: .infinity)
                                    .background(
                                        RoundedRectangle(cornerRadius: 8)
                                            .stroke(expiryError.isEmpty ? Color.gray.opacity(0.3) : Color.red, lineWidth: 1)
                                    )
                                
                                TextField("YY", text: $expiryYear)
                                    .keyboardType(.numberPad)
                                    .padding()
                                    .frame(maxWidth: .infinity)
                                    .background(
                                        RoundedRectangle(cornerRadius: 8)
                                            .stroke(expiryError.isEmpty ? Color.gray.opacity(0.3) : Color.red, lineWidth: 1)
                                    )
                            }
                            .onChange(of: expiryMonth) { _ in
                                validateExpiry()
                            }
                            .onChange(of: expiryYear) { _ in
                                validateExpiry()
                            }
                            
                            if !expiryError.isEmpty {
                                Text(expiryError)
                                    .font(.caption)
                                    .foregroundColor(.red)
                            }
                        }
                        .frame(maxWidth: .infinity)
                        
                        VStack(alignment: .leading) {
                            Text("CVV")
                                .font(.caption)
                                .foregroundColor(.gray)
                            
                            TextField("123", text: $cvv)
                                .keyboardType(.numberPad)
                                .padding()
                                .background(
                                    RoundedRectangle(cornerRadius: 8)
                                        .stroke(cvvError.isEmpty ? Color.gray.opacity(0.3) : Color.red, lineWidth: 1)
                                )
                                .onChange(of: cvv) { _ in
                                    let result = validateCvv(cvv)
                                    cvvError = result ? "" : "Invalid CVV"
                                }
                            
                            if !cvvError.isEmpty {
                                Text(cvvError)
                                    .font(.caption)
                                    .foregroundColor(.red)
                            }
                        }
                        .frame(maxWidth: .infinity)
                    }
                    
                    Button(action: processPayment) {
                        if isSubmitting {
                            ProgressView()
                                .progressViewStyle(CircularProgressViewStyle())
                        } else {
                            Text("Pay Now")
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(Color.blue)
                                .foregroundColor(.white)
                                .cornerRadius(8)
                        }
                    }
                    .disabled(isSubmitting)
                }
                .padding()
            }
            .navigationTitle("Payment Details")
            .alert("Payment Successful", isPresented: $showSuccessAlert) {
                Button("OK", role: .cancel) { }
            }
            .alert("Payment Failed", isPresented: $showErrorAlert) {
                Button("OK", role: .cancel) { }
            } message: {
                Text(errorMessage)
            }
        }
    }
    
    private func validateCardNumber(_ number: String) -> Bool {
        return CreditCard.Companion.shared.validateNumber(number: number)
    }
    
    private func validateCardholderName(_ name: String) -> Bool {
        return CreditCard.Companion.shared.validateName(name: name)
    }
    
    private func validateExpiry() {
        if expiryMonth.isEmpty && expiryYear.isEmpty {
            expiryError = ""
            return
        }
        
        let result = CreditCard.Companion.shared.validateExpiry(month: expiryMonth, year: expiryYear)
        expiryError = result ? "" : "Invalid expiry date"
    }
    
    private func validateCvv(_ cvv: String) -> Bool {
        return CreditCard.Companion.shared.validateCvv(cvv: cvv)
    }
    
    private func processPayment() {
        let isNumberValid = validateCardNumber(cardNumber)
        let isNameValid = validateCardholderName(cardholderName)
        let isExpiryValid = CreditCard.Companion.shared.validateExpiry(month: expiryMonth, year: expiryYear)
        let isCvvValid = validateCvv(cvv)
        
        if !isNumberValid {
            numberError = "Invalid card number"
        }
        
        if !isNameValid {
            nameError = "Invalid name"
        }
        
        if !isExpiryValid {
            expiryError = "Invalid expiry date"
        }
        
        if !isCvvValid {
            cvvError = "Invalid CVV"
        }
        
        if isNumberValid && isNameValid && isExpiryValid && isCvvValid {
            isSubmitting = true
            
            // Simulate API call with delay
            DispatchQueue.main.asyncAfter(deadline: .now() + 1.5) {
                isSubmitting = false
                showSuccessAlert = true
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
