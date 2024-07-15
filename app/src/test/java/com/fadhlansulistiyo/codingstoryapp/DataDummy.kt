package com.fadhlansulistiyo.codingstoryapp

import com.fadhlansulistiyo.codingstoryapp.data.model.ListStoriesItem

object DataDummy {

    fun generateDummyStoriesResponse(): List<ListStoriesItem> {
        val items: MutableList<ListStoriesItem> = arrayListOf()
        for (i in 0..100) {
            val stories = ListStoriesItem(
                "story-Q1L_F3QlhdKTSgTw",
                "https://story-api.dicoding.dev/images/stories/photos-1721002649525_1e6c1ef7b0062a05ad6e.jpg",
                "2024-07-15T00:17:29.534Z",
                "Fadhlan",
                "cekcek",
                null,
                null
            )
            items.add(stories)
        }
        return items
    }
}