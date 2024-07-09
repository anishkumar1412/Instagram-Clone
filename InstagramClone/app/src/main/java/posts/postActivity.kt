package posts

import POST
import POST_FOLDER
import USER_NODE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.instagramclone.HomeActivity
import com.example.instagramclone.R
import com.example.instagramclone.databinding.ActivityPostBinding
import com.example.instagramclone.modals.Post
import com.example.instagramclone.modals.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import util.uploadImage

class postActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    private var imageUrl: String? = null
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImage(uri, POST_FOLDER) { url ->
                if (url != null) {
                    binding.selectImage.setImageURI(uri)
                    imageUrl = url
                } else {
                    Log.e("PostActivity", "Error uploading image")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            startActivity(Intent(this@postActivity, HomeActivity::class.java))
        }
        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }
        binding.postbtn.setOnClickListener {
            postContent()
        }
        binding.cancelbtn.setOnClickListener {
            startActivity(Intent(this@postActivity, HomeActivity::class.java))
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun postContent() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Log.e("PostActivity", "User not logged in")
            return
        }

        if (imageUrl == null) {
            Log.e("PostActivity", "Image URL is null")
            return
        }

        val caption = binding.caption.editText?.text.toString()
        if (caption.isEmpty()) {
            Log.e("PostActivity", "Caption is empty")
            return
        }

        Firebase.firestore.collection(USER_NODE).document(currentUser.uid).get()
            .addOnSuccessListener { document ->
                val user = document.toObject<User>()
                if (user == null) {
                    Log.e("PostActivity", "User object is null")
                    return@addOnSuccessListener
                }

                val post = Post(
                    postUrl = imageUrl!!,
                    caption = caption,
                    uid = currentUser.uid,
                    time = System.currentTimeMillis().toString(),
                    likes = 0 // Initialize like count to 0
                )

                Firebase.firestore.collection(POST).add(post)
                    .addOnSuccessListener {
                        Log.d("PostActivity", "Post added successfully")
                        startActivity(Intent(applicationContext, HomeActivity::class.java))
                    }
                    .addOnFailureListener { e ->
                        Log.e("PostActivity", "Error adding post to POST collection", e)
                    }

                Firebase.firestore.collection(currentUser.uid).add(post)
                    .addOnSuccessListener {
                        Log.d("PostActivity", "Post added to user collection successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("PostActivity", "Error adding post to user collection", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("PostActivity", "Error fetching user document", e)
            }
    }
}
