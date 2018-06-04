package com.pac.masternodeapp.View;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pac.masternodeapp.Model.CustomFingerprintCallback;
import com.pac.masternodeapp.R;

import me.aflak.libraries.callback.FingerprintCallback;
import me.aflak.libraries.view.Fingerprint;

public class CustomFingerprintDialog extends DialogFragment implements FingerprintCallback {

    private CustomFingerprintCallback mCallback;

    public CustomFingerprintDialog(){}

    public static CustomFingerprintDialog newInstance(CustomFingerprintCallback callback){
        CustomFingerprintDialog fingerprintDialog = new CustomFingerprintDialog();
        fingerprintDialog.mCallback = callback;
        return fingerprintDialog;
    }

    @Override
    public void onCreate(Bundle savedInstace){
        super.onCreate(savedInstace);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fingerprint_dialog, container, false);

        Fingerprint fingerprint = v.findViewById(R.id.fingerprint);
        fingerprint.callback(this)
                .authenticate();

        getDialog().getWindow().setBackgroundDrawableResource(R.drawable.fingerprint_dialog_background);

        Button cancel = v.findViewById(R.id.dialog_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onAuthenticationSucceeded() {
        if (!this.isVisible())
            return;
        TextView message = getView().findViewById(R.id.fingerprint_dialog_message);
        message.setText(getResources().getString(R.string.mn_fingerprint_success));
        mCallback.onSuccess();
    }

    @Override
    public void onAuthenticationFailed() {
        if (!this.isVisible())
            return;
        TextView message = getView().findViewById(R.id.fingerprint_dialog_message);
        message.setText(getResources().getString(R.string.mn_fingerprint_failed));
    }

    @Override
    public void onAuthenticationError(int errorCode, String error) {
        if (!this.isVisible())
            return;
        TextView message = getView().findViewById(R.id.fingerprint_dialog_message);
        message.setText(getResources().getString(R.string.mn_fingerprint_error));
    }
}
