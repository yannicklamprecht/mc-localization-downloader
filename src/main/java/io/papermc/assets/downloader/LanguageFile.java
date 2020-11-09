package io.papermc.assets.downloader;

import java.util.Locale;
import java.util.Objects;
import java.util.StringJoiner;

public class LanguageFile {
    private Locale key;
    private String value;

    public LanguageFile(Locale key, String value) {
        this.key = key;
        this.value = value;
    }

    public LanguageFile() {
    }

    public Locale getKey() {
        return key;
    }

    public void setKey(Locale key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LanguageFile.class.getSimpleName() + "[", "]")
                .add("key=" + key)
                .add("value='" + value + "'")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LanguageFile)) return false;
        LanguageFile that = (LanguageFile) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}
