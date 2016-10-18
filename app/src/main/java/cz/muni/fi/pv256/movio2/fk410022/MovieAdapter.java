package cz.muni.fi.pv256.movio2.fk410022;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by suomiy on 10/4/16.
 */

class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private final OnItemClickListener mListener;
    private Film[] mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;
        RelativeLayout detail;
        TextView rating;
        private final Context context;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            title = (TextView) itemView.findViewById(R.id.view_item_title);
            imageView = (ImageView) itemView.findViewById(R.id.view_item_image);
            detail = (RelativeLayout) itemView.findViewById(R.id.view_item_detail);
            rating = (TextView) itemView.findViewById(R.id.view_item_rating);
        }
    }

    MovieAdapter(Film[] myDataset, OnItemClickListener listener) {
        mDataset = myDataset;
        mListener = listener;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_item, parent, false);
        Log.i(TAG, "create viev holder:");
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Film film = mDataset[position];
        holder.title.setText(film.getTitle());
        holder.rating.setText(String.format("%.1f", film.getPopularity()));
        holder.imageView.setImageResource(film.getCoverPath());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClick(film);
            }
        });

        Bitmap myBitmap = BitmapFactory.decodeResource(holder.context.getResources(), film.getCoverPath());
        if (myBitmap != null && !myBitmap.isRecycled()) {

            Palette palette = Palette.from(myBitmap).generate();
            int muted = palette.getMutedColor(holder.context.getResources().getColor(R.color.palette_default));
            holder.detail.setBackgroundColor(Utils.addAlphaToColor(muted, 0.5f));
        }

        Log.i(TAG, "bind viev holder:" + film.getTitle());

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}


