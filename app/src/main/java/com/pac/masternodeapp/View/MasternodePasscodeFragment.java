package com.pac.masternodeapp.View;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pac.masternodeapp.Controller.SQLiteHandler;
import com.pac.masternodeapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MasternodePasscodeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String passcode = "";
    private String confPasscode = "";
    private int passcodeCount = 0;
    private int confPasscodeCount = 0;
    List<ImageView> imageViewList;
    Boolean passcodeFlag = true;
    TextView txtPin;
    Handler timeHandler = new Handler();
    SharedPreferences preferences;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MasternodePasscodeFragment() {
    }

    public static MasternodePasscodeFragment newInstance(String param1, String param2) {
        MasternodePasscodeFragment fragment = new MasternodePasscodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View passcodeView = inflater.inflate(R.layout.fragment_masternode_passcode, container, false);
        final ImageView imageView = passcodeView.findViewById(R.id.passcode_img_pac_icon_fragment);
        final Typeface regularFont = ResourcesCompat.getFont(getContext(), R.font.karla_regular);
        Glide.with(this).load(getResources()
                .getIdentifier("logopac", "drawable", getActivity().getPackageName()))
                .into(imageView);
        final Button btnOne = passcodeView.findViewById(R.id.passcode_btn_one_fragment);
        final Button btnTwo = passcodeView.findViewById(R.id.passcode_btn_two_fragment);
        final Button btnThree = passcodeView.findViewById(R.id.passcode_btn_three_fragment);
        final Button btnFour = passcodeView.findViewById(R.id.passcode_btn_four_fragment);
        final Button btnFive = passcodeView.findViewById(R.id.passcode_btn_five_fragment);
        final Button btnSix = passcodeView.findViewById(R.id.passcode_btn_six_fragment);
        final Button btnSeven = passcodeView.findViewById(R.id.passcode_btn_seven_fragment);
        final Button btnEight = passcodeView.findViewById(R.id.passcode_btn_eight_fragment);
        final Button btnNine = passcodeView.findViewById(R.id.passcode_btn_nine_fragment);
        final Button btnZero = passcodeView.findViewById(R.id.passcode_btn_zero_fragment);
        final ImageButton btnClear = passcodeView.findViewById(R.id.passcode_btn_clear_fragment);
        txtPin = passcodeView.findViewById(R.id.mn_passcode_pin_fragment);
        preferences = getContext().getSharedPreferences("active_passcode", 0);
        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchImage(v);
            }
        });
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchImage(v);
            }
        });
        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchImage(v);
            }
        });
        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchImage(v);
            }
        });
        btnFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchImage(v);
            }
        });
        btnSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchImage(v);
            }
        });
        btnSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchImage(v);
            }
        });
        btnEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchImage(v);
            }
        });
        btnNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchImage(v);
            }
        });
        btnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchImage(v);
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passcodeFlag) {
                    if (passcodeCount >= 1) {
                        passcodeCount--;
                        passcode = passcode.substring(0, passcode.length() - 1);
                        imageViewList.get(passcodeCount).setImageResource(R.mipmap.ic_circle_white);
                    }
                } else {
                    if (confPasscodeCount >= 1) {
                        confPasscodeCount--;
                        confPasscode = confPasscode.substring(0, confPasscode.length() - 1);
                        imageViewList.get(confPasscodeCount).setImageResource(R.mipmap.ic_circle_white);
                    }
                }
            }
        });

        final ImageView circleOne = passcodeView.findViewById(R.id.passcode_circle_one_fragment);
        final ImageView circleTwo = passcodeView.findViewById(R.id.passcode_circle_two_fragment);
        final ImageView circleThree = passcodeView.findViewById(R.id.passcode_circle_three_fragment);
        final ImageView circleFour = passcodeView.findViewById(R.id.passcode_circle_four_fragment);
        final ImageView circleFive = passcodeView.findViewById(R.id.passcode_circle_five_fragment);
        final ImageView circleSix = passcodeView.findViewById(R.id.passcode_circle_six_fragment);

        imageViewList = new ArrayList<>();
        imageViewList.add(circleOne);
        imageViewList.add(circleTwo);
        imageViewList.add(circleThree);
        imageViewList.add(circleFour);
        imageViewList.add(circleFive);
        imageViewList.add(circleSix);

        return passcodeView;
    }

    boolean canContinue = false;
    private void switchImage(View v) {
        int passcodeLength = 6;
        if (passcodeFlag) {
            if (passcodeCount < passcodeLength) {
                imageViewList.get(passcodeCount).setImageResource(R.mipmap.ic_circle_yellow);
                switch (v.getId()) {
                    case R.id.passcode_btn_one_fragment:
                        passcode += "1";
                        break;
                    case R.id.passcode_btn_two_fragment:
                        passcode += "2";
                        break;
                    case R.id.passcode_btn_three_fragment:
                        passcode += "3";
                        break;
                    case R.id.passcode_btn_four_fragment:
                        passcode += "4";
                        break;
                    case R.id.passcode_btn_five_fragment:
                        passcode += "5";
                        break;
                    case R.id.passcode_btn_six_fragment:
                        passcode += "6";
                        break;
                    case R.id.passcode_btn_seven_fragment:
                        passcode += "7";
                        break;
                    case R.id.passcode_btn_eight_fragment:
                        passcode += "8";
                        break;
                    case R.id.passcode_btn_nine_fragment:
                        passcode += "9";
                        break;
                    case R.id.passcode_btn_zero_fragment:
                        passcode += "0";
                        break;
                }
                passcodeCount++;
            }
            if (passcodeCount == passcodeLength) {
                resetImage();
                passcodeFlag = false;
                txtPin.setText(R.string.passcode_confirm_passcode);
                passcodeCount++;


                timeHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        canContinue = true;
                    }
                }, 250);
            }
        } else {
            if (canContinue == false) {
                return;
            }

            if (confPasscodeCount < passcodeLength) {
                imageViewList.get(confPasscodeCount).setImageResource(R.mipmap.ic_circle_yellow);
                switch (v.getId()) {
                    case R.id.passcode_btn_one_fragment:
                        confPasscode += "1";
                        break;
                    case R.id.passcode_btn_two_fragment:
                        confPasscode += "2";
                        break;
                    case R.id.passcode_btn_three_fragment:
                        confPasscode += "3";
                        break;
                    case R.id.passcode_btn_four_fragment:
                        confPasscode += "4";
                        break;
                    case R.id.passcode_btn_five_fragment:
                        confPasscode += "5";
                        break;
                    case R.id.passcode_btn_six_fragment:
                        confPasscode += "6";
                        break;
                    case R.id.passcode_btn_seven_fragment:
                        confPasscode += "7";
                        break;
                    case R.id.passcode_btn_eight_fragment:
                        confPasscode += "8";
                        break;
                    case R.id.passcode_btn_nine_fragment:
                        confPasscode += "9";
                        break;
                    case R.id.passcode_btn_zero_fragment:
                        confPasscode += "0";
                        break;
                }
            }
            confPasscodeCount++;
            if (confPasscodeCount == passcodeLength) {
                resetImage();
                Boolean isEqual = confPasscode(passcode, confPasscode);
                preferences = getContext().getSharedPreferences("active_passcode", 0);
                final SharedPreferences.Editor editor = preferences.edit();
                if (isEqual) {
                    Boolean isSet = setPasscode(confPasscode);
                    if (isSet) {

                        editor.putBoolean("active_passcode", true);
                        HomeActivity.fragmentManager.popBackStack(HomeActivity.homeFragment, 0);
                        Snackbar.make(getView(), R.string.passcode_set, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(getView(), R.string.passcode_does_not_match, Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    editor.putBoolean("active_passcode", false);
                    passcodeCount = 0;
                    confPasscodeCount = 0;
                    passcode = "";
                    confPasscode = "";
                    passcodeFlag = true;
                    txtPin.setText(R.string.mn_passcode_enter_pin);
                    resetImage();
                }
                editor.apply();
            }
        }
    }

    private void resetImage() {
        timeHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (ImageView img : imageViewList) {
                    img.setImageResource(R.mipmap.ic_circle_white);
                }
            }
        }, 250);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private Boolean setPasscode(String passcode) {
        SQLiteHandler sqLiteHandler = new SQLiteHandler(getContext());
        sqLiteHandler.removePasscode();
        long affectedRows = sqLiteHandler.addPasscode(passcode);
        return affectedRows >= 1;
    }

    private Boolean verifyPasscode(String passcode) {
        SQLiteHandler sqLiteHandler = new SQLiteHandler(getContext());
        return sqLiteHandler.verifyPasscode(passcode);
    }

    private Boolean confPasscode(String passcode, String confPasscode) {
        return passcode.equals(confPasscode);
    }

    public void onStart() {
        super.onStart();
        HomeActivity.main_menu.findItem(R.id.action_search).setVisible(false);
        HomeActivity.actionButton.hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        Boolean isActive = preferences.getBoolean("active_passcode", false);
        if (!isActive || preferences == null) {
            Snackbar.make(getView(), R.string.passcode_not_set, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}

