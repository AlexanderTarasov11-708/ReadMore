package com.itis.readmore.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.itis.readmore.R
import com.itis.readmore.models.User
import com.itis.readmore.presenters.EditProfilePresenter
import com.itis.readmore.views.EditProfileView
import kotlinx.android.synthetic.main.fragment_edit_profile.view.*

class EditProfileFragment : MvpAppCompatFragment(), EditProfileView {

    @InjectPresenter
    lateinit var editProfilePresenter: EditProfilePresenter

    @ProvidePresenterTag(presenterClass = EditProfilePresenter::class)
    fun provideEditProfilePresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideEditProfilePresenter() = EditProfilePresenter()

    val fuser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val dbreference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Users").child(fuser!!.uid)
    lateinit var user: User
    private var root: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_edit_profile, null)

        hide()
        dbreference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                user = dataSnapshot.getValue<User>(User::class.java)!!
                root!!.et_username.setText(user.login)
                root!!.et_name.setText(user.name)
                root!!.et_birthdate.setText(user.age)

                root!!.et_city.setText(user.city)
                root!!.et_about.setText(user.about)
                show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                context?.let {
                    Toast.makeText(
                        it, "Failed to read value.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })


        root!!.confirm.setOnClickListener {
            if (!TextUtils.isEmpty(root!!.et_username.text.toString())
                && !TextUtils.isEmpty(root!!.et_name.text.toString())
                && !TextUtils.isEmpty(root!!.et_birthdate.text.toString())
                && !TextUtils.isEmpty(root!!.et_city.text.toString())
                && !TextUtils.isEmpty(root!!.et_about.text.toString())
            ) {
                val query: Query =
                    FirebaseDatabase.getInstance().reference.child("Users").orderByChild("username")
                        .equalTo(root!!.et_username.text.toString())
                query.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.childrenCount > 0 && !dataSnapshot.hasChild(fuser!!.uid)) {
                            context?.let {
                                Toast.makeText(
                                    it, "Имя пользователя занято.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            register()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        context?.let {
                            Toast.makeText(
                                it, "Reading data failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
            }
        }
        return root
    }

    private fun register() {
        hide()
        val hashMap: HashMap<String, String> = HashMap()
        hashMap["id"] = user.id!!
        hashMap["login"] = root!!.et_username.text.toString()
        hashMap["name"] = root!!.et_name.text.toString()
        hashMap["age"] = root!!.et_birthdate.text.toString()
        hashMap["rating"] = user.rating!!
        hashMap["city"] = root!!.et_city.text.toString()
        hashMap["about"] = root!!.et_about.text.toString()
        hashMap["email"] = user.email!!
        hashMap["imageURL"] = user.imageURL!!


        dbreference.setValue(hashMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                changeFragment(ProfileFragment())
                show()
            }
        }
    }

    override fun changeFragment(fragment: Fragment) {
        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun hide() {
        root!!.linearLayout.visibility = CoordinatorLayout.INVISIBLE
        root!!.progressBar.visibility = CoordinatorLayout.VISIBLE
    }

    override fun show() {
        root!!.linearLayout.visibility = CoordinatorLayout.VISIBLE
        root!!.progressBar.visibility = CoordinatorLayout.INVISIBLE
    }
}