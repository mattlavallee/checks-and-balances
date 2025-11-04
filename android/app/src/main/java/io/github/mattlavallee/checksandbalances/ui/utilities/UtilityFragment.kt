package io.github.mattlavallee.checksandbalances.ui.utilities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import io.github.mattlavallee.checksandbalances.R
import io.github.mattlavallee.checksandbalances.ui.tags.ManageTagsFragment

class UtilityFragment : Fragment() {
    private lateinit var utilityPageAdapter: UtilityPageAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_utility, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        utilityPageAdapter = UtilityPageAdapter(this)
        viewPager = view.findViewById(R.id.utility_view_pager)
        viewPager.adapter = utilityPageAdapter

        val tabLayout = view.findViewById<TabLayout>(R.id.utility_tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = if (position == 0) "Manage Tags" else "Currency Converter"
        }.attach()
    }
}

private class UtilityPageAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            ManageTagsFragment()
        } else {
            CurrencyConverterFragment()
        }
    }
}
