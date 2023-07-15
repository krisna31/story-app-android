package com.krisna31.story_app_android.ui.detail_story

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.krisna31.story_app_android.R
import com.krisna31.story_app_android.data.user.UserPreference
import com.krisna31.story_app_android.databinding.ActivityDetailStoryBinding
import com.krisna31.story_app_android.ui.ViewModelFactory
import com.krisna31.story_app_android.ui.main.dataStore
import com.krisna31.story_app_android.ui.welcome.WelcomeActivity

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var detailStoryBinding: ActivityDetailStoryBinding
    private lateinit var detailStoryViewModel: DetailStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailStoryBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(detailStoryBinding.root)

        setupView()
        setupViewModel()
    }

    private fun setupView() {
        supportActionBar?.title = getString(R.string.detail_app_name)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewModel() {
        detailStoryViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[DetailStoryViewModel::class.java]

        detailStoryViewModel.getUser().observe(this) { user ->
            if (user.apiToken.isNotEmpty() || user.apiToken != "") {
                detailStoryViewModel.getStory(user.apiToken, intent.getStringExtra(EXTRA_STORY)!!)
                detailStoryViewModel.story.observe(this) { story ->
                    detailStoryBinding.tvOwner.text = story.name
                    detailStoryBinding.tvDeskripsi.text = story.description
                    Glide
                        .with(this)
                        .load(story.photoUrl)
                        .centerCrop()
                        .into(detailStoryBinding.ivStory)
                }
                detailStoryViewModel.isLoading.observe(this) { isLoading ->
                    showLoading(isLoading)
                }
                detailStoryViewModel.errorMessage.observe(this) {
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

    private fun showLoading(state: Boolean) {
        if (state) {
            detailStoryBinding.progressBar.visibility = View.VISIBLE
        } else {
            detailStoryBinding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_STORY = "EXTRA_STORY"
    }
}