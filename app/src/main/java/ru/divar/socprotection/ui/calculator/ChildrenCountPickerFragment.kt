package ru.divar.socprotection.ui.calculator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.fragment_children_count.*
import ru.divar.socprotection.App
import ru.divar.socprotection.R
import ru.divar.socprotection.data.PreferenceRepository
import ru.divar.socprotection.epoxy.age.ChildrenController

class ChildrenCountPickerFragment : Fragment(R.layout.fragment_children_count) {
    private lateinit var preferenceRepository: PreferenceRepository
    private lateinit var childrenController: ChildrenController

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

        val counts = arrayListOf<Int>()

        if (!this::childrenController.isInitialized)
            childrenController = ChildrenController(preferenceRepository)

        children_recycler?.layoutManager =
            LinearLayoutManager(
                context
            )

        for (i in -1..100) {
            counts.add(i)
        }

        childrenController.items = counts

        children_recycler?.apply {
            adapter = childrenController.adapter
            setHasFixedSize(false)
            scrollToPosition(2)
        }

        go.setOnClickListener { goNext() }
        arrow_next.setOnClickListener { goNext() }
        arrow_back.setOnClickListener { findNavController().popBackStack() }
    }

    private fun goNext() {
        findNavController().navigate(
            ChildrenCountPickerFragmentDirections.actionChildrenCountPickerFragmentToEngagedPickerFragment()
        )
    }
}