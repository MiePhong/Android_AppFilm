package doan.cuoiki.models;

public class Slide {

    String Image;

    String Title;
    String Id;

    public Slide(String id, String title, String image) {
        Image = image;
        Title = title;
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
