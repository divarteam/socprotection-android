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
import ru.divar.socprotection.databinding.FragmentChildrenCountBinding
import ru.divar.socprotection.epoxy.age.ChildrenController

class ChildrenCountPickerFragment : Fragment(R.layout.fragment_children_count) {
    private lateinit var preferenceRepository: PreferenceRepository
    private lateinit var childrenController: ChildrenController

    private lateinit var binding: FragmentChildrenCountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentChildrenCountBinding.inflate(inflater, container, false).let {
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

        val counts = arrayListOf<Int>()

        if (!this::childrenController.isInitialized)
            childrenController = ChildrenController(preferenceRepository)

        binding.childrenRecycler.layoutManager =
            LinearLayoutManager(
                context
            )

        for (i in -1..100) {
            counts.add(i)
        }

        childrenController.items = counts

        binding.childrenRecycler.apply {
            adapter = childrenController.adapter
            setHasFixedSize(false)
            scrollToPosition(2)
        }

        binding.go.setOnClickListener { goNext() }
        binding.arrowNext.setOnClickListener { goNext() }
        binding.arrowBack.setOnClickListener { findNavController().popBackStack() }
    }

    private fun goNext() {
        findNavController().navigate(
            ChildrenCountPickerFragmentDirections.actionChildrenCountPickerFragmentToEngagedPickerFragment()
        )
    }
}