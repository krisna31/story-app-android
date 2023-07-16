package com.krisna31.story_app_android.ui.maps

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.krisna31.story_app_android.R
import com.krisna31.story_app_android.data.user.UserPreference
import com.krisna31.story_app_android.databinding.ActivityMapsBinding
import com.krisna31.story_app_android.ui.ViewModelFactory
import com.krisna31.story_app_android.ui.main.dataStore
import com.krisna31.story_app_android.ui.welcome.WelcomeActivity

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MapsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupViewModel()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        mapsViewModel.getUser().observe(this) { user ->
            if (user.apiToken.isNotEmpty() || user.apiToken != "") {
                mapsViewModel.getStoriesOnlyLocation(user.apiToken)
                mapsViewModel.story.observe(this) { story ->
                    if (story != null) {
                        for (i in story.indices) {
                            val location = LatLng(story[i].lat as Double, story[i].lon as Double)
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(location)
                                    .title(story[i].name)
                                    .snippet(story[i].description)
                            )
                        }
                        val latestStory = LatLng(
                            story[story.size - 1].lat as Double,
                            story[story.size - 1].lon as Double
                        )
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latestStory, 15f))
                    }
                }
                mapsViewModel.errorMessage.observe(this) {
                    if (it != null) {
                        AlertDialog.Builder(this).apply {
                            setTitle("Oops!")
                            setMessage(it)
                            setPositiveButton("OK") { _, _ -> }
                            create()
                            show()
                        }
                    }
                }
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupViewModel() {
        mapsViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MapsViewModel::class.java]
    }
}