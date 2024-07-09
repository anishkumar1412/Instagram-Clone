package posts

import REEL
import REEL_FOLDER
import USER_NODE
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.instagramclone.HomeActivity
import com.example.instagramclone.MainActivity
import com.example.instagramclone.R
import com.example.instagramclone.databinding.ActivityReelsBinding
import com.example.instagramclone.modals.Reel
import com.example.instagramclone.modals.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import util.uploadVideo

class ReelsActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityReelsBinding.inflate(layoutInflater)
    }
    private lateinit var videoUrl: String
    lateinit var progressDialog: ProgressDialog
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadVideo(uri, REEL_FOLDER, this, progressDialog) {
                if (it != null) {
                    // Handle error
                    videoUrl = it
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.selectReel.setOnClickListener {
            launcher.launch("video/*")
        }
        binding.postReelsBtn.setOnClickListener {
            FirebaseFirestore.getInstance().collection(USER_NODE)
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .get().addOnSuccessListener {
                    val user: User? = it.toObject<User>()!!
                    val reel = Reel(videoUrl, binding.captionReel.editText?.text.toString(),
                        user?.image!!)

                    FirebaseFirestore.getInstance().collection(REEL).document().set(reel).addOnSuccessListener {
                        startActivity(Intent(this@ReelsActivity, HomeActivity::class.java))
                        finish()
                    }
                }
        }
        binding.cancelbtn.setOnClickListener {
            startActivity(Intent(this@ReelsActivity, HomeActivity::class.java))
        }
    }
}
