package io.papermc.assets.downloader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static void main(String[] args) {
        LocalizationDownloader localizationDownloader = new LocalizationDownloader(System.getProperty("mcver"));
        String targetDir = System.getProperty("outDir", "languages");

        ExecutorService executor = Executors.newScheduledThreadPool(20);
        try {
            File targetFileDir = new File(targetDir);
            targetFileDir.mkdirs();
            Collection<CompletableFuture<LanguageFile>> completableFutures = localizationDownloader.run(executor);
            AtomicInteger index = new AtomicInteger();
            AtomicInteger failed = new AtomicInteger();
            long count = completableFutures.size();
            for (CompletableFuture<LanguageFile> languageFileCompletableFuture : completableFutures) {
                languageFileCompletableFuture
                        .whenCompleteAsync((languageFile, throwable) -> {
                            int current = index.incrementAndGet();
                            if (throwable != null) {
                                System.out.println(current + " of " + count + " Files failed downloading.");
                                failed.incrementAndGet();
                            } else {
                                System.out.println(current + " of " + count + " Files downloaded successfully");
                                localizationDownloader.safeStringToFile(languageFile.getValue(), targetFileDir.getAbsolutePath() + File.separator + languageFile.getKey().toString() + ".json");
                            }
                            if (index.get() == count) {
                                System.out.println("Statistic: success: " + (index.get() - failed.get()) + " failed: " + failed.get());
                                executor.shutdown();
                            }
                        });
            }

        } catch (IOException e) {
            e.printStackTrace();
            executor.shutdown();
        }
    }
}
