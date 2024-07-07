package doan.cuoiki.models;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class  Movie {
    String id;
    String title;
    String description;
    String imgLink;
    String imgCoverLink;
    
    String dateTime;
    String voteAverage;
    ArrayList<Integer> genre_ids;

    public Movie(String id, String title, String description, String imgLink, String imgCoverLink, String dateTime, String voteAverage, JSONArray jsonArray) throws JSONException {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgLink = imgLink;
        this.imgCoverLink = imgCoverLink;
        this.dateTime = dateTime;
        this.voteAverage = voteAverage;
        //lưu id
        ArrayList<Integer> arr = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            int element = jsonArray.getInt(i);
            arr.add(element);
        }
        this.genre_ids = arr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getImgCoverLink() {
        return imgCoverLink;
    }

    public void setImgCoverLink(String imgCoverLink) {
        this.imgCoverLink = imgCoverLink;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public ArrayList<Integer> getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(ArrayList<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }
}
