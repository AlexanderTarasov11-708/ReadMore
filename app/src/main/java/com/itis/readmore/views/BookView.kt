package com.itis.readmore.views

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpView

interface BookView : MvpView {
    fun hide()
    fun show()
    fun openFragment(fragment: Fragment)
}