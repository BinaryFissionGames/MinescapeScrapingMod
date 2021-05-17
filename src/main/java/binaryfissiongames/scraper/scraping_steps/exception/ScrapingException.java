package binaryfissiongames.scraper.scraping_steps.exception;

public class ScrapingException extends Exception {
    private final String humanReadableMessage;

    public ScrapingException(String internalMessage){
        this(internalMessage, internalMessage);
    }

    public ScrapingException(String internalMessage, Exception cause){
        this(internalMessage, internalMessage,  cause);
    }

    public ScrapingException(String internalMessage, String shownMessage){
        super(internalMessage);
        this.humanReadableMessage = shownMessage;
    }

    public ScrapingException(String internalMessage, String shownMessage, Exception cause){
        super(internalMessage, cause);
        this.humanReadableMessage = shownMessage;
    }

    public String getHumanReadableMessage(){
        return humanReadableMessage;
    }
}
