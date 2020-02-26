package com.itis.readmore.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.itis.readmore.models.MyBook
import com.itis.readmore.views.ActiveView

@InjectViewState
class ActivePresenter : MvpPresenter<ActiveView>() {
    val fuser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    fun getAllBooks() {
        viewState.hide()
        val rootRef = FirebaseDatabase.getInstance().reference
        rootRef.child("Books").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val listBook = ArrayList<MyBook>()

                dataSnapshot.children.forEach {
                    val bookModel = it.getValue(MyBook::class.java)
                    if (bookModel!!.libraryId == fuser!!.uid)
                        listBook.add(bookModel)
                }

                viewState.initializeRecycler(listBook)
                viewState.show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
    }
}