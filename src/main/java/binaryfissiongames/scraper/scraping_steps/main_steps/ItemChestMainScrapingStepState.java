package binaryfissiongames.scraper.scraping_steps.main_steps;

public enum ItemChestMainScrapingStepState {
    INITIAL,
    SCRAPING,
    WAITING_TO_PRESS_NEXT,
    WAITING_FOR_NEXT_TO_CHANGE,
    DONE
}
