package com.operationsmiley.aarogyaecare;

public class AdImageSliderModel {
    int Image;

    private String editTextFeedSuggestion;
    private String timestamp;

    public AdImageSliderModel() {
    }

    public AdImageSliderModel(String editTextFeedSuggestion, String timestamp) {
    }

    public AdImageSliderModel(int image) {
        Image = image;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getEditTextFeedSuggestion() {
        return editTextFeedSuggestion;
    }

    public void setEditTextFeedSuggestion(String editTextFeedSuggestion) {
        this.editTextFeedSuggestion = editTextFeedSuggestion;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
