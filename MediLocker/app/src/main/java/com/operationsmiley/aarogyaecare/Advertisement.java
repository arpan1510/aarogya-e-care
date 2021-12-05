package com.operationsmiley.aarogyaecare;

public class Advertisement {
    String ad_header,ad_image,ad_url,ad_hashtag;

    public Advertisement() {
    }

    public Advertisement(String ad_header, String ad_image, String ad_url, String ad_hashtag) {
        this.ad_header = ad_header;
        this.ad_image = ad_image;
        this.ad_url = ad_url;
        this.ad_hashtag = ad_hashtag;
    }

    public String getAd_hashtag() {
        return ad_hashtag;
    }

    public void setAd_hashtag(String ad_hashtag) {
        this.ad_hashtag = ad_hashtag;
    }

    public String getAd_url() {
        return ad_url;
    }

    public void setAd_url(String ad_url) {
        this.ad_url = ad_url;
    }

    public String getAd_header() {
        return ad_header;
    }

    public void setAd_header(String ad_header) {
        this.ad_header = ad_header;
    }

    public String getAd_image() {
        return ad_image;
    }

    public void setAd_image(String ad_image) {
        this.ad_image = ad_image;
    }
}
