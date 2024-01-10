import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.wisebuyer.login.UserCredentials
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<Pair<HashMap<String,Any>, String>>()
    val loginResult: LiveData<Pair<HashMap<String,Any>, String>> get() = _loginResult

    private lateinit var auth: FirebaseAuth
    val db = FirebaseFirestore.getInstance()


    fun loginUser(credentials: UserCredentials) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(credentials.email, credentials.password)
            .addOnCompleteListener { task ->
                db.collection("Users").document(credentials.email)
                    .get()
                    .addOnSuccessListener { result ->
                        Log.w("APP", "${result.id} => ${result.data}")
                        _loginResult.value = Pair(result.data as HashMap<String, Any>, result.id)
                    }
                    .addOnFailureListener { exception ->
                        Log.w("APP", "Error getting documents.", exception)
                        _loginResult.value = Pair(hashMapOf<String, Any>(), "")
                    }
            }
    }

    fun clearLoginResult() {
        _loginResult.value = Pair(hashMapOf<String, Any>(), "")
    }

}
