package pojos.manifest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VersionManifest {


    @Expose
    @SerializedName("versions")
    private List<Versions> versions;
    @Expose
    @SerializedName("latest")
    private Latest latest;

    public List<Versions> getVersions() {
        return versions;
    }

    public void setVersions(List<Versions> versions) {
        this.versions = versions;
    }

    public Latest getLatest() {
        return latest;
    }

    public void setLatest(Latest latest) {
        this.latest = latest;
    }
}
