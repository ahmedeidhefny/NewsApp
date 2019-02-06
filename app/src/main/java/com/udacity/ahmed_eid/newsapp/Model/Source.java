package com.udacity.ahmed_eid.newsapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Source implements Parcelable {
    private String name;
    private int image;
    private String websiteLink;
    private String allNewsURL;
    private ArrayList<News> news;

    public Source(String name, int image, String websiteLink,String allNewsURL, ArrayList<News> news) {
        this.name = name;
        this.image = image;
        this.news = news;
        this.websiteLink = websiteLink;
        this.allNewsURL = allNewsURL;
    }

    protected Source(Parcel in) {
        name = in.readString();
        websiteLink = in.readString();
        allNewsURL = in.readString();
        image = in.readInt();
        news = in.createTypedArrayList(News.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(websiteLink);
        dest.writeString(allNewsURL);
        dest.writeInt(image);
        dest.writeTypedList(news);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Source> CREATOR = new Creator<Source>() {
        @Override
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        @Override
        public Source[] newArray(int size) {
            return new Source[size];
        }
    };

    public String getAllNewsURL() {
        return allNewsURL;
    }

    public void setAllNewsURL(String allNewsURL) {
        this.allNewsURL = allNewsURL;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public ArrayList<News> getNews() {
        return news;
    }

    public void setNews(ArrayList<News> news) {
        this.news = news;
    }
}
