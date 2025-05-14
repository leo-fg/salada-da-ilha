package br.com.saladadailha.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.saladadailha.databinding.FragmentCartBinding
import br.com.saladadailha.ui.adapters.CartAdapter
import br.com.saladadailha.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class CartFragment : Fragment() {
    
    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    
    private val cartViewModel: CartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeCart()
        setupClickListeners()
    }
    
    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onQuantityChanged = { itemId, quantity ->
                cartViewModel.updateQuantity(itemId, quantity)
            },
            onRemoveClicked = { itemId ->
                cartViewModel.removeFromCart(itemId)
            }
        )
        
        binding.recyclerView.adapter = cartAdapter
    }
    
    private fun observeCart() {
        lifecycleScope.launch {
            cartViewModel.cartItems.collect { items ->
                cartAdapter.submitList(items)
                binding.btnCheckout.isEnabled = items.isNotEmpty()
            }
        }
        
        lifecycleScope.launch {
            cartViewModel.subtotal.collect { subtotal ->
                binding.txtSubtotal.text = formatPrice(subtotal)
            }
        }
    }
    
    private fun setupClickListeners() {
        binding.btnCheckout.setOnClickListener {
            findNavController().navigate(
                CartFragmentDirections.actionCartFragmentToCheckoutFragment()
            )
        }
    }
    
    private fun formatPrice(price: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(price)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
