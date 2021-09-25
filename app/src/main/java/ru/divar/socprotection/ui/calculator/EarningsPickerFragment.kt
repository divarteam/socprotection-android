package ru.divar.socprotection.ui.calculator

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.fragment_earnings.*
import ru.divar.socprotection.App
import ru.divar.socprotection.R
import ru.divar.socprotection.data.PreferenceRepository

class EarningsPickerFragment : Fragment(R.layout.fragment_earnings) {

    private lateinit var preferenceRepository: PreferenceRepository
    private var canGo = true

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
                earnings_edit?.editText?.setText(it.toString())
        }
        earnings_edit?.editText?.doAfterTextChanged {
            if (it.toString().length < 1) {
                arrow_next.alpha = 0.1f
                canGo = false
            } else {
                println("a")
                arrow_next.alpha = 1f
                canGo = true
            }
        }
        if (earnings_edit.editText!!.text.length < 1) {
            arrow_next.alpha = 0.1f
            canGo = false
        }

        go.setOnClickListener { goNext() }
        arrow_next.setOnClickListener { goNext() }
        arrow_back.setOnClickListener { findNavController().popBackStack() }
    }

    fun goNext() {
        if (canGo) {
            preferenceRepository.earnings =
                Integer.parseInt(earnings_edit?.editText?.text.toString())
            findNavController().navigate(
                EarningsPickerFragmentDirections.actionEarningsPickerFragmentToDisabilityPickerFragment()
            )
        }
    }
}