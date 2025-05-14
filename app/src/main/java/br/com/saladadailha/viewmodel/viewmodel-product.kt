package br.com.saladadailha.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.saladadailha.data.ProductRepository
import br.com.saladadailha.model.Product
import br.com.saladadailha.model.ProductCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    
    private val repository = ProductRepository()
    
    private val _salads = MutableStateFlow<List<Product>>(emptyList())
    val salads: StateFlow<List<Product>> = _salads
    
    private val _proteins = MutableStateFlow<List<Product>>(emptyList())
    val proteins: StateFlow<List<Product>> = _proteins
    
    private val _drinks = MutableStateFlow<List<Product>>(emptyList())
    val drinks: StateFlow<List<Product>> = _drinks
    
    init {
        loadProducts()
    }
    
    private fun loadProducts() {
        viewModelScope.launch {
            repository.getProductsByCategory(ProductCategory.SALAD).collectLatest {
                _salads.value = it
            }
        }
        
        viewModelScope.launch {
            repository.getProductsByCategory(ProductCategory.PROTEIN).collectLatest {
                _proteins.value = it
            }
        }
        
        viewModelScope.launch {
            repository.getProductsByCategory(ProductCategory.DRINK).collectLatest {
                _drinks.value = it
            }
        }
    }
    
    fun addInitialProducts() {
        viewModelScope.launch {
            repository.addInitialProducts()
        }
    }
}
