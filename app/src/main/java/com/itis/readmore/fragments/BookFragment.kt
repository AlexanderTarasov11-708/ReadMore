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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.itis.readmore.R
import com.itis.readmore.models.MyBook
import com.itis.readmore.models.User
import com.itis.readmore.presenters.BookPresenter
import com.itis.readmore.views.BookView
import kotlinx.android.synthetic.main.fragment_book.view.*

class BookFragment : MvpAppCompatFragment(), BookView {

    @InjectPresenter
    lateinit var bookPresenter: BookPresenter

    @ProvidePresenterTag(presenterClass = BookPresenter::class)
    fun provideBookPresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideBookPresenter() = BookPresenter()

    val fuser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private var root: View? = null
    val list = ArrayList<String>()
    lateinit var book: MyBook
    lateinit var user: User


    companion object {
        fun newInstance(): BookFragment =
            BookFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_book, null)

        hide()

        val bundle = arguments

        book = bundle!!.getSerializable("book") as MyBook
        user = bundle!!.getSerializable("user") as User

        context?.let {
            Glide.with(it).load(book.imageURL).into(root!!.book_cover)
        }

        root!!.book_title.text = book.title
        root!!.book_desc.text = book.description
        root!!.book_author.text = book.author
        root!!.book_year.text = book.year

        show()

        return root
    }

    override fun openFragment(fragment: Fragment) {
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun hide() {
        root!!.layout_book.visibility = CoordinatorLayout.INVISIBLE
        root!!.progress_bar.visibility = CoordinatorLayout.VISIBLE
    }

    override fun show() {
        root!!.layout_book.visibility = CoordinatorLayout.VISIBLE
        root!!.progress_bar.visibility = CoordinatorLayout.INVISIBLE
    }
}