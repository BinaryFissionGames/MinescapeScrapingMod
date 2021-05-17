package binaryfissiongames.scraper.scraping_steps;

import binaryfissiongames.scraper.scraping_steps.exception.ScrapingException;

import java.util.List;
import java.util.Map;

public interface MainScrapingStep {
    // Called each tick. Should keep scraping.
    void tick() throws ScrapingException;
    // Indicates whether scraping is done or not
    boolean isDone();
    // Resets scraping; isDone should be false after this call (unless there is nothing to scrape)
    void reset();
    // Results of scraping. Should not be called if isDone is not set. Null is acceptable here.
    List<Map<String, Object>> result();
    // Returns a screen validator; This should validate if the screen we are currently on is a valid screen for scraping.
    // This is necessary to ensure data consistency as well as robustness against possible user input/interruption.
    ScreenValidator screenValidator();
}
