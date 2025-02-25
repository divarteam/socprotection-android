package ru.divar.socprotection.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import ru.divar.socprotection.App
import ru.divar.socprotection.R
import ru.divar.socprotection.data.PreferenceRepository
import ru.divar.socprotection.databinding.FragmentEarningsBinding

class EarningsPickerFragment : Fragment(R.layout.fragment_earnings) {

    private lateinit var preferenceRepository: PreferenceRepository
    private var canGo = true

    private lateinit var binding: FragmentEarningsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentEarningsBinding.inflate(inflater, container, false).let {
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
        preferenceRepository.earnings.let {
            if (it > 0)
                binding.earningsEdit.editText?.setText("$it")
        }
        binding.earningsEdit.editText?.doAfterTextChanged {
            if (it.toString().length < 1) {
                binding.arrowNext.alpha = 0.1f
                canGo = false
            } else {
                println("a")
                binding.arrowNext.alpha = 1f
                canGo = true
            }
        }
        if (binding.earningsEdit.editText!!.text.length < 1) {
            binding.arrowNext.alpha = 0.1f
            canGo = false
        }

        binding.go.setOnClickListener { goNext() }
        binding.arrowNext.setOnClickListener { goNext() }
        binding.arrowBack.setOnClickListener { findNavController().popBackStack() }
    }

    fun goNext() {
        if (canGo) {
            preferenceRepository.earnings =
                Integer.parseInt(binding.earningsEdit.editText?.text.toString())
            findNavController().navigate(
                EarningsPickerFragmentDirections.actionEarningsPickerFragmentToDisabilityPickerFragment()
            )
        }
    }
}