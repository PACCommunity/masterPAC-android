package com.pac.masternodeapp.View;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pac.masternodeapp.Controller.DataParser;
import com.pac.masternodeapp.Controller.RequestController;
import com.pac.masternodeapp.Controller.SQLiteHandler;
import com.pac.masternodeapp.Helpers.NotificationHelper;
import com.pac.masternodeapp.Model.Masternode;
import com.pac.masternodeapp.Model.SocketCallback;
import com.pac.masternodeapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

import static com.pac.masternodeapp.Model.Constants.MASTERNODES_LIST;

/**
 * Created by PACcoin Team on 3/14/2018.
 */

public class MasternodesAddListFragment extends Fragment {

    public static final String FRAGMENT_TAG = "add_mn";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private static MasternodesAddListRecyclerViewAdapter recyclerViewAdapter;
    private static LinearLayout progressBar;
    private static RecyclerView recyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */

    public MasternodesAddListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MasternodesAddListFragment newInstance(int columnCount) {
        MasternodesAddListFragment fragment = new MasternodesAddListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_masternodes_add_list, container, false);

        // Set the adapter
        if (view instanceof RelativeLayout) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            progressBar = (LinearLayout) view.findViewById(R.id.linear_progress);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerViewAdapter = new MasternodesAddListRecyclerViewAdapter(Masternode.EmptyList);
            recyclerView.setAdapter(recyclerViewAdapter);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0 && HomeActivity.actionButton.getVisibility() == View.VISIBLE) {
                        HomeActivity.actionButton.hide();
                    } else if (dy < 0 && HomeActivity.actionButton.getVisibility() != View.VISIBLE) {
                        HomeActivity.actionButton.show();
                    }
                }
            });
        }

        progressBar.setVisibility(View.VISIBLE);
        if (checkConnection()) {
            getAllMasternodes();
        } else {
            progressBar.setVisibility(View.GONE);
            Snackbar.make(getActivity().findViewById(R.id.home_action_button), getResources().getString(R.string.mn_get_list_error), Snackbar.LENGTH_LONG).show();
        }

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        HomeActivity.visibleSearch = true;
        Log.d("BE4 CRASH", "It gonna crash");
        HomeActivity.main_menu.findItem(R.id.action_search).setVisible(true);
        HomeActivity.buttonAction = false;
        HomeActivity.actionButton.setImageResource(R.mipmap.ic_check_black);
        HomeActivity.actionButton.show();
        if (!HomeActivity.checkedList)
            MasternodesAddListRecyclerViewAdapter.ClearCheckedList();
        HomeActivity.checkedList = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        HomeActivity.visibleSearch = false;
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

    public static void Search(String query) {
        recyclerViewAdapter.FilterData(query);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public static void Reset() {
        recyclerViewAdapter.RestoreData();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void getAllMasternodes() {
        RequestController requestController = new RequestController(new SocketCallback() {
            @Override
            public void onCallResponse(String response) {
                Log.d("Response", response);
                DataParser dataParser = new DataParser();
                try {
                    JSONObject masternodesJson = new JSONObject(response);
                    JSONArray mnArray = masternodesJson.getJSONArray("result");
                    if (mnArray == null || mnArray.length() == 0)
                        Snackbar.make(getActivity().findViewById(R.id.home_action_button), getResources().getString(R.string.mn_get_list_error), Snackbar.LENGTH_LONG).show();
                    else
                        PopulateUI((List<Masternode>) dataParser.GetMasternodeList(mnArray));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        requestController.execute(MASTERNODES_LIST);
    }

    private void PopulateUI(List<Masternode> masternodesList) {
        if (!this.isVisible())
            return;
        else if (masternodesList.isEmpty()) {
            Snackbar.make(getActivity().findViewById(R.id.home_action_button), getResources().getString(R.string.mn_get_list_error), Snackbar.LENGTH_LONG).show();
            return;
        }
        SQLiteHandler sqLiteHandler = new SQLiteHandler(getContext());
        sqLiteHandler.updateMasternodeCount(masternodesList.size());

        List<Masternode> dbMasternodes = sqLiteHandler.getMasternodes();
        if (!dbMasternodes.isEmpty()) {
            for (int i = 0; i < dbMasternodes.size(); i++) {
                String mymnlistpayee = dbMasternodes.get(i).getPayee();
                for (int j = 0; j < masternodesList.size(); j++) {
                    String mnlistpayee = masternodesList.get(j).getPayee();
                    if (Objects.equals(mymnlistpayee, mnlistpayee)) {
                        Log.d("IPsRemoved: ", masternodesList.get(j).getIp());
                        masternodesList.remove(j);
                        break;
                    }
                }
            }
        }
        recyclerViewAdapter.ReplaceData(masternodesList);
        recyclerViewAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    public boolean isRecyclerScrollable() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (layoutManager == null || adapter == null) return false;

        return layoutManager.findLastCompletelyVisibleItemPosition() < adapter.getItemCount() - 1;
    }
}
