<<<<<<< HEAD:app/src/main/java/com/tolstoy/zurichat/ui/MainActivity.kt
package com.tolstoy.zurichat.ui
=======
package com.tolstoy.zurichat.ui.activities
>>>>>>> 0b21b703117800eadc3d318923d586d431ef1e1f:app/src/main/java/com/tolstoy/zurichat/ui/activities/MainActivity.kt

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.tolstoy.zurichat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
        crossinline bindingInflater: (LayoutInflater) -> T
    ) =
        lazy(LazyThreadSafetyMode.NONE) {
            bindingInflater.invoke(layoutInflater)}
}