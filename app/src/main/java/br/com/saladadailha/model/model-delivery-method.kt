package br.com.saladadailha.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

enum class DeliveryType {
    PICKUP, DELIVERY
}

class DeliveryMethod : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var type: String = DeliveryType.DELIVERY.name
    var address: String = ""
    var deliveryFee: Double = 0.0
}
