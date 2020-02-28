package com.itis.readmore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.itis.readmore.R
import com.itis.readmore.models.Library
import com.itis.readmore.presenters.ShelfPresenter
import com.itis.readmore.views.ShelfView
import kotlinx.android.synthetic.main.fragment_shelf.view.*

class ShelfFragment : MvpAppCompatFragment(), ShelfView {
    @InjectPresenter
    lateinit var shelfPresenter: ShelfPresenter

    @ProvidePresenterTag(presenterClass = ShelfPresenter::class)
    fun provideShelfPresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideShelfPresenter() = ShelfPresenter()

    private var root: View? = null
    lateinit var library: Library

    companion object {
        fun newInstance(): ShelfFragment =
            ShelfFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_shelf, null)

        hide()

        val bundle = arguments

        library = bundle!!.getSerializable("library") as Library

        root!!.shelf_title.text = library.name
        root!!.shelf_id.text = library.id
        root!!.shelf_books.text = library.bookCount

        show()

        return root
    }

    override fun hide() {
        root!!.layout_shelf.visibility = CoordinatorLayout.INVISIBLE
        root!!.progressBar.visibility = CoordinatorLayout.VISIBLE
    }

    override fun show() {
        root!!.layout_shelf.visibility = CoordinatorLayout.VISIBLE
        root!!.progressBar.visibility = CoordinatorLayout.INVISIBLE
    }
}