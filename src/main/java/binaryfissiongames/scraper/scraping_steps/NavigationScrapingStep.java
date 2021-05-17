package binaryfissiongames.scraper.scraping_steps;

import binaryfissiongames.scraper.scraping_steps.exception.ScrapingException;

public interface NavigationScrapingStep {
    void tick() throws ScrapingException; // Called once per tick. Should do some stuff to make progress to navigate to the screen for scraping
    boolean isDone(); // If the navigation is done, this will be true. Otherwise, this should be false.
    void reset(); // Should reset the navigation; This will be called if navigation needs to occur again. Should change isDone() back to false, unless it's verified that no additional navigation is necessary
}
