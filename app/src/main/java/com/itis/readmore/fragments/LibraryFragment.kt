package com.itis.readmore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.itis.readmore.R
import com.itis.readmore.models.RecyclerAdapter
import com.itis.readmore.presenters.LibraryPresenter
import com.itis.readmore.views.LibraryView
import kotlinx.android.synthetic.main.fragment_library.view.*

class LibraryFragment : MvpAppCompatFragment(), LibraryView {

    @InjectPresenter
    lateinit var libraryPresenter: LibraryPresenter

    @ProvidePresenterTag(presenterClass = LibraryPresenter::class)
    fun provideLibraryPresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideLibraryPresenter() = LibraryPresenter()

    private var root: View? = null
    private lateinit var adapter: RecyclerAdapter

    companion object {
        fun newInstance(): LibraryFragment =
            LibraryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root =  inflater.inflate(R.layout.fragment_library, null)

        libraryPresenter.getMyLibrary()

        return root
    }


    override fun initRecycler() {
        adapter = RecyclerAdapter()
        adapter.library = libraryPresenter.listLibraries
        root!!.recyclerView.layoutManager = LinearLayoutManager(context)
        root!!.recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            context,
            LinearLayoutManager(context).orientation
        )
        root!!.recyclerView.addItemDecoration(dividerItemDecoration)
    }

    override fun hide() {
        root!!.recyclerView.visibility = CoordinatorLayout.INVISIBLE
        root!!.progressBar.visibility = CoordinatorLayout.VISIBLE
    }

    override fun show() {
        root!!.recyclerView.visibility = CoordinatorLayout.VISIBLE
        root!!.progressBar.visibility = CoordinatorLayout.INVISIBLE
    }
}