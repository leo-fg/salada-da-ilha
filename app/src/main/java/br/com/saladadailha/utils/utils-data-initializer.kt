package br.com.saladadailha.utils

import android.content.Context
import android.content.SharedPreferences
import br.com.saladadailha.data.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DataInitializer {
    
    private const val PREFS_NAME = "SaladariaPrefs"
    private const val KEY_DATA_INITIALIZED = "data_initialized"
    
    fun initializeDataIfNeeded(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        if (!prefs.getBoolean(KEY_DATA_INITIALIZED, false)) {
            CoroutineScope(Dispatchers.IO).launch {
                val productRepository = ProductRepository()
                productRepository.addInitialProducts()
                
                prefs.edit().putBoolean(KEY_DATA_INITIALIZED, true).apply()
            }
        }
    }
}
