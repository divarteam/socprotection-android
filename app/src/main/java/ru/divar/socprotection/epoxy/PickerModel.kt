package ru.divar.socprotection.epoxy

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import ru.divar.socprotection.R

@EpoxyModelClass(layout = R.layout.layout_age)
abstract class PickerModel : EpoxyModelWithHolder<PickerModel.Holder>() {

    @EpoxyAttribute
    var number: Int = 0

    override fun bind(holder: Holder) {
        if (number != -1)
            holder.age.text = number.toString()
        else
            holder.card.visibility = View.INVISIBLE
    }

    override fun shouldSaveViewState(): Boolean = true

    override fun getViewType(): Int {
        return number
    }

    inner class Holder : KotlinHolder() {
        val age by bind<TextView>(R.id.age)
        val card by bind<MaterialCardView>(R.id.card)
    }
}