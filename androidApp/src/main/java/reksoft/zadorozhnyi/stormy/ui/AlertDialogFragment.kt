package reksoft.zadorozhnyi.stormy.ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import reksoft.zadorozhnyi.stormy.R

class AlertDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context: Context? = activity
        val builder = AlertDialog.Builder(context)
            .setTitle(R.string.error_title)
            .setMessage(R.string.error_message)
            .setPositiveButton(R.string.error_ok_button_text, null)
        return builder.create()
    }
}