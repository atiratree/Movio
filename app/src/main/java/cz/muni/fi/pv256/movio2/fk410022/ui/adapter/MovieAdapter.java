package cz.muni.fi.pv256.movio2.fk410022.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.model.store.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.ui.drawable.GradientStar;
import cz.muni.fi.pv256.movio2.fk410022.ui.listener.OnFilmClickListener;
import cz.muni.fi.pv256.movio2.fk410022.util.Utils;
import cz.muni.fi.pv256.movio2.fk410022.util.image.ImageHelper;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();

    private final OnFilmClickListener mListener;
    private Film[] mDataset;
    private FilmListType mType;
    private Context context;

    private boolean empty = true;
    private String emptyMessage;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public MovieAdapter(List<Film> myDataset, FilmListType type, OnFilmClickListener listener, Context context) {
        this(myDataset.toArray(new Film[myDataset.size()]), type, listener, context);
    }

    public MovieAdapter(String errorMessage, FilmListType type, Context context) {
        this(new Film[]{}, type, null, context);
        emptyMessage = errorMessage;
    }

    public MovieAdapter(Film[] myDataset, FilmListType type, OnFilmClickListener listener, Context context) {
        mType = type;
        mDataset = myDataset;
        mListener = listener;
        this.context = context.getApplicationContext();

        empty = myDataset == null || myDataset.length == 0;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(empty ? R.layout.message_view_item : R.layout.view_item, parent, false);
        return empty ? new MessageViewHolder(v) : new FilmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (empty) {
            onMessageBind((MessageViewHolder) holder, emptyMessage);
        } else {
            onFilmBind((FilmViewHolder) holder, mDataset[position]);
        }
    }

    private void onMessageBind(MessageViewHolder holder, String message) {
        if (emptyMessage == null) {
            emptyMessage = context.getString(R.string.no_data);
        }

        holder.message.setText(message);
    }

    private void onFilmBind(final FilmViewHolder holder, final Film film) {

        holder.imageView.setImageDrawable(null); // clear
        holder.detail.setBackgroundColor(Utils.addAlphaToColor(ContextCompat
                .getColor(context, R.color.palette_default), 0.5f));

        ImageHelper.displayPoster(film, new ImageViewAware(holder.imageView) {
            @Override
            protected void setImageBitmapInto(Bitmap bitmap, View view) {
                super.setImageBitmapInto(bitmap, view);
                Palette palette = Palette.from(bitmap).generate();
                int muted = palette.getMutedColor(ContextCompat.getColor(context, R.color.palette_default));
                Drawable background = ContextCompat.getDrawable(context, R.drawable.fiml_list_detail);
                ((GradientDrawable) background).setColors(new int[]{Utils.addAlphaToColor(muted, 0.3f),
                        Utils.addAlphaToColor(muted, 0.7f)});
                holder.detail.setBackground(background);
            }
        });

        holder.imageView.setOnClickListener(view -> mListener.onItemClick(film));
        holder.title.setText(film.getTitle());
        holder.rating.setText(String.format("%.1f", film.getRating()));
        holder.star.setImageDrawable(new GradientStar(ContextCompat.getColor(context, R.color.star_start_gradient),
                ContextCompat.getColor(context, R.color.star_end_gradient),
                ContextCompat.getColor(context, R.color.star_line_start_gradient),
                ContextCompat.getColor(context, R.color.star_line_end_gradient)));
    }

    @Override
    public int getItemCount() {
        return empty ? 1 : mDataset.length; // 1 for message
    }

    private static class MessageViewHolder extends ViewHolder {
        TextView message;

        MessageViewHolder(View itemView) {
            super(itemView);

            message = (TextView) itemView.findViewById(R.id.view_item_message);
        }
    }

    private static class FilmViewHolder extends ViewHolder {
        TextView title;
        ImageView imageView;
        ImageView star;
        RelativeLayout detail;
        TextView rating;
        View line;

        FilmViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.view_item_title);
            imageView = (ImageView) itemView.findViewById(R.id.view_item_image);
            star = (ImageView) itemView.findViewById(R.id.view_item_star);
            detail = (RelativeLayout) itemView.findViewById(R.id.view_item_detail);
            rating = (TextView) itemView.findViewById(R.id.view_item_rating);
            line = itemView.findViewById(R.id.line);
        }
    }
}

