package br.guilherme.androidmovies.main;

import android.app.AlertDialog;
import android.app.Dialog;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import br.guilherme.androidmovies.Helpers;
import br.guilherme.androidmovies.R;
import br.guilherme.androidmovies.base.BaseActivity;
import br.guilherme.androidmovies.moviedb.ApiFactory;
import butterknife.BindView;
import butterknife.ButterKnife;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;



public class MainActivity extends BaseActivity<MainPresenter> implements NavigationView.OnNavigationItemSelectedListener
{

    @BindView(R.id.movieList)
    RecyclerView movieList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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

        movieList.setLayoutManager(isViewWithCatalog ? new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL):new LinearLayoutManager(this) );
        mla = new MovieListAdapter(new ArrayList<>(), isViewWithCatalog, this, item ->
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

    public void receiveMovieList(MovieResultsPage upcoming)
    {
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_refresh_movies:
                movieList.setBackground(getDrawable(R.drawable.ic_rolling));
                presenter.updateMovieList();
                return true;
            case R.id.action_change_view_mode:
                isViewWithCatalog = !isViewWithCatalog;
                supportInvalidateOptionsMenu();

                movieList.setLayoutManager(isViewWithCatalog ? new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL):new LinearLayoutManager(this) );
                mla.alternateLayoutMode();
                movieList.setAdapter(mla);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
            // Handle the camera action
        } else if (id == R.id.nav_gallery)
        {

        } else if (id == R.id.nav_slideshow)
        {

        } else if (id == R.id.nav_manage)
        {

        } else if (id == R.id.nav_share)
        {

        } else if (id == R.id.nav_send)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
