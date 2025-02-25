package ru.divar.socprotection.epoxy

import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import ru.divar.socprotection.R

@EpoxyModelClass
abstract class HeaderModel : EpoxyModelWithHolder<HeaderModel.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.layout_header

    @EpoxyAttribute
    lateinit var hint: String

    override fun bind(holder: Holder) {
        holder.hint.text = hint
    }

    inner class Holder : KotlinHolder() {
        val hint by bind<TextView>(R.id.hint)
    }
}