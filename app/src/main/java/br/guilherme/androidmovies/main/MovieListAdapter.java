package br.guilherme.androidmovies.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.guilherme.androidmovies.Helpers;
import br.guilherme.androidmovies.R;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder>
{

    public List<MovieDb> getItems() {
        return dataSet;
    }

    public interface OnItemClickListener {
        void onItemClick(MovieDb item);
    }

    private List<MovieDb> dataSet;

    private boolean isCatalogStyle;

    private OnItemClickListener listener;

    private DisplayImageOptions options;

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView title, releaseDate, popularity, vote_average;
        private ImageView poster;

        public MyViewHolder(View view)
        {
            super(view);
            poster = (ImageView) view.findViewById(R.id.image_poster);
            title = (TextView) view.findViewById(R.id.text_title);
            releaseDate = (TextView) view.findViewById(R.id.text_release_date);
            popularity  = (TextView) view.findViewById(R.id.text_popularity);
            vote_average  = (TextView) view.findViewById(R.id.text_vote_average);
        }

    }

    public MovieListAdapter(List<MovieDb> dataSet, boolean isCatalogStyle, Context context, OnItemClickListener listener)
    {
        this.listener = listener;

        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_cut_board)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

        this.isCatalogStyle = isCatalogStyle;
        this.dataSet = dataSet;
    }

    @Override
    public MovieListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(isCatalogStyle ? R.layout.card_movie_catalog : R.layout.card_movie_vertical, parent, false);
        return new MyViewHolder(v);
    }

    public void alternateLayoutMode()
    {
        isCatalogStyle = !isCatalogStyle;
        notifyDataSetChanged();
    }

    public void update(List<MovieDb> results)
    {
        this.dataSet = results;
        notifyDataSetChanged();
    }
    public void clear() {
        this.dataSet = new ArrayList<>();
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {

        final MovieDb movie = dataSet.get(position);
        if (holder.title != null)
            holder.title.setText(movie.getTitle());

        if (holder.popularity != null)
            holder.popularity.setText(String.valueOf(movie.getPopularity()));

        if (holder.vote_average != null)
            holder.vote_average.setText(String.valueOf(movie.getVoteAverage()));

        if (holder.releaseDate != null)
            holder.releaseDate.setText(Helpers.getFormatedDate(movie.getReleaseDate()));


        holder.itemView.setOnClickListener(v -> listener.onItemClick(movie));

        String baseUrl = movie.getPosterPath() == null ? null : "https://image.tmdb.org/t/p/w185_and_h278_bestv2" + movie.getPosterPath();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(baseUrl, holder.poster, options);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return dataSet.size();
    }
//    @Override
//    public int getItemViewType(int position) {
//        return 10;
//    }

//    @Override
//    public void onViewAttachedToWindow(final RecyclerView.ViewHolder holder)
//        super.onViewAttachedToWindow(holder);
//    }
//
//    @Override
//    public void onViewDetachedFromWindow(final RecyclerView.ViewHolder holder) {
//            holder.setIsRecyclable(true);
//
//        super.onViewDetachedFromWindow(holder);
//    }
}