package com.example.jainsaab.movielib.favouriteMovies;

class FavouriteMovies {

    private int movieId;
    private String title;
    private String releaseDate;
    private String userRating;
    private String genre;
    private String plot;
    private String cbfcRating;
    private String[] author;
    private String[] content;
    private String[] reviewUrl;
    private String[] characterName;
    private String[] actorName;
    private String[] trailerName;
    private String[] key;

    FavouriteMovies(int mI, String t, String rD, String uR, String g, String p, String cR,
                           String[] a, String[] c, String[] rU, String[] cN, String[] aN, String[] tN, String[] k){

        movieId = mI;
        releaseDate = rD;
        title = t;
        plot = p;
        userRating = uR;
        genre = g;
        author = a;
        content = c;
        trailerName = tN;
        key = k;
        reviewUrl = rU;
        characterName = cN;
        actorName = aN;
        cbfcRating = cR;
    }

    int getMovieId() {
        return movieId;
    }

    String getMovieTitle() {
        return title;
    }

    String getReleaseDate() {
        return releaseDate;
    }

    public String getGenre() {
        return genre;
    }

    String getPlot() {
        return plot;
    }

    String getUserRating() {
        return userRating;
    }

    String[] getActorName() {
        return actorName;
    }

    String[] getAuthor() {
        return author;
    }

    String[] getCharacterName() {
        return characterName;
    }

    String[] getReviewContent() {
        return content;
    }

    String[] getKey() {
        return key;
    }

    String[] getReviewUrl() {
        return reviewUrl;
    }

    String[] getTrailerName() {
        return trailerName;
    }

    String getCbfcRating() {
        return cbfcRating;
    }
}
