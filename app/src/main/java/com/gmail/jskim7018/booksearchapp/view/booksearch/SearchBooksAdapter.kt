package com.gmail.jskim7018.booksearchapp.view.booksearch

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gmail.jskim7018.booksearchapp.model.data.BookListItem
import com.gmail.jskim7018.booksearchapp.R
import com.gmail.jskim7018.booksearchapp.view.bookdetail.BookDetailActivity

class SearchBooksAdapter(private val dataSet: MutableList<BookListItem>, private val context: Context):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_VIEW_TYPE_BOOK = 0
        const val ITEM_VIEW_TYPE_LOADING = 1
    }

    inner class BookViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view), View.OnClickListener {
        private var bookListItem: BookListItem? = null
        private val titleView: TextView = view.findViewById(R.id.title_tv)
        private val subtitleView: TextView = view.findViewById(R.id.subtitle_tv)
        private val priceView: TextView = view.findViewById(R.id.price_tv)
        private val isbn13View: TextView = view.findViewById(R.id.isbn13_tv)
        private val imageView: ImageView = view.findViewById(R.id.imageView)

        init {
            view.setOnClickListener(this)
        }

        fun bindBookListItem(bookListItem: BookListItem, context: Context) {
            this.bookListItem = bookListItem
            setTextOrGone(titleView, bookListItem.title)
            setTextOrGone(subtitleView, bookListItem.subTitle)
            setTextOrGone(priceView, bookListItem.price)
            setTextOrGone(isbn13View, bookListItem.isbn13)

            Glide.with(context).load(bookListItem.imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView)
        }

        override fun onClick(view: View) {
            val intent = Intent(context, BookDetailActivity::class.java)
            intent.putExtra(BookDetailActivity.BOOK_ISBN13_ID, bookListItem?.isbn13)
            context.startActivity(intent)
        }

        private fun setTextOrGone(textView: TextView, text:String) {
            if(TextUtils.isEmpty(text)) {
                textView.visibility = TextView.GONE
            } else {
                textView.visibility = TextView.VISIBLE
                textView.text = text
            }
        }
    }

    inner class LoadingViewHolder(view:View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_BOOK -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_row_item, parent, false)
                BookViewHolder(view, context)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.loading_list_row_item, parent, false)
                LoadingViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holderBook: RecyclerView.ViewHolder, position: Int) {
        if(holderBook is BookViewHolder) holderBook.bindBookListItem(dataSet.get(position), context)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet.get(position).isLoading) {
            ITEM_VIEW_TYPE_LOADING
        } else {
            ITEM_VIEW_TYPE_BOOK
        }
    }
}