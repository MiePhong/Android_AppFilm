package doan.cuoiki.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import doan.cuoiki.R;
import doan.cuoiki.models.Cast;
import doan.cuoiki.models.Comment;
import doan.cuoiki.models.Movie;
import doan.cuoiki.ui.DetailActivity;
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    Context context;
    List<Comment> cData;

    public CommentAdapter(Context context, List<Comment> cData) {
        this.cData = cData;
        this.context = context;
    }

//    @NonNull
//    @Override
//    public CommentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.review, parent, false);
//        return new CommentAdapter.MyViewHolder(view);
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, int position) {
        holder.name.setText(cData.get(position).getUsername());
        holder.comment.setText(cData.get(position).getContent());
        holder.date.setText(cData.get(position).getDate());
        if(!cData.get(position).getAvatarUrl().equals("null"))
        {
            Glide.with(context).load("https://image.tmdb.org/t/p/w500" + cData.get(position).getAvatarUrl()).circleCrop().into(holder.imgUser);
        }
    }

    @Override
    public int getItemCount() {
        return cData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView comment;
        TextView date;
        ImageView imgUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.review);
            date = itemView.findViewById(R.id.date);
            imgUser = itemView.findViewById(R.id.imgUser);
        }
    }
}
