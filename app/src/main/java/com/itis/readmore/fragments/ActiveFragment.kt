package com.itis.readmore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.itis.readmore.R
import com.itis.readmore.models.MyBook
import com.itis.readmore.models.RecyclerAdapter
import com.itis.readmore.presenters.ActivePresenter
import com.itis.readmore.views.ActiveView
import kotlinx.android.synthetic.main.fragment_active.view.*

class ActiveFragment : MvpAppCompatFragment(), ActiveView {

    @InjectPresenter
    lateinit var activePresenter: ActivePresenter

    @ProvidePresenterTag(presenterClass = ActivePresenter::class)
    fun provideActivePresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideActivePresenter() = ActivePresenter()

    private var root: View? = null
    private lateinit var adapter: RecyclerAdapter


    companion object {
        fun newInstance(): ActiveFragment =
            ActiveFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_active, null)

        activePresenter.getAllBooks()

        return root
    }

    override fun initializeRecycler(listEvents: ArrayList<MyBook>) {
        adapter = RecyclerAdapter()
        adapter.books = listEvents
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