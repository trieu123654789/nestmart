package com.models;

public class FeedbackImage {
    private int feedbackImageID;
    private int feedbackID;
    private String image;

    public int getFeedbackImageID() {
        return feedbackImageID;
    }

    public void setFeedbackImageID(int feedbackImageID) {
        this.feedbackImageID = feedbackImageID;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
