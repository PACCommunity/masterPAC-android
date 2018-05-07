package com.pac.masternodeapp.View;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pac.masternodeapp.Controller.RecyclerItemTouchHelper;
import com.pac.masternodeapp.Controller.SQLiteHandler;
import com.pac.masternodeapp.Model.Masternode;
import com.pac.masternodeapp.R;

import java.util.List;

public class MasternodesAddAliasListFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    public static final String FRAGMENT_TAG = "add_alias";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private MasternodesAddAliasListRecyclerViewAdapter masternodesAddAliasListRecyclerViewAdapter;
    private Snackbar snackbar;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MasternodesAddAliasListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MasternodesAddAliasListFragment newInstance(int columnCount) {
        MasternodesAddAliasListFragment fragment = new MasternodesAddAliasListFragment();
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
        View view = inflater.inflate(R.layout.fragment_masternodes_add_alias_list, container, false);

        // Set the adapter
        if (view instanceof LinearLayout) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_add_alias);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            masternodesAddAliasListRecyclerViewAdapter = new MasternodesAddAliasListRecyclerViewAdapter(MasternodesAddListRecyclerViewAdapter.GetCheckedList());
            recyclerView.setAdapter(masternodesAddAliasListRecyclerViewAdapter);


            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        }

        Button doneButton = (Button) view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteHandler sqLiteHandler = new SQLiteHandler(getContext());
                List<Masternode> mvalues = masternodesAddAliasListRecyclerViewAdapter.GetmValues();
                long response = sqLiteHandler.addMasternodes(masternodesAddAliasListRecyclerViewAdapter.GetmValues());
                if (response > 0)
                    Snackbar.make(getActivity().findViewById(R.id.home_action_button), getResources().getString(R.string.mns_add_success), Snackbar.LENGTH_LONG).show();
                else
                    Snackbar.make(getActivity().findViewById(R.id.home_action_button), getResources().getString(R.string.mns_add_error), Snackbar.LENGTH_LONG).show();
                MasternodesAddListRecyclerViewAdapter.ClearCheckedList();
                HomeActivity.main_menu.findItem(R.id.action_search).setVisible(true);
                HomeActivity.fragmentManager.popBackStack(HomeActivity.homeFragment, 0);
            }
        });

        return view;
    }

    public void onStart(){
        super.onStart();

        HomeActivity.actionButton.hide();
    }

    public void onStop(){
        super.onStop();

        if (!(snackbar == null))
            snackbar.dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        if (viewHolder instanceof MasternodesAddAliasListRecyclerViewAdapter.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = MasternodesAddListRecyclerViewAdapter.GetCheckedList().get(viewHolder.getAdapterPosition()).getIp();

            // backup of removed item for undo purpose
            final Masternode deletedItem = MasternodesAddListRecyclerViewAdapter.GetCheckedList().get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            masternodesAddAliasListRecyclerViewAdapter.RemoveItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            snackbar = Snackbar
                    .make(HomeActivity.actionButton, name + " was removed!", 5000);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // undo is selected, restore the deleted item
                    masternodesAddAliasListRecyclerViewAdapter.RestoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.pacYellow));
            snackbar.show();
        }
    }
}
