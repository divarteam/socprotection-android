package ru.divar.socprotection.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.transition.TransitionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_loading.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.divar.socprotection.R
import ru.divar.socprotection.util.NavigationUtil.setupWithNavController

class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private var isActivityPaused = false

    override fun onCreate(savedInstanceState: Bundle?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        } else {
            window.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
                decorView.systemUiVisibility =
                    decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                statusBarColor = Color.TRANSPARENT
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        val navGraphIds = listOf(
            R.navigation.nav_main,
            R.navigation.nav_statistics
        )

        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.main_container,
            intent = intent
        )

        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    fun startLoadingAnimation() {
        isActivityPaused = true
        MainScope().launch {
            prepareLoadingView()
            loading_progress_bar.visibility = View.VISIBLE

            val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            root?.let {
                TransitionManager.beginDelayedTransition(it, sharedAxis)
            }

            loading_root.visibility = View.VISIBLE
        }
    }

    fun stopLoadingAnimation() {
        MainScope().launch {
            val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, true)
            root?.let {
                TransitionManager.beginDelayedTransition(it, sharedAxis)
            }

            loading_root.visibility = View.GONE
            isActivityPaused = false
        }
    }

    fun onError(errorText: String) {
        MainScope().launch {
            var sharedAxis: MaterialSharedAxis

            if (errorText != "")
                error_text.text = errorText

            if (loading_root.visibility == View.GONE) {
                isActivityPaused = true
                prepareLoadingView()

                error_pic.visibility = View.VISIBLE
                error_text.visibility = View.VISIBLE

                sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, true)

                root?.let {
                    TransitionManager.beginDelayedTransition(it, sharedAxis)
                }

                loading_root.visibility = View.VISIBLE
            } else {

                sharedAxis = MaterialSharedAxis(MaterialSharedAxis.X, true)
                loading_root?.let {
                    TransitionManager.beginDelayedTransition(it, sharedAxis)
                }

                loading_progress_bar.visibility = View.GONE
                error_pic.visibility = View.VISIBLE
                error_text.visibility = View.VISIBLE
            }

            GlobalScope.launch {
                delay(1000)
                MainScope().launch {
                    sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, true)
                    root?.let {
                        TransitionManager.beginDelayedTransition(it, sharedAxis)
                    }

                    isActivityPaused = false
                    loading_root.visibility = View.GONE
                }
            }
        }
    }

    private fun prepareLoadingView() {
        loading_progress_bar.visibility = View.GONE
        error_pic.visibility = View.GONE
        error_text.visibility = View.GONE
        done_pic.visibility = View.GONE
    }
}