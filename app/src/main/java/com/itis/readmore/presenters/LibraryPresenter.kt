package com.itis.readmore.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.itis.readmore.models.Library
import com.itis.readmore.views.LibraryView

@InjectViewState
class LibraryPresenter : MvpPresenter<LibraryView>() {
    var listLibraries = ArrayList<Library>()

    fun getMyLibrary() {
        viewState.hide()
        val valueShelveListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (valueRes in dataSnapshot.children) {
                    val libraryModel = valueRes.getValue(Library::class.java)
                    listLibraries.add(libraryModel!!)
                }

                viewState.initRecycler()
                viewState.show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        }

        val rootRef = FirebaseDatabase.getInstance().reference
        rootRef.child("Library").addListenerForSingleValueEvent(valueShelveListener)
    }
}