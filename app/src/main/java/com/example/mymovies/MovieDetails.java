package com.example.mymovies;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.mymovies.model.Genre;
import com.example.mymovies.model.Movie;
import com.example.mymovies.model.Review;
import com.example.mymovies.model.Video;
import com.example.mymovies.utility.ExpandableTextView;
import com.example.mymovies.utility.Lists;
import com.example.mymovies.utility.MoviesApiService;
import com.example.mymovies.utility.MyApplication;
import com.example.mymovies.utility.UiUtility;
import com.example.mymovies.utility.ViewPagers;
import com.example.mymovies.utility.VolleyRequestQueue;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MovieDetails extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "movie";

    private Movie mMovie;
    ImageView backdrop;
    ImageView poster;
    TextView title;
    ExpandableTextView description;
    TextView txtgenre;
    TextView txtrelaesed;
    ImageView img_relaesed;
    RatingBar movie_rating;
    ViewPagers viewpager;
    LinearLayout mVideosGroup;
    LinearLayout mReviewsGroup;
    private Video mTrailer;
    VolleyRequestQueue requestqueue;
    private ImageLoader mImageLoader;
    MyApplication singltonclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.movie_details);
        try {
            if (getIntent().hasExtra(EXTRA_MOVIE)) {
                mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
            } else {
                throw new IllegalArgumentException("Detail activity must receive a movie parcelable");
            }

            singltonclass = MyApplication.getInstance();
            LinearLayout mSliderDots = (LinearLayout) findViewById(R.id.SliderDots);
            ViewPager pager_container = (ViewPager) findViewById(R.id.pager_container);
            viewpager = new ViewPagers(MovieDetails.this, pager_container, mSliderDots);
            viewpager.viewpagerinit(new String[]{UiUtility.getDisplayReleaseDate(mMovie.getReleaseDate()), String.valueOf(mMovie.getVoteCount()), mMovie.getLanguage()}, new String[]{mMovie.getAdult()});
            String strgenre = "";
            List<Integer> genreid = mMovie.getGenreIds();
            List<Genre> genrelist = UiUtility.genrelist();

            for (Genre item : genrelist) {
                if (genreid.contains(item.getId())) {
                    strgenre += item.getName() + ",";
                } else {

                }
            }
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
            toolbarLayout.setTitle(mMovie.getTitle());
            mVideosGroup = (LinearLayout) findViewById(R.id.movie_videos_container);
            mReviewsGroup = (LinearLayout) findViewById(R.id.reviews_container);
            backdrop = (ImageView) findViewById(R.id.backdrop);
            title = (TextView) findViewById(R.id.movie_title);
            description = (ExpandableTextView) findViewById(R.id.movie_overview);
            poster = (ImageView) findViewById(R.id.movie_poster);
            txtgenre = (TextView) findViewById(R.id.movie_genre);
            txtrelaesed = (TextView) findViewById(R.id.txt_release);
            img_relaesed = (ImageView) findViewById(R.id.img_release);
            movie_rating = (RatingBar) findViewById(R.id.movie_rating);
            title.setText(mMovie.getTitle());
            txtgenre.setText(strgenre.length() > 0 ? strgenre.substring(0, strgenre.length() - 1) : strgenre);
            float rating = (float) mMovie.getVoteAverage();
            movie_rating.setRating(rating / 2);
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd");

            Date strDate = null;
            String isrelaesed = "";
            try {
                strDate = dateFormat.parse(mMovie.getReleaseDate());
                if (new Date().before(strDate)) {
                    img_relaesed.setBackground(getResources().getDrawable(android.R.drawable.presence_video_busy));
                    txtrelaesed.setTextColor(getResources().getColor(R.color.notreleased));
                    isrelaesed = "Not Released";
                } else {
                    isrelaesed = "Released";
                    img_relaesed.setBackground(getResources().getDrawable(android.R.drawable.presence_video_online));
                    txtrelaesed.setTextColor(getResources().getColor(R.color.released));
                }

                txtrelaesed.setText(isrelaesed);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            description.setText(mMovie.getDescription());
            Picasso.with(this)
                    .load(mMovie.getPoster())
                    .into(poster);
            Picasso.with(this)
                    .load(mMovie.getBackdrop())
                    .into(backdrop);

            getvideos(Integer.parseInt(String.valueOf(mMovie.getId())));
            getReviews(Integer.parseInt(String.valueOf(mMovie.getId())));
        }catch(Exception ex)
        {
            Log.e("Details OnCreate",ex.getMessage().toString());
        }
    }


    private void getvideos(int movieid) {
      try {
          MoviesApiService service = singltonclass.getResadapter().create(MoviesApiService.class);
          service.getVideos(movieid, new Callback<Video.VideoResult>() {
              @Override
              public void success(Video.VideoResult movieResult, Response response) {
                  onVideosLoaded(movieResult.getResults());
              }

              @Override
              public void failure(RetrofitError error) {
                  error.printStackTrace();
              }
          });
      }catch (Exception ex)
      {
          Log.e("get Videos ex",ex.getMessage().toString());
      }
    }

    private void getReviews(int movieid) {
        try{
        MoviesApiService service = singltonclass.getResadapter().create(MoviesApiService.class);
        service.getReviews(movieid,new Callback<Review.ReviewResult>() {
            @Override
            public void success(Review.ReviewResult reviewResult, Response response) {
                onReviewsLoaded(reviewResult.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
        }catch (Exception ex)
        {
            Log.e("get Reviews ex",ex.getMessage().toString());
        }
    }

    private void onVideosLoaded(List<Video> videos) {

        try {
            for (int i = mVideosGroup.getChildCount() - 1; i >= 2; i--) {
                mVideosGroup.removeViewAt(i);
            }
            boolean hasVideos = false;
            final LayoutInflater inflater = LayoutInflater.from(MovieDetails.this);
            for (final Video video : videos) {
                final View videoView = inflater.inflate(R.layout.item_video, mVideosGroup, false);
                final TextView videoNameView = (TextView)videoView.findViewById(R.id.video_name);
                final NetworkImageView mNetworkImageView= (NetworkImageView)videoView.findViewById(R.id.video_image);

                mImageLoader = VolleyRequestQueue.getInstance(this.getApplicationContext())
                        .getImageLoader();

                final String url = "http://img.youtube.com/vi/"+video.getKey()+"/0.jpg";
                mImageLoader.get(url, ImageLoader.getImageListener(mNetworkImageView,
                        R.drawable.ic_play_circle, android.R.drawable
                                .ic_dialog_alert));
                mNetworkImageView.setImageUrl(url,mImageLoader);

                videoNameView.setText(video.getSite() + ": " + video.getName());
                videoView.setTag(video);
                videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (video.getSite().equals(Video.SITE_YOUTUBE))
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
                        else
                            Log.e("Unsupport video format","");
                    }
                });

                mVideosGroup.addView(videoView);
                hasVideos = true;
            }

            if (!Lists.isEmpty(videos)) {
                for (final Video video : videos)
                    if (video.getType().equals(Video.TYPE_TRAILER)) {
                        Log.d("Found trailer!","");
                        mTrailer = video;

                        backdrop.setTag(video);
                        backdrop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (video.getSite().equals(Video.SITE_YOUTUBE))
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
                                else
                                    Log.e("Unsupport video format","");
                            }
                        });
                        break;
                    }

            }

            backdrop.setClickable(mTrailer != null);
            // mPosterPlayImage.setVisibility(mTrailer != null ? View.VISIBLE : View.GONE);
            mVideosGroup.setVisibility(hasVideos ? View.VISIBLE : View.GONE);
        }catch (Exception ex)
        {
            Log.d("Video Loaded ex: " ,ex.getMessage().toString());
        }


    }

    private void onReviewsLoaded(List<Review> reviews) {
        try {
            for (int i = mReviewsGroup.getChildCount() - 1; i >= 2; i--) {
                mReviewsGroup.removeViewAt(i);
            }

            final LayoutInflater inflater = LayoutInflater.from(MovieDetails.this);
            boolean hasReviews = false;

            if (!Lists.isEmpty(reviews)) {
                for (Review review : reviews) {
                    if (TextUtils.isEmpty(review.getAuthor())) {
                        continue;
                    }

                    final View reviewView = inflater.inflate(R.layout.item_review_detail, mReviewsGroup, false);
                    final TextView reviewAuthorView = (TextView)reviewView.findViewById(R.id.review_author);
                    final TextView reviewContentView = (TextView)reviewView.findViewById(R.id.review_content);

                    reviewAuthorView.setText(review.getAuthor());
                    reviewContentView.setText(review.getContent());

                    mReviewsGroup.addView(reviewView);
                    hasReviews = true;
                }
            }

            mReviewsGroup.setVisibility(hasReviews ? View.VISIBLE : View.GONE);
        }catch (Exception ex)
        {
            Log.e("Review Loaded ex: " , ex.getMessage().toString());
        }

    }
}
