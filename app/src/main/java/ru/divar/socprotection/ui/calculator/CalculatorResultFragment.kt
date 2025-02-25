package ru.divar.socprotection.ui.calculator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import ru.divar.socprotection.App
import ru.divar.socprotection.R
import ru.divar.socprotection.data.PreferenceRepository
import ru.divar.socprotection.databinding.FragmentCalculatorResultBinding
import ru.divar.socprotection.ui.MainActivity
import ru.divar.socprotection.util.GetCalculate

class CalculatorResultFragment : Fragment(R.layout.fragment_calculator_result) {

    private lateinit var preferenceRepository: PreferenceRepository

    private lateinit var binding: FragmentCalculatorResultBinding

    private val coroutineScope = CoroutineScope(Job() + CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentCalculatorResultBinding.inflate(inflater, container, false).let {
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

        binding.go.setOnClickListener {
            findNavController().popBackStack(
                R.id.calculatorFragment,
                false
            )
        }

        (activity as MainActivity).startLoadingAnimation()
        try {
            coroutineScope.launch(Dispatchers.IO) {
                val response = GetCalculate(
                    preferenceRepository.age,
                    preferenceRepository.earnings,
                    preferenceRepository.handicapped,
                    preferenceRepository.childrenCount,
                    preferenceRepository.married,
                    preferenceRepository.locality,
                    preferenceRepository.gender
                ).get()

                launch(Dispatchers.Main) {
                    when (response.code) {
                        200 -> {
                            val obj = JSONObject(response.body)

                            if (obj.getBoolean("with_soc_protect")) {

                                binding.firstTitle.text = "Вы можете получить пособие!"
                                binding.first.text =
                                    "Исходя из введённых Вами данных, вы подходите по критериям для получения социальной помощи. Для этого зайдите на ГосУслуги или позвоните социальному работнику."
                                binding.firstIcon.setImageResource(R.drawable.ic_tick_square)

                            } else {

                                binding.firstTitle.text = "Кажется, Вам ничего не полагается..."
                                binding.first.text =
                                    "Проверьте свои данные ещё раз. Если Вы нигде не ошиблись, то увы - Вас нет ни в одной из категорий."
                                binding.firstIcon.setImageResource(R.drawable.ic_close_square)

                            }

                            (activity as MainActivity).stopLoadingAnimation()
                        }

                        else -> {
                            MainScope().launch {
                                (activity as MainActivity).onError("Произошла ошибка при загрузке результатов.")
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            coroutineScope.launch(Dispatchers.Main) {
                (activity as MainActivity).onError("Произошла ошибка при загрузке результатов.")
                findNavController().popBackStack()
                e.printStackTrace()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        findNavController().popBackStack(R.id.calculatorFragment, false)
    }
}