package com.example.notesapp.graph

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.notesapp.R

class GraphDialogFragment : DialogFragment() {

    private var mListener: GraphDialogFragmentListener? = null

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        retainInstance = true
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (mListener == null)
            dismiss()

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("Sure to open Graph?")
            .setIcon(R.drawable.ic_baseline_graphic_eq_24)
            .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, id: Int) {
                    mListener?.onSelectedYesNo(buttonClicked = true)
                }
            })
            .setNegativeButton("No", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, id: Int) {}
            })
        return builder.create()
    }

    companion object {
        fun getInstance(listener: GraphDialogFragmentListener?): GraphDialogFragment {
            val fragment = GraphDialogFragment()
            fragment.mListener = listener
            return fragment
        }
    }

    interface GraphDialogFragmentListener {
        fun onSelectedYesNo(buttonClicked: Boolean)
    }
}