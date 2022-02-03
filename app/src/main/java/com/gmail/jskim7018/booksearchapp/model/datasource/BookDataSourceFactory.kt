package com.gmail.jskim7018.booksearchapp.model.datasource

object BookDataSourceFactory {
    fun createInstance(): BookDataSourceInterface {
        return BookHttpDataSource()
    }
}