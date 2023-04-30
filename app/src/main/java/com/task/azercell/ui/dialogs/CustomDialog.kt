package az.expressbank.e24.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import com.task.azercell.databinding.InfoDialogBinding

class CustomDialog(
    context: Context,
    private val title: String?,
    private val message: String?,
    private val dynamicMessage: (tv: TextView, dialog: Dialog) -> Unit? = { _, _ -> },
    private val drawableRes: Int? = null,
    @StringRes private val positiveButtonText: Int?,
    @StringRes private val negativeButtonText: Int?,
    private val onPositiveButtonClickListener: () -> Unit? = {},
    private val onNegativeButtonClickListener: () -> Unit? = {},
    private val onDismissListener: () -> Unit? = {}
) : Dialog(context) {
    private val binding by lazy { InfoDialogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(binding.root)

        window?.let {
            it.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            it.setBackgroundDrawable(InsetDrawable(ColorDrawable(Color.TRANSPARENT), 50))
        }

        title?.let { binding.title.text = it }
        message?.let { binding.message.text = it }
        dynamicMessage(binding.message, this)
        if (drawableRes != null) binding.icon.setImageResource(drawableRes)
        positiveButtonText?.let { binding.positiveButton.text = context.getString(it) }
        negativeButtonText?.let {
            binding.negativeButton.text = context.getString(it)
        }
        binding.positiveButton.setOnClickListener {
            onPositiveButtonClickListener()
            dismiss()
        }
        binding.negativeButton.setOnClickListener {
            onNegativeButtonClickListener()
            dismiss()
        }
        setOnCancelListener {
            onDismissListener()
        }

    }
}