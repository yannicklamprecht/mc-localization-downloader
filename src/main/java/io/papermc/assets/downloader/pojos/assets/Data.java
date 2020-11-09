package io.papermc.assets.downloader.pojos.assets;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {


    @Expose
    @SerializedName("assetIndex")
    private Assetindex assetindex;

    public Assetindex getAssetindex() {
        return assetindex;
    }

    public void setAssetindex(Assetindex assetindex) {
        this.assetindex = assetindex;
    }
}
