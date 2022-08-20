package com.mtc.print.starprint.starprntsdk;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class DisplayContrastDialogFragment extends CommonAlertDialogFragment {

    private Callback mCallbackTarget;

    public static DisplayContrastDialogFragment newInstance(String tag) {
        DisplayContrastDialogFragment dialogFragment = new DisplayContrastDialogFragment();

        Bundle args = new Bundle();
        args.putString(DIALOG_TAG, tag);
        args.putBoolean(CANCEL, false);
        args.putBoolean(CALLBACK, true);
        args.putString(LABEL_NEGATIVE, "Cancel");

        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(false);

        return dialogFragment;
    }

    @Override
    public @NonNull
    Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Select Contrast.");

        builder.setItems(new String[]{
                        "Contrast -3",
                        "Contrast -2",
                        "Contrast -1",
                        "Default",
                        "Contrast +1",
                        "Contrast +2",
                        "Contrast +3"},
                mOnContrastClickListener);

        setupNegativeButton(builder);

        mCallbackTarget = (Callback) getParentFragment();

        return builder.create();
    }

    private DialogInterface.OnClickListener mOnContrastClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent intentForPassingData = new Intent();
            intentForPassingData.putExtra(CommonActivity.BUNDLE_KEY_INDEX, which);

            mCallbackTarget.onDialogResult(getArguments().getString(DIALOG_TAG), intentForPassingData);

            dismiss();
        }
    };
}
