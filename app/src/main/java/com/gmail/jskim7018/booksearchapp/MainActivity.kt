package com.gmail.jskim7018.booksearchapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

import com.gmail.jskim7018.booksearchapp.model.book.BookModelFactory
import com.gmail.jskim7018.booksearchapp.model.data.BookDetailItem
import com.gmail.jskim7018.booksearchapp.model.data.BookListItem
import com.gmail.jskim7018.booksearchapp.view.bookdetail.BookDetailContract
import com.gmail.jskim7018.booksearchapp.view.booksearch.BookSearchActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.View {
    var presenter: MainContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "IT Book Search App"

        presenter = MainPresenter(this)

        search_button.setOnClickListener {
            val intent = Intent(this, BookSearchActivity::class.java)
            startActivity(intent)
        }

        presenter?.loadNewBookList()
    }

    override fun updateNewBookListView(bookList: MutableList<BookListItem>) {
        val newBooksAdapter = NewBooksAdapter(dataSet= bookList,context = this)
        new_books_recycler_view.adapter = newBooksAdapter
    }

    override fun showProgressUi() {
        new_books_recycler_view.visibility = RecyclerView.INVISIBLE
        loading_progress_ui.visibility = ProgressBar.VISIBLE
    }

    override fun hideProgressUi() {
        new_books_recycler_view.visibility = RecyclerView.VISIBLE
        loading_progress_ui.visibility = ProgressBar.INVISIBLE
    }


    override fun onDestroy() {
        super.onDestroy()
        presenter?.dispose()
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}