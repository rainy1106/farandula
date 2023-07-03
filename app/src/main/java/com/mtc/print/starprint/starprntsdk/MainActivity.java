package com.mtc.print.starprint.starprntsdk;

import android.os.Bundle;
import android.view.KeyEvent;

import com.mtc.R;

@SuppressWarnings("SimplifiableIfStatement")
public class MainActivity extends CommonActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Communication.CommunicationResult.appContext = this;
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode != KeyEvent.KEYCODE_BACK) {                 // Do not close MainActivity.
//            return super.onKeyDown(keyCode, event);
//        } else {
//            return false;
//        }
//    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
