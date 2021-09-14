package com.tolstoy.zurichat.ui.dm

import androidx.fragment.app.Fragment
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.dm.adapters.MessageAdapter
import com.tolstoy.zurichat.util.setUpApplicationTheme
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult
import java.util.*

class DMFragment : Fragment(R.layout.fragment_dm) {
    lateinit var imagePicker: ImagePicker
    private val adapter by lazy { MessageAdapter(requireContext(), 0) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //initialize imagePicker library
        imagePicker = ImagePicker(this)


        //Launch Attachment popup
        val attachment = requireActivity().findViewById<ImageView>(R.id.imageView_attachment)
        val popupView: View = layoutInflater.inflate(R.layout.attachment_popup, null)
        var popupWindow = PopupWindow(
            popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setBackgroundDrawable(ColorDrawable())
        popupWindow.isOutsideTouchable = true

        attachment.setOnClickListener {
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 600)
        }

    }

    /**function to take image using camera
    we are using a library to easen the work
     */
    private fun takePictureCamera() {
        //take image
        imagePicker.takeFromCamera { imageResult ->
            /**
             * when we take an image successfully,we take the uri and load using glide
             * we also set the view to visible
             */

            when (imageResult) {
                is ImageResult.Success -> {
//                    val uri = imageResult.value
//
//                    imageView.visibility = View.VISIBLE
//                    Glide.with(this)
//                        .load(uri)
//                        .into(imageView)

                }

                /**
                 * incase it's unsuccessful we toast the message and hide the image view
                 */
                is ImageResult.Failure -> {
//                    imageView.visibility = View.GONE
                    Toast.makeText(requireContext(), "Picture not taken", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
