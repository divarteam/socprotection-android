package ru.divar.socprotection.epoxy.districts

import com.airbnb.epoxy.EpoxyController
import ru.divar.socprotection.data.District
import ru.divar.socprotection.epoxy.header
import ru.divar.socprotection.ui.calculator.DistrictsPickerFragment

class DistrictsController(
    private val fragment: DistrictsPickerFragment
) : EpoxyController() {
    var items: List<District> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        header {
            id(9999)
            hint("Выберите город или район, в котором вы проживаете. На выбор представлены все районы Удмуртской республики.")
        }
        items.forEach {
            district(fragment) {
                id(it.id)
                districtID(it.id)
                name(it.name)
            }
        }
    }
}