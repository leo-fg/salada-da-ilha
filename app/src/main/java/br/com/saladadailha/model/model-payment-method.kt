package br.com.saladadailha.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

enum class PaymentType {
    CREDIT_CARD, DEBIT_CARD, CASH, PIX
}

class PaymentMethod : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var type: String = PaymentType.CREDIT_CARD.name
    var cardNumber: String = ""
    var cardholderName: String = ""
    var expiryDate: String = ""
    var cvv: String = ""
}
