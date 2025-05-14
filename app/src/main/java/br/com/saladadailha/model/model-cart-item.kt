package br.com.saladadailha.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class CartItem : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var product: Product? = null
    var quantity: Int = 1
    var observations: String = ""
}
