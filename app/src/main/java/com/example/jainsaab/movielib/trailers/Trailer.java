package com.example.jainsaab.movielib.trailers;

class Trailer {

    private String trailerName;
    private String trailerKey;

    Trailer(String trailerName, String trailerKey){
        this.trailerName = trailerName;
        this.trailerKey = trailerKey;
    }

    String getTrailerKey() {
        return trailerKey;
    }

    String getTrailerName() {
        return trailerName;
    }
}
