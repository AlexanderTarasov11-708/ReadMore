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
import com.itis.readmore.models.User
import com.itis.readmore.presenters.RatingPresenter
import com.itis.readmore.views.RatingView
import kotlinx.android.synthetic.main.fragment_rating.view.*

class RatingFragment : MvpAppCompatFragment(), RatingView {

    @InjectPresenter
    lateinit var ratingPresenter: RatingPresenter

    @ProvidePresenterTag(presenterClass = RatingPresenter::class)
    fun provideRatingPresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideRatingPresenter() = RatingPresenter()

    private var root: View? = null


    companion object {
        fun newInstance(): RatingFragment =
            RatingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_rating, null)

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
        root!!.layout_rating.visibility = CoordinatorLayout.INVISIBLE
        root!!.progress_bar.visibility = CoordinatorLayout.VISIBLE
    }

    override fun show() {
        root!!.layout_rating.visibility = CoordinatorLayout.VISIBLE
        root!!.progress_bar.visibility = CoordinatorLayout.INVISIBLE
    }
}