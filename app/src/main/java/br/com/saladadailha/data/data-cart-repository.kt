package br.com.saladadailha.data

import br.com.saladadailha.SaladariaApplication
import br.com.saladadailha.model.CartItem
import br.com.saladadailha.model.Product
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class CartRepository {
    
    private val realm = SaladariaApplication.realm
    
    
    fun getAllCartItems(): Flow<List<CartItem>> {
        return realm.query<CartItem>().asFlow().map { it.list }
    }
    
    
    suspend fun addToCart(product: Product, quantity: Int = 1, observations: String = "") {
        realm.write {
            val existingItem = query<CartItem>("product._id == $0", product._id).first().find()
            
            if (existingItem != null) {
                findLatest(existingItem)?.quantity = existingItem.quantity + quantity
            } else {
                copyToRealm(CartItem().apply {
                    this.product = product
                    this.quantity = quantity
                    this.observations = observations
                })
            }
        }
    }
    
    
    suspend fun updateQuantity(itemId: ObjectId, quantity: Int) {
        realm.write {
            val item = query<CartItem>("_id == $0", itemId).first().find()
            item?.let { findLatest(it)?.quantity = quantity }
        }
    }
    
    
    suspend fun removeFromCart(itemId: ObjectId) {
        realm.write {
            val item = query<CartItem>("_id == $0", itemId).first().find()
            item?.let { delete(it) }
        }
    }
    
    
    suspend fun clearCart() {
        realm.write {
            delete(query<CartItem>().find())
        }
    }
    
    
    fun getCartSubtotal(): Flow<Double> {
        return getAllCartItems().map { items ->
            items.sumOf { it.product?.price?.times(it.quantity) ?: 0.0 }
        }
    }
}
