package com.zurichat.app.utils.views.list_item

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.zurichat.app.R
import com.zurichat.app.ui.base.BaseItem
import com.zurichat.app.utils.asFabButton
import com.zurichat.app.utils.dp
import com.zurichat.app.utils.views.list_item.binding.DoubleViewBinding

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 07-Dec-21 at 6:22 AM
 *
 */
class TextAndButtonItem (
    private val text: String,
    private inline val buttonExtras: ((Button) -> Unit)? = null
) : BaseItem<String, DoubleViewBinding>(text, BaseItemUtil.itemId<TextAndButtonItem>(), text){

    override fun inflate(parent: ViewGroup): DoubleViewBinding {
        val textView = MaterialTextView(parent.context).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, 1f)
        }
        val button = MaterialButton(parent.context).apply{
            asFabButton(ContextCompat.getDrawable(context, R.drawable.ic_add), 20.dp(resources))
            updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginStart = 8.dp(resources)
            }
        }
        val root = LinearLayout(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            orientation = LinearLayout.HORIZONTAL
            addView(textView)
            addView(button)
        }
        return  DoubleViewBinding(root, textView, button)
    }

    override fun bind(binding: DoubleViewBinding) : Unit = with(binding){
        (view1 as TextView).text = this@TextAndButtonItem.text
        buttonExtras?.invoke((view2 as Button))
    }
}