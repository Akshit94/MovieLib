package com.example.jainsaab.movielib.movies;

public class Movies {
    private String movieTitle;
    private String movieYear;
    private String moviePoster;
    private String movieBackdrop;
    private String cbfcRating;
    private String userRating;
    private int[] genre;
    private String plot;
    private String movieId;
    public Movies(String movieTitle, String movieYear, String moviePoster, String cbfcRating, String movieBackdrop,
           String userRating, int[] genre, String plot, String movieId){
        this.movieTitle = movieTitle;
        this.movieYear = movieYear;
        this.moviePoster = moviePoster;
        this.cbfcRating = cbfcRating;
        this.movieBackdrop = movieBackdrop;
        this.userRating = userRating;
        this.genre = genre;
        this.plot = plot;
        this.movieId = movieId;
    }
    String getMovieTitle(){
        return movieTitle;
    }
    String getMovieYear(){
        return movieYear;
    }
    String getMoviePoster(){
        return moviePoster;
    }
    String getCbfcRating(){
        return cbfcRating;
    }
    String getMovieBackdrop(){
        return movieBackdrop;
    }
    String getUserRating(){
        return userRating;
    }
    public int[] getGenre(){
        return genre;
    }
    String getPlot(){
        return plot;
    }
    String getMovieId(){
        return movieId;
    }
}
