package com.mossco.todoapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.mossco.todoapp.database.TodoItem
import com.mossco.todoapp.databinding.AddToDoDialogBinding

class AddToDoDialogFragment : DialogFragment() {

    private lateinit var listener: TodoItemDialogListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            val builder = AlertDialog.Builder(it)
            val binding: AddToDoDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.add_to_do_dialog, null, false
            )

            val dialog = dialog as AlertDialog?

            dialog?.getButton(AlertDialog.BUTTON_POSITIVE)?.isEnabled =
                false //isTextEntered(binding)

            builder.setView(binding.root)
                .setPositiveButton(
                    R.string.add_to_do_item
                ) { _, _ ->
                    if (!binding.todoItemTitleEditTex.text.isNullOrBlank() && !binding.descriptionEditTex.text.isNullOrBlank()) {
                        val item = TodoItem(
                            binding.todoItemTitleEditTex.text.toString(),
                            binding.descriptionEditTex.text.toString(),
                            false
                        )
                        listener.onDialogPositiveClick(this, item)
                    } else {
                        listener.onNothingAdded()
                    }

                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    listener.onDialogNegativeClick(this)
                }
                .setTitle(getString(R.string.add_to_do_item))
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun isTextEntered(binding: AddToDoDialogBinding) =
        !binding.todoItemTitleEditTex.text.toString()
            .isNullOrBlank() && !binding.descriptionEditTex.text.toString()
            .isNullOrBlank()

    interface TodoItemDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, item: TodoItem)
        fun onDialogNegativeClick(dialog: DialogFragment)
        fun onNothingAdded()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as TodoItemDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }
}