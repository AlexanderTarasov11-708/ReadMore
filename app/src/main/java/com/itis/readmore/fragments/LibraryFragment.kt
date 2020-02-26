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
import com.itis.readmore.presenters.LibraryPresenter
import com.itis.readmore.views.LibraryView

class LibraryFragment : MvpAppCompatFragment(), LibraryView {

    @InjectPresenter
    lateinit var awardsPresenter: LibraryPresenter

    @ProvidePresenterTag(presenterClass = LibraryPresenter::class)
    fun provideAwardsPresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideAwardsPresenter() = LibraryPresenter()

    companion object {
        fun newInstance(): LibraryFragment =
            LibraryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_library, null)
    }

    override fun hide() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun show() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}