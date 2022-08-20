package com.mtc.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Window
import android.widget.DatePicker
import androidx.lifecycle.MutableLiveData
import com.mtc.R
import java.util.*


class DialogDate : Dialog {
    var _dateSelected = MutableLiveData<String>()

    constructor(context: Context, _dateSelected: MutableLiveData<String>) : super(context) {
        this._dateSelected = _dateSelected
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, themeResId: Int) : super(context, themeResId)

    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.date_picker_xml)
        val datePicker = findViewById<DatePicker>(R.id.date_Picker)
        val today = Calendar.getInstance()
        datePicker.maxDate = System.currentTimeMillis()
        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val _month = setMonth(month)
            val _day = setDay(day)
            val msg = "$year-$_month-$_day"
            _dateSelected.value = msg
            dismiss()
            //Toast.makeText(window!!.context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMonth(month: Int): String {
        val month = month + 1
        var _month = ""
        if (month.toString().length == 1) {
            _month = "0$month"
        } else {
            _month = month.toString()
        }
        return _month
    }

    private fun setDay(day: Int): String {
        var _day = ""
        if (day.toString().length == 1) {
            _day = "0$day"
        } else {
            _day = day.toString()
        }
        return _day
    }


}