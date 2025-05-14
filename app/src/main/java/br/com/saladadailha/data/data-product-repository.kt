package br.com.saladadailha.data

import br.com.saladadailha.SaladariaApplication
import br.com.saladadailha.model.Product
import br.com.saladadailha.model.ProductCategory
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class ProductRepository {
    
    private val realm = SaladariaApplication.realm
    
    
    fun getAllProducts(): Flow<List<Product>> {
        return realm.query<Product>().asFlow().map { it.list }
    }
    
    
    fun getProductsByCategory(category: ProductCategory): Flow<List<Product>> {
        return realm.query<Product>("category == $0", category.name).asFlow().map { it.list }
    }
    
    
    fun getProductById(id: ObjectId): Flow<Product?> {
        return realm.query<Product>("_id == $0", id).asFlow().map { it.list.firstOrNull() }
    }
    
    
    suspend fun addInitialProducts() {
        realm.write {
            
            copyToRealm(Product().apply {
                name = "Salada Caesar"
                description = "Alface romana, croutons, queijo parmesão e molho caesar"
                price = 19.90
                imageUrl = "https://example.com/caesar.jpg"
                category = ProductCategory.SALAD.name
            })
            
            copyToRealm(Product().apply {
                name = "Salada Grega"
                description = "Tomate, pepino, azeitona, queijo feta e cebola roxa"
                price = 22.90
                imageUrl = "https://example.com/greek.jpg"
                category = ProductCategory.SALAD.name
            })
            
            
            copyToRealm(Product().apply {
                name = "Frango Grelhado"
                description = "Peito de frango grelhado com ervas"
                price = 10.90
                imageUrl = "https://example.com/chicken.jpg"
                category = ProductCategory.PROTEIN.name
            })
            
            copyToRealm(Product().apply {
                name = "Salmão"
                description = "Filé de salmão grelhado"
                price = 18.90
                imageUrl = "https://example.com/salmon.jpg"
                category = ProductCategory.PROTEIN.name
            })
            
            
            copyToRealm(Product().apply {
                name = "Suco de Laranja"
                description = "Suco de laranja natural"
                price = 8.90
                imageUrl = "https://example.com/orange.jpg"
                category = ProductCategory.DRINK.name
            })
            
            copyToRealm(Product().apply {
                name = "Água Mineral"
                description = "Água mineral sem gás"
                price = 4.90
                imageUrl = "https://example.com/water.jpg"
                category = ProductCategory.DRINK.name
            })
        }
    }
}
