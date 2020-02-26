package com.itis.readmore.models

import java.io.Serializable


class MyBook : Serializable{
    var isbn: String? = null
    var title: String? = null
    var year: String? = null
    var author: String? = null
    var description: String? = null
    var imageURL: String? = null
    var libraryId: String? = null

class BookBuilder {
    private var ISBN: String? = null
    private var libraryId: String? = null
    private var title: String? = null
    private var author: String? = null
    private var publisher: String? = null
    private var publicationDate: String? = null
    private var category: String? = null
    private var description: String? = null
    private var image: String? = null
    private var previewLink: String? = null
    private var price: Double? = null
    private var rating: Double? = null
    fun ISBN(ISBN: String?): BookBuilder {
        this.ISBN = ISBN
        return this
    }

    fun libraryId(libraryId: String?): BookBuilder {
        this.libraryId = libraryId
        return this
    }

    fun title(title: String?): BookBuilder {
        this.title = title
        return this
    }

    fun author(author: String?): BookBuilder {
        this.author = author
        return this
    }

    fun publisher(publisher: String?): BookBuilder {
        this.publisher = publisher
        return this
    }

    fun publicationDate(publicationDate: String?): BookBuilder {
        this.publicationDate = publicationDate
        return this
    }

    fun price(price: Double?): BookBuilder {
        this.price = price
        return this
    }

    fun category(category: String?): BookBuilder {
        this.category = category
        return this
    }

    fun rating(rating: Double?): BookBuilder {
        this.rating = rating
        return this
    }

    fun image(image: String?): BookBuilder {
        this.image = image
        return this
    }

    fun previewLink(previewLink: String?): BookBuilder {
        this.previewLink = previewLink
        return this
    }

    fun description(description: String?): BookBuilder {
        this.description = description
        return this
    }

    fun build(): MyBook {
        val book = MyBook()
        book.title = title
        book.author = author
        book.year = publicationDate
        book.imageURL = image
        book.description = description
        return book
    }
}
}