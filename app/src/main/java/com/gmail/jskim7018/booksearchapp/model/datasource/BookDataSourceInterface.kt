package com.gmail.jskim7018.booksearchapp.model.datasource

import com.gmail.jskim7018.booksearchapp.model.data.BookDetailItem
import com.gmail.jskim7018.booksearchapp.model.data.BookListItem

interface BookDataSourceInterface {

    fun getBookDetailItem(isbn13: String): BookDetailItem

    fun getBookListItemsByKeyword(keyword: String, pageNum: Int): MutableList<BookListItem>

    fun getNewBookListItems(): MutableList<BookListItem>
}