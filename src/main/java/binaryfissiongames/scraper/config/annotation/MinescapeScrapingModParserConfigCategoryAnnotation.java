package binaryfissiongames.scraper.config.annotation;

import binaryfissiongames.scraper.scraping_steps.ScraperProvider;

import java.beans.Transient;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
* Annotation that defines an object to be a certain category.
* Goes on an object type that extends MinescapeScrapingModParserConfigCategory.
* A category itself can be switched on/off to completely skip the parsing steps.
* */
@Target({
        ElementType.FIELD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface MinescapeScrapingModParserConfigCategoryAnnotation {
    String categoryTransKey();
    Class<? extends ScraperProvider> scraperProvider();
    String outFile();
    String enableFieldName() default "none";
}
