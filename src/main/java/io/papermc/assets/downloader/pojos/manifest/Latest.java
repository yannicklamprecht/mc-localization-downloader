package io.papermc.assets.downloader.pojos.manifest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Latest {
    @Expose
    @SerializedName("snapshot")
    private String snapshot;
    @Expose
    @SerializedName("release")
    private String release;

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }
}
