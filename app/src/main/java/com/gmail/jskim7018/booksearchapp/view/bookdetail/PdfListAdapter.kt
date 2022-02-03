package com.gmail.jskim7018.booksearchapp.view.bookdetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.jskim7018.booksearchapp.R
import com.gmail.jskim7018.booksearchapp.model.data.BookPdfItem

class PdfListAdapter(private val dataSet: List<BookPdfItem>, private val context: Context):
    RecyclerView.Adapter<PdfListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view), View.OnClickListener {
        private var bookPdfItem: BookPdfItem? = null
        private val pdfBtn: TextView = view.findViewById(R.id.pdf_btn)

        init {
            pdfBtn.setOnClickListener(this)
        }

        fun bindBookListItem(bookPdfItem: BookPdfItem) {
            this.bookPdfItem = bookPdfItem
            pdfBtn.text = bookPdfItem.title + ": " + bookPdfItem.url
        }

        override fun onClick(view: View) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(bookPdfItem?.url))
            context.startActivity(browserIntent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pdf_list_row_item, parent, false)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindBookListItem(dataSet.get(position))
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}