package cz.muni.fi.pv256.movio2.fk410022;

import android.graphics.drawable.Drawable;
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
    private Movie[] mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.view_item_text);
            imageView = (ImageView) itemView.findViewById(R.id.view_item_image);
        }
    }

    MyAdapter(Movie[] myDataset) {
        mDataset = myDataset;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(mDataset[position].getName());
        holder.imageView.setImageResource(mDataset[position].getPictureId());
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}


