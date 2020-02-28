package com.itis.readmore.views

import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpView

interface RatingView : MvpView {
    fun hide()
    fun show()
    fun openFragment(fragment: Fragment)
}