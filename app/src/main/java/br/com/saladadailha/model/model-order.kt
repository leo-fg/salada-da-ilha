package br.com.saladadailha.model

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId
import java.util.Date

enum class OrderStatus {
    PENDING, CONFIRMED, PREPARING, READY, DELIVERING, DELIVERED, CANCELLED
}

class Order : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var items: RealmList<CartItem> = RealmList()
    var deliveryMethod: DeliveryMethod? = null
    var paymentMethod: PaymentMethod? = null
    var status: String = OrderStatus.PENDING.name
    var subtotal: Double = 0.0
    var deliveryFee: Double = 0.0
    var total: Double = 0.0
    var createdAt: Date = Date()
    var updatedAt: Date = Date()
}
