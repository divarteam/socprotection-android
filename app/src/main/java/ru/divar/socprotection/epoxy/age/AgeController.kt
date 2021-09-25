package ru.divar.socprotection.epoxy.age

import android.view.Gravity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyController
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import ru.divar.socprotection.data.PreferenceRepository
import ru.divar.socprotection.epoxy.CustomSnappingCarouselModel_
import ru.divar.socprotection.epoxy.PickerModel_
import ru.divar.socprotection.epoxy.customSnappingCarousel
import java.util.*

class AgeController(private val preferenceRepository: PreferenceRepository) : EpoxyController() {

    private var k = 0

    var items: List<Int> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {

        customSnappingCarousel {
            numViewsToShowOnScreen(3F)
            id("carousel")

            this@AgeController.preferenceRepository.age = 0

            models(this@AgeController.items.map { item ->
                PickerModel_()
                    .id(this@AgeController.k++ + 1000)
                    .number(item)
            })

            onBind { _, view, _ ->
                view.setSnapHelperCallback { newPosition ->
                    this@AgeController.preferenceRepository.age = newPosition + 1
                }
            }
        }
    }
}