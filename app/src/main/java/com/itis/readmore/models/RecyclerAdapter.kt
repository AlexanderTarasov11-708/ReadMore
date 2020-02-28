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
import com.itis.readmore.fragments.LibraryFragment
import com.itis.readmore.fragments.ShelfFragment
import com.itis.readmore.inflate
import kotlinx.android.synthetic.main.recyclerview_book.view.*

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.LibraryHolder>() {
    var library = ArrayList<Library>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryHolder {
        val inflatedView = parent.inflate(R.layout.recyclerview_book, false)
        return LibraryHolder(inflatedView)
    }

    class LibraryHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        private var view: View = v
        private var library: Library? = null
        val fuser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val shelfFragment = ShelfFragment()
            val arguments = Bundle()
            arguments.putSerializable("library", library)

            val rootRef = FirebaseDatabase.getInstance().reference
            rootRef.child("Users").child(fuser!!.uid)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val user = dataSnapshot.getValue(User::class.java)
                        arguments.putSerializable("user", user)
                        shelfFragment.arguments = arguments
                        val activity = view.context as AppCompatActivity
                        val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.container, shelfFragment)
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        throw databaseError.toException()
                    }
                })
        }

        companion object {
            private val LIBRARY_KEY = "LIBRARY"
        }

        fun bindLibrary(library: Library) {
            this.library = library
            view.library_title.text = library.name
            view.library_id.text = library.id
            view.library_books.text = library.bookCount + "книг в библиотеке"
        }
    }

    override fun getItemCount() = library.size

    override fun onBindViewHolder(holder: LibraryHolder, position: Int) {
        val itemLibrary = library[position]
        holder.bindLibrary(itemLibrary)
    }
}