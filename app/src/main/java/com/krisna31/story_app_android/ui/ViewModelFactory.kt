package com.krisna31.story_app_android.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krisna31.story_app_android.data.user.UserPreference
import com.krisna31.story_app_android.ui.add_story.AddStoryViewModel
import com.krisna31.story_app_android.ui.detail_story.DetailStoryViewModel
import com.krisna31.story_app_android.ui.login.LoginViewModel
import com.krisna31.story_app_android.ui.main.MainViewModel
import com.krisna31.story_app_android.ui.maps.MapsViewModel
import com.krisna31.story_app_android.ui.register.RegisterViewModel
import com.krisna31.story_app_android.util.Injection

class ViewModelFactory(private val pref: UserPreference, private val context: Context? = null) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref, Injection.provideRepository(context)) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(pref) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(pref) as T
            }

            modelClass.isAssignableFrom(DetailStoryViewModel::class.java) -> {
                DetailStoryViewModel(pref) as T
            }

            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(pref) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}