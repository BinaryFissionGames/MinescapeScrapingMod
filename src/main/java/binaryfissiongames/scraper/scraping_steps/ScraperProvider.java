package binaryfissiongames.scraper.scraping_steps;

import java.util.List;

public interface ScraperProvider {
    Scraper getScraper(List<ItemDataParser<?>> parsers, String outFile);
}
