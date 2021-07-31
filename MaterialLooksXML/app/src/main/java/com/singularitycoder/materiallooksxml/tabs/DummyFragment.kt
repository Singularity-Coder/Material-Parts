package com.singularitycoder.materiallooksxml.tabs

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.singularitycoder.materiallooksxml.MainActivity
import com.singularitycoder.materiallooksxml.R
import com.singularitycoder.materiallooksxml.Route
import com.singularitycoder.materiallooksxml.databinding.FragmentDummyBinding

class DummyFragment(val route: Route, val pageNumber: Int = 0) : Fragment() {

    private lateinit var purpleColorsArray: TypedArray
    private lateinit var limeColorsArray: TypedArray
    private lateinit var cyanColorsArray: TypedArray
    private lateinit var orangeColorsArray: TypedArray
    private lateinit var myContext: Context
    private lateinit var myActivity: MainActivity
    private lateinit var binding: FragmentDummyBinding

    // Fragments must have public no-arg constructor
    constructor() : this(Route.NONE, 0)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
        myActivity = context as MainActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDummyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        purpleColorsArray = myContext.resources.obtainTypedArray(R.array.purple_colors)
        limeColorsArray = myContext.resources.obtainTypedArray(R.array.lime_colors)
        cyanColorsArray = myContext.resources.obtainTypedArray(R.array.cyan_colors)
        orangeColorsArray = myContext.resources.obtainTypedArray(R.array.orange_colors)

        val clFlowItemList = listOf<View>(
            binding.item1,
            binding.item2,
            binding.item3,
            binding.item4,
            binding.item5,
            binding.item6,
            binding.item7,
            binding.item8,
            binding.item9,
            binding.item10
        )
        when (route) {
            Route.NONE -> Unit
            Route.TAB -> {
                binding.tvDummy.visibility = View.VISIBLE
                binding.tvDummy.text = "Page $pageNumber"
                binding.clDummyGrid.visibility = View.GONE
            }
            Route.NAV_RAIL_RECENT -> {
                clFlowItemList.forEachIndexed { index, item ->
                    item.setBackgroundColor(purpleColorsArray.getColor(index, 0))
                }
            }
            Route.NAV_RAIL_PHOTOS -> {
                clFlowItemList.forEachIndexed { index, item ->
                    item.setBackgroundColor(limeColorsArray.getColor(index, 0))
                }
            }
            Route.NAV_RAIL_VIDEOS -> {
                clFlowItemList.forEachIndexed { index, item ->
                    item.setBackgroundColor(cyanColorsArray.getColor(index, 0))
                }
            }
            Route.NAV_RAIL_SELFIES -> {
                clFlowItemList.forEachIndexed { index, item ->
                    item.setBackgroundColor(orangeColorsArray.getColor(index, 0))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        purpleColorsArray.recycle()
        limeColorsArray.recycle()
        cyanColorsArray.recycle()
        orangeColorsArray.recycle()
    }
}