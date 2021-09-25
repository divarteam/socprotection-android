package ru.divar.socprotection.epoxy

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import ru.divar.socprotection.R

@EpoxyModelClass(layout = R.layout.layout_header)
abstract class HeaderModel : EpoxyModelWithHolder<HeaderModel.Holder>() {

    @EpoxyAttribute
    lateinit var hint: String

    override fun bind(holder: Holder) {
        holder.hint.text = hint
    }

    inner class Holder : KotlinHolder() {
        val hint by bind<TextView>(R.id.hint)
    }
}