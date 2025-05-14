package br.com.saladadailha.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import br.com.saladadailha.databinding.ItemProductBinding
import br.com.saladadailha.model.Product
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val onAddToCartClicked: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(product: Product) {
            binding.apply {
                txtProductName.text = product.name
                txtProductDescription.text = product.description
                txtProductPrice.text = formatPrice(product.price)
                
                Glide.with(imgProduct.context)
                    .load(product.imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(imgProduct)
                
                btnAddToCart.setOnClickListener {
                    onAddToCartClicked(product)
                }
            }
        }
        
        private fun formatPrice(price: Double): String {
            return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(price)
        }
    }
    
    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem._id == newItem._id
        }
        
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name &&
                   oldItem.description == newItem.description &&
                   oldItem.price == newItem.price
        }
    }
}
