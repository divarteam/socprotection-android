package ru.divar.socprotection.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import ru.divar.socprotection.App
import ru.divar.socprotection.R
import ru.divar.socprotection.data.PreferenceRepository
import ru.divar.socprotection.databinding.FragmentDisabilityBinding

class DisabilityPickerFragment : Fragment(R.layout.fragment_disability) {

    private lateinit var preferenceRepository: PreferenceRepository

    private lateinit var binding: FragmentDisabilityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDisabilityBinding.inflate(inflater, container, false).let {
        binding = it
        it.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceRepository = (activity?.application as App).preferenceRepository
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.arrowBack.setOnClickListener { findNavController().popBackStack() }

        binding.no.setOnClickListener {
            preferenceRepository.handicapped = false
            goNext()
        }

        binding.yes.setOnClickListener {
            preferenceRepository.handicapped = true
            goNext()
        }
    }

    private fun goNext() {
        findNavController().navigate(
            DisabilityPickerFragmentDirections.actionDisabilityPickerFragmentToChildrenCountPickerFragment()
        )
    }
}