package ru.divar.socprotection.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PreferenceRepository(private val sharedPreferences: SharedPreferences) {

    // Night mode repository

    var nightMode: Int
        get() = sharedPreferences.getInt(PREFERENCE_NIGHT_MODE, PREFERENCE_NIGHT_MODE_DEF_VAL)
        set(value) {
            sharedPreferences.edit().putInt(PREFERENCE_NIGHT_MODE, value).apply()
            AppCompatDelegate.setDefaultNightMode(value)
        }

    // Age repository

    var age: Int
        get() = sharedPreferences.getInt(PREFERENCE_AGE, 199)
        set(value) = sharedPreferences.edit().putInt(PREFERENCE_AGE, value).apply()

    private val _ageLive: MutableLiveData<Int> = MutableLiveData()
    val ageLive: LiveData<Int>
        get() = _ageLive

    // Earnings repository

    var earnings: Int
        get() = sharedPreferences.getInt(PREFERENCE_EARNINGS, 0)
        set(value) = sharedPreferences.edit().putInt(PREFERENCE_EARNINGS, value).apply()

    private val _earningsLive: MutableLiveData<Int> = MutableLiveData()
    val earningsLive: LiveData<Int>
        get() = _earningsLive

    // Handicapped repository

    var handicapped: Boolean
        get() = sharedPreferences.getBoolean(PREFERENCE_HANDICAPPED, false)
        set(value) = sharedPreferences.edit().putBoolean(PREFERENCE_HANDICAPPED, value).apply()

    private val _handicappedLive: MutableLiveData<Boolean> = MutableLiveData()
    val handicappedLive: LiveData<Boolean>
        get() = _handicappedLive

    // Children count repository

    var childrenCount: Int
        get() = sharedPreferences.getInt(PREFERENCE_CHILDREN_COUNT, 0)
        set(value) = sharedPreferences.edit().putInt(PREFERENCE_CHILDREN_COUNT, value).apply()

    private val _childrenCountLive: MutableLiveData<Int> = MutableLiveData()
    val childrenCountLive: LiveData<Int>
        get() = _childrenCountLive

    // Handicapped repository

    var married: Boolean
        get() = sharedPreferences.getBoolean(PREFERENCE_MARRIED, false)
        set(value) = sharedPreferences.edit().putBoolean(PREFERENCE_MARRIED, value).apply()

    private val _marriedLive: MutableLiveData<Boolean> = MutableLiveData()
    val marriedLive: LiveData<Boolean>
        get() = _marriedLive

    // Locality repository

    var locality: Int
        get() = sharedPreferences.getInt(PREFERENCE_LOCALITY, 0)
        set(value) = sharedPreferences.edit().putInt(PREFERENCE_LOCALITY, value).apply()

    private val _localityLive: MutableLiveData<Int> = MutableLiveData()
    val localityLive: LiveData<Int>
        get() = _localityLive

    var localityName: String?
        get() = sharedPreferences.getString(PREFERENCE_LOCALITY_NAME, "")
        set(value) = sharedPreferences.edit().putString(PREFERENCE_LOCALITY_NAME, value).apply()

    private val _localityNameLive: MutableLiveData<String> = MutableLiveData()
    val localityNameLive: LiveData<String>
        get() = _localityNameLive

    // Gender repository

    var gender: Int
        get() = sharedPreferences.getInt(PREFERENCE_GENDER, GENDER_NONE)
        set(value) = sharedPreferences.edit().putInt(PREFERENCE_GENDER, value).apply()

    private val _genderLive: MutableLiveData<Int> = MutableLiveData()
    val genderLive: LiveData<Int>
        get() = _genderLive

    private val preferenceChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {

                PREFERENCE_AGE ->
                    _ageLive.value = age

                PREFERENCE_EARNINGS ->
                    _earningsLive.value = earnings
                
                PREFERENCE_HANDICAPPED ->
                    _handicappedLive.value = handicapped

                PREFERENCE_CHILDREN_COUNT ->
                    _childrenCountLive.value = childrenCount

                PREFERENCE_LOCALITY ->
                    _localityLive.value = locality

                PREFERENCE_LOCALITY_NAME ->
                    _localityNameLive.value = localityName

                PREFERENCE_GENDER ->
                    _genderLive.value = gender
            }
        }

    init {
        _ageLive.value = age
        _earningsLive.value = earnings
        _handicappedLive.value = handicapped
        _childrenCountLive.value = childrenCount
        _localityLive.value = locality
        _genderLive.value = gender

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }

    companion object {
        const val DEFAULT_PREFERENCES = "preference_social_default"

        // Dark mode constants
        private const val PREFERENCE_NIGHT_MODE = "preference_social_night"
        private const val PREFERENCE_NIGHT_MODE_DEF_VAL = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM

        // Social constants
        private const val PREFERENCE_AGE = "preference_social_age"
        private const val PREFERENCE_EARNINGS = "preference_social_earnings"
        private const val PREFERENCE_HANDICAPPED = "preference_social_handicapped"
        private const val PREFERENCE_CHILDREN_COUNT = "preference_social_children_count"
        private const val PREFERENCE_MARRIED = "preference_social_married"
        private const val PREFERENCE_LOCALITY = "preference_social_locality"
        private const val PREFERENCE_LOCALITY_NAME = "preference_social_locality_name"

        // Gender constants
        private const val PREFERENCE_GENDER = "preference_social_gender"
        const val GENDER_NONE = 2
        const val GENDER_MALE = 1
        const val GENDER_FEMALE = 0
    }
}