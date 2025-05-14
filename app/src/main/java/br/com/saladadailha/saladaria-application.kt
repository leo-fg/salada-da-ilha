package br.com.saladadailha

import android.app.Application
import br.com.saladadailha.model.*
import br.com.saladadailha.utils.DataInitializer
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class SaladariaApplication : Application() {
    
    companion object {
        lateinit var realm: Realm
            private set
    }
    
    override fun onCreate() {
        super.onCreate()
        
        val config = RealmConfiguration.Builder(
            schema = setOf(
                Product::class,
                CartItem::class,
                DeliveryMethod::class,
                PaymentMethod::class,
                Order::class
            )
        )
        .name("saladaria.realm")
        .schemaVersion(1)
        .build()
        
        realm = Realm.open(config)
        
        
        DataInitializer.initializeDataIfNeeded(this)
    }
}
