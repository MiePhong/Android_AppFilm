package doan.cuoiki.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import doan.cuoiki.R;
import doan.cuoiki.models.Movie;

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchAdapter.MyViewHolder> {

    Context context;
    List<Movie> mData;

    MovieItemClickListener movieItemClickListener;

    public MovieSearchAdapter(Context context, List<Movie> mData, MovieItemClickListener listener) {
        this.context = context;
        this.mData = mData;
        movieItemClickListener = listener;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.movie_item_in_search,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvName.setText(mData.get(position).getTitle());
        holder.tvDescription.setText(mData.get(position).getDescription());
        holder.tvDateTime.setText(mData.get(position).getDateTime());
        holder.tvVoteAverage.setText(mData.get(position).getVoteAverage());
        Glide.with(context).load("https://image.tmdb.org/t/p/w500"+mData.get(position).getImgLink()).into(holder.imgMovie);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        TextView tvDescription;
        TextView tvDateTime;
        TextView tvVoteAverage;

        ImageView imgMovie;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvNameinSearch);
            tvDescription = itemView.findViewById(R.id.tvDescriptioninSearch);
            tvDateTime = itemView.findViewById(R.id.tvDateTimeinSearch);
            tvVoteAverage = itemView.findViewById(R.id.tvVoteAverageinSearch);
            imgMovie = itemView.findViewById(R.id.imginSearch);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    movieItemClickListener.onMovieClick(mData.get(getAdapterPosition()),imgMovie);
                }
            });
        }
    }
}
