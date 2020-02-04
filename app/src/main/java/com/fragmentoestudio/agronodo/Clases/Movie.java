package com.fragmentoestudio.agronodo.Clases;

public class Movie {

    private String title;
    private boolean expanded;

    public Movie(String title) {
        this.title = title;
        this.expanded = false;
    }

    public String getTitle() {
        return title;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public String toString() {
        return "JSON:{" +
                "name='" + title + '\'' +
                "}";
    }
}
