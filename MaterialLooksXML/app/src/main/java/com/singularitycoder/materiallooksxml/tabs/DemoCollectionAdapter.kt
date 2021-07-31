package com.singularitycoder.materiallooksxml.tabs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.singularitycoder.materiallooksxml.Route

class DemoCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 10

    override fun createFragment(position: Int): Fragment {
        return DummyFragment(route = Route.TAB, pageNumber = position + 1)
    }
}