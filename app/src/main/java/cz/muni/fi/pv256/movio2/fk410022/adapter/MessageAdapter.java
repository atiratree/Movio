package cz.muni.fi.pv256.movio2.fk410022.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.muni.fi.pv256.movio2.fk410022.R;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private static final String TAG = MessageAdapter.class.getSimpleName();

    private String[] mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        private final Context context;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            title = (TextView) itemView.findViewById(R.id.view_item_title);
        }
    }

    public MessageAdapter(String message) {
        mDataset = new String[]{message};
    }

    MessageAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                        int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_view_item, parent, false);
        Log.i(TAG, "create message_view_item viev holder:");
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final String message = mDataset[position];
        holder.title.setText(message);

        Log.i(TAG, "bind message_view_item viev holder:" + message);

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}


