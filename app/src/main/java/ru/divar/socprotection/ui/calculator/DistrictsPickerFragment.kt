package ru.divar.socprotection.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.divar.socprotection.App
import ru.divar.socprotection.R
import ru.divar.socprotection.data.District
import ru.divar.socprotection.data.PreferenceRepository
import ru.divar.socprotection.databinding.FragmentDistrictsBinding
import ru.divar.socprotection.epoxy.districts.DistrictsController
import ru.divar.socprotection.ui.MainActivity
import ru.divar.socprotection.util.APIUtils.Companion.toMap
import ru.divar.socprotection.util.GetDistricts

class DistrictsPickerFragment : Fragment(R.layout.fragment_districts) {

    lateinit var preferenceRepository: PreferenceRepository
    private lateinit var districtsController: DistrictsController

    private lateinit var binding: FragmentDistrictsBinding

    private val coroutineScope = CoroutineScope(Job() + CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDistrictsBinding.inflate(inflater, container, false).let {
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

        if (!this::districtsController.isInitialized)
            districtsController = DistrictsController(this)

        binding.districtsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = districtsController.adapter
            setHasFixedSize(false)
            scrollToPosition(2)
        }

        (activity as MainActivity).startLoadingAnimation()

        try {
            coroutineScope.launch(Dispatchers.IO) {
                val response = GetDistricts().get()

                when (response.code) {
                    200 -> {
                        val obj = JSONObject(response.body)
                        val responseMap = obj.toMap()

                        val list = arrayListOf<District>()

                        responseMap.forEach { s, any ->
                            list.add(District(Integer.parseInt(s), any as String))
                        }

                        districtsController.items = list
                        launch(Dispatchers.Main) {
                            (activity as MainActivity).stopLoadingAnimation()
                        }
                    }
                    else -> {
                        (activity as MainActivity).onError("Произошла ошибка при загрузке списка районов.")
                        findNavController().popBackStack()
                    }
                }
            }
        } catch (e: Exception) {
            (activity as MainActivity).onError("Произошла ошибка при загрузке списка районов.")
            findNavController().popBackStack()
            e.printStackTrace()
        }
    }
}