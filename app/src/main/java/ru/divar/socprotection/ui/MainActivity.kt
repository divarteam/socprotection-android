package ru.divar.socprotection.ui

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.transition.TransitionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.divar.socprotection.R
import ru.divar.socprotection.databinding.ActivityMainBinding
import ru.divar.socprotection.util.NavigationUtil.setupWithNavController

class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private var isActivityPaused = false

    private val coroutineScope = CoroutineScope(Job() + CoroutineExceptionHandler { coroutineContext, throwable ->
        throwable.printStackTrace()
    })

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        configureEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun onBackPressed() {
        stopLoadingAnimation()
        super.onBackPressed()
    }

    private fun configureEdgeToEdge() {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
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
        coroutineScope.launch(Dispatchers.Main) {
            prepareLoadingView()
            binding.loading.loadingProgressBar.visibility = View.VISIBLE

            val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, false)
            binding.root.let {
                TransitionManager.beginDelayedTransition(it, sharedAxis)
            }

            binding.loading.loadingRoot.visibility = View.VISIBLE
        }
    }

    fun stopLoadingAnimation() {

        coroutineScope.launch(Dispatchers.Main) {
            val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, true)
            binding.root.let {
                TransitionManager.beginDelayedTransition(it, sharedAxis)
            }

            binding.loading.loadingRoot.visibility = View.GONE
            isActivityPaused = false
        }
    }

    fun onError(errorText: String) {
        coroutineScope.launch(Dispatchers.Main) {
            var sharedAxis: MaterialSharedAxis

            if (errorText != "")
                binding.loading.errorText.text = errorText

            if (binding.loading.loadingRoot.visibility == View.GONE) {
                isActivityPaused = true
                prepareLoadingView()

                binding.loading.errorPic.visibility = View.VISIBLE
                binding.loading.errorText.visibility = View.VISIBLE

                sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, true)

                binding.root.let {
                    TransitionManager.beginDelayedTransition(it, sharedAxis)
                }

                binding.loading.loadingRoot.visibility = View.VISIBLE
            } else {

                sharedAxis = MaterialSharedAxis(MaterialSharedAxis.X, true)
                binding.loading.loadingRoot.let {
                    TransitionManager.beginDelayedTransition(it, sharedAxis)
                }

                binding.loading.loadingProgressBar.visibility = View.GONE
                binding.loading.errorPic.visibility = View.VISIBLE
                binding.loading.errorText.visibility = View.VISIBLE
            }

            coroutineScope.launch(Dispatchers.IO) {
                delay(1000)
                coroutineScope.launch(Dispatchers.Main) {
                    sharedAxis = MaterialSharedAxis(MaterialSharedAxis.Z, true)
                    binding.root.let {
                        TransitionManager.beginDelayedTransition(it, sharedAxis)
                    }

                    isActivityPaused = false
                    binding.loading.loadingRoot.visibility = View.GONE
                }
            }
        }
    }

    private fun prepareLoadingView() {
        binding.loading.loadingProgressBar.visibility = View.GONE
        binding.loading.errorPic.visibility = View.GONE
        binding.loading.errorText.visibility = View.GONE
        binding.loading.donePic.visibility = View.GONE
    }
}