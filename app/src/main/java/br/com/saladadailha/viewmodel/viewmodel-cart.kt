package br.com.saladadailha.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.saladadailha.data.CartRepository
import br.com.saladadailha.model.CartItem
import br.com.saladadailha.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.mongodb.kbson.ObjectId

class CartViewModel : ViewModel() {
    
    private val repository = CartRepository()
    
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems
    
    private val _subtotal = MutableStateFlow(0.0)
    val subtotal: StateFlow<Double> = _subtotal
    
    init {
        viewModelScope.launch {
            repository.getAllCartItems().collectLatest {
                _cartItems.value = it
            }
        }
        
        viewModelScope.launch {
            repository.getCartSubtotal().collectLatest {
                _subtotal.value = it
            }
        }
    }
    
    fun addToCart(product: Product, quantity: Int = 1, observations: String = "") {
        viewModelScope.launch {
            repository.addToCart(product, quantity, observations)
        }
    }
    
    fun updateQuantity(itemId: ObjectId, quantity: Int) {
        viewModelScope.launch {
            repository.updateQuantity(itemId, quantity)
        }
    }
    
    fun removeFromCart(itemId: ObjectId) {
        viewModelScope.launch {
            repository.removeFromCart(itemId)
        }
    }
    
    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}
