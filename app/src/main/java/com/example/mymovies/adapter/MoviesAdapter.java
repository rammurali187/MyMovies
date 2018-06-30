package com.example.mymovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mymovies.MovieDetails;
import com.example.mymovies.R;
import com.example.mymovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    static Context context;
    List<Movie> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_movie, parent, false);
        final MovieHolder viewHolder = new MovieHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Intent intent = new Intent(context, MovieDetails.class);
                intent.putExtra(MovieDetails.EXTRA_MOVIE, movies.get(position));
                context.startActivity(intent);
            }
        });
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

            ((MovieHolder)holder).bindData(movies.get(position));


    }

    @Override
    public int getItemViewType(int position) {

        return 0;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }



    static class MovieHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView movie_title;
        public TextView movie_rating;
        public MovieHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            movie_title = (TextView) itemView.findViewById(R.id.movie_title);
            movie_rating = (TextView) itemView.findViewById(R.id.movie_rating);
        }

        void bindData(Movie movie){
            Picasso.with(context)
                    .load(movie.getPoster())
                    .placeholder(R.color.colorAccent)
                    .into(imageView);
            movie_title.setText(movie.getTitle());
            movie_rating.setText(movie.getVoteAverage()+"/10");
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }


    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }
}
