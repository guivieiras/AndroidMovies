package br.guilherme.androidmovies.main;

import android.media.Image;
import android.util.Log;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

import br.guilherme.androidmovies.base.BasePresenter;
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
}
