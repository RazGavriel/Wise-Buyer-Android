import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.wisebuyer.profile.UserMetaData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileViewModel : ViewModel() {
    private val _showProfilePhoto = MutableLiveData<Uri?>()
    private val _uploadProfileImageResult = MutableLiveData<Int>()
    val showProfilePhoto: LiveData<Uri?> get() = _showProfilePhoto
    val uploadProfileImageResult: LiveData<Int> get() = _uploadProfileImageResult

    private val storage = FirebaseStorage.getInstance()

    fun getProfileImage(userMetaData: UserMetaData) {
        val gsReference = storage.reference.child(userMetaData.profilePicture)
        gsReference.downloadUrl
            .addOnSuccessListener { uri ->
                _showProfilePhoto.value = uri
            }
            .addOnCompleteListener {
                _showProfilePhoto.value = null
            }
    }

    fun uploadProfileImage(userMetaData: UserMetaData, imageUri: Uri) {
        val imageRef: StorageReference = storage.getReference(userMetaData.profilePicture)

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                getProfileImage(userMetaData)
            }
            .addOnFailureListener {
                _uploadProfileImageResult.value = -1
            }
            .addOnProgressListener {
                _uploadProfileImageResult.value = 0
            }
            .addOnCompleteListener {
                _uploadProfileImageResult.value = 1
            }
    }
}

