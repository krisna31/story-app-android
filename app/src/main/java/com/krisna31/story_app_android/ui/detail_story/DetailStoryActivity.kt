package com.krisna31.story_app_android.ui.detail_story

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.krisna31.story_app_android.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var detailStoryBinding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailStoryBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(detailStoryBinding.root)

        val story = intent.getStringExtra(EXTRA_STORY)
    }

    companion object {
        const val EXTRA_STORY = "EXTRA_STORY"
    }
}