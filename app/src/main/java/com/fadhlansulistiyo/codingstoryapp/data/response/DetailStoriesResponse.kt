package com.fadhlansulistiyo.codingstoryapp.data.response

data class DetailStoriesResponse(
	val story: Story? = null
)

data class Story(
	val photoUrl: String? = null,
	val createdAt: String? = null,
	val name: String? = null,
	val description: String? = null,
	val id: String? = null
)

