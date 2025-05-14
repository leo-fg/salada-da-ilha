package br.com.saladadailha.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

enum class ProductCategory {
    SALAD, PROTEIN, DRINK
}

class Product : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var name: String = ""
    var description: String = ""
    var price: Double = 0.0
    var imageUrl: String = ""
    var category: String = ProductCategory.SALAD.name
    var available: Boolean = true
}
