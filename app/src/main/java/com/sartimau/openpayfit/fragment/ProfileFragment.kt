package com.sartimau.openpayfit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sartimau.openpayfit.R
import com.sartimau.openpayfit.adapter.ProfileTabAdapter
import com.sartimau.openpayfit.databinding.FragmentProfileBinding
import com.sartimau.openpayfit.dialog.ErrorDialog
import com.sartimau.openpayfit.domain.entity.PopularPerson
import com.sartimau.openpayfit.utils.loadImage
import com.sartimau.openpayfit.viewmodel.ProfileViewModel
import com.sartimau.openpayfit.viewmodel.ProfileViewModel.ProfileData
import com.sartimau.openpayfit.viewmodel.ProfileViewModel.ProfileState.EMPTY_STATE
import com.sartimau.openpayfit.viewmodel.ProfileViewModel.ProfileState.ON_ERROR
import com.sartimau.openpayfit.viewmodel.ProfileViewModel.ProfileState.SHOW_INFO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe({ lifecycle }, ::updateUi)
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchProfileInfo()
    }

    private fun updateUi(data: ProfileData) {
        when (data.state) {
            SHOW_INFO -> showProfileInfo(data.mostPopularPerson)
            ON_ERROR -> ErrorDialog.newInstance().show(childFragmentManager, ErrorDialog.TAG)
            EMPTY_STATE -> TODO()
        }
    }

    private fun showProfileInfo(mostPopularPerson: PopularPerson?) {
        mostPopularPerson?.let { person ->

            binding.personImage.loadImage(person.profilePath)
            binding.personId.text = getString(R.string.popular_person_id, person.id.toString())
            binding.personName.text = person.name
            binding.personDepartment.text = person.knownForDepartment
            binding.personPopularity.text = person.popularity.toString()

            binding.recycler.apply {
                adapter = ProfileTabAdapter(person.knownFor)
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
            }
        }
    }
}
