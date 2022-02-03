package com.gmail.jskim7018.booksearchapp.model.book

import android.util.Log
import com.gmail.jskim7018.booksearchapp.model.data.BookDetailItem
import com.gmail.jskim7018.booksearchapp.model.data.BookListItem
import com.gmail.jskim7018.booksearchapp.model.datasource.BookDataSourceInterface
import io.reactivex.rxjava3.core.Flowable

class BookModel(private val bookDataSource: BookDataSourceInterface): BookModelInterface{
    override fun getNewBookListItems(): Flowable<MutableList<BookListItem>> {
        return Flowable.fromCallable {
            bookDataSource.getNewBookListItems()
        }
    }

    override fun getBookDetailItem(isbn13: String): Flowable<BookDetailItem> {
        return Flowable.fromCallable {
            bookDataSource.getBookDetailItem(isbn13)
        }
    }

    override fun getBookListByKeywords(
        keywords: List<String>,
        startPage: Int,
        minNeedItemCnt:Int
    ): Flowable<Pair<Int, MutableList<BookListItem>>> {
        return Flowable.fromCallable {
            var currPage = startPage
            val hashMap = hashMapOf<String, Pair<Int, BookListItem>>()
            var order = 0
            while(hashMap.size < minNeedItemCnt) {
                var isAllBookListEmpty = true
                for (keyword in keywords) {
                    val bookItemList = bookDataSource.getBookListItemsByKeyword(keyword, currPage)
                    if(!bookItemList.isEmpty()) isAllBookListEmpty = false
                    for (bookItem in bookItemList) {
                        if(bookItem.title.contains(keyword, ignoreCase = true) ||
                            bookItem.subTitle.contains(keyword, ignoreCase = true)) {
                           hashMap.put(bookItem.isbn13, Pair(order++, bookItem))
                        }
                    }
                }
                if(isAllBookListEmpty) break
                currPage++
            }
            Pair(currPage,getOrderedBookListFromPair(hashMap.values.toMutableList()))
        }
    }

    override fun getBookListByIncludeKeywordsAndExcludeKeywords(
        includeKeywords: List<String>,
        excludeKeywords: List<String>,
        startPage: Int,
        minNeedItemCnt: Int
    ): Flowable<Pair<Int, MutableList<BookListItem>>> {
        return Flowable.fromCallable {
            var currPage = startPage
            val hashMap = hashMapOf<String, Pair<Int,BookListItem>>()
            var order = 0
            while(hashMap.size < minNeedItemCnt) {
                var isAllBookListEmpty = true
                for (includeKeyword in includeKeywords) {
                    val bookItemList = bookDataSource.getBookListItemsByKeyword(includeKeyword, currPage)
                    if(!bookItemList.isEmpty()) isAllBookListEmpty = false
                    for (bookItem in bookItemList) {
                        if(bookItem.title.contains(includeKeyword, ignoreCase = true) ||
                            bookItem.subTitle.contains(includeKeyword, ignoreCase = true)) {
                            var containsExcludeKeyword = false
                            for (excludeKeyword in excludeKeywords) {
                                if(bookItem.title.contains(excludeKeyword, ignoreCase = true) ||
                                    bookItem.subTitle.contains(excludeKeyword, ignoreCase = true)) {
                                    containsExcludeKeyword = true
                                    break
                                }
                            }
                            if(!containsExcludeKeyword) hashMap.put(bookItem.isbn13, Pair(order++,bookItem))
                        }
                    }
                }
                if(isAllBookListEmpty) break
                currPage++
            }
            Pair(currPage,getOrderedBookListFromPair(hashMap.values.toMutableList()))
        }
    }
    private fun getOrderedBookListFromPair(orderPairBookList: MutableList<Pair<Int,BookListItem>>):
            MutableList<BookListItem> {
        orderPairBookList.sortBy { it.first }
        val orderedBookList = orderPairBookList.map {
            it.second
        }
        return orderedBookList.toMutableList()
    }
}