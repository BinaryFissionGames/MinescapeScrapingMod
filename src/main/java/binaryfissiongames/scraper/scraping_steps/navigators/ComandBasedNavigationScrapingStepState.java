package binaryfissiongames.scraper.scraping_steps.navigators;

public enum ComandBasedNavigationScrapingStepState {
    INITIAL,
    CLOSING_CURRENT_SCREEN,
    ENTERING_COMMAND,
    WAITING_FOR_COMMAND_TO_END,
    DONE
}
