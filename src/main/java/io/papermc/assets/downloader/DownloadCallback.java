package io.papermc.assets.downloader;


import java.util.Locale;

@FunctionalInterface
public interface DownloadCallback {
    void onDataIncomming(Locale locale, String data, int index, int amount);
}
