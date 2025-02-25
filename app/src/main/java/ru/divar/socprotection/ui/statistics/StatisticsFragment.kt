package ru.divar.socprotection.ui.statistics

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
import org.json.JSONArray
import org.json.JSONObject
import ru.divar.socprotection.App
import ru.divar.socprotection.R
import ru.divar.socprotection.data.PreferenceRepository
import ru.divar.socprotection.databinding.FragmentStatisticsBinding
import ru.divar.socprotection.epoxy.header
import ru.divar.socprotection.epoxy.statistic.StatisticController
import ru.divar.socprotection.epoxy.statistic.statistic
import ru.divar.socprotection.ui.MainActivity
import ru.divar.socprotection.util.GetStatistics

class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    lateinit var preferenceRepository: PreferenceRepository
    private lateinit var statisticController: StatisticController
    private lateinit var binding: FragmentStatisticsBinding
    private val coroutineScope = CoroutineScope(Job() + CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentStatisticsBinding.inflate(inflater, container, false).let {
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

        if (!this::statisticController.isInitialized)
            statisticController = StatisticController()

        (activity as MainActivity).startLoadingAnimation()

        binding.statisticRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = statisticController.adapter
            setHasFixedSize(false)
        }

        try {
            coroutineScope.launch(Dispatchers.IO) {
                val response = GetStatistics().get()

                when (response.code) {
                    200 -> {
                        val jsonArray = JSONArray(response.body)
                        var obj: JSONObject

                        launch(Dispatchers.Main) {
                            binding.statisticRecycler.withModels {
                                header {
                                    id(999999)
                                    hint("Вашему вниманию представляется статистика по необходимости выплат социальной помощи жителям Удмуртской республики, разделённая по регионам.")
                                }
                                for (i in 0 until jsonArray.length()) {
                                    obj = jsonArray.getJSONObject(i)
                                    statistic {
                                        id(obj.getInt("id"))
                                        regionID(obj.getInt("id"))
                                        regionName(obj.getString("name"))
                                        percentages(obj.getInt("need_soc_protect"))
                                    }
                                }
                            }
                            (activity as MainActivity).stopLoadingAnimation()
                        }
                    }
                    else -> {
                        (activity as MainActivity).onError("Произошла ошибка при загрузке статистики.")
                        findNavController().popBackStack()
                    }
                }
            }
        } catch (e: Exception) {
            (activity as MainActivity).onError("Произошла ошибка при загрузке статистики.")
            findNavController().popBackStack()
            e.printStackTrace()
        }
    }
}