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
import ru.divar.socprotection.databinding.FragmentEngagedBinding

class EngagedPickerFragment : Fragment(R.layout.fragment_engaged) {

    private lateinit var preferenceRepository: PreferenceRepository

    private lateinit var binding: FragmentEngagedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentEngagedBinding.inflate(inflater, container, false).let {
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
            preferenceRepository.married = false
            goNext()
        }

        binding.yes.setOnClickListener {
            preferenceRepository.married = true
            goNext()
        }
    }

    private fun goNext() {
        findNavController().navigate(
            EngagedPickerFragmentDirections.actionEngagedPickerFragmentToDistrictsPickerFragment()
        )
    }
}