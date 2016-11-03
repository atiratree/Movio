package cz.muni.fi.pv256.movio2.fk410022.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.util.Utils;
import cz.muni.fi.pv256.movio2.fk410022.drawable.GradientStar;
import cz.muni.fi.pv256.movio2.fk410022.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.ui.OnItemClickListener;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private final OnItemClickListener mListener;
    private Film[] mDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView imageView;
        ImageView star;
        RelativeLayout detail;
        TextView rating;
        private final Context context;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();

            title = (TextView) itemView.findViewById(R.id.view_item_title);
            imageView = (ImageView) itemView.findViewById(R.id.view_item_image);
            star = (ImageView) itemView.findViewById(R.id.view_item_star);
            detail = (RelativeLayout) itemView.findViewById(R.id.view_item_detail);
            rating = (TextView) itemView.findViewById(R.id.view_item_rating);
        }
    }

    public MovieAdapter(Film[] myDataset, OnItemClickListener listener) {
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
        Context context = holder.context;
        final Film film = mDataset[position];

        holder.title.setText(film.getTitle());
        holder.rating.setText(String.format("%.1f", film.getPopularity()));
        holder.imageView.setImageResource(film.getCoverPath());
        holder.star.setImageDrawable(new GradientStar(ContextCompat.getColor(context, R.color.star_start_gradient),
                ContextCompat.getColor(context, R.color.star_end_gradient),
                ContextCompat.getColor(context, R.color.star_line_start_gradient),
                ContextCompat.getColor(context, R.color.star_line_end_gradient)));

        holder.imageView.setOnClickListener(view -> mListener.onItemClick(film));

        Bitmap myBitmap = BitmapFactory.decodeResource(context.getResources(), film.getCoverPath());
        if (myBitmap != null && !myBitmap.isRecycled()) {
            Palette palette = Palette.from(myBitmap).generate();
            int muted = palette.getMutedColor(ContextCompat.getColor(context, R.color.palette_default));
            Drawable background = ContextCompat.getDrawable(context, R.drawable.fiml_list_detail);
            ((GradientDrawable) background).setColors(new int[]{Utils.addAlphaToColor(muted, 0f),
                    Utils.addAlphaToColor(muted, 0.5f)});
            holder.detail.setBackground(background);
        }

        Log.i(TAG, "bind view holder:" + film.getTitle());
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}


