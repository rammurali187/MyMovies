package com.example.mymovies.utility;

import com.example.mymovies.model.Movie;
import com.example.mymovies.model.Review;
import com.example.mymovies.model.Video;


import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


public interface MoviesApiService {
    @GET("/discover/movie")
    void getPopularMovies(@Query("sort_by") String sortBy, @Query("page") int page, Callback<Movie.MovieResult> cb);

    @GET("/movie/{id}/videos")
    void getVideos(@Path("id") int movieId, Callback<Video.VideoResult> cb);

    @GET("/movie/{id}/reviews")
    void getReviews(@Path("id") int movieId, Callback<Review.ReviewResult> cb);




}
