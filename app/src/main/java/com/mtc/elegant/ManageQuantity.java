package com.mtc.elegant;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.mtc.R;

public class ManageQuantity extends LinearLayout {
    public Button addBtn, subtractBtn;
    private TextView number_counter;
    private final int initialNumber = 1;
    private int lastNumber=1;
    private int currentNumber;
    private final int finalNumber = 100;
    private OnClickListener mListener;
    private OnValueChangeListener mOnValueChangeListener;

    public ManageQuantity(Context context) {
        super(context);
        initView(context);
    }

    public ManageQuantity(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public ManageQuantity(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    void initView(Context context) {
        inflate(context, R.layout.layout, this);
        subtractBtn = findViewById(R.id.subtract_btn);
        addBtn = findViewById(R.id.add_btn);
        number_counter = findViewById(R.id.number_counter);
        clickListener();
    }

    void clickListener() {
        subtractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                int num = Integer.parseInt(number_counter.getText().toString());
                setNumber(String.valueOf(num - 1), true);
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                int num = Integer.parseInt(number_counter.getText().toString());
                setNumber(String.valueOf(num + 1), true);
            }
        });
    }

    public void setNumber(String number) {
        lastNumber = currentNumber;
        this.currentNumber = Integer.parseInt(number);
        if (this.currentNumber > finalNumber) {
            this.currentNumber = finalNumber;
        }
        if (this.currentNumber < initialNumber) {
            this.currentNumber = initialNumber;
        }
        number_counter.setText(String.valueOf(currentNumber));
    }

    public interface OnValueChangeListener {
        void onValueChange(ManageQuantity view, int oldValue, int newValue);
    }

    private void callListener(View view) {
        if (mListener != null) {
            mListener.onClick(view);
        }

        if (mOnValueChangeListener != null) {
            if (lastNumber != currentNumber) {
                mOnValueChangeListener.onValueChange(this, lastNumber, currentNumber);
            }
        }
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        mOnValueChangeListener = onValueChangeListener;
    }

    public void setNumber(String number, boolean notifyListener) {
        setNumber(number);
        if (notifyListener) {
            callListener(this);
        }
    }
}
