package com.gmail.jskim7018.booksearchapp.model.book

import com.gmail.jskim7018.booksearchapp.model.datasource.BookDataSourceFactory
import com.gmail.jskim7018.booksearchapp.model.datasource.BookHttpDataSource

object BookModelFactory {
    fun createInstance(): BookModelInterface {
        return BookModel(BookDataSourceFactory.createInstance())
    }
}