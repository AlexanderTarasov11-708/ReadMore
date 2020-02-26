package com.itis.readmore.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.intmainreturn00.grapi.grapi
import com.itis.readmore.R
import com.itis.readmore.ScopedAppActivity
import com.itis.readmore.presenters.LoginPresenter
import com.itis.readmore.views.LoginView
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import org.jetbrains.anko.browse

class LoginActivity : ScopedAppActivity(),
    View.OnClickListener,
    LoginView {

    @InjectPresenter
    lateinit var loginPresenter: LoginPresenter

    @ProvidePresenterTag(presenterClass = LoginPresenter::class)
    fun provideLoginPresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideLoginPresenter() = LoginPresenter()

    private lateinit var auth: FirebaseAuth
    val dbreference = FirebaseDatabase.getInstance().getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sync_gr.setOnClickListener(this)
        btn_login.setOnClickListener(this)
        auth = FirebaseAuth.getInstance()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        else {
            launch {
                grapi.loginEnd(intent) { ok ->
                    if (ok) {
                        // if sync with GoodReads is ok, sign up
                        signUp()
                    } else
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
        }
    }

    private fun signIn() {
        if (!TextUtils.isEmpty(etEmail.text.toString()) && !TextUtils.isEmpty(etPassword.text.toString()))
            auth.signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        else
            Toast.makeText(
                baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun syncGR() {
        if (!grapi.isLoggedIn()) {
            launch {
                grapi.loginStart()
                browse(grapi.getAuthorizationUrl())
            }


        }

        val user = auth.currentUser
        updateUI(user)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> signIn()
            R.id.sync_gr -> syncGR()
        }
    }

    private fun signUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private val TAG = "SignInActivity"
        private val RC_SIGN_IN = 9001
    }
}