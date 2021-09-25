package ru.divar.socprotection.ui.calculator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.fragment_disability.*
import kotlinx.android.synthetic.main.fragment_earnings.*
import kotlinx.android.synthetic.main.fragment_earnings.arrow_back
import ru.divar.socprotection.App
import ru.divar.socprotection.R
import ru.divar.socprotection.data.PreferenceRepository

class EngagedPickerFragment : Fragment(R.layout.fragment_engaged) {

    private lateinit var preferenceRepository: PreferenceRepository

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
        arrow_back.setOnClickListener { findNavController().popBackStack() }

        no.setOnClickListener {
            preferenceRepository.married = false
            goNext()
        }

        yes.setOnClickListener {
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