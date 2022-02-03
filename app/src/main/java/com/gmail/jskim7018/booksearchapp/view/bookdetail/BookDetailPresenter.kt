package com.gmail.jskim7018.booksearchapp.view.bookdetail

import android.util.Log
import com.gmail.jskim7018.booksearchapp.model.book.BookModelFactory
import com.gmail.jskim7018.booksearchapp.model.book.BookModelInterface
import com.gmail.jskim7018.booksearchapp.model.data.BookDetailItem
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class BookDetailPresenter(private val view: BookDetailContract.View): BookDetailContract.Presenter {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val bookModel: BookModelInterface = BookModelFactory.createInstance()

    override fun loadDetailBookItem(isbn13: String) {
        view.showProgressUi()
            val disposable = bookModel.getBookDetailItem(isbn13)
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorComplete {
                view.showErrorMessage("Error: $it")
                true
            }
            .doFinally{
                view.hideProgressUi()
            }
            .subscribeOn(Schedulers.io())
            .subscribe {
                if(it != null) view.updateDetailBookItemView(it)
            }
        compositeDisposable.add(disposable)
    }

    override fun dispose() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}