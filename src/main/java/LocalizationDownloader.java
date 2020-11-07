import com.google.gson.Gson;
import pojos.assets.Data;
import pojos.clientassets.ClientAssets;
import pojos.clientassets.HashSize;
import pojos.manifest.VersionManifest;
import pojos.manifest.Versions;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LocalizationDownloader {

    private final Gson gson = new Gson();

    private String mcVersion;

    public LocalizationDownloader(String mcVersion) {
        this.mcVersion = mcVersion;
    }

    public static void main(String[] args) {
        LocalizationDownloader localizationDownloader = new LocalizationDownloader(System.getProperty("mcver"));
        String targetDir = System.getProperty("outDir", "languages");

        try {
            File targetFileDir = new File(targetDir);
            targetFileDir.mkdirs();
            localizationDownloader.run((locale, data, index, amount) -> {
                System.out.println("Downloaded " + index + " of " + amount + " Files.");
                localizationDownloader.safeStringToFile(data, targetFileDir.getAbsolutePath() + File.separator + locale.toString() + ".json");
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void run(DownloadCallback languageDataConsumer) throws IOException {
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
                Map<String, HashSize> assetObjects = clientAssets.getObjects()
                        .entrySet().stream()
                        .filter(entry -> entry.getKey().startsWith("minecraft/lang/"))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                int index = 0;
                int size = assetObjects.size();
                for (Map.Entry<String, HashSize> entry : assetObjects.entrySet()) {

                    index++;

                    String languageFileName = entry.getKey();
                    String hash = entry.getValue().getHash();
                    String shortHash = hash.substring(0, 2);

                    String url = "http://resources.download.minecraft.net/" + shortHash + "/" + hash;
                    try {
                        String languageData = fetch(url);
                        String[] langKeyArray = languageFileName.replaceAll("minecraft/lang/|.json", "").split("_");
                        if (langKeyArray.length == 2) {
                            Locale locale = new Locale(langKeyArray[0], langKeyArray[1]);
                            languageDataConsumer.onDataIncomming(locale, languageData, index, size);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FunctionalInterface
    public interface DownloadCallback {
        void onDataIncomming(Locale locale, String data, int index, int amount);
    }
}
