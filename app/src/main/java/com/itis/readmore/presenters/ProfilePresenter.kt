package com.itis.readmore.presenters

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.itis.readmore.R
import com.itis.readmore.fragments.RatingFragment
import com.itis.readmore.views.ProfileView

@InjectViewState
class ProfilePresenter : MvpPresenter<ProfileView>() {
    fun rating () {
        val ratingFragment = RatingFragment()
        viewState.openFragment(ratingFragment)
    }
}