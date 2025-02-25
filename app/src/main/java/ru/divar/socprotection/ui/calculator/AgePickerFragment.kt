package ru.divar.socprotection.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import ru.divar.socprotection.App
import ru.divar.socprotection.R
import ru.divar.socprotection.data.PreferenceRepository
import ru.divar.socprotection.databinding.FragmentAgeBinding
import ru.divar.socprotection.epoxy.age.AgeController

class AgePickerFragment : Fragment(R.layout.fragment_age) {

    private lateinit var preferenceRepository: PreferenceRepository
    private lateinit var ageController: AgeController

    private lateinit var binding: FragmentAgeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentAgeBinding.inflate(inflater, container, false).let {
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

        binding.ageRecycler.layoutManager =
            LinearLayoutManager(
                context
            )

        binding.ageRecycler.apply {
            adapter = ageController.adapter
            setHasFixedSize(false)
            scrollToPosition(2)
        }

        binding.go.setOnClickListener { goNext() }
        binding.arrowNext.setOnClickListener { goNext() }
        binding.arrowBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun goNext() {
        findNavController().navigate(
            AgePickerFragmentDirections.actionAgePickerFragmentToEarningsPickerFragment()
        )
    }
}