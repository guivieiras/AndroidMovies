package br.guilherme.androidmovies.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.guilherme.androidmovies.Helpers;
import br.guilherme.androidmovies.R;
import br.guilherme.androidmovies.base.BaseActivity;
import br.guilherme.androidmovies.data.InternalStorage;
import br.guilherme.androidmovies.moviedb.ApiFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;



public class MainActivity extends BaseActivity<MainPresenter> {

    @BindView(R.id.movieList)
    RecyclerView movieList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.spin_kit)
    SpinKitView spinKit;

    private boolean isViewWithCatalog;

    private MovieListAdapter mla;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new MainPresenter(this);

        setSupportActionBar(toolbar);

        updateListLayoutManager(getResources().getConfiguration());

        List<MovieDb> movies = presenter.loadCachedMovies();

        mla = new MovieListAdapter(movies, isViewWithCatalog, this, item ->
        {
            AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
            View v = getLayoutInflater().inflate(R.layout.dialog_movie_details, null);
            TextView title  = (TextView) v.findViewById(R.id.text_title);
            TextView release_date  = (TextView) v.findViewById(R.id.text_release_date);
            TextView overview  = (TextView) v.findViewById(R.id.text_overview);
            TextView genre  = (TextView) v.findViewById(R.id.text_genre);
            TextView popularity  = (TextView) v.findViewById(R.id.text_popularity);
            TextView vote_count  = (TextView) v.findViewById(R.id.text_vote_count);
            TextView vote_average  = (TextView) v.findViewById(R.id.text_vote_average);
            TextView adult  = (TextView) v.findViewById(R.id.text_adult);

            title.setText(item.getTitle());
            release_date.setText(Helpers.getFormatedDate(item.getReleaseDate()));
            overview.setText(item.getOverview());

            //Removido pois API não carrega os gêneros dos filmes
            //List<String> a = item.getGenres().stream().map(x->x.getName()).collect(Collectors.toList());
            //genre.setText(String.join(", ", a));

            popularity.setText(String.valueOf(item.getPopularity()));
            vote_count.setText(String.valueOf(item.getVoteCount()));
            vote_average.setText(String.valueOf(item.getVoteAverage()));
            adult.setText(item.isAdult() ? "Sim" : "Não");
            b.setView(v);
            b.show();

        });
        movieList.setAdapter(mla);
    }

    @Override
    public void onPause(){
        super.onPause();
        presenter.cacheMovies(mla.getItems());
    }

    public void receiveMovieList(MovieResultsPage upcoming)
    {
        hideLoading();
        mla.update(upcoming.getResults());
    }


    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case R.id.action_refresh_movies:
                mla.clear();
                showLoading();
                presenter.updateMovieList();
                return true;
            case R.id.action_change_view_mode:
                isViewWithCatalog = !isViewWithCatalog;
                supportInvalidateOptionsMenu();

                updateListLayoutManager(getResources().getConfiguration());
                mla.alternateLayoutMode();
                movieList.setAdapter(mla);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateListLayoutManager(Configuration config){
        int orientation = config.orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            movieList.setLayoutManager(isViewWithCatalog ? new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL):new LinearLayoutManager(this) );
        else
            movieList.setLayoutManager(isViewWithCatalog ? new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL):new LinearLayoutManager(this) );

    }

    public void showLoading(){
        spinKit.setVisibility(View.VISIBLE);
    }
    public void hideLoading(){
        spinKit.setVisibility(View.GONE);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        updateListLayoutManager(newConfig);
    }



}
