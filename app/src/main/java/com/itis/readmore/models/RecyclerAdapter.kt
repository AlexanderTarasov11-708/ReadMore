package com.itis.readmore.models

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.itis.readmore.R
import com.itis.readmore.fragments.BookFragment
import com.itis.readmore.inflate
import kotlinx.android.synthetic.main.recyclerview_book.view.*

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.BookHolder>() {
    var books = ArrayList<MyBook>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_book, false)
        return BookHolder(inflatedView)
    }


    class BookHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var book: MyBook? = null
        val fuser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val bookFragment = BookFragment()
            val arguments = Bundle()
            arguments.putSerializable("book", book)

            val rootRef = FirebaseDatabase.getInstance().reference
            rootRef.child("Users").child(fuser!!.uid)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user = dataSnapshot.getValue(User::class.java)
                        arguments.putSerializable("user", user)
                        bookFragment.arguments = arguments
                        val activity = view.context as AppCompatActivity
                        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.container, bookFragment)
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        throw databaseError.toException()
                    }
                })
        }

        companion object {
            private val BOOK_KEY = "BOOK"
        }

        fun bindBook(book: MyBook) {
            this.book = book
            Glide.with(view.context).load(book.imageURL).into(view.bookImage)
            view.bookTitle.text = book.title
            view.bookAuthor.text = book.author
            view.bookDesc.text = "Описание: " + book.description
        }
    }

    override fun getItemCount() = books.size

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val itemBook = books[position]
        holder.bindBook(itemBook)
    }
}