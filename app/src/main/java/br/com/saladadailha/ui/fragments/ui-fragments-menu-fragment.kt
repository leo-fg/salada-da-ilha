package br.com.saladadailha.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.saladadailha.databinding.FragmentMenuBinding
import br.com.saladadailha.ui.adapters.CategoryPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MenuFragment : Fragment() {
    
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViewPager()
        setupClickListeners()
    }
    
    private fun setupViewPager() {
        val pagerAdapter = CategoryPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = pagerAdapter
        
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Saladas"
                1 -> "Proteínas"
                2 -> "Bebidas"
                else -> "Categoria"
            }
        }.attach()
    }
    
    private fun setupClickListeners() {
        binding.btnViewCart.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToCartFragment()
            )
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
