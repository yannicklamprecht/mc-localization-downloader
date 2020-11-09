package io.papermc.assets.downloader.pojos.clientassets;

import java.util.HashMap;
import java.util.Map;

public class ClientAssets {

    @com.google.gson.annotations.Expose
    @com.google.gson.annotations.SerializedName("objects")
    private Map<String,HashSize> objects = new HashMap<>();

    public Map<String, HashSize> getObjects() {
        return objects;
    }

    public void setObjects(Map<String, HashSize> objects) {
        this.objects = objects;
    }
}