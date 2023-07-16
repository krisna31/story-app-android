package com.krisna31.story_app_android.utils

import com.krisna31.story_app_android.data.api.response.ListStoryItem

object DataDummy {

    fun generateDummyNewsEntity(): List<ListStoryItem> {

        val storyList = ArrayList<ListStoryItem>()

        for (i in 0..10) {
            val story = ListStoryItem(
                "Title $i",
                "2022-02-22T22:22:22Z",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "https://www.dicoding.com/",
                20.10,
                "id $i",
                10.10,
            )
            storyList.add(story)
        }

        return storyList

    }
}