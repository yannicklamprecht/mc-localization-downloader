package pojos.manifest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Versions {
    @Expose
    @SerializedName("releaseTime")
    private String releasetime;
    @Expose
    @SerializedName("time")
    private String time;
    @Expose
    @SerializedName("url")
    private String url;
    @Expose
    @SerializedName("type")
    private String type;
    @Expose
    @SerializedName("id")
    private String id;

    public String getReleasetime() {
        return releasetime;
    }

    public void setReleasetime(String releasetime) {
        this.releasetime = releasetime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
