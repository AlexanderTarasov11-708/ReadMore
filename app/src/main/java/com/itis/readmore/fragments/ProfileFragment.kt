package com.itis.readmore.fragments

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.arellomobile.mvp.presenter.ProvidePresenterTag
import com.bumptech.glide.Glide
import com.itis.readmore.models.User
import com.itis.readmore.presenters.ProfilePresenter
import com.itis.readmore.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.itis.readmore.views.ProfileView
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.*

class ProfileFragment : MvpAppCompatFragment(), ProfileView {

    @InjectPresenter
    lateinit var profilePresenter: ProfilePresenter

    @ProvidePresenterTag(presenterClass = ProfilePresenter::class)
    fun provideProfilePresenterTag(): String = "Hello"

    @ProvidePresenter
    fun provideProfilePresenter() = ProfilePresenter()

    val fuser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val dbreference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Users").child(fuser!!.uid)
    val storageReference: StorageReference = FirebaseStorage.getInstance().getReference("uploads")
    val IMAGE_REQUEST = 1
    lateinit var imageUri: Uri
    lateinit var uploadTask: UploadTask
    var isInitialized: Boolean = false
    private var root: View? = null

    companion object {
        fun newInstance(): ProfileFragment =
            ProfileFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_profile, null)

        hide()

        dbreference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                root!!.user_login.text = user?.login
                if (!isEmpty(user?.name))
                    root!!.user_name.text = user?.name
                if (!isEmpty(user?.age))
                    root!!.user_birthdate.text = user?.age
                if (!isEmpty(user?.city))
                    root!!.user_city.text = user?.city
                if (!isEmpty(user?.about))
                    root!!.user_about.text = user?.about
                if (!isEmpty(user?.rating))
                    root!!.user_rating.text = user?.rating
                if (!isEmpty(user?.email))
                    root!!.user_email.text = user?.email
                if (user!!.imageURL == "default") {
                    root!!.user_image.setImageResource(R.drawable.ic_profile)
                } else {
                    context?.let {
                        Glide.with(it).load(user.imageURL).into(root!!.user_image)
                    }
                }
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

        root!!.edit.setOnClickListener {
            val eProfileFragment = EditProfileFragment()
            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, eProfileFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

        root!!.user_image.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, IMAGE_REQUEST)
        }

        return root
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = context!!.contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadImage() {
        val pd = ProgressDialog(context)
        pd.setMessage("Загрузка")
        pd.show()

        if (imageUri != null) {
            val fileReference =
                storageReference.child(
                    System.currentTimeMillis().toString() + "." + getFileExtension(
                        imageUri
                    )
                )
            uploadTask = fileReference.putFile(imageUri)
            isInitialized = true
            uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                @Throws(Exception::class)
                override fun then(@NonNull task: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                    if (!task.isSuccessful) {
                        throw task.exception!!
                    }
                    return fileReference.downloadUrl
                }
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val mUri = downloadUri.toString()

                    val map = HashMap<String, Any>()
                    map.put("imageURL", mUri)
                    dbreference.updateChildren(map)

                    pd.dismiss()
                } else {
                    context?.let {
                        Toast.makeText(
                            it, "Не удалось!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    pd.dismiss()
                }
            }.addOnFailureListener { e ->
                context?.let {
                    Toast.makeText(
                        it, e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    pd.dismiss()
                }
            }
        } else {
            context?.let {
                Toast.makeText(
                    it, "Изображение не выбрано",
                    Toast.LENGTH_SHORT
                ).show()
                pd.dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!

            if (isInitialized) {
                if (uploadTask.isInProgress) {
                    context?.let {
                        Toast.makeText(
                            it, "Загрузка в процессе",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                uploadImage()
            }
        }
    }

    override fun hide() {
        root!!.linearLayout2.visibility = CoordinatorLayout.GONE
        root!!.progressBar.visibility = CoordinatorLayout.VISIBLE
    }

    override fun show() {
        root!!.linearLayout2.visibility = CoordinatorLayout.VISIBLE
        root!!.progressBar.visibility = CoordinatorLayout.INVISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_logout, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // Handle item selection
//        return when (item.itemId) {
//            R.id.frag1_item -> {
//                FirebaseAuth.getInstance().signOut()
//                context?.let {
//                    val intent = Intent(it, GoogleSignInActivity::class.java)
//                    startActivity(intent)
//                }
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}
