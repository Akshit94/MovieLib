package com.example.jainsaab.movielib.reviews;

class Review {

    private String authorName;
    private String content;
    private String reviewUrl;

    Review(String authorName, String content, String reviewUrl){
        this.authorName = authorName;
        this.content = content;
        this.reviewUrl = reviewUrl;
    }

    String getAuthorName() {
        return authorName;
    }

    String getReviewContent() {
        return content;
    }

    String getReviewUrl() {
        return reviewUrl;
    }
}
