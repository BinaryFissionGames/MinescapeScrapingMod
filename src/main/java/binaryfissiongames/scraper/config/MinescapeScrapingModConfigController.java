package binaryfissiongames.scraper.config;

import binaryfissiongames.scraper.MinescapeScrapingMod;
import binaryfissiongames.scraper.Util;
import binaryfissiongames.scraper.config.annotation.MinescapeScrapingModParserConfigCategoryAnnotation;
import binaryfissiongames.scraper.config.annotation.MinescapeScrapingModParserConfigOption;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import binaryfissiongames.scraper.scraping_steps.Scraper;
import binaryfissiongames.scraper.scraping_steps.ScraperProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class MinescapeScrapingModConfigController {
    private static MinescapeScrapingModConfig config;

    private static final String CONFIG_NAME = "minescape-scraping-client.json";

    public static MinescapeScrapingModConfig getConfig() {
        if (config != null) {
            return config;
        }
        try {
            config = loadConfig();
        } catch (Exception e) {
            MinescapeScrapingMod.LOGGER.error("FAILED to load config! Using default config instead...", e);
            config = defaultConfig();
        }
        return config;
    }

    public static void saveConfig() throws IOException {
        if (config == null) {
            config = defaultConfig();
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        String configJson = gson.toJson(config);
        Files.write(getConfigPath(), configJson.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }

    public static List<Scraper> buildScrapersFromConfig() {
        List<Scraper> scrapers = new ArrayList<>();
        MinescapeScrapingModConfig config = getConfig();

        for (Field categoryField : MinescapeScrapingModConfig.class.getFields()) {
            MinescapeScrapingModParserConfigCategoryAnnotation configAnnotation = categoryField.getAnnotation(MinescapeScrapingModParserConfigCategoryAnnotation.class);
            if(configAnnotation == null){
                continue;
            }

            if (!configAnnotation.enableFieldName().equals("none") && !Util.getBooleanField(config, configAnnotation.enableFieldName())) {
                continue; // This scraper is disabled; we will skip over this one.
            }

            try {
                Object categoryObject = categoryField.get(config);
                /* Get configured parsers */
                List<ItemDataParser<?>> itemDataParsers = new ArrayList<>();
                for (Field parserEnableField : categoryField.getType().getFields()) {
                    MinescapeScrapingModParserConfigOption parserConfigAnnotation = parserEnableField.getAnnotation(MinescapeScrapingModParserConfigOption.class);
                    if (parserConfigAnnotation != null &&
                            (parserEnableField.getType().equals(boolean.class) || parserEnableField.getType().equals(Boolean.class)) &&
                            Util.getBooleanField(categoryObject, parserEnableField.getName())
                    ) {
                        itemDataParsers.add(Util.instantiateNoArgsConstructor(parserConfigAnnotation.parserClass()));
                    }
                }

                /* Instantiate a scraper, and add it to our list! */
                ScraperProvider scraperProvider = Util.instantiateNoArgsConstructor(configAnnotation.scraperProvider());
                scrapers.add(scraperProvider.getScraper(itemDataParsers, configAnnotation.outFile()));

            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
                MinescapeScrapingMod.LOGGER.error("Failed to get config info for field " + categoryField.getName() + ", skipping scraper for this field.", e);
            }
        }

        return scrapers;
    }

    private static MinescapeScrapingModConfig loadConfig() throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        Path configPath = getConfigPath();
        if (!Files.exists(configPath)) {
            //If the config does NOT exist already, save a default version.
            saveConfig();
            return config;
        }

        config = gson.fromJson(new String(Files.readAllBytes(configPath), StandardCharsets.UTF_8), MinescapeScrapingModConfig.class);
        return config;
    }

    private static MinescapeScrapingModConfig defaultConfig() {
        return new MinescapeScrapingModConfig();
    }

    private static Path getConfigPath() {
        return Minecraft.getInstance().gameDir.toPath().resolve("config/" + CONFIG_NAME);
    }


}
