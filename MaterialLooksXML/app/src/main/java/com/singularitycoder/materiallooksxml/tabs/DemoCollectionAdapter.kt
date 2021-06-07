package com.singularitycoder.materiallooksxml.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.singularitycoder.materiallooksxml.Constants

class DemoCollectionAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 100

    override fun createFragment(position: Int): Fragment {
        return DummyTabFragment().apply {
            arguments = Bundle().apply {
                putInt(Constants.ARG_OBJECT, position + 1)
            }
        }
    }
}