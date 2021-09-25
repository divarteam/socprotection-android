package ru.divar.socprotection.epoxy.age

import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import ru.divar.socprotection.data.PreferenceRepository
import ru.divar.socprotection.epoxy.CustomSnappingCarouselModel_
import ru.divar.socprotection.epoxy.PickerModel_
import ru.divar.socprotection.epoxy.customSnappingCarousel
import java.util.*

class ChildrenController(private val preferenceRepository: PreferenceRepository) : EpoxyController() {

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

            this@ChildrenController.preferenceRepository.childrenCount = 0

            models(this@ChildrenController.items.map { item ->
                PickerModel_()
                    .id(this@ChildrenController.k++ + 2000)
                    .number(item)
            })

            onBind { _, view, _ ->
                view.setSnapHelperCallback { newPosition ->
                    this@ChildrenController.preferenceRepository.childrenCount = newPosition + 1
                }
            }
        }
    }
}