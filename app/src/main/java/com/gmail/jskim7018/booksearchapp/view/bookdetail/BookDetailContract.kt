package com.gmail.jskim7018.booksearchapp.view.bookdetail

import com.gmail.jskim7018.booksearchapp.model.data.BookDetailItem

interface BookDetailContract {
    interface View {
        fun updateDetailBookItemView(bookDetailItem: BookDetailItem)
        fun showProgressUi()
        fun hideProgressUi()

        fun showErrorMessage(msg: String)
    }

    interface Presenter {
        fun loadDetailBookItem(isbn13: String)
        fun dispose()
    }
}