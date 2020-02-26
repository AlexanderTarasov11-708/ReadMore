package com.itis.readmore.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.itis.readmore.R
import com.itis.readmore.fragments.FeedFragment
import com.itis.readmore.fragments.LibraryFragment
import com.itis.readmore.fragments.ProfileFragment
import com.itis.readmore.presenters.MainPresenter
import com.itis.readmore.views.MainView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : MvpAppCompatActivity(), MainView {

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    @ProvidePresenterTag(presenterClass = MainPresenter::class)
    fun provideMainPresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideMainPresenter() = MainPresenter()

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_profile -> {
                    title = "Профиль"
                    val profileFragment =
                        ProfileFragment.newInstance()
                    openFragment(profileFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_feed -> {
                    title = "Фид"
                    val feedFragment =
                        FeedFragment.newInstance()
                    openFragment(feedFragment)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_library -> {
                    title = "Библиотека"
                    val libraryFragment =
                        LibraryFragment.newInstance()
                    openFragment(libraryFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        title = "Профиль"
        val profileFragment =
            ProfileFragment.newInstance()
        openFragment(profileFragment)
        bottom_navigation.selectedItemId = R.id.action_profile

        bottom_navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    override fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}


