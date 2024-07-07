package doan.cuoiki.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import doan.cuoiki.adapters.MovieAdapter;
import doan.cuoiki.adapters.MovieItemClickListener;
import doan.cuoiki.adapters.MovieSearchAdapter;
import doan.cuoiki.models.Movie;

public class SearchFlim extends AppCompatActivity implements MovieItemClickListener {

    RecyclerView lv_resultForSearch;
    ImageButton btn_cancelSearch;
    EditText edt_search;
    ProgressBar progressBar;
    LinearLayout nothingResult;
    Button btn_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flim);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.search_bar);

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
                startActivity(new Intent(getApplicationContext(), ListFlim.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (itemId == R.id.search_bar) {
                // Xử lý khi itemId là R.id.search_bar

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

    public void addControls()
    {
        progressBar = findViewById(R.id.progressBar);
        lv_resultForSearch = findViewById(R.id.lv_resultForSearch);
        edt_search = findViewById(R.id.edt_search);
        btn_cancelSearch = findViewById(R.id.btn_cancelSearch);
        nothingResult = findViewById(R.id.nothingResult);
        btn_search = findViewById(R.id.btn_search);
    }

    public void addEvents()
    {
        getResultForSearch("https://api.themoviedb.org/3/movie/top_rated?api_key=23f1e4dc64dcdfe853dd2aee44cf082d");


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edt_search.getText().toString().isEmpty())
                {
                    btn_cancelSearch.setVisibility(View.GONE);
                }
                else{
                    btn_cancelSearch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearch(v);
            }
        });

        btn_cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt_search.setText("");
                btn_cancelSearch.setVisibility(View.GONE);
                edt_search.requestFocus();
                // hiện bàn phím lên
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edt_search, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    handleSearch(v);
                    return true;
                }
                return false;
            }
        });
    }

    public void handleSearch(View v)
    {
        if(edt_search.getText().toString().isEmpty())
        {
            Toast.makeText(SearchFlim.this, "Bạn đang để trống", Toast.LENGTH_SHORT).show();
        }
        else{
            //Gỡ bỏ dữ liệu khỏi recycleView
            clearAdapter();
            //Ẩn layout nothingResult
            nothingResult.setVisibility(View.GONE);
            // Hiển thị ProgressBar
            progressBar.setVisibility(View.VISIBLE);
            getResultForSearch("https://api.themoviedb.org/3/search/movie?api_key=23f1e4dc64dcdfe853dd2aee44cf082d&query="+edt_search.getText());
            // dòng dưới đây là để ẩn bàn phím sau khi gõ trong ô nhập search
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            edt_search.clearFocus();
        }
    }

    public void getResultForSearch(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(SearchFlim.this);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseJsonData(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchFlim.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void parseJsonData(String response) throws JSONException
    {
        List<Movie> arrSearch = new ArrayList<>();
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
            arrSearch.add(movie);
        }
        if(!arrSearch.isEmpty())
        {
            MovieSearchAdapter movieAdapter = new MovieSearchAdapter(this,arrSearch,this);
            lv_resultForSearch.setAdapter(movieAdapter);
            // Ẩn ProgressBar
            progressBar.setVisibility(View.GONE);
            lv_resultForSearch.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        }
        else{
            progressBar.setVisibility(View.GONE);
            nothingResult.setVisibility(View.VISIBLE);
        }
    }

    public void clearAdapter()
    {
        List<Movie> arrSearch = new ArrayList<>();
        MovieSearchAdapter movieAdapter = new MovieSearchAdapter(this,arrSearch,this);
        lv_resultForSearch.setAdapter(movieAdapter);
        lv_resultForSearch.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }


    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        Intent intent = new Intent(this,DetailActivity.class);

        intent.putExtra("id",movie.getId());
        intent.putExtra("title",movie.getTitle());
        intent.putExtra("description",movie.getDescription());
        intent.putExtra("imgURL",movie.getImgLink());
        intent.putExtra("imgCover",movie.getImgCoverLink());


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SearchFlim.this,movieImageView,"sharedName");
        startActivity(intent,options.toBundle());
    }
}