package com.example.nattapongpaka.demo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by nattapongpaka on 3/22/2017 AD.
 */

public class PhotoDaos implements Parcelable {

    /**
     * albumId : 1
     * id : 1
     * title : accusamus beatae ad facilis cum similique qui sunt
     * url : http://placehold.it/600/92c952
     * thumbnailUrl : http://placehold.it/150/92c952
     */

    @SerializedName("albumId")
    private int albumId;
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;

    public PhotoDaos(){}

    private PhotoDaos(Parcel in) {
        albumId = in.readInt();
        id = in.readInt();
        title = in.readString();
        url = in.readString();
        thumbnailUrl = in.readString();
    }

    public static final Creator<PhotoDaos> CREATOR = new Creator<PhotoDaos>() {
        @Override
        public PhotoDaos createFromParcel(Parcel in) {
            return new PhotoDaos(in);
        }

        @Override
        public PhotoDaos[] newArray(int size) {
            return new PhotoDaos[size];
        }
    };

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(albumId);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(thumbnailUrl);
    }
}
