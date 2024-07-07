package doan.cuoiki.models;

public class Cast {
    String id;
    String name;
    String img_link;

    public Cast(String id, String name, String img_link) {
        this.id = id;
        this.name = name;
        this.img_link = img_link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_link() {
        return img_link;
    }

    public void setImg_link(String img_link) {
        this.img_link = img_link;
    }
}
