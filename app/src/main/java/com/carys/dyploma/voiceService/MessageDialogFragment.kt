/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.carys.dyploma.voiceService

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment


/**
 * A simple dialog with a message.
 *
 *
 * The calling [android.app.Activity] needs to implement [ ].
 */
class MessageDialogFragment : AppCompatDialogFragment() {

    interface MDListener {
        /**
         * Called when the dialog is dismissed.
         */
        fun onMessageDialogDismissed()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
            .setMessage(arguments!!.getString(ARG_MESSAGE))
            .setPositiveButton(android.R.string.ok) { dialog, which -> (activity as MDListener).onMessageDialogDismissed() }
            .setOnDismissListener { (activity as MDListener).onMessageDialogDismissed() }
            .create()
    }

    companion object {

        private val ARG_MESSAGE = "message"

        /**
         * Creates a new instance of [MessageDialogFragment].
         *
         * @param message The message to be shown on the dialog.
         * @return A newly created dialog fragment.
         */
        fun newInstance(message: String): MessageDialogFragment {
            val fragment = MessageDialogFragment()
            val args = Bundle()
            args.putString(ARG_MESSAGE, message)
            fragment.arguments = args
            return fragment
        }
    }

}
