package com.pac.masternodeapp.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pac.masternodeapp.Model.Masternode;
import com.pac.masternodeapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PACcoin Team on 3/14/2018.
 */

public class MasternodesAddListRecyclerViewAdapter extends RecyclerView.Adapter<MasternodesAddListRecyclerViewAdapter.ViewHolder> {

    private static List<Masternode> mValues;
    private static List<Masternode> checkedList = new ArrayList<>();
    private static List<Masternode> originalData;

    public MasternodesAddListRecyclerViewAdapter(List<Masternode> items) {
        mValues = new ArrayList<Masternode>(items);
        originalData = new ArrayList<Masternode>(items);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_masternodes_add_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getIp());

        holder.mCheckBox.setOnCheckedChangeListener(null);

        for (int i = 0 ; i < checkedList.size() ; i++){
            if (checkedList.get(i).getIp().matches(mValues.get(position).getIp())){
                mValues.get(position).setIsChecked(true);
                break;
            }
        }

        holder.mCheckBox.setChecked(holder.mItem.getIsChecked());
        holder.mCheckBoxBackground.setAlpha(holder.mItem.getIsChecked() ? 1 : 0);

        holder.mCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    checkedList.add(mValues.get(position));
                    holder.mCheckBoxBackground.setAlpha(1);
                    mValues.get(position).setIsChecked(true);
                }
                else {
                    checkedList.remove(position);
                    holder.mCheckBoxBackground.setAlpha(0);
                    mValues.get(position).setIsChecked(false);
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
        public final TextView mIdView;
        public final CheckBox mCheckBox;
        public final LinearLayout mCheckBoxBackground;
        public Masternode mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.ip);
            mCheckBox = (CheckBox) view.findViewById(R.id.checkbox);
            mCheckBoxBackground = (LinearLayout) view.findViewById(R.id.checkbox_background);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCheckBox.getText() + "'";
        }
    }

    public static List<Masternode> GetCheckedList(){
        return checkedList;
    }

    public static void ClearCheckedList(){
        for (int i = 0; i < mValues.size(); i++){
            for (int j = 0; j < checkedList.size(); j++) {
                if (mValues.get(i).getIp().contains(checkedList.get(j).getIp())) {
                    mValues.get(i).setIsChecked(false);
                }
            }
        }
        checkedList.clear();
    }

    public void FilterData(String query){
        mValues.clear();
        for (int i = 0; i < originalData.size(); i++){
            if (originalData.get(i).getIp().contains(query)) {
                mValues.add(originalData.get(i));
            }
        }
    }

    public void RestoreData(){
        mValues.clear();
        mValues = new ArrayList<Masternode>(originalData);
    }

    public void ReplaceData(List<Masternode> masternodeList){
        mValues.clear();
        mValues = new ArrayList<Masternode>(masternodeList);
        originalData.clear();
        originalData = new ArrayList<Masternode>(masternodeList);
    }
}
