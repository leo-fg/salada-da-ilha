package br.com.saladadailha.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.saladadailha.data.CartRepository
import br.com.saladadailha.data.OrderRepository
import br.com.saladadailha.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class CheckoutViewModel : ViewModel() {
    
    private val cartRepository = CartRepository()
    private val orderRepository = OrderRepository()
    
    private val _deliveryMethod = MutableStateFlow<DeliveryMethod?>(null)
    val deliveryMethod: StateFlow<DeliveryMethod?> = _deliveryMethod
    
    private val _paymentMethod = MutableStateFlow<PaymentMethod?>(null)
    val paymentMethod: StateFlow<PaymentMethod?> = _paymentMethod
    
    private val _deliveryFee = MutableStateFlow(0.0)
    val deliveryFee: StateFlow<Double> = _deliveryFee
    
    private val _orderCreated = MutableStateFlow<ObjectId?>(null)
    val orderCreated: StateFlow<ObjectId?> = _orderCreated
    
    fun setDeliveryMethod(type: DeliveryType, address: String = "") {
        val method = DeliveryMethod().apply {
            this.type = type.name
            this.address = address
            this.deliveryFee = if (type == DeliveryType.DELIVERY) 5.0 else 0.0
        }
        
        _deliveryMethod.value = method
        _deliveryFee.value = method.deliveryFee
    }
    
    fun setPaymentMethod(type: PaymentType, cardDetails: Map<String, String> = emptyMap()) {
        val method = PaymentMethod().apply {
            this.type = type.name
            if (type == PaymentType.CREDIT_CARD || type == PaymentType.DEBIT_CARD) {
                this.cardNumber = cardDetails["cardNumber"] ?: ""
                this.cardholderName = cardDetails["cardholderName"] ?: ""
                this.expiryDate = cardDetails["expiryDate"] ?: ""
                this.cvv = cardDetails["cvv"] ?: ""
            }
        }
        
        _paymentMethod.value = method
    }
    
    fun createOrder(subtotal: Double) {
        viewModelScope.launch {
            val cartItems = cartRepository.getAllCartItems().value ?: emptyList()
            val deliveryMethod = _deliveryMethod.value ?: return@launch
            val paymentMethod = _paymentMethod.value ?: return@launch
            
            val orderId = orderRepository.createOrder(
                cartItems = cartItems,
                deliveryMethod = deliveryMethod,
                paymentMethod = paymentMethod,
                subtotal = subtotal,
                deliveryFee = _deliveryFee.value
            )
            
            
            cartRepository.clearCart()
            
            _orderCreated.value = orderId
        }
    }
    
    fun resetOrder() {
        _orderCreated.value = null
        _deliveryMethod.value = null
        _paymentMethod.value = null
        _deliveryFee.value = 0.0
    }
}
