package com.example.labaccess.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.labaccess.databinding.FragmentAccessCardsBinding


class AccessCardsFragment : Fragment() {

    private lateinit var binding: FragmentAccessCardsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccessCardsBinding.inflate(inflater, container, false)
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val teacherId = sharedPreferences.getString("relatedId", null)



        return binding.root
    }
}
