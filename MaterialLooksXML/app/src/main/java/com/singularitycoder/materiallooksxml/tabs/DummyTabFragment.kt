package com.singularitycoder.materiallooksxml.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.singularitycoder.materiallooksxml.Constants
import com.singularitycoder.materiallooksxml.databinding.FragmentDummyTabBinding
import com.singularitycoder.materiallooksxml.prefixWith

class DummyTabFragment : Fragment() {

    private lateinit var binding: FragmentDummyTabBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDummyTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(Constants.ARG_OBJECT) }?.apply {
            binding.tvDummy.text = getInt(Constants.ARG_OBJECT).toString() prefixWith "Page "
        }
    }
}