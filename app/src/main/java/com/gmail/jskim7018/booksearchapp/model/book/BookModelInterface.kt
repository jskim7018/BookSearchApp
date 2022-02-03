package com.gmail.jskim7018.booksearchapp.model.book

import com.gmail.jskim7018.booksearchapp.model.data.BookDetailItem
import com.gmail.jskim7018.booksearchapp.model.data.BookListItem
import io.reactivex.rxjava3.core.Flowable

interface BookModelInterface {

    fun getNewBookListItems(): Flowable<MutableList<BookListItem>>

    fun getBookDetailItem(isbn13: String): Flowable<BookDetailItem>

    /**
     * @param keywords keywords to search
     * @param startPage page to start query
     * @param minNeedItemCnt mimimum items need to be queried
     */
    fun getBookListByKeywords(keywords: List<String>, startPage:Int, minNeedItemCnt:Int):Flowable<Pair<Int, MutableList<BookListItem>>>

    /**
     * @param keywords keywords to search
     * @param notKeywords keywords to exclude items from search results
     * @param startPage the page to start search from
     * @param minNeedItemCnt mimimum items need to be queried
     */
    fun getBookListByIncludeKeywordsAndExcludeKeywords(includeKeywords: List<String>, excludeKeywords:List<String>,
                                            startPage:Int, minNeedItemCnt: Int): Flowable<Pair<Int, MutableList<BookListItem>>>
}