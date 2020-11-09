package io.papermc.assets.downloader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.papermc.assets.downloader.pojos.assets.Data;
import io.papermc.assets.downloader.pojos.clientassets.ClientAssets;
import io.papermc.assets.downloader.pojos.clientassets.HashSize;
import io.papermc.assets.downloader.pojos.manifest.VersionManifest;
import io.papermc.assets.downloader.pojos.manifest.Versions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class LocalizationDownloader {

    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<List<String>>() {}.getType();

    private String mcVersion;

    public LocalizationDownloader(String mcVersion) {
        this.mcVersion = mcVersion;
    }

    public void safeStringListToFile(List<String> stringList, String fileName){
        safeStringToFile(gson.toJson(stringList, listType), fileName);
    }
    public void safeStringToFile(String data, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String fetch(String urlString) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()))) {
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        }
    }

    public Collection<CompletableFuture<LanguageFile>> run(ExecutorService executor) throws IOException {
        Collection<CompletableFuture<LanguageFile>> languageFiles = new ArrayList<>();

        String versionManifest = fetch("https://launchermeta.mojang.com/mc/game/version_manifest.json");
        VersionManifest versionManifestGson = gson.fromJson(versionManifest, VersionManifest.class);

        if (mcVersion == null) {
            mcVersion = versionManifestGson.getLatest().getRelease();
        }

        System.out.println("Downloading language file for mc version: " + mcVersion);

        String finalMcVersion = mcVersion;
        Optional<Versions> version = versionManifestGson.getVersions().parallelStream().filter(versions -> versions.getId().equals(finalMcVersion)).findFirst();
        version.ifPresent(versions -> {
            try {
                String data = fetch(versions.getUrl());
                Data assets = gson.fromJson(data, Data.class);

                String assetData = fetch(assets.getAssetindex().getUrl());

                ClientAssets clientAssets = gson.fromJson(assetData, ClientAssets.class);
                for (Map.Entry<String, HashSize> stringHashSizeEntry : clientAssets.getObjects()
                        .entrySet()) {
                    if (stringHashSizeEntry.getKey().startsWith("minecraft/lang/")) {
                        CompletableFuture<LanguageFile> languageFileCompletableFuture = downloadAsync(stringHashSizeEntry.getKey(),stringHashSizeEntry.getValue().getHash(), executor);
                        languageFiles.add(languageFileCompletableFuture);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return languageFiles;
    }

    private CompletableFuture<LanguageFile> downloadAsync(String languageKey, String hash, Executor executor){
        String shortHash = hash.substring(0, 2);
        String url = "http://resources.download.minecraft.net/" + shortHash + "/" + hash;

        return CompletableFuture.supplyAsync(() -> {
            LanguageFile languageFile = null;
            try {
                String languageData = fetch(url);
                String[] langKeyArray = languageKey.replaceAll("minecraft/lang/|.json", "").split("_");
                if (langKeyArray.length == 2) {
                    Locale locale = new Locale(langKeyArray[0], langKeyArray[1]);
                    languageFile = new LanguageFile(locale, languageData);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            if(languageFile==null) throw new IllegalStateException();

            return languageFile;
        }, executor);

    }


}
