package com.pac.masternodeapp.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pac.masternodeapp.Controller.SQLiteHandler;
import com.pac.masternodeapp.Model.Masternode;
import com.pac.masternodeapp.R;
import com.pac.masternodeapp.View.MasternodesListFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class MasternodesListRecyclerViewAdapter extends RecyclerView.Adapter<MasternodesListRecyclerViewAdapter.ViewHolder> {

    private static List<Masternode> originalData;
    private static List<Masternode> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MasternodesListRecyclerViewAdapter(List<Masternode> items, OnListFragmentInteractionListener listener) {
        mValues = new ArrayList<Masternode>(items);
        originalData = new ArrayList<Masternode>(items);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_masternodes_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mAliasView.setText(mValues.get(position).getAlias());
        String status = mValues.get(position).getStatus();
        holder.mStatusView.setText(status);
        holder.mRankView.setText(String.valueOf(mValues.get(position).getRank()));

        switch (status){
            case "ENABLED":
                holder.mColorView.setImageResource(R.mipmap.ic_green);
                break;
            case "NEW_START_REQUIRED":
                holder.mColorView.setImageResource(R.mipmap.ic_yellow);
                break;
            case "DISABLED":
                holder.mColorView.setImageResource(R.mipmap.ic_red);
                break;
            default:
                holder.mColorView.setImageResource(R.mipmap.ic_yellow);
                break;
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAliasView;
        public final TextView mStatusView;
        public final TextView mRankView;
        public final ImageView mColorView;
        public Masternode mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAliasView = view.findViewById(R.id.alias);
            mStatusView = view.findViewById(R.id.status);
            mRankView = view.findViewById(R.id.rank);
            mColorView = view.findViewById(R.id.color);
        }
    }

    public void FilterData(String query){
        mValues.clear();
        for (int i = 0; i < originalData.size(); i++){
            if (originalData.get(i).getAlias().toLowerCase().contains(query.toLowerCase())) {
                mValues.add(originalData.get(i));
            }
        }
    }

    public void RestoreData(){
        mValues.clear();
        mValues = new ArrayList<Masternode>(originalData);
    }

    public String GetPayees(){
        String payees = "";
        for (int i = 0 ; i < originalData.size() ; i++){
            payees += originalData.get(i).getPayee();
            if (i != originalData.size() - 1)
                payees += ",";
        }
        return payees;
    }

    public List<Masternode> ReplaceData(List<Masternode> masternodeList){
        mValues.clear();
        mValues = new ArrayList<Masternode>(masternodeList);
        originalData.clear();
        originalData = new ArrayList<Masternode>(masternodeList);

        return mValues;
    }

    public List<Masternode> SetAliases(List<Masternode> masternodeList){
        for (int i = 0; i < masternodeList.size(); i++){
            for (int j = 0; j < originalData.size(); j++){
                if (masternodeList.get(i).getPayee().equals(originalData.get(j).getPayee()))
                    masternodeList.get(i).setAlias(originalData.get(j).getAlias());
            }
        }
        return masternodeList;
    }
}
