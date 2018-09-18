package br.guilherme.androidmovies.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import br.guilherme.androidmovies.R;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieListAdapter2 extends ArrayAdapter<MovieDb>
{

    private boolean isCatalogStyle;

    public void update(List<MovieDb> results)
    {
        super.clear();
        super.addAll(results);
        super.notifyDataSetChanged();
    }
    private DisplayImageOptions options;


    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieListAdapter2(List<MovieDb> dataSet, boolean isCatalogStyle, Context context)
    {
        super(context, R.layout.card_movie_catalog, dataSet);


        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_cut_board)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();

        this.isCatalogStyle = isCatalogStyle;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieDb user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(isCatalogStyle ? R.layout.card_movie_catalog : R.layout.card_movie_catalog, parent,false);
        }
        ImageView poster = (ImageView) convertView.findViewById(R.id.image_poster);
        TextView title = (TextView) convertView.findViewById(R.id.text_title);


        MovieDb movie = getItem(position);
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        title.setText(movie.getTitle());

        String baseUrl = movie.getPosterPath() == null ? null : "https://image.tmdb.org/t/p/w185_and_h278_bestv2" + movie.getPosterPath();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(baseUrl, poster, options);


        return convertView;
    }

    public void alternateLayoutMode()
    {
        isCatalogStyle = !isCatalogStyle;
        notifyDataSetChanged();
    }
}