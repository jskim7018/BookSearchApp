package com.gmail.jskim7018.booksearchapp.model.data

data class BookDetailItem(val title: String, val subTitle: String, val authors: String,
                          val publisher: String, val language: String, val isbn10: String,
                          val isbn13: String, val pages: String, val year: String,
                          val rating: Double, val desc: String, val price: String,
                          val imageUrl: String, val url: String, val pdfUrlList: List<BookPdfItem>)