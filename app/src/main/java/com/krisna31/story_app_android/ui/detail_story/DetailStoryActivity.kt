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

        
    }
}