package doan.cuoiki.adapters;

import android.widget.ImageView;

import doan.cuoiki.models.Movie;

public interface MovieItemClickListener {
    void onMovieClick(Movie movie, ImageView movieImageView);
}
