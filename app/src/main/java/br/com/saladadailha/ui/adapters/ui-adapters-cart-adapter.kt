package br.com.saladadailha.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.saladadailha.databinding.ItemCartBinding
import br.com.saladadailha.model.CartItem
import org.mongodb.kbson.ObjectId
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
    private val onQuantityChanged: (ObjectId, Int) -> Unit,
    private val onRemoveClicked: (ObjectId) -> Unit
) : ListAdapter<CartItem, CartAdapter.CartViewHolder>(CartDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CartViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(cartItem: CartItem) {
            val product = cartItem.product ?: return
            
            binding.apply {
                txtProductName.text = product.name
                txtProductPrice.text = formatPrice(product.price)
                txtQuantity.text = cartItem.quantity.toString()
                txtItemTotal.text = formatPrice(product.price * cartItem.quantity)
                
                btnIncrease.setOnClickListener {
                    val newQuantity = cartItem.quantity + 1
                    txtQuantity.text = newQuantity.toString()
                    onQuantityChanged(cartItem._id, newQuantity)
                }
                
                btnDecrease.setOnClickListener {
                    if (cartItem.quantity > 1) {
                        val newQuantity = cartItem.quantity - 1
                        txtQuantity.text = newQuantity.toString()
                        onQuantityChanged(cartItem._id, newQuantity)
                    }
                }
                
                btnRemove.setOnClickListener {
                    onRemoveClicked(cartItem._id)
                }
            }
        }
        
        private fun formatPrice(price: Double): String {
            return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(price)
        }
    }
    
    class CartDiffCallback : DiffUtil.ItemCallback<CartItem>() {
        override fun areItemsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem._id == newItem._id
        }
        
        override fun areContentsTheSame(oldItem: CartItem, newItem: CartItem): Boolean {
            return oldItem.product?._id == newItem.product?._id &&
                   oldItem.quantity == newItem.quantity
        }
    }
}
