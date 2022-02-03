package com.gmail.jskim7018.booksearchapp.view.booksearch

import android.util.Log
import com.gmail.jskim7018.booksearchapp.model.book.BookModelFactory
import com.gmail.jskim7018.booksearchapp.model.book.BookModelInterface
import com.gmail.jskim7018.booksearchapp.model.data.BookListItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*

class BookSearchPresenter(private val view: BookSearchContract.View): BookSearchContract.Presenter {

    companion object {
        const val PAGE_ITEM_COUNT = 10
    }
    enum class SearchType{
        OR, EXCLUDE
    }
    private var bookItemList: MutableList<BookListItem> = mutableListOf()
    private val cachedQueueItemList: Queue<BookListItem> = LinkedList()
    private var currPage: Int = 1
    private var isLoading: Boolean = false
    private var lastQuery: String=""
    private var currSearchType: SearchType = SearchType.OR
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private var canLoadMore = true

    private val bookModel: BookModelInterface = BookModelFactory.createInstance()

    init{
        view.initSearchList(bookItemList)
    }

    override fun loadBookItemsBySearch(queryText: String): Boolean {
        val trimmedQuery = queryText.trim()
        if(trimmedQuery == lastQuery) return false

        if(queryText.matches(Regex("\\w+")) || queryText.matches(Regex("\\w+\\|\\w+"))) {
            currSearchType = SearchType.OR
        } else if(queryText.matches(Regex("\\w+-\\w+"))) {
            currSearchType = SearchType.EXCLUDE
        } else {
            view.showInvalidQueryMessage("$queryText is invalid. " +
                    "\nPlease input search keyword in keyword1|keyword2 or keyword1-keyword2 format.")
            return false
        }
        canLoadMore = true
        lastQuery = trimmedQuery
        currPage = 1
        bookItemList.clear()
        cachedQueueItemList.clear()
        view.updateAllSearchListItem()
        view.hideNoSearchResultUi()
        return loadNextBookItems(true)
    }

    override fun loadNextBookItems(initialLoad: Boolean): Boolean {
        if(isLoading || !canLoadMore) return false
        if(cachedQueueItemList.size >= PAGE_ITEM_COUNT) {
            updateNextItemsWithQueue()
            return true
        }
        val minNeedItemCnt = PAGE_ITEM_COUNT - cachedQueueItemList.size
        if(!initialLoad) insertLoadingItem()
        isLoading = true

        var flowable = Flowable.empty<Pair<Int, MutableList<BookListItem>>>()
        when(currSearchType) {
            SearchType.OR -> {
                flowable = bookModel.getBookListByKeywords(lastQuery.split("|"), currPage, minNeedItemCnt)
            }
            SearchType.EXCLUDE-> {
                val splitKeywords = lastQuery.split("-")
                val includeList = if (splitKeywords.isNotEmpty()) mutableListOf(splitKeywords.get(0)) else emptyList()
                val excludeList = if (splitKeywords.size>1)mutableListOf(splitKeywords.get(1)) else emptyList()
                flowable = bookModel.getBookListByIncludeKeywordsAndExcludeKeywords(
                    includeList, excludeList,
                        currPage, minNeedItemCnt
                    )
            }
        }

        if(initialLoad) {
            view.hideGuideUi()
            view.showProgressUi()
        }
        val disposable = flowable
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                if(initialLoad) view.hideProgressUi()
                isLoading = false
            }
            .subscribeOn(Schedulers.io())
            .onErrorComplete {
                view.showErrorMessage("Error: $it")
                if(!initialLoad) removeLoadingItem()
                true
            }
            .subscribe {
                if(it.second.size == 0) {
                    if(initialLoad) view.showNoSearchResultUi()
                    canLoadMore = false
                }
                else {
                    cachedQueueItemList.addAll(it.second)
                }
                if(!initialLoad) removeLoadingItem()
                updateNextItemsWithQueue()
                currPage = it.first
            }
        compositeDisposable.add(disposable)
        return true
    }

    override fun dispose() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    private fun updateNextItemsWithQueue() {
        if(cachedQueueItemList.size == 0) return
        val startPos = bookItemList.size
        var insertedSize = 0
        for(i in 0 until PAGE_ITEM_COUNT ) {
            if(cachedQueueItemList.isEmpty()) break
            bookItemList.add(cachedQueueItemList.peek())
            insertedSize++
            cachedQueueItemList.remove()
        }
        view.updateInsertedSearchListItemRange(startPos!!, insertedSize)
        view.showLoadCompleteMessage(insertedSize, bookItemList.size)
    }

    private fun insertLoadingItem() {
        val pos = bookItemList.size
        bookItemList.add(getLoadingBookItem())
        view.updateInsertedSearchListItem(pos)
    }

    private fun removeLoadingItem() {
        if(bookItemList.size == 0) return
        val pos = bookItemList.size-1
        bookItemList.removeLast()
        view.updateRemovedSearchListItem(pos)
    }

    private fun getLoadingBookItem(): BookListItem {
        return BookListItem("","","","","",",",true)
    }
}