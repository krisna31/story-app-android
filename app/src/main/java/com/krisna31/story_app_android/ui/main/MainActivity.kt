package com.krisna31.story_app_android.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.krisna31.story_app_android.R
import com.krisna31.story_app_android.data.user.UserPreference
import com.krisna31.story_app_android.databinding.ActivityMainBinding
import com.krisna31.story_app_android.ui.ViewModelFactory
import com.krisna31.story_app_android.ui.add_story.AddStoryActivity
import com.krisna31.story_app_android.ui.maps.MapsActivity
import com.krisna31.story_app_android.ui.welcome.WelcomeActivity

public val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    private fun setupView() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.main_app_name)
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this@MainActivity,
            ViewModelFactory(UserPreference.getInstance(dataStore), this@MainActivity)
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user.apiToken.isNotEmpty() || user.apiToken != "") {
                val adapter = MainAdapter()
                binding.rvStory.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
                mainViewModel.getStories(user.apiToken).observe(this) {
                    adapter.submitData(lifecycle, it)
                }
                binding.progressBar.visibility = View.GONE
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_act_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutButton -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Yakin Logout?")
                    setPositiveButton("Yakin") { _, _ ->
                        mainViewModel.logout()
                        val intent = Intent(context, WelcomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    setNegativeButton("Batal") { dialog, _ ->
                        dialog.cancel()
                    }
                    create()
                    show()
                }
                return true
            }

            R.id.mapsButton -> {
                startActivity(Intent(this, MapsActivity::class.java))
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}