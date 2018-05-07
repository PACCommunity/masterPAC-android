package com.pac.masternodeapp.View;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pac.masternodeapp.Model.Masternode;
import com.pac.masternodeapp.R;

import java.util.List;

public class MasternodesAddAliasListRecyclerViewAdapter extends RecyclerView.Adapter<MasternodesAddAliasListRecyclerViewAdapter.ViewHolder> {

    private static List<Masternode> mValues;

    public MasternodesAddAliasListRecyclerViewAdapter(List<Masternode> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_masternodes_add_alias_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getIp());

        holder.mContentView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mValues.get(position).setAlias(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        public final EditText mContentView;
        public Masternode mItem;
        public RelativeLayout viewBackground, viewForeground;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.ip_alias);
            mContentView = (EditText) view.findViewById(R.id.add_alias);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public List<Masternode> GetmValues(){
        for (int i = 0; i < mValues.size(); i++)
            if (mValues.get(i).getAlias() == null)
                mValues.get(i).setAlias(mValues.get(i).getIp());
        return mValues;
    }

    public void RemoveItem(int position){
        mValues.remove(position);
         //This performs delete animation
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void RestoreItem(Masternode mn, int position){
        mValues.add(position, mn);
        //This performs delete animation
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

}
