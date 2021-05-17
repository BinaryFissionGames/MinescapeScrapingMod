package binaryfissiongames.scraper.config;

import binaryfissiongames.scraper.config.annotation.MinescapeScrapingModParserConfigCategoryAnnotation;
import binaryfissiongames.scraper.config.data.MinescapeScrapingModItemParsingConfig;
import binaryfissiongames.scraper.config.data.MinescapeScrapingModMobParsingConfig;
import binaryfissiongames.scraper.scraping_steps.provider.ItemScraperProvider;
import binaryfissiongames.scraper.scraping_steps.provider.MobScraperProvider;

public class MinescapeScrapingModConfig {
    public boolean itemParsingConfigEnabled = true;

    @MinescapeScrapingModParserConfigCategoryAnnotation(
            categoryTransKey = "minescapescraping.config.itemparser.catdesc",
            scraperProvider = ItemScraperProvider.class,
            outFile = "items.json",
            enableFieldName = "itemParsingConfigEnabled"
    )
    public MinescapeScrapingModItemParsingConfig itemParsingConfig = new MinescapeScrapingModItemParsingConfig();

    public boolean mobParsingConfigEnabled = true;

    @MinescapeScrapingModParserConfigCategoryAnnotation(
            categoryTransKey = "minescapescraping.config.mobparser.catdesc",
            scraperProvider = MobScraperProvider.class,
            outFile = "mobs.json",
            enableFieldName = "mobParsingConfigEnabled"
    )
    public MinescapeScrapingModMobParsingConfig mobParsingConfig = new MinescapeScrapingModMobParsingConfig();

    //Purposely package private constructor!
    //Use MinescapeScrapingModConfigController to get an instance of this class
    MinescapeScrapingModConfig(){}
}
