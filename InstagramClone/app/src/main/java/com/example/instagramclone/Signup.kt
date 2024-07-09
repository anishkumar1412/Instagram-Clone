package com.example.instagramclone


import USER_PROFILE_FOLDER
import USER_NODE
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.instagramclone.databinding.ActivitySignupBinding
import com.example.instagramclone.modals.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.squareup.picasso.Picasso
import util.uploadImage

class Signup : AppCompatActivity() {
    val binding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    lateinit var user: com.example.instagramclone.modals.User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, USER_PROFILE_FOLDER) { imageUrl ->
                if (imageUrl == null) {
                    // Handle error
                } else {
                    user.image = imageUrl
                    binding.profileImageIcon.setImageURI(uri)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val text =
            "<font color=#6C6C6C>Already have an Account</font> <font color=#1E88E5>login</font>"
        binding.logintext.text = Html.fromHtml(text);
        user = User()
        if (intent.hasExtra("MODE")) {
            if (intent.getIntExtra("MODE", -1) == 1) {
                binding.signupbtn.text = "Update Profile"
                Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
                    .get()
                    .addOnSuccessListener {
                        user = it.toObject<User>()!!

                        if (!user.image.isNullOrEmpty()) {
                            Picasso.get().load(user.image).into(binding.profileImageIcon);
                        }
                        binding.name.editText?.setText(user.name)
                        binding.email.editText?.setText(user.email)
                        binding.password.editText?.setText(user.password)

//                        binding.password.editText?.visibility = View.GONE
                        binding.logintext.visibility = View.GONE
                 }
            }
        }
        binding.signupbtn.setOnClickListener {
            if (intent.hasExtra("MODE")) {
                if (intent.getIntExtra("MODE", -1) == 1) {
                    FirebaseFirestore.getInstance().collection(USER_NODE)
                        .document(FirebaseAuth.getInstance().currentUser!!.uid).set(user)
                        .addOnSuccessListener {
                            startActivity(Intent(this@Signup, HomeActivity::class.java))

                            finish()
                        }
                }
            }
                 else {


                    if (binding.name.editText?.text.toString().isEmpty() ||
                        binding.email.editText?.text.toString().isEmpty() ||
                        binding.password.editText?.text.toString().isEmpty()
                    ) {
                        Toast.makeText(
                            this@Signup,
                            "Please fill all the information to proceed",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            binding.email.editText?.text.toString(),
                            binding.password.editText?.text.toString()
                        ).addOnCompleteListener { result ->
                            if (result.isSuccessful) {
                                user.name = binding.name.editText?.text.toString()
                                user.email = binding.email.editText?.text.toString()
                                user.password = binding.password.editText?.text.toString()

                                FirebaseFirestore.getInstance().collection(USER_NODE)
                                    .document(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .set(user)
                                    .addOnSuccessListener {
                                        startActivity(Intent(this@Signup, HomeActivity::class.java))
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            this@Signup,
                                            e.localizedMessage,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                Toast.makeText(
                                    this@Signup,
                                    result.exception?.localizedMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }


            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            binding.plus.setOnClickListener {
                launcher.launch("image/*")
            }
            binding.logintext.setOnClickListener {
                startActivity(Intent(this@Signup, LoginActivity::class.java))
            }
        }
    }

