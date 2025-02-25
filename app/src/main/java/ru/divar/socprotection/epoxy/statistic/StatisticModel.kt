package ru.divar.socprotection.epoxy.statistic

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import ru.divar.socprotection.R
import ru.divar.socprotection.epoxy.KotlinHolder

@EpoxyModelClass
abstract class StatisticModel : EpoxyModelWithHolder<StatisticModel.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.layout_statistic

    @EpoxyAttribute
    var regionID: Int = 0

    @EpoxyAttribute
    lateinit var regionName: String

    @EpoxyAttribute
    var percentages: Int = 0

    @SuppressLint("SetTextI18n")
    override fun bind(holder: Holder) {
        holder.percentages.setTextColor(
            ContextCompat.getColor(
                holder.percentages.context,
                if (percentages in 0..40)
                    android.R.color.holo_green_dark
                else if (percentages in 41..80)
                    android.R.color.holo_orange_dark
                else
                    android.R.color.holo_red_dark
            )
        )
        holder.percentages.text = "$percentages%"
        holder.cityName.text = regionName
    }

    inner class Holder : KotlinHolder() {
        val cityName by bind<TextView>(R.id.city)
        val percentages by bind<TextView>(R.id.percentages)
        val card by bind<MaterialCardView>(R.id.card)
    }
}