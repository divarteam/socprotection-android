package ru.divar.socprotection.epoxy.statistic

import com.airbnb.epoxy.EpoxyController
import ru.divar.socprotection.data.District
import ru.divar.socprotection.epoxy.districts.district
import ru.divar.socprotection.epoxy.header

class StatisticController : EpoxyController() {
    var items: List<District> = emptyList()
        set(value) {
            field = value
            requestModelBuild()
        }

    override fun buildModels() {
        header {
            id(999999)
            hint("Вашему вниманию представляется статистика по необходимости выплат социальной помощи жителям Удмуртской области, разделённая по регионам.")
        }
        items.forEach {
            statistic {
                val i = it.id + 10000
                id(i)
                regionID(it.id)
                regionName(it.name)
            }
        }
    }
}