package com.augmentedimages.app.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.augmentedimages.app.R
import com.augmentedimages.app.core.ext.onClick
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLibrary.onClick {
            findNavController().navigate(R.id.action_homeFragment_to_listMarkersFragment)
        }
        btnArScene.onClick {
            findNavController().navigate(R.id.action_homeFragment_to_imageArFragment)
        }
    }
}