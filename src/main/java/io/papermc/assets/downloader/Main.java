package io.papermc.assets.downloader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        LocalizationDownloader localizationDownloader = new LocalizationDownloader(System.getProperty("mcver"));
        String targetDir = System.getProperty("outDir", "languages");

        Executor executor = Executors.newScheduledThreadPool(20);
        try {
            File targetFileDir = new File(targetDir);
            targetFileDir.mkdirs();
            Collection<CompletableFuture<LanguageFile>> completableFutures = localizationDownloader.run(executor);
            AtomicInteger index = new AtomicInteger();
            long count = completableFutures.size();
            for (CompletableFuture<LanguageFile> languageFileCompletableFuture : completableFutures) {
                languageFileCompletableFuture.thenAcceptAsync(languageFile -> {
                    System.out.println("Downloaded " + index.incrementAndGet() + " of " + count + " Files.");
                    localizationDownloader.safeStringToFile(languageFile.getValue(), targetFileDir.getAbsolutePath() + File.separator + languageFile.getKey().toString() + ".json");
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
