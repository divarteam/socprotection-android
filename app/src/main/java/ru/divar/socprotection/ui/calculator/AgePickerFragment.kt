package ru.divar.socprotection.ui.calculator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.fragment_age.*
import ru.divar.socprotection.App
import ru.divar.socprotection.R
import ru.divar.socprotection.data.PreferenceRepository
import ru.divar.socprotection.epoxy.age.AgeController

class AgePickerFragment : Fragment(R.layout.fragment_age) {

    private lateinit var preferenceRepository: PreferenceRepository
    private lateinit var ageController: AgeController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceRepository = (activity?.application as App).preferenceRepository

        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)

        if (!this::ageController.isInitialized)
            ageController = AgeController(preferenceRepository)

        val ages = arrayListOf<Int>()

        for (i in -1..100) {
            ages.add(i)
        }

        ageController.items = ages
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        age_recycler?.layoutManager =
            LinearLayoutManager(
                context
            )

        age_recycler?.apply {
            adapter = ageController.adapter
            setHasFixedSize(false)
            scrollToPosition(2)
        }

        go.setOnClickListener { goNext() }
        arrow_next.setOnClickListener { goNext() }
        arrow_back.setOnClickListener { findNavController().popBackStack() }
    }

    private fun goNext() {
        findNavController().navigate(
            AgePickerFragmentDirections.actionAgePickerFragmentToEarningsPickerFragment()
        )
    }
}