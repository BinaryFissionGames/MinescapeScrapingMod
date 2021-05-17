package binaryfissiongames.scraper.scraping_steps.provider;

import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import binaryfissiongames.scraper.scraping_steps.NavigationScrapingStep;
import binaryfissiongames.scraper.scraping_steps.Scraper;
import binaryfissiongames.scraper.scraping_steps.ScraperProvider;
import binaryfissiongames.scraper.scraping_steps.main_steps.ItemChestMainScrapingStep;
import binaryfissiongames.scraper.scraping_steps.navigators.ComandBasedNavigationScrapingStep;
import binaryfissiongames.scraper.scraping_steps.validators.GenericChestValidator;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.regex.Pattern;

public class ItemScraperProvider implements ScraperProvider {

    @Override
    public Scraper getScraper(List<ItemDataParser<?>> parsers, String outFile) {
        return new Scraper(
                new NavigationScrapingStep[]{new ComandBasedNavigationScrapingStep("/items")},
                new ItemChestMainScrapingStep(new GenericChestValidator(6, Pattern.compile("^Items.*")), parsers, 3000),
                Minecraft.getInstance().gameDir.toPath().resolve("scraping/" + outFile)
        );
    }
}
