package binaryfissiongames.scraper.config.annotation;

import binaryfissiongames.scraper.scraping_steps.ItemDataParser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
* Annotates a boolean config field that dictates if a parser step is active or not.
* End result is that fields may be included/not included depending on if the field is true/false.
* */

@Target({
        ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinescapeScrapingModParserConfigOption {
    String descriptionTransKey();
    Class<? extends ItemDataParser<?>> parserClass();
}
