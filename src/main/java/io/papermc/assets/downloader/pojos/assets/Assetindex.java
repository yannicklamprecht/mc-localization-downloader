package io.papermc.assets.downloader.pojos.assets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Assetindex {
    @Expose
    @SerializedName("url")
    private String url;
    @Expose
    @SerializedName("totalSize")
    private int totalsize;
    @Expose
    @SerializedName("size")
    private int size;
    @Expose
    @SerializedName("sha1")
    private String sha1;
    @Expose
    @SerializedName("id")
    private String id;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotalsize() {
        return totalsize;
    }

    public void setTotalsize(int totalsize) {
        this.totalsize = totalsize;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
