package ru.divar.socprotection.ui.calculator

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.fragment_calculator_result.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.divar.socprotection.App
import ru.divar.socprotection.R
import ru.divar.socprotection.data.District
import ru.divar.socprotection.data.PreferenceRepository
import ru.divar.socprotection.ui.MainActivity
import ru.divar.socprotection.util.APIUtils.Companion.toMap
import ru.divar.socprotection.util.GetCalculate
import ru.divar.socprotection.util.GetDistricts
import java.lang.Exception

class CalculatorResultFragment : Fragment(R.layout.fragment_calculator_result) {

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

        go.setOnClickListener { findNavController().popBackStack(R.id.calculatorFragment, false) }

        (activity as MainActivity).startLoadingAnimation()
        try {
            GlobalScope.launch {
                val response = GetCalculate(
                    preferenceRepository.age,
                    preferenceRepository.earnings,
                    preferenceRepository.handicapped,
                    preferenceRepository.childrenCount,
                    preferenceRepository.married,
                    preferenceRepository.locality,
                    preferenceRepository.gender
                ).get()

                MainScope().launch {
                    when (response.code) {
                        200 -> {
                            val obj = JSONObject(response.body)

                            if (obj.getBoolean("with_soc_protect")) {

                                first_title?.text = "Вам положена соцпомощь!"
                                first?.text =
                                    "Исходя из введённых вами данных, вы подходите по критериям для получения социальной помощи."
                                first_icon?.setImageResource(R.drawable.ic_tick_square)

                            } else {

                                first_title?.text = "Вам не положена соцпомощь."
                                first?.text =
                                    "Исходя из введённых вами данных, вы не подходите по критериям для получения социальной помощи."
                                first_icon?.setImageResource(R.drawable.ic_close_square)

                            }

                            (activity as MainActivity).stopLoadingAnimation()
                        }
                        else -> {
                            (activity as MainActivity).onError("Произошла ошибка при загрузке результатов.")
                            println()
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            MainScope().launch {
                (activity as MainActivity).onError("Произошла ошибка при загрузке результатов.")
                findNavController().popBackStack()
                e.printStackTrace()
            }
        }
    }
}