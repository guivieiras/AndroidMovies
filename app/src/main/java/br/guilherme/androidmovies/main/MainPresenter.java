package br.guilherme.androidmovies.main;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import br.guilherme.androidmovies.base.BasePresenter;
import br.guilherme.androidmovies.data.InternalStorage;
import br.guilherme.androidmovies.moviedb.ApiFactory;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

public class MainPresenter extends BasePresenter<MainActivity>
{
    MainPresenter(MainActivity view)
    {
        super(view);
    }

    public void updateMovieList()
    {
        new Thread(() ->
                   {
                       TmdbMovies movies = ApiFactory.newInstance().getMovies();
                       final MovieResultsPage upcoming = movies.getUpcoming("pt-BR", 0, "BR");

                       view.runOnUiThread(() -> view.receiveMovieList(upcoming));

                   }).start();
    }

    public List<MovieDb> loadCachedMovies(){
        List<MovieDb> movies = new ArrayList<>();
        try
        {
            movies = (List<MovieDb>) InternalStorage.readObject(view,"movies");
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return movies;
    }

    public void cacheMovies(List<MovieDb> items) {
        try {
            InternalStorage.writeObject(view, "movies", items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
