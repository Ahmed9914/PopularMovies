package nanodegree.udacity.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Property;

/**
 * Created by Ahmd on 16/02/2018.
 */

public class Movie implements Parcelable{
    private String title;
    private String releaseDate;
    private String posterLink;
    private double voteAverage;
    private String overview;

    public Movie() {}

    public Movie(String mTitle, String mReleaseDate, String mPosterLink, double mVoteAverage, String mOverview) {
        this.title = mTitle;
        this.releaseDate = mReleaseDate;
        this.posterLink = mPosterLink;
        this.voteAverage = mVoteAverage;
        this.overview = mOverview;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(posterLink);
        parcel.writeDouble(voteAverage);
        parcel.writeString(overview);
    }

    public Movie(Parcel in) {
        title = in.readString();
        releaseDate = in.readString();
        posterLink= in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public String getPosterLink() {
        return posterLink;
    }

    public void setPosterLink(String posterLink) {
        this.posterLink = posterLink;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {

        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}
