package binaryfissiongames.scraper.config.data;

import binaryfissiongames.scraper.config.annotation.MinescapeScrapingModParserConfigOption;
import binaryfissiongames.scraper.item_scraping.mobs.MobCombatInfo;
import binaryfissiongames.scraper.scraping_steps.parsers.NBTInfoItemDataParser;
import binaryfissiongames.scraper.scraping_steps.parsers.TooltipInfoItemDataParser;
import binaryfissiongames.scraper.scraping_steps.parsers.mob.GeneralMobInfoItemDataParser;
import binaryfissiongames.scraper.scraping_steps.parsers.mob.MobCombatInfoItemDataParser;
import binaryfissiongames.scraper.scraping_steps.parsers.mob.MobDropTableInfoItemDataParser;

public class MinescapeScrapingModMobParsingConfig {
    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.mobparser.includeGeneralInfo",
            parserClass = GeneralMobInfoItemDataParser.class
    )
    public boolean includeGeneralInfo = true;

    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.mobparser.includeCombatInfo",
            parserClass = MobCombatInfoItemDataParser.class
    )
    public boolean includeCombatInfo = true;

    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.mobparser.includeDropTableInfo",
            parserClass = MobDropTableInfoItemDataParser.class
    )
    public boolean includeDropTableInfo = true;

    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeNBTInfo",
            parserClass = NBTInfoItemDataParser.class
    )
    public boolean includeNBTInfo = false;

    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeTooltipInfo",
            parserClass = TooltipInfoItemDataParser.class
    )
    public boolean includeTooltipInfo = false;
}
