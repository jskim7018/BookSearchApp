package com.gmail.jskim7018.booksearchapp

import com.gmail.jskim7018.booksearchapp.model.book.BookModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainPresenter(private val view: MainContract.View): MainContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadNewBookList() {
        view.showProgressUi()
        val disposable = BookModelFactory.createInstance().getNewBookListItems()
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                view.hideProgressUi()
            }
            .onErrorComplete {
                view.showErrorMessage("Error: $it")
                true
            }
            .subscribeOn(Schedulers.io())
            .subscribe {
                view.updateNewBookListView(it)
            }

        compositeDisposable.add(disposable)
    }

    override fun dispose() {
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}