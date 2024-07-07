package doan.cuoiki.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import doan.cuoiki.R;
import doan.cuoiki.models.Slide;
import doan.cuoiki.ui.DetailActivity;
import doan.cuoiki.ui.MoviePlayerActivity;

public class SlidePagerAdapter extends PagerAdapter {

    Context mContext;
    List<Slide> mList;

    public SlidePagerAdapter(Context mContext, List<Slide> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout = inflater.inflate(R.layout.slide_item,null);
        ImageView slideImag = slideLayout.findViewById(R.id.slide_img);
        TextView slideText = slideLayout.findViewById(R.id.slide_title);
        Glide.with(mContext).load("https://image.tmdb.org/t/p/w500"+mList.get(position).getImage()).into(slideImag);
        slideText.setText(mList.get(position).getTitle());
        FloatingActionButton floatingActionButton = slideLayout.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MoviePlayerActivity.class);
                intent.putExtra("idMovie",mList.get(position).getId());
                mContext.startActivity(intent);
            }
        });
        container.addView(slideLayout);
        return slideLayout;

    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
