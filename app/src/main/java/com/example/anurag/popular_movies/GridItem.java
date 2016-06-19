package com.example.anurag.popular_movies;

/**
 * Created by anurag on 4/4/16.
 */

public class GridItem {
    private String image;
    private String title;
    private String releaseDate;
    private String vote_average;
    private String overview;
    private String movie_id;

    public GridItem() {
        super();
    }

    public String getId() {
        return movie_id;
    }

    public void setId(String id) {
        this.movie_id = id;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVote_average() {
        return vote_average;
    }

    public  void  setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

}
