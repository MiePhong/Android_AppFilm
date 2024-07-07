package doan.cuoiki.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import doan.cuoiki.R;

public class MoviePlayerActivity extends AppCompatActivity {
    WebView webView;
    LinearLayout nothingVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Thiết lập chế độ xoay màn hình theo chiều ngang
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_movie_player);
        addControls();
        getVideo("https://api.themoviedb.org/3/movie/"+getIntent().getExtras().getString("idMovie")+"/videos?api_key=23f1e4dc64dcdfe853dd2aee44cf082d");
    }

    public void addControls()
    {
        webView = findViewById(R.id.webView);
        nothingVideo = findViewById(R.id.nothingVideo);
        nothingVideo.setVisibility(View.GONE);
    }

    public String getYouTubeEmbedHTML(String videoUrl) {
        return "<html><body style='margin:0;padding:0;'><iframe width='100%' height='100%' src='" +
                videoUrl +
                "' frameborder='0' allowfullscreen></iframe></body></html>";
    }

    public void loadVideo(String videoUrl)
    {
        //WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(videoUrl);
        webView.loadDataWithBaseURL(null, getYouTubeEmbedHTML(videoUrl), "text/html", "UTF-8", null);
    }

    public void getVideo(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(MoviePlayerActivity.this);
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
                Toast.makeText(MoviePlayerActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public void parseJsonData(String response) throws JSONException
    {
        JSONObject dataObject = new JSONObject(response);
        JSONArray resultsObject = dataObject.getJSONArray("results");
        for(int i=0;i<resultsObject.length(); i++)
        {
            JSONObject jsonObject = resultsObject.getJSONObject(i);
            String name = jsonObject.getString("name");
            String type = jsonObject.getString("type");
            if(name.equals("Official Trailer") || name.equals("Trailer") || type.equals("Trailer"))
            {
                String videoUrl = "https://www.youtube.com/embed/" + jsonObject.getString("key");
                loadVideo(videoUrl);
                return;
            }
        }
        if(resultsObject.length()>0)
        {
            JSONObject jsonObject = resultsObject.getJSONObject(0);
            String videoUrl = "https://www.youtube.com/embed/" + jsonObject.getString("key");
            loadVideo(videoUrl);
        }
        else{
            nothingVideo.setVisibility(View.VISIBLE);
        }
    }

}