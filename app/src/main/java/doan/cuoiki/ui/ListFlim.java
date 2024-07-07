package doan.cuoiki.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import doan.cuoiki.R;
import doan.cuoiki.models.Movie;
import doan.cuoiki.adapters.MovieAdapter;
import doan.cuoiki.adapters.MovieItemClickListener;

public class ListFlim extends AppCompatActivity implements MovieItemClickListener {

    RecyclerView lv_nowplaying, lv_popular, lv_toprated, lv_upcoming;
    Button btn_apply;
    LinearLayout linearLayout;
    ProgressBar progressBarinFilter;
    FrameLayout LayoutListMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_flim);

       BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.list_item);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            // Thay thế switch-case cũ bằng switch-case mới
            int itemId = item.getItemId();
            if (itemId == R.id.home_item) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                // Xử lý khi itemId là R.id.home_item
                return true;
            } else if (itemId == R.id.list_item) {
                // Xử lý khi itemId là R.id.list_item

                return true;
            } else if (itemId == R.id.search_bar) {
                // Xử lý khi itemId là R.id.search_bar
                startActivity(new Intent(getApplicationContext(), SearchFlim.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }else if (itemId == R.id.user) {
                // Xử lý khi itemId là R.id.search_bar
                startActivity(new Intent(getApplicationContext(),profile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else {
                return false;
            }


        });

        addControls();
        addEvents();
    }

    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        Intent intent = new Intent(this,DetailActivity.class);

        intent.putExtra("id",movie.getId());
        intent.putExtra("title",movie.getTitle());
        intent.putExtra("description",movie.getDescription());
        intent.putExtra("imgURL",movie.getImgLink());
        intent.putExtra("imgCover",movie.getImgCoverLink());


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ListFlim.this,movieImageView,"sharedName");
        startActivity(intent,options.toBundle());
    }




    public void addControls()
    {
        lv_nowplaying = findViewById(R.id.lv_nowplaying);
        lv_popular = findViewById(R.id.lv_popular);
        lv_toprated = findViewById(R.id.lv_toprated);
        lv_upcoming = findViewById(R.id.lv_upcoming);
        btn_apply = findViewById(R.id.btn_apply);
        linearLayout = findViewById(R.id.wrapper_btn_filter);
        progressBarinFilter = findViewById(R.id.progressBarinFilter);
        LayoutListMovies = findViewById(R.id.LayoutListMovies);
    }

    public void addEvents()
    {
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutListMovies.setVisibility(View.GONE);
                progressBarinFilter.setVisibility(View.VISIBLE);
                loadListMovies();
            }
        });

        getMovies("https://api.themoviedb.org/3/movie/popular?api_key=23f1e4dc64dcdfe853dd2aee44cf082d","popular",null);
        getMovies("https://api.themoviedb.org/3/movie/top_rated?api_key=23f1e4dc64dcdfe853dd2aee44cf082d","toprated",null);
        getMovies("https://api.themoviedb.org/3/movie/upcoming?api_key=23f1e4dc64dcdfe853dd2aee44cf082d","upcoming",null);
        getMovies("https://api.themoviedb.org/3/movie/now_playing?api_key=23f1e4dc64dcdfe853dd2aee44cf082d","nowplaying",null);
        getGenre("https://api.themoviedb.org/3/genre/movie/list?api_key=23f1e4dc64dcdfe853dd2aee44cf082d");
    }

    public void loadListMovies()
    {
        ArrayList<Integer> arrGenre = new ArrayList<>();
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            Button button = (Button) linearLayout.getChildAt(i);
            if(button.getBackground().getConstantState() == getResources().getDrawable(R.drawable.pressed_btn_filter).getConstantState())
            {
                arrGenre.add((int)button.getTag());
            }
        }
        getMovies("https://api.themoviedb.org/3/movie/popular?api_key=23f1e4dc64dcdfe853dd2aee44cf082d","popular",arrGenre);
        getMovies("https://api.themoviedb.org/3/movie/top_rated?api_key=23f1e4dc64dcdfe853dd2aee44cf082d","toprated",arrGenre);
        getMovies("https://api.themoviedb.org/3/movie/upcoming?api_key=23f1e4dc64dcdfe853dd2aee44cf082d","upcoming",arrGenre);
        getMovies("https://api.themoviedb.org/3/movie/now_playing?api_key=23f1e4dc64dcdfe853dd2aee44cf082d","nowplaying",arrGenre);
    }

    public void getGenre(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(ListFlim.this);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseJsonDataFilter(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListFlim.this, "Có lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void parseJsonDataFilter(String response) throws JSONException
    {
        JSONObject dataObject = new JSONObject(response);
        JSONArray genresArr = dataObject.getJSONArray("genres");
        for(int i=0;i<genresArr.length(); i++)
        {
            JSONObject jsonObject = genresArr.getJSONObject(i);
            int id = jsonObject.getInt("id");
            String name = jsonObject.getString("name");
            // Tạo đối tượng Button
            Button button = new Button(this);
            button.setTag(id);
            button.setText(name);
            button.setBackgroundResource(R.drawable.non_pressed_btn_filter);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 20, 0);

            linearLayout.addView(button, layoutParams);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Drawable currentDrawable =  ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_pressed_btn_filter);;
                    Drawable btnDrawable = button.getBackground();
                    if(btnDrawable.getConstantState() == getResources().getDrawable(R.drawable.non_pressed_btn_filter).getConstantState())
                    {
                        button.setBackgroundResource(R.drawable.pressed_btn_filter);
                        button.setTextColor(Color.parseColor("#FF0000"));
                    }
                    else{
                        button.setBackgroundResource(R.drawable.non_pressed_btn_filter);
                        button.setTextColor(Color.parseColor("#000000"));
                    }
                }
            });
        }
    }

    public void getMovies(String url,String type,ArrayList<Integer> arrGenre)
    {
        RequestQueue queue = Volley.newRequestQueue(ListFlim.this);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseJsonData(response,type,arrGenre);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListFlim.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void parseJsonData(String response,String type,ArrayList<Integer> arrGenre) throws JSONException
    {
        List<Movie> arrMovies = new ArrayList<>();
        JSONObject dataObject = new JSONObject(response);
        JSONArray resultsObject = dataObject.getJSONArray("results");
        for(int i=0;i<resultsObject.length(); i++)
        {
            JSONObject jsonObject = resultsObject.getJSONObject(i);
            String id = jsonObject.getString("id");
            String title = jsonObject.getString("title");
            String description = jsonObject.getString("overview");
            String posterPath = jsonObject.getString("poster_path");
            String backdropPath = jsonObject.getString("backdrop_path");
            String dateTime = jsonObject.getString("release_date");
            String voteAverage = jsonObject.getString("vote_average");
            Movie movie = new Movie(id,title,description,posterPath,backdropPath,dateTime,voteAverage,jsonObject.getJSONArray("genre_ids"));
            if(arrGenre == null)
            {
                arrMovies.add(movie);
            }
            else{
                if(movie.getGenre_ids().containsAll(arrGenre))
                {
                    arrMovies.add(movie);
                }
            }
        }


        MovieAdapter movieAdapter = new MovieAdapter(this,arrMovies,this);
        if(type.equals("popular"))
        {
            lv_popular.setAdapter(movieAdapter);
            lv_popular.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        }
        else if(type.equals("toprated"))
        {
            lv_toprated.setAdapter(movieAdapter);
            lv_toprated.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        } else if (type.equals("upcoming")) {
            lv_upcoming.setAdapter(movieAdapter);
            lv_upcoming.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        }
        else{
            lv_nowplaying.setAdapter(movieAdapter);
            lv_nowplaying.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            if(arrGenre != null)
            {
                LayoutListMovies.setVisibility(View.VISIBLE);
                progressBarinFilter.setVisibility(View.GONE);
            }
        }
    }



}