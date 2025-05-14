package br.com.saladadailha.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.saladadailha.model.ProductCategory
import br.com.saladadailha.ui.fragments.ProductListFragment

class CategoryPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    
    private val categories = listOf(
        ProductCategory.SALAD,
        ProductCategory.PROTEIN,
        ProductCategory.DRINK
    )
    
    override fun getItemCount(): Int = categories.size
    
    override fun createFragment(position: Int): Fragment {
        return ProductListFragment.newInstance(categories[position])
    }
}
