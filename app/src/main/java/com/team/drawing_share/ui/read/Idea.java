package com.team.drawing_share.ui.read;

public class Idea {
    private String Image;
    private String Title;
    private String Writer;
    private String Time;

    public Idea(){}

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public String getWriter() {
        return Writer;
    }

    public void setWriter(String writer) {
        this.Writer = writer;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String date) {
        this.Time = date;
    }
}
