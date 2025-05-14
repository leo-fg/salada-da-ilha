package br.com.saladadailha.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.saladadailha.databinding.FragmentProductListBinding
import br.com.saladadailha.model.ProductCategory
import br.com.saladadailha.ui.adapters.ProductAdapter
import br.com.saladadailha.viewmodel.CartViewModel
import br.com.saladadailha.viewmodel.ProductViewModel
import kotlinx.coroutines.launch

class ProductListFragment : Fragment() {
    
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    
    private val productViewModel: ProductViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    
    private lateinit var productAdapter: ProductAdapter
    private var category: ProductCategory = ProductCategory.SALAD
    
    companion object {
        private const val ARG_CATEGORY = "category"
        
        fun newInstance(category: ProductCategory): ProductListFragment {
            val fragment = ProductListFragment()
            val args = Bundle().apply {
                putString(ARG_CATEGORY, category.name)
            }
            fragment.arguments = args
            return fragment
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val categoryName = it.getString(ARG_CATEGORY) ?: ProductCategory.SALAD.name
            category = ProductCategory.valueOf(categoryName)
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeProducts()
    }
    
    private fun setupRecyclerView() {
        productAdapter = ProductAdapter { product ->
            cartViewModel.addToCart(product)
            Toast.makeText(
                requireContext(),
                "${product.name} adicionado ao carrinho",
                Toast.LENGTH_SHORT
            ).show()
        }
        
        binding.recyclerView.adapter = productAdapter
    }
    
    private fun observeProducts() {
        lifecycleScope.launch {
            when (category) {
                ProductCategory.SALAD -> productViewModel.salads.collect { productAdapter.submitList(it) }
                ProductCategory.PROTEIN -> productViewModel.proteins.collect { productAdapter.submitList(it) }
                ProductCategory.DRINK -> productViewModel.drinks.collect { productAdapter.submitList(it) }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
