package doan.cuoiki.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.List;

import doan.cuoiki.R;
import doan.cuoiki.adapters.CommentAdapter;
import doan.cuoiki.models.Comment;

public class Comments extends AppCompatActivity {

    RecyclerView Discussions;
    LinearLayout nothingResultComment;
    CommentAdapter commentAdapter;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        addControls();
        addEvents();
    }

    private void addControls() {
        imageButton = findViewById(R.id.backbt2);
        Discussions = findViewById(R.id.discussions);
        nothingResultComment = findViewById(R.id.nothingResultComment);
    }

    private void addEvents() {
//        String name = getIntent().getStringExtra("name");
//        String review = getIntent().getStringExtra("content");
//        String date = getIntent().getStringExtra("created_at");

        getComments("https://api.themoviedb.org/3/movie/" + getIntent().getStringExtra("id") + "/reviews?api_key=23f1e4dc64dcdfe853dd2aee44cf082d");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getComments(String id) {
        RequestQueue queue = Volley.newRequestQueue(Comments.this);
        StringRequest stringRequest = new StringRequest(id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    parseJsonDataComments(response);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Comments.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

//    private void parseJsonDataComments(String response) throws JSONException {
//        List<Comment> commentList = new ArrayList<>();
//        JSONObject dataObject = new JSONObject(response);
//        JSONArray reviewsArray = dataObject.getJSONArray("results");
//
//        for (int i = 0; i < reviewsArray.length(); i++) {
//            JSONObject reviewObject = reviewsArray.getJSONObject(i);
//            String id = reviewObject.getString("id");
//            String author = reviewObject.getString("author");
//            String content = reviewObject.getString("content");
//            String createdAt = reviewObject.getString("created_at");
//            String profilePath = "";
//
//            // Kiểm tra xem có chiều dài của avatar_path lớn hơn 0 không
//            if (reviewObject.has("author_details") && !reviewObject.isNull("author_details")) {
//                JSONObject authorDetails = reviewObject.getJSONObject("author_details");
//                if (authorDetails.has("avatar_path") && !authorDetails.isNull("avatar_path")) {
//                    profilePath = authorDetails.getString("avatar_path");
//                }
//            }
//
//            Comment comment = new Comment(profilePath, author, content, createdAt,id);
//            commentList.add(comment);
//        }
//
//        commentAdapter = new CommentAdapter(this, commentList);
//        Discussions.setAdapter(commentAdapter);
//        Discussions.setLayoutManager(new LinearLayoutManager(this));
//    }

    private void parseJsonDataComments(String response) throws JSONException {
        List<Comment> commentList = new ArrayList<>();
        JSONObject dataObject = new JSONObject(response);
        JSONArray reviewsArray = dataObject.getJSONArray("results");
        if(reviewsArray.length()>0)
        {
            for (int i = 0; i < reviewsArray.length(); i++) {
            JSONObject reviewObject = reviewsArray.getJSONObject(i);
            String id = reviewObject.getString("id");
            String author = reviewObject.getString("author");
            String content = reviewObject.getString("content");
            String createdAt = reviewObject.getString("created_at");
            JSONObject author_details = reviewObject.getJSONObject("author_details");
            String profilePath = author_details.getString("avatar_path");
            Comment comment = new Comment(profilePath, author, content, createdAt,id);
            commentList.add(comment);
            }
        }
        else{
            Discussions.setVisibility(View.GONE);
            nothingResultComment.setVisibility(View.VISIBLE);
        }

        commentAdapter = new CommentAdapter(this, commentList);
        Discussions.setAdapter(commentAdapter);
        Discussions.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
