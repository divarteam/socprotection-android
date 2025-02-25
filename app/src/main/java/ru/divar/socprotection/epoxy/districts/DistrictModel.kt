package ru.divar.socprotection.epoxy.districts

import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.google.android.material.card.MaterialCardView
import ru.divar.socprotection.R
import ru.divar.socprotection.epoxy.KotlinHolder
import ru.divar.socprotection.ui.calculator.DistrictsPickerFragment
import ru.divar.socprotection.ui.calculator.DistrictsPickerFragmentDirections

@EpoxyModelClass
abstract class DistrictModel(
    private val fragment: DistrictsPickerFragment
) : EpoxyModelWithHolder<DistrictModel.Holder>() {

    override fun getDefaultLayout(): Int = R.layout.layout_city

    @EpoxyAttribute
    var districtID: Int = 0

    @EpoxyAttribute
    lateinit var name: String

    override fun bind(holder: Holder) {
        holder.district.text = name
        holder.card.setOnClickListener {
            fragment.preferenceRepository.locality = districtID
            fragment.findNavController().navigate(
                DistrictsPickerFragmentDirections.actionDistrictsPickerFragmentToGenderPickerFragment()
            )
        }
    }

    override fun shouldSaveViewState(): Boolean = true

    override fun getViewType(): Int {
        return districtID
    }

    inner class Holder : KotlinHolder() {
        val district by bind<TextView>(R.id.city)
        val card by bind<MaterialCardView>(R.id.card)
    }
}