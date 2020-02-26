package com.itis.readmore.views

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpView

interface MainView : MvpView {
    fun openFragment(fragment: Fragment)
}