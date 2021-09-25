package ru.divar.socprotection.ui.calculator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.fragment_districts.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.divar.socprotection.App
import ru.divar.socprotection.R
import ru.divar.socprotection.data.District
import ru.divar.socprotection.data.PreferenceRepository
import ru.divar.socprotection.epoxy.age.AgeController
import ru.divar.socprotection.epoxy.districts.DistrictsController
import ru.divar.socprotection.ui.MainActivity
import ru.divar.socprotection.util.APIUtils.Companion.toMap
import ru.divar.socprotection.util.GetDistricts
import java.lang.Exception

class DistrictsPickerFragment : Fragment(R.layout.fragment_districts) {

    lateinit var preferenceRepository: PreferenceRepository
    private lateinit var districtsController: DistrictsController

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

        arrow_back?.setOnClickListener { findNavController().popBackStack() }

        if (!this::districtsController.isInitialized)
            districtsController = DistrictsController(this)

        districts_recycler?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = districtsController.adapter
            setHasFixedSize(false)
            scrollToPosition(2)
        }

        (activity as MainActivity).startLoadingAnimation()

        try {
            GlobalScope.launch {
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
                        MainScope().launch {
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