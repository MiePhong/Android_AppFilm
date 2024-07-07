package doan.cuoiki.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import doan.cuoiki.R;
import doan.cuoiki.adapters.CastAdapter;
import doan.cuoiki.models.Cast;

public class DetailActivity extends AppCompatActivity {
    ImageView MovieThumbnailImg,MovieCoverImg;
    TextView tv_title,tv_description,textView2;
    FloatingActionButton play_fab;
    Button review;

    ImageButton imageButton;

    RecyclerView RvCast;

    CastAdapter castAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        addControls();
        addEvents();

    }

    public void addControls(){

        imageButton = (ImageButton) findViewById(R.id.backbt);

        RvCast = (RecyclerView) findViewById(R.id.rv_cast);

        play_fab = (FloatingActionButton) findViewById(R.id.play_fab);


        MovieThumbnailImg = (ImageView) findViewById(R.id.detail_movie_img);


        MovieCoverImg = (ImageView) findViewById(R.id.detail_movie_cover);

        tv_title = (TextView) findViewById(R.id.detail_movie_title);
        textView2 = (TextView) findViewById(R.id.textView2);
        tv_description = (TextView) findViewById(R.id.detail_movie_desc);

        review = (Button) findViewById(R.id.btn_review);


    }


    public void addEvents(){
        String movieText = getIntent().getExtras().getString("title");
        String TextView2 = getIntent().getExtras().getString("title");
        String description = getIntent().getExtras().getString("description");
//        int imageResourced = getIntent().getExtras().getInt("imgURL");
//        int imageCover = getIntent().getExtras().getInt("imgCover");
//        MovieThumbnailImg.setImageResource(imageResourced);
        Glide.with(this).load("https://image.tmdb.org/t/p/w500"+getIntent().getExtras().getString("imgURL")).into(MovieThumbnailImg);

        Glide.with(this).load("https://image.tmdb.org/t/p/w500"+getIntent().getExtras().getString("imgCover")).into(MovieCoverImg);

        tv_title.setText(movieText);
        textView2.setText(TextView2);
        tv_description.setText(description);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(movieText);
            actionBar.setTitle(TextView2);
        }

        play_fab.setAnimation(AnimationUtils.loadAnimation(this,R.anim.scale_animation));
        play_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this,MoviePlayerActivity.class);
                intent.putExtra("idMovie",getIntent().getExtras().getString("id"));
                startActivity(intent);
            }
        });


        getCast("https://api.themoviedb.org/3/movie/"+getIntent().getExtras().getString("id")+"/credits?api_key=23f1e4dc64dcdfe853dd2aee44cf082d");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, Comments.class);
                intent.putExtra("id", getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });


    }

    public void getCast(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(DetailActivity.this);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseJsonDataCast(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void parseJsonDataCast(String response) throws JSONException
    {
        List<Cast> mdata = new ArrayList<>();
        JSONObject dataObject = new JSONObject(response);
        JSONArray castObject = dataObject.getJSONArray("cast");
        for(int i=0;i<castObject.length(); i++)
        {
            JSONObject jsonObject = castObject.getJSONObject(i);
            String id = jsonObject.getString("id");
            String name = jsonObject.getString("name");
            String profilePath = jsonObject.getString("profile_path");
            Cast cast = new Cast(id,name,profilePath);
            mdata.add(cast);
        }

        castAdapter1 = new CastAdapter( this,mdata);
        RvCast.setAdapter(castAdapter1);
        RvCast.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}