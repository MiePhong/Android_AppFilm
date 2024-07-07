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

import java.util.List;

import doan.cuoiki.R;
import doan.cuoiki.models.Cast;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.MyViewHolder> {

    Context context;
    List<Cast> mData;


    public CastAdapter(Context context, List<Cast> mData) {
        this.context = context;
        this.mData = mData;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_cast,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(mData.get(position).getName());
        Glide.with(context).load("https://image.tmdb.org/t/p/w500"+mData.get(position).getImg_link()).into(holder.img);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

//        TextView Tvtitle;
//
//        ImageView imagMovie;
        ImageView img;
        TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_cast);
            img = itemView.findViewById(R.id.img_cast);
        }
    }
}
