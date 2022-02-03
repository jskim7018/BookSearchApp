package com.gmail.jskim7018.booksearchapp.model.data

data class BookListItem(val title: String, val subTitle: String, val isbn13: String,
                        val price: String, val imageUrl: String, val url: String,
                        val isLoading:Boolean = false)