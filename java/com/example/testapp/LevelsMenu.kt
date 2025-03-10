package com.example.testapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.testapp.databinding.LevelsMenuBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LevelsMenu : Fragment() {

    private var _binding: LevelsMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LevelsMenuBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.buttonSecond.setOnClickListener{
            findNavController().navigate(R.id.action_levels_menu_to_level1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}