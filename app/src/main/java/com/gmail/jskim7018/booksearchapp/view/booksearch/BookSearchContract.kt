package com.gmail.jskim7018.booksearchapp.view.booksearch

import com.gmail.jskim7018.booksearchapp.model.data.BookListItem

interface BookSearchContract {
    interface View {
        fun initSearchList(bookItemList: MutableList<BookListItem>)
        fun updateInsertedSearchListItemRange(startPos:Int, size:Int)
        fun updateInsertedSearchListItem(position: Int)
        fun updateRemovedSearchListItem(position: Int)
        fun updateAllSearchListItem()
        fun showInvalidQueryMessage(msg: String)
        fun showLoadCompleteMessage(loadSize: Int, loadedTotalSize: Int)

        fun showProgressUi()
        fun hideProgressUi()
        fun showNoSearchResultUi()
        fun hideNoSearchResultUi()

        fun hideGuideUi()
        fun showErrorMessage(msg: String)
    }
    interface Presenter {
        fun loadBookItemsBySearch(queryText: String): Boolean
        fun loadNextBookItems(initialLoad: Boolean): Boolean
        fun dispose()
    }
}