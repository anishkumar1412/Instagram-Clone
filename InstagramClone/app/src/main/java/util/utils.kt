package util

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


//fun uploadImage(uri: Uri,folderName:String) :String{// In this app getting crashed due to the null return
fun uploadImage(uri: Uri, folderName: String, callback: (String?) -> Unit) {

    var imageUrl: String? =
        null //folderName is the name where the image gets uploaded in the firebase
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                callback(imageUrl)
            }
        }
}

fun uploadVideo(uri: Uri, folderName: String, context: Context, progressDialog: ProgressDialog,callback: (String?) -> Unit) {
    ProgressDialog(context).setTitle("Uploading. . . . .")
    progressDialog.show()
    var imageUrl: String? =
        null //folderName is the name where the image gets uploaded in the firebase
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                progressDialog.dismiss()
                callback(imageUrl)
            }
        }.addOnProgressListener {
            val uploadedValue :Long = (it.bytesTransferred/it.totalByteCount)*100
            progressDialog.setMessage("Uploaded $uploadedValue %")
        }
}
