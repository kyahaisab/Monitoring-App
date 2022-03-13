package com.example.notesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notesapp.R
import com.example.notesapp.fragments.ClearDialogFragment.Companion.NO
import com.example.notesapp.fragments.ClearDialogFragment.Companion.YES
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_item_list_dialog_list_dialog.*

const val ARG_ITEM_COUNT = "item_count"

class ItemListDialogFragment : BottomSheetDialogFragment() {
    var fListener: ((String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list_dialog_list_dialog, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deleteYes.setOnClickListener {
            fListener?.invoke(YES)

        }
        deleteNo.setOnClickListener {
            fListener?.invoke(NO)
            super.dismiss()
        }
    }

    companion object {
        fun newInstance(itemCount: Int): ItemListDialogFragment =
            ItemListDialogFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_ITEM_COUNT, itemCount)
                }
            }

    }
}