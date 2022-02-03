package com.gmail.jskim7018.booksearchapp.model.datasource

import android.util.Log
import com.gmail.jskim7018.booksearchapp.model.data.BookDetailItem
import com.gmail.jskim7018.booksearchapp.model.data.BookListItem
import com.gmail.jskim7018.booksearchapp.model.data.BookPdfItem
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

class BookHttpDataSource : BookDataSourceInterface {
    companion object {
        private const val API_BOOK_STORE_1_LINK = "https://api.itbook.store/1.0"
        private const val API_NEW_BOOKS_URL = "$API_BOOK_STORE_1_LINK/new"
        private const val API_BOOK_DETAIL_URL = "$API_BOOK_STORE_1_LINK/books"
        private const val API_BOOK_SEARCH_URL = "$API_BOOK_STORE_1_LINK/search"
    }

    private val okHttpClient: OkHttpClient = OkHttpClient()

    override fun getBookDetailItem(isbn13: String): BookDetailItem {
        val request: Request = Request.Builder()
            .url("$API_BOOK_DETAIL_URL/$isbn13")
            .build()
        val response = okHttpClient.newCall(request).execute()
        val responseData = response.body().string()

        val bookJSONObject = JSONObject(responseData)
        val bookPdfJSONObject = bookJSONObject.optJSONObject("pdf")?:JSONObject()
        val bookPdfItemList : MutableList<BookPdfItem> = mutableListOf()
        for(key in bookPdfJSONObject.keys()) {
            bookPdfItemList.add(BookPdfItem(title=key, bookPdfJSONObject.getString(key)))
        }

        val bookDetailItem = BookDetailItem(
            title= bookJSONObject.getString("title"),
            subTitle= bookJSONObject.getString("subtitle"),
            authors= bookJSONObject.getString("authors") ,
            publisher= bookJSONObject.getString("publisher"),
            language= bookJSONObject.getString("language"),
            isbn10= bookJSONObject.getString("isbn10"),
            isbn13= bookJSONObject.getString("isbn13"),
            pages= bookJSONObject.getString("pages"),
            year= bookJSONObject.getString("year"),
            rating= bookJSONObject.getDouble("rating"),
            desc= bookJSONObject.getString("desc"),
            price= bookJSONObject.getString("price"),
            imageUrl= bookJSONObject.getString("image"),
            url= bookJSONObject.getString("url"),
            pdfUrlList = bookPdfItemList.toList()
        )
        return bookDetailItem
    }

    override fun getBookListItemsByKeyword(keyword: String, pageNum: Int): MutableList<BookListItem> {
        return requestForBookListItems("$API_BOOK_SEARCH_URL/$keyword/$pageNum")
    }

    override fun getNewBookListItems(): MutableList<BookListItem> {
        return requestForBookListItems(API_NEW_BOOKS_URL)
    }

    private fun requestForBookListItems(url: String): MutableList<BookListItem> {
        val request: Request = Request.Builder()
            .url(url)
            .build()

        val response = okHttpClient.newCall(request).execute()
        val responseData = response.body().string()

        val jsonObject = JSONObject(responseData)

        val bookJSONArray = jsonObject.getJSONArray("books")

        val bookItemList: MutableList<BookListItem> = mutableListOf()
        for (i in 0 until bookJSONArray.length()) {
            val bookJSONObject = bookJSONArray.get(i) as JSONObject

            val bookItem = BookListItem(
                title = bookJSONObject.getString("title"),
                subTitle = bookJSONObject.getString("subtitle"),
                isbn13 = bookJSONObject.getString("isbn13"),
                price = bookJSONObject.getString("price"),
                imageUrl = bookJSONObject.getString("image"),
                url = bookJSONObject.getString("url")
            )
            bookItemList.add(bookItem)
        }
        return bookItemList
    }
}