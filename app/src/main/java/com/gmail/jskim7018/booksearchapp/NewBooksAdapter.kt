package com.gmail.jskim7018.booksearchapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.jskim7018.booksearchapp.model.data.BookListItem
import com.gmail.jskim7018.booksearchapp.view.bookdetail.BookDetailActivity

class NewBooksAdapter(private val dataSet: List<BookListItem>, private val context: Context):
    RecyclerView.Adapter<NewBooksAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view), View.OnClickListener {
        private var bookListItem: BookListItem? = null
        private val titleView: TextView = view.findViewById(R.id.titleView)
        private val subtitleView: TextView = view.findViewById(R.id.subtitleView)
        private val imageView: ImageView = view.findViewById(R.id.imageView)

        init {
            view.setOnClickListener(this)
        }

        fun bindBookListItem(bookListItem: BookListItem, context: Context) {
            this.bookListItem = bookListItem
            titleView.text = bookListItem.title
            subtitleView.text = bookListItem.subTitle
            Glide.with(context).load(bookListItem.imageUrl).into(imageView)
        }

        override fun onClick(view: View) {
            val intent = Intent(context, BookDetailActivity::class.java)
            intent.putExtra(BookDetailActivity.BOOK_ISBN13_ID, bookListItem?.isbn13)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.book_list_grid_item, parent, false)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindBookListItem(dataSet.get(position), context)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}