package com.gmail.jskim7018.booksearchapp

import com.gmail.jskim7018.booksearchapp.model.data.BookListItem

interface MainContract {
    interface View {
        fun updateNewBookListView(bookList: MutableList<BookListItem>)
        fun showProgressUi()
        fun hideProgressUi()

        fun showErrorMessage(msg: String)
    }
    interface Presenter{
        fun loadNewBookList()
        fun dispose()
    }
}