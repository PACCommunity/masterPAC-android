package com.pac.masternodeapp.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.pac.masternodeapp.Controller.DataParser;
import com.pac.masternodeapp.Controller.RequestController;
import com.pac.masternodeapp.Controller.SQLiteHandler;
import com.pac.masternodeapp.Model.Masternode;
import com.pac.masternodeapp.Model.SocketCallback;
import com.pac.masternodeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.pac.masternodeapp.Model.Constants.MASTERNODES_INFO;

/**
 * Created by PACcoin Team on 3/14/2018.
 */
public class MasternodeInformationFragment extends Fragment {

    private static final String ARG_VIN = "mn_info_vin";
    private static final String ARG_STATUS = "mn_info_status";
    private static final String ARG_PAYMENT_POSITION = "mn_info_rank";
    private static final String ARG_IP = "mn_info_ip";
    private static final String ARG_PROTOCOL = "mn_info_protocol";
    private static final String ARG_PAYEE = "mn_info_payee";
    private static final String ARG_ACTIVE_SECONDS = "mn_info_active_seconds";
    private static final String ARG_LAST_SEEN = "mn_info_last_seen";
    private static final String ARG_REWARD_STATUS = "mn_info_reward_status";
    private static final String ARG_ALIAS = "mn_info_alias";
    private static final String ARG_CURRENT_BALANCE = "mn_info_total_rewards";
    private static final String ARG_IS_CHECKED = "mn_info_is_checked";
    private static final String ARG_LAST_UPDATED = "mn_info_last_updated";
    private static final String ARG_IN_PAYMENT_QUEUE = "mn_info_in_payment_queue";
    private static final String ARG_IN_PAYMENT_QUEUE_NOTIFICATION = "mn_info_in_payment_queue_notification";
    private static Masternode masternode;

    private Bundle argsMn;

    public MasternodeInformationFragment() {
        // Required empty public constructor
    }

    public static MasternodeInformationFragment newInstance(Masternode mn) {
        MasternodeInformationFragment fragment = new MasternodeInformationFragment();

        masternode = mn;

        Bundle args = new Bundle();
        args.putString(ARG_VIN, mn.getVin());
        args.putString(ARG_STATUS, mn.getStatus());
        args.putString(ARG_PAYMENT_POSITION, String.valueOf(mn.getRank()));
        args.putString(ARG_IP, mn.getIp());
        args.putString(ARG_PROTOCOL, mn.getProtocol());
        args.putString(ARG_PAYEE, mn.getPayee());
        args.putString(ARG_ACTIVE_SECONDS, mn.getActiveseconds());
        args.putString(ARG_LAST_SEEN, mn.getLastseen());
        args.putString(ARG_REWARD_STATUS, mn.getRewardStatus());
        args.putString(ARG_ALIAS, mn.getAlias());
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        args.putString(ARG_CURRENT_BALANCE, formatter.format(mn.getBalance()));
        args.putBoolean(ARG_IS_CHECKED, mn.getIsChecked());
        args.putString(ARG_LAST_UPDATED, mn.getLastUpdated());

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        argsMn = getArguments();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View infoView = inflater.inflate(R.layout.fragment_masternode_information, container, false);

        final ViewSwitcher switcher = infoView.findViewById(R.id.mn_information_view_switcher);
        final ImageButton btnEdit = infoView.findViewById(R.id.mn_information_btn_edit_alias);
        final TextView txtAlias = infoView.findViewById(R.id.mn_information_alias_body);
        final TextView txtIP = infoView.findViewById(R.id.mn_information_ip_body);
        final TextView txtStatus = infoView.findViewById(R.id.mn_information_status_body);
        final TextView txtRewardStatus = infoView.findViewById(R.id.mn_information_reward_status_body);
        final TextView txtPaymentPosition = infoView.findViewById(R.id.mn_information_rank_body);
        final TextView txtBalance = infoView.findViewById(R.id.mn_information_total_rewards_body);
        final TextView txtLastUpdated = infoView.findViewById(R.id.mn_information_last_updated);

        final EditText edtChangeAlias = infoView.findViewById(R.id.mn_information_alias_body_edit);
        edtChangeAlias.setSelection(edtChangeAlias.getText().length());
        edtChangeAlias.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        final Button btnRemoveMN = infoView.findViewById(R.id.mn_information_btn_remove);

        final Typeface regularFont = ResourcesCompat.getFont(getContext(), R.font.karla_regular);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_body)
                .setPositiveButton(R.string.dialog_button_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteHandler sqLiteHandler = new SQLiteHandler(getContext());
                        int deletedRows = sqLiteHandler.removeMasternode(argsMn.getString(ARG_PAYEE));
                        if (deletedRows > 0)
                            Snackbar.make(getActivity().findViewById(R.id.home_action_button),
                                    String.format(getResources().getString(R.string.mn_remove_success), argsMn.getString(ARG_ALIAS)), Snackbar.LENGTH_LONG).show();
                        else
                            Snackbar.make(getActivity().findViewById(R.id.home_action_button),
                                    String.format(getResources().getString(R.string.mn_remove_error), argsMn.getString(ARG_ALIAS)), Snackbar.LENGTH_LONG).show();
                        dialog.dismiss();
                        HomeActivity.fragmentManager.popBackStack(HomeActivity.homeFragment, 0);
                    }

                })
                .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        final AlertDialog confDialog = dialogBuilder.create();

        confDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                confDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(getContext(), R.color.buttonConfirm));
                confDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTypeface(regularFont);
                confDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(getContext(), R.color.buttonCancel));
                confDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTypeface(regularFont);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switcher.getDisplayedChild() == 0) {
                    switcher.showNext();
                    edtChangeAlias.setText(txtAlias.getText());
                    edtChangeAlias.requestFocus();
                    btnEdit.setImageResource(R.mipmap.ic_check_black);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                } else {
                    if (!edtChangeAlias.getText().equals(masternode.getAlias())) {
                        masternode.setAlias(edtChangeAlias.getText().toString());
                        SQLiteHandler sqLiteHandler = new SQLiteHandler(getContext());
                        sqLiteHandler.updateMasternode(masternode);
                    }
                    txtAlias.setText(masternode.getAlias());
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    switcher.showPrevious();
                    btnEdit.setImageResource(R.mipmap.ic_clear_edit_2);
                }

            }
        });

        btnRemoveMN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confDialog.show();
            }
        });

        txtAlias.setText(argsMn.getString(ARG_ALIAS));
        txtIP.setText(argsMn.getString(ARG_IP));
        txtStatus.setText(argsMn.getString(ARG_STATUS));
        txtRewardStatus.setText(argsMn.getString(ARG_REWARD_STATUS));
        txtPaymentPosition.setText(argsMn.getString(ARG_PAYMENT_POSITION));
        txtBalance.setText(argsMn.getString(ARG_CURRENT_BALANCE + " $PAC"));
        txtLastUpdated.setText(argsMn.getString(ARG_LAST_UPDATED));

        return infoView;
    }

    @Override
    public void onStart() {
        super.onStart();
        HomeActivity.main_menu.findItem(R.id.action_search).setVisible(false);
        HomeActivity.actionButton.hide();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (checkConnection())
            getMasternodeInfo();
        else {
            Snackbar.make(getActivity().findViewById(R.id.home_action_button), String.format(getResources().getString(R.string.mn_update_error), argsMn.getString(ARG_LAST_UPDATED)), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        HomeActivity.actionButton.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private boolean checkConnection() {
        ConnectivityManager ConnectionManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true)
            return true;
        else
            return false;
    }

    private void getMasternodeInfo() {

        RequestController requestController = new RequestController(new SocketCallback() {
            @Override
            public void onCallResponse(String response) {
                Log.d("Response", response);
                DataParser dataParser = new DataParser();
                try {
                    JSONObject masternodesJson = new JSONObject(response);
                    JSONArray mnArray = masternodesJson.getJSONArray("result");
                    if (mnArray == null || mnArray.length() == 0)
                        Snackbar.make(getActivity().findViewById(R.id.home_action_button), getResources().getString(R.string.mn_get_info_error), Snackbar.LENGTH_LONG).show();
                    else
                        PopulateUI(dataParser.GetMasternode(mnArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        String request = MASTERNODES_INFO + "/" + argsMn.getString(ARG_PAYEE);
        requestController.execute(request);
    }

    private void PopulateUI(Masternode mn) {
        if (!this.isVisible())
            return;
        try {
            SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            final TextView txtAlias = getView().findViewById(R.id.mn_information_alias_body);
            final TextView txtIP = getView().findViewById(R.id.mn_information_ip_body);
            final TextView txtStatus = getView().findViewById(R.id.mn_information_status_body);
            final TextView txtRewardStatus = getView().findViewById(R.id.mn_information_reward_status_body);
            final TextView txtPaymentPosition = getView().findViewById(R.id.mn_information_rank_body);
            final TextView txtBalance = getView().findViewById(R.id.mn_information_total_rewards_body);
            final TextView txtLastUpdated = getView().findViewById(R.id.mn_information_last_updated);

            txtAlias.setText(argsMn.getString(ARG_ALIAS));
            txtIP.setText(mn.getIp());
            txtStatus.setText(mn.getStatus());
            txtRewardStatus.setText(mn.getRewardStatus());
            txtPaymentPosition.setText(String.valueOf(mn.getRank()));
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            txtBalance.setText(formatter.format(mn.getBalance()) + " $PAC");
            txtLastUpdated.setText(String.format(
                    getResources().getString(R.string.mn_information_last_updated),
                    dateTimeFormatter.format(date)));

        } catch (Exception ex) {
            Log.d("Error: ", ex.toString());
        }
    }
}
