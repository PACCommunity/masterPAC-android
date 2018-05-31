package com.pac.masternodeapp.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pac.masternodeapp.Controller.SQLiteHandler;
import com.pac.masternodeapp.Model.CustomFingerprintCallback;
import com.pac.masternodeapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.aflak.libraries.callback.FingerprintCallback;
import me.aflak.libraries.callback.FingerprintDialogCallback;
import me.aflak.libraries.dialog.DialogAnimation;
import me.aflak.libraries.dialog.FingerprintDialog;

/**
 * Created by PACcoin Team on 3/14/2018.
 */

public class PasscodeActivity extends AppCompatActivity implements CustomFingerprintCallback {
    List<ImageView> imageViewList;
    int passcodeCount = 0;
    String passcode = "";
    LinearLayout circleLayout;
    Handler timeHandler = new Handler();
    TextView passcodeTxt;
    final int passcodeLength = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode2);
        SharedPreferences preferences = this.getSharedPreferences("active_passcode", 0);
        Boolean passcodePref = preferences.getBoolean("active_passcode", false);

        if (passcodePref) {

            final ImageView imageView = findViewById(R.id.passcode_img_pac_icon);

            Glide.with(this).load(getResources()
                    .getIdentifier("logopac", "drawable", this.getPackageName()))
                    .into(imageView);

            final Button btnOne = findViewById(R.id.passcode_btn_one);
            final Button btnTwo = findViewById(R.id.passcode_btn_two);
            final Button btnThree = findViewById(R.id.passcode_btn_three);
            final Button btnFour = findViewById(R.id.passcode_btn_four);
            final Button btnFive = findViewById(R.id.passcode_btn_five);
            final Button btnSix = findViewById(R.id.passcode_btn_six);
            final Button btnSeven = findViewById(R.id.passcode_btn_seven);
            final Button btnEight = findViewById(R.id.passcode_btn_eight);
            final Button btnNine = findViewById(R.id.passcode_btn_nine);
            final Button btnZero = findViewById(R.id.passcode_btn_zero);
            final ImageButton btnClear = findViewById(R.id.passcode_btn_clear);
            final ImageView circleOne = findViewById(R.id.passcode_circle_one);
            final ImageView circleTwo = findViewById(R.id.passcode_circle_two);
            final ImageView circleThree = findViewById(R.id.passcode_circle_three);
            final ImageView circleFour = findViewById(R.id.passcode_circle_four);
            final ImageView circleFive = findViewById(R.id.passcode_circle_five);
            final ImageView circleSix = findViewById(R.id.passcode_circle_six);
            passcodeTxt = findViewById(R.id.mn_passcode_pin);

            circleLayout = findViewById(R.id.passcode_circle_layout);
            imageViewList = new ArrayList<>();
            imageViewList.add(circleOne);
            imageViewList.add(circleTwo);
            imageViewList.add(circleThree);
            imageViewList.add(circleFour);
            imageViewList.add(circleFive);
            imageViewList.add(circleSix);

            btnOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchImage(1);
                }
            });

            btnTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchImage(2);
                }
            });

            btnThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchImage(3);
                }
            });

            btnFour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchImage(4);
                }
            });

            btnFive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchImage(5);
                }
            });

            btnSix.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchImage(6);
                }
            });

            btnSeven.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchImage(7);
                }
            });

            btnEight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchImage(8);
                }
            });

            btnNine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchImage(9);
                }
            });

            btnZero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchImage(0);
                }
            });

            btnClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (passcodeCount >= 1) {
                        passcodeCount--;
                        passcode = passcode.substring(0, passcode.length() - 1);
                        imageViewList.get(passcodeCount).setImageResource(R.mipmap.ic_circle_white);
                    }
                }
            });

            //active_fingerprint
            Boolean fingerprintPref = preferences.getBoolean("active_fingerprint", false);

            if (fingerprintPref){
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null)
                    ft.remove(prev);
                ft.addToBackStack(null);

                DialogFragment customFingerprintDialog = CustomFingerprintDialog.newInstance(this);
                customFingerprintDialog.show(ft, "dialog");
            }

        } else {
            switchActivity();
        }
    }

    private void switchActivity() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }

    private void switchImage(int button) {
        passcodeTxt.setText(R.string.mn_passcode_enter_pin);
        if (passcodeCount < passcodeLength) {
            imageViewList.get(passcodeCount).setImageResource(R.mipmap.ic_circle_yellow);
            switch (button) {
                case 1:
                    passcode += "1";
                    break;
                case 2:
                    passcode += "2";
                    break;
                case 3:
                    passcode += "3";
                    break;
                case 4:
                    passcode += "4";
                    break;
                case 5:
                    passcode += "5";
                    break;
                case 6:
                    passcode += "6";
                    break;
                case 7:
                    passcode += "7";
                    break;
                case 8:
                    passcode += "8";
                    break;
                case 9:
                    passcode += "9";
                    break;
                case 0:
                    passcode += "0";
                    break;
            }
            passcodeCount++;
        }
        if (passcodeCount == passcodeLength) {
            verifyPasscode();
        }
    }

    private void verifyPasscode() {
        SQLiteHandler sqLiteHandler = new SQLiteHandler(this);
        if (sqLiteHandler.verifyPasscode(passcode)) {
            switchActivity();
        } else {
            timeHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (ImageView img : imageViewList) {
                        img.setImageResource(R.mipmap.ic_circle_white);
                        passcodeCount = 0;
                        passcode = "";
                    }

                    circleLayout.startAnimation(
                    AnimationUtils.loadAnimation(PasscodeActivity.this, R.anim.shake_animation));
                    showMessage();
                }
            }, 250);
        }
    }

    private void showMessage() {
        passcodeTxt.setText(R.string.passcode_error);
    }

    @Override
    public void onSuccess() {
        switchActivity();
    }
}
