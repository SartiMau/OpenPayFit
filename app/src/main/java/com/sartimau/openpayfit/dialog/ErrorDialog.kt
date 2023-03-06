package com.sartimau.openpayfit.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.sartimau.openpayfit.R
import com.sartimau.openpayfit.databinding.DialogErrorBinding

interface ErrorDialogListener {
    operator fun invoke()
}

class ErrorDialog : DialogFragment() {

    private var onClickListener: ErrorDialogListener? = null
    private lateinit var binding: DialogErrorBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogErrorBinding.inflate(layoutInflater)

        context?.let {
            AlertDialog.Builder(it)
                .setView(binding.root)
                .setCancelable(false)
                .create()
        }

        return binding.root
    }

    // Added this just to avoid a visual defect
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireActivity())
        dialog.setContentView(R.layout.dialog_error)

        val window: Window? = dialog.window
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        onClickListener = context as? ErrorDialogListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getInt(TITLE_TEXT_KEY)?.let { title -> binding.titleText.setText(title) }
        arguments?.getInt(MESSAGE_TEXT_KEY)?.let { message -> binding.messageText.setText(message) }
        arguments?.getInt(BUTTON_TEXT_KEY)?.let { button -> binding.button.setText(button) }
        binding.button.setOnClickListener {
            dismiss()
            onClickListener?.invoke()
        }
    }

    companion object {
        const val TAG = "ERROR_DIALOG_TAG"

        private const val TITLE_TEXT_KEY = "TITLE_TEXT_KEY"
        private const val MESSAGE_TEXT_KEY = "MESSAGE_TEXT_KEY"
        private const val BUTTON_TEXT_KEY = "BUTTON_TEXT_KEY"

        fun newInstance(
            titleText: Int = R.string.error_dialog_default_title,
            messageText: Int = R.string.error_dialog_default_message,
            buttonText: Int = R.string.close
        ): ErrorDialog {
            val fragment = ErrorDialog()
            val args = Bundle().apply {
                putInt(TITLE_TEXT_KEY, titleText)
                putInt(MESSAGE_TEXT_KEY, messageText)
                putInt(BUTTON_TEXT_KEY, buttonText)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
