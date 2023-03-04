package com.sartimau.openpayfit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sartimau.openpayfit.databinding.FragmentImagesBinding
import com.sartimau.openpayfit.viewmodel.ImagesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagesFragment : Fragment() {

    private val viewModel: ImagesViewModel by viewModels()

    private lateinit var _binding: FragmentImagesBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textImages
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }
}
