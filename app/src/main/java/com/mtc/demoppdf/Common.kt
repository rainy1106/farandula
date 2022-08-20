package com.mtc.demoppdf

import android.content.Context
import android.os.Environment
import java.io.File

class Common {

    companion object {

        fun getPath(context: Context): String {

//            val file = File(
//                android.os.Environment.DIRECTORY_DOWNLOADS
//                        + File.separator
//                        + context.resources.getString(R.string.app_name) +
//                        File.separator
//            )
            val filename = "/Download"
            val file = File(Environment.getExternalStorageDirectory().path + filename)
            if (!file.exists()) file.mkdir()
            return file.path + File.separator
        }
    }
}