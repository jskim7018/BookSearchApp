package com.gmail.jskim7018.booksearchapp.view.bookdetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gmail.jskim7018.booksearchapp.R
import com.gmail.jskim7018.booksearchapp.model.data.BookDetailItem
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.activity_book_detail.loading_progress_ui
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class BookDetailActivity : AppCompatActivity(), BookDetailContract.View {
    companion object {
        const val BOOK_ISBN13_ID = "BOOK_ISBN13_ID"
    }
    var presenter: BookDetailContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        supportActionBar?.title = "IT Book Detail"


        presenter = BookDetailPresenter(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val bookId = intent.extras?.getString(BOOK_ISBN13_ID) as String

        presenter?.loadDetailBookItem(bookId)
    }

    override fun updateDetailBookItemView(bookDetailItem: BookDetailItem) {
        Glide.with(this).load(bookDetailItem.imageUrl).into(book_image_view)
        val title = "${bookDetailItem.title}${if (!TextUtils.isEmpty(bookDetailItem.subTitle)) 
            ":"+bookDetailItem.subTitle else ""}"
        title_tv.text = title
        authors_tv.text = bookDetailItem.authors
        publisher_tv.text = bookDetailItem.publisher
        language_tv.text = bookDetailItem.language
        rating_bar.rating = bookDetailItem.rating.toFloat()
        price_tv.text = bookDetailItem.price
        year_tv.text = bookDetailItem.year
        page_tv.text = bookDetailItem.pages
        isbn10_tv.text = bookDetailItem.isbn10
        isbn13_tv.text = bookDetailItem.isbn13
        desc_tv.text = bookDetailItem.desc
        url_btn.text = bookDetailItem.url
        url_btn.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url_btn.text.toString()))
            startActivity(browserIntent)
        }
        if(!bookDetailItem.pdfUrlList.isEmpty()) {
            book_pdf_recycler_view.adapter = PdfListAdapter(bookDetailItem.pdfUrlList, this)
        } else {
            pdf_text.visibility = TextView.GONE
            book_pdf_recycler_view.visibility = RecyclerView.GONE
        }
    }

    override fun showProgressUi() {
        detail_view_layout.visibility = LinearLayout.INVISIBLE
        loading_progress_ui.visibility = ProgressBar.VISIBLE
    }

    override fun hideProgressUi() {
        loading_progress_ui.visibility = ProgressBar.INVISIBLE
        detail_view_layout.visibility = LinearLayout.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.dispose()
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}