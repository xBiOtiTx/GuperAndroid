package com.erstudio.guper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.erstudio.guper.R;

import java.util.List;

/**
 * Created by Евгений on 21.09.2015.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private List<String> mItems;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.message);
            // mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessageAdapter(List<String> items) {
        mItems = items;
    }

    public void  add(String item) {
//        mItems.add(0,item);
//        notifyItemInserted(0);

        mItems.add(item);
        notifyItemInserted(mItems.size()-1);

        //notifyDataSetChanged();
        //mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //...

        //TextView message = (TextView) v.findViewById(R.id.message);
        //v.removeView(message);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.mTextView.setText(mItems.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItems.size();
    }
}