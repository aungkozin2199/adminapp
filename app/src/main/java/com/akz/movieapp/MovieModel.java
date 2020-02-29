package com.akz.movieapp;

public class MovieModel {
    public String name;
    public  String imageLink;
    public String videoLink;
    public String category;
    public String series;

    public MovieModel(String name, String imageLink, String videoLink, String category, String series) {
        this.name = name;
        this.imageLink = imageLink;
        this.videoLink = videoLink;
        this.category = category;
        this.series = series;
    }

    public MovieModel(String name, String imageLink, String videoLink, String category) {
        this.name = name;
        imageLink = imageLink;
        this.videoLink = videoLink;
        this.category = category;
    }

    public MovieModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
