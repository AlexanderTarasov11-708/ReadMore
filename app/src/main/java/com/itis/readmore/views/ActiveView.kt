package com.itis.readmore.views

import com.arellomobile.mvp.MvpView
import com.itis.readmore.models.MyBook

interface ActiveView : MvpView {
    fun hide()
    fun show()
    fun initializeRecycler(listEvents: ArrayList<MyBook>)
}