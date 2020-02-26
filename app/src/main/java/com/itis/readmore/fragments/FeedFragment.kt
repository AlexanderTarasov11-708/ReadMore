package com.itis.readmore.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.itis.readmore.R
import com.itis.readmore.presenters.FeedPresenter
import com.itis.readmore.views.FeedView

class FeedFragment : MvpAppCompatFragment(), FeedView {
    @InjectPresenter
    lateinit var teamsPresenter: FeedPresenter

    @ProvidePresenterTag(presenterClass = FeedPresenter::class)
    fun provideTeamsPresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideTeamsPresenter() = FeedPresenter()

    companion object {
        fun newInstance(): FeedFragment =
            FeedFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, null)
    }
}