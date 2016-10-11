package cz.muni.fi.pv256.movio2.fk410022;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by suomiy on 10/4/16.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private final OnItemClickListener mListener;
    private Film[] mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        private final Context context;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            textView = (TextView) itemView.findViewById(R.id.view_item_text);
            imageView = (ImageView) itemView.findViewById(R.id.view_item_image);
        }
    }

    MyAdapter(Film[] myDataset, OnItemClickListener listener) {
        mDataset = myDataset;
        mListener = listener;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Film film = mDataset[position];
        holder.imageView.setImageResource(film.getCoverPath());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(film);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}


