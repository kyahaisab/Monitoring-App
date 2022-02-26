package com.example.notesapp.graph

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.notesapp.R

class ClearDialogFragment : DialogFragment() {

    private var mListener: GraphDialogFragmentListener? = null

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        retainInstance = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (mListener == null)
            dismiss()

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("Sure to clear entire data?")
            .setIcon(R.drawable.ic_baseline_delete_sweep_24)
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, id: Int) {
                    mListener?.onSelectedYesNo(buttonClicked = YES)
                }
            })
            .setNegativeButton("No", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, id: Int) {
                    mListener?.onSelectedYesNo(buttonClicked = NO)
                }
            })
        return builder.create()
    }

    companion object {
        const val YES = "YES"
        const val NO = "NO"
        fun getInstance(listener: GraphDialogFragmentListener?): ClearDialogFragment {
            val fragment = ClearDialogFragment()
            fragment.mListener = listener
            return fragment
        }
    }

    interface GraphDialogFragmentListener {
        fun onSelectedYesNo(buttonClicked: String)
    }
}