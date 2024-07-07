package doan.cuoiki.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import doan.cuoiki.models.Movie;
import doan.cuoiki.adapters.MovieAdapter;
import doan.cuoiki.adapters.MovieItemClickListener;
import doan.cuoiki.R;
import doan.cuoiki.models.Slide;
import doan.cuoiki.adapters.SlidePagerAdapter;


public class MainActivity extends AppCompatActivity implements MovieItemClickListener {

    List<Slide> lstSlides;
    ViewPager sliderpager;
    TabLayout indicator ;

    DrawerLayout drawerLayout;

    BottomNavigationView bottomNavigationView;
    RecyclerView MoviesRV, moviesRVWeek ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        addControls();
        //addSlides();
        getSlideNowplaying("https://api.themoviedb.org/3/movie/now_playing?api_key=23f1e4dc64dcdfe853dd2aee44cf082d");

        getMovies("https://api.themoviedb.org/3/movie/top_rated?api_key=23f1e4dc64dcdfe853dd2aee44cf082d","toprated");
        getMovies("https://api.themoviedb.org/3/movie/popular?api_key=23f1e4dc64dcdfe853dd2aee44cf082d","popular");

        addMenu();

    }



    private void addControls() {
        sliderpager = (ViewPager) findViewById(R.id.slider_pager);
        indicator = (TabLayout) findViewById(R.id.indicator);
        MoviesRV = (RecyclerView) findViewById(R.id.Rv_movies);
        moviesRVWeek = (RecyclerView) findViewById(R.id.rv_week_movies);
    }

    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        Intent intent = new Intent(this,DetailActivity.class);

        intent.putExtra("id",movie.getId());
        intent.putExtra("title",movie.getTitle());
        intent.putExtra("description",movie.getDescription());
        intent.putExtra("imgURL",movie.getImgLink());
        intent.putExtra("imgCover",movie.getImgCoverLink());


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,movieImageView,"sharedName");
        startActivity(intent,options.toBundle());
    }

    @SuppressLint("NonConstantResourceId")
    public void addMenu(){
//        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                int itemId = item.getItemId();
//                if (itemId == R.id.home_item) {
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    overridePendingTransition(0, 0);
////                    return true;
//                } else if (itemId == R.id.home_item) {
//                    // Your code for handling the "test" item selected
//                    return true;
//                } else if (itemId == R.id.list_search) {
//                    startActivity(new Intent(getApplicationContext(), ListFlim.class));
//                    overridePendingTransition(0, 0);
////                    return true;
//                } else if (itemId == R.id.list_search) {
//                    startActivity(new Intent(getApplicationContext(), SearchFlim.class));
//                    overridePendingTransition(0, 0);
////                    return true;
//
//                }
//                return false;
//            }
//        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home_item);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            // Thay thế switch-case cũ bằng switch-case mới
            int itemId = item.getItemId();
            if (itemId == R.id.home_item) {
                // Xử lý khi itemId là R.id.home_item
                return true;
            } else if (itemId == R.id.list_item) {
                // Xử lý khi itemId là R.id.list_item
                startActivity(new Intent(getApplicationContext(), ListFlim.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.search_bar) {
                // Xử lý khi itemId là R.id.search_bar
                startActivity(new Intent(getApplicationContext(), SearchFlim.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if (itemId == R.id.user) {
            // Xử lý khi itemId là R.id.search_bar
            startActivity(new Intent(getApplicationContext(),profile.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
            return true;
            }else {
                return false;
            }


        });
    }


    class SliderTimer extends TimerTask {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderpager.getCurrentItem()<lstSlides.size()-1) {
                        sliderpager.setCurrentItem(sliderpager.getCurrentItem()+1);
                    }else {
                        sliderpager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    public void getMovies(String url,String type)
    {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseMovies(response,type);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void parseMovies(String response,String type) throws JSONException
    {
        List<Movie> arrMovie = new ArrayList<>();
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
            arrMovie.add(movie);
        }

        MovieAdapter movieAdapter = new MovieAdapter(this,arrMovie,this);

        if(type.equals("popular"))
        {
            MoviesRV.setAdapter(movieAdapter);
            MoviesRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        }
        else{
            moviesRVWeek.setAdapter(movieAdapter);
            moviesRVWeek.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        }
    }

    public void getSlideNowplaying(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseNowplaying(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }


    public void parseNowplaying(String response) throws JSONException
    {
        List<Slide> dataSlides = new ArrayList<>();
        JSONObject dataObject = new JSONObject(response);
        JSONArray resultsObject = dataObject.getJSONArray("results");
        for(int i=0;i<4; i++)
        {
            JSONObject jsonObject = resultsObject.getJSONObject(i);
            String id = jsonObject.getString("id");
            String title = jsonObject.getString("title");
            String backdropPath = jsonObject.getString("backdrop_path");
            Slide slide = new Slide(id,title,backdropPath);
            dataSlides.add(slide);
        }
        addSlides(dataSlides);

    }

    private void addSlides(List<Slide> dataSlides) {

        lstSlides = dataSlides;
//        lstSlides.add(new Slide(R.drawable.slide1,"Silde Title /nmore text here"));
//        lstSlides.add(new Slide(R.drawable.slide2,"Silde Title /nmore text here"));
//        lstSlides.add(new Slide(R.drawable.slide1,"Silde Title /nmore text here"));
//        lstSlides.add(new Slide(R.drawable.slide2,"Silde Title /nmore text here"));

        SlidePagerAdapter adapter = new SlidePagerAdapter(this, lstSlides);

        sliderpager.setAdapter(adapter);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(),2000,4000);

        indicator.setupWithViewPager(sliderpager,true);
    }


}