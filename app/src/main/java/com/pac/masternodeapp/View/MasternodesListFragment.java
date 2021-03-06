package com.pac.masternodeapp.View;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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

import static com.pac.masternodeapp.Model.Constants.MASTERNODES_INFO;

/**
 * Created by PACcoin Team on 3/14/2018.
 */

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MasternodesListFragment extends Fragment {

    public static final String FRAGMENT_TAG = "mn_list";
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_LAST_UPDATED = "mn_list_last_updated";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private static MasternodesListRecyclerViewAdapter recyclerViewAdapter;
    private static RecyclerView recyclerView;
    private static List<Masternode> masternodes;
    private static TextView txtLastUpdated;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MasternodesListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MasternodesListFragment newInstance(int columnCount) {
        MasternodesListFragment fragment = new MasternodesListFragment();
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
    public void onResume() {
        super.onResume();
        Log.d("onResume Get MN Data", "obtaining most recent data");
        if (checkConnection() && !masternodes.isEmpty())
            getMaternodes();
        else if (masternodes.isEmpty())
            Snackbar.make(getActivity().findViewById(R.id.home_action_button), getResources().getString(R.string.mn_get_list_error_empty), Snackbar.LENGTH_LONG).show();
        else{
            Snackbar.make(getActivity().findViewById(R.id.home_action_button), String.format(getResources().getString(R.string.mn_update_error), "date"), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_masternodes_list, container, false);

        // Set the adapter
        if (view instanceof LinearLayout) {
            Context context = view.getContext();

            txtLastUpdated = view.findViewById(R.id.mn_information_last_updated);

            SQLiteHandler sqLiteHandler = new SQLiteHandler(getContext());
            masternodes = sqLiteHandler.getMasternodes();

            recyclerView = (RecyclerView) view.findViewById(R.id.list);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));

            }
            recyclerViewAdapter = new MasternodesListRecyclerViewAdapter(!masternodes.isEmpty() ? masternodes : Masternode.EmptyList, mListener);
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

            ((SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout)).setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (checkConnection() && !masternodes.isEmpty()) {
                        getMaternodes();
                        ((SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout)).setRefreshing(false);
                    }
                    else if (masternodes.isEmpty()) {
                        Snackbar.make(getActivity().findViewById(R.id.home_action_button), getResources().getString(R.string.mn_get_list_error_empty), Snackbar.LENGTH_LONG).show();
                        ((SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout)).setRefreshing(false);
                    }
                    else{
                        Snackbar.make(getActivity().findViewById(R.id.home_action_button), String.format(getResources().getString(R.string.mn_update_error), "date"), Snackbar.LENGTH_LONG).show();
                        ((SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout)).setRefreshing(false);
                    }
                }
            });

            if (!masternodes.isEmpty())
                txtLastUpdated.setText(String.format(getResources().getString(R.string.mn_information_last_updated), masternodes.get(0).getLastUpdated()));
        }

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        if (HomeActivity.menuCreated)
            HomeActivity.main_menu.findItem(R.id.action_search).setVisible(true);
        HomeActivity.visibleSearch = true;
        HomeActivity.buttonAction = true;
        HomeActivity.actionButton.setImageResource(R.mipmap.ic_add_black);
        HomeActivity.actionButton.show();
    }

    @Override
    public void onStop(){
        super.onStop();
        HomeActivity.visibleSearch = false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Masternode item);
    }

    private boolean checkConnection(){
        ConnectivityManager ConnectionManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
            return true;
        else
            return false;
    }

    public static void Search(String query){
        recyclerViewAdapter.FilterData(query);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    public static void Reset(){
        recyclerViewAdapter.RestoreData();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void getMaternodes() {

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

        String request = MASTERNODES_INFO + "/" + recyclerViewAdapter.GetPayees();
        requestController.execute(request);
    }

    private void PopulateUI(List<Masternode> masternodesList){
        if (!this.isVisible())
            return;
        else if (masternodesList.isEmpty()) {
            Snackbar.make(getActivity().findViewById(R.id.home_action_button), getResources().getString(R.string.mn_get_list_error), Snackbar.LENGTH_LONG).show();
            return;
        }

        SQLiteHandler sqLiteHandler = new SQLiteHandler(getContext());
        List<Masternode> updated = recyclerViewAdapter.SetAliases(masternodesList);
        for (int i = 0; i < updated.size(); i++){
            int updatedRows = sqLiteHandler.updateMasternode(updated.get(i));
            Log.d("Update-" + i, String.valueOf(updatedRows));
        }

        List<Masternode> updatedDB = sqLiteHandler.getMasternodes();
        if (!updatedDB.isEmpty())
            recyclerViewAdapter.ReplaceData(updatedDB);
        recyclerViewAdapter.notifyDataSetChanged();

        txtLastUpdated.setText(String.format(getResources().getString(R.string.mn_information_last_updated), updatedDB.get(0).getLastUpdated()));

        for (Masternode item : updatedDB) {
            if (item.isInPaymentQueue() && !item.isInPaymentQueueNotification()){
                NotificationHelper.createLocalNotification(getContext(), getResources().getString(R.string.mn_notification_title), getResources().getString(R.string.mn_notification_message));
                break;
            }
        }

        try{
            ((SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_layout)).setRefreshing(false);
        }
        catch (Exception ex){
            Log.d("Error: ", ex.toString());
        }
    }

    public boolean isRecyclerScrollable() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (layoutManager == null || adapter == null) return false;

        return layoutManager.findLastCompletelyVisibleItemPosition() < adapter.getItemCount() - 1;
    }
}
