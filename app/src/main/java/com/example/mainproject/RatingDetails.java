package com.example.mainproject;

public class RatingDetails {
    private String userId;
    private float rating;
    private int likesCount;
    private String comment;
    private boolean isLiked;

    public RatingDetails() {
        // Default constructor required for Firebase
    }

    public RatingDetails(String userId, float rating, int likesCount, String comment) {
        this.userId = userId;
        this.rating = rating;
        this.likesCount = likesCount;
        this.comment = comment;
        this.isLiked = false;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
