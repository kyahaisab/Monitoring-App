package com.example.notesapp.fragments

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

        @JvmStatic
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
/*SuppressLint(setTextI18n) explain:

fun(id: String){
textView1.text="Error due to: $id"
}
above will show lint, so put SuppressLint(setTextI18n) just above fun(id: String)
 */




/*  @JvmStatic : its usage is given

TestKotlin.kt

class TestKotlin {
    companion object {
        val someString = "hello world"
    }
}
Which is then called by Java, like this:

TestJava.java

public class TestJava {
    String kotlinStaticString = TestKotlin.Companion.getSomeString();
}
but then, there's this option 2:

TestKotlin.kt v2

class TestKotlin {
    companion object {
        @JvmStatic  // <-- notice the @JvmStatic annotation
        val someString = "hello world"
    }
}
And then, call it from Java, like this:

TestJava.java v2

public class TestJava {
    String kotlinStaticString = TestKotlin.getSomeString();
}

 */