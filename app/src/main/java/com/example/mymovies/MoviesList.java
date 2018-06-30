package com.example.mymovies;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mymovies.adapter.MoviesAdapter;
import com.example.mymovies.model.Movie;
import com.example.mymovies.utility.MoviesApiService;
import com.example.mymovies.utility.MyApplication;
import com.example.mymovies.utility.VerticalLineDecorator;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MoviesList extends AppCompatActivity {

    protected int mSelectedPosition = -1;
    RecyclerView recyclerView;
    List<Movie> movies;
    MoviesAdapter adapter;
    private String movie_type;
    private String title;
    Context context;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_list);
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            if (savedInstanceState != null) {
                movies = savedInstanceState.getParcelableArrayList("state_movies");
                mSelectedPosition = savedInstanceState.getInt("state_selected_position", -1);
                movie_type = savedInstanceState.getString("state_selected_type");
                title = savedInstanceState.getString("state_selected_title");
            } else {
                movies = new ArrayList<Movie>();
                mSelectedPosition = -1;
                getMovies(movie_type, 1);
                movie_type = "popularity.desc";
                title = "Most Popular";
            }

            getSupportActionBar().setTitle(title);
            context = this;
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

            Log.d("Movises List",movies.toString());
            adapter = new MoviesAdapter(this, movies);
            recyclerView.setAdapter(adapter);
            adapter.setLoadMoreListener(new MoviesAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            mSelectedPosition = movies.size() - 1;
                            int index = movies.size() - 1;
                            getMoreMovies(movie_type, mSelectedPosition);
                        }
                    });

                }
            });
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new VerticalLineDecorator(2));
            recyclerView.setAdapter(adapter);
            final int columns = getResources().getInteger(R.integer.movies_columns);
            Log.d("Noof list Columns",String.valueOf(columns));
            recyclerView.setLayoutManager(new GridLayoutManager(MoviesList.this, columns));
            Log.d("Selected Position",String.valueOf(mSelectedPosition));
            if (mSelectedPosition != -1) recyclerView.scrollToPosition(mSelectedPosition);

        }catch(Exception ex)
        {
            Log.e("List Oncreate",ex.getMessage().toString());
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("state_movies", new ArrayList<Movie>(movies));
        outState.putInt("state_selected_position", mSelectedPosition);
        outState.putString("state_selected_type", movie_type);
        outState.putString("state_selected_title", title);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
    }

    private void getMovies(String sort,int page) {
        try {
            Log.d("Sort and Page",sort +" and "+String.valueOf(page));
            movies.clear();
            MoviesApiService service = MyApplication.getInstance().getResadapter().create(MoviesApiService.class);
            service.getPopularMovies(sort, page, new Callback<Movie.MovieResult>() {
                @Override
                public void success(Movie.MovieResult movieResult, Response response) {
                    Log.d("Movies List",movieResult.getResults().toString());
                    movies.addAll(movieResult.getResults());
                    adapter.notifyDataChanged();

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
        }catch (Exception ex)
        {
            Log.e("get Movies ex",ex.getMessage().toString());
        }
    }

    private void getMoreMovies(String sort,int page) {
        try {
            Log.d("Sort and Page",sort +" and "+String.valueOf(page));
            movies.add(new Movie("load"));
            adapter.notifyItemInserted(movies.size() - 1);
            MoviesApiService service = MyApplication.getInstance().getResadapter().create(MoviesApiService.class);
            service.getPopularMovies(sort, page, new Callback<Movie.MovieResult>() {
                @Override
                public void success(Movie.MovieResult movieResult, Response response) {
                    movies.remove(movies.size() - 1);
                    Log.d("Movies List",movieResult.getResults().toString());
                    if (movieResult.getResults().size() > 0) {
                        movies.addAll(movieResult.getResults());
                    } else {
                        adapter.setMoreDataAvailable(false);
                        Toast.makeText(context, "No More Data Available", Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
        }catch (Exception ex)
        {
            Log.e("get More Movies ex",ex.getMessage().toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_mostpopular) {
            movie_type = "popularity.desc";
            getMovies(movie_type,1);
            title  = "Most Popular";
            toolbar.setTitle(title);
            return true;
        }else if(id == R.id.action_highestrated)
        {
            movie_type = "vote_average.desc";
            getMovies(movie_type,1);
            title  =  "Highest Rated";
            toolbar.setTitle(title);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
