package br.com.saladadailha.data

import br.com.saladadailha.SaladariaApplication
import br.com.saladadailha.model.*
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.util.Date

class OrderRepository {
    
    private val realm = SaladariaApplication.realm
    
    
    fun getAllOrders(): Flow<List<Order>> {
        return realm.query<Order>().asFlow().map { it.list }
    }
    
    
    suspend fun createOrder(
        cartItems: List<CartItem>,
        deliveryMethod: DeliveryMethod,
        paymentMethod: PaymentMethod,
        subtotal: Double,
        deliveryFee: Double
    ): ObjectId {
        var orderId = ObjectId()
        
        realm.write {
            val order = copyToRealm(Order().apply {
                _id = orderId
                items.addAll(cartItems)
                this.deliveryMethod = deliveryMethod
                this.paymentMethod = paymentMethod
                this.subtotal = subtotal
                this.deliveryFee = deliveryFee
                this.total = subtotal + deliveryFee
                this.createdAt = Date()
                this.updatedAt = Date()
            })
            
            orderId = order._id
        }
        
        return orderId
    }
    
    
    suspend fun updateOrderStatus(orderId: ObjectId, status: OrderStatus) {
        realm.write {
            val order = query<Order>("_id == $0", orderId).first().find()
            order?.let { 
                findLatest(it)?.status = status.name
                findLatest(it)?.updatedAt = Date()
            }
        }
    }
    
    
    fun getOrderById(orderId: ObjectId): Flow<Order?> {
        return realm.query<Order>("_id == $0", orderId).asFlow().map { it.list.firstOrNull() }
    }
}
