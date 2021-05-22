package binaryfissiongames.scraper.config.data;

import binaryfissiongames.scraper.config.annotation.MinescapeScrapingModParserConfigOption;
import binaryfissiongames.scraper.scraping_steps.parsers.ItemCustomSkinInfoItemDataParser;
import binaryfissiongames.scraper.scraping_steps.parsers.NBTInfoItemDataParser;
import binaryfissiongames.scraper.scraping_steps.parsers.TooltipInfoItemDataParser;
import binaryfissiongames.scraper.scraping_steps.parsers.item.*;

public class MinescapeScrapingModItemParsingConfig {
    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeDyeableArmorItemInfo",
            parserClass = DyeableArmorItemInfoItemDataParser.class
    )
    public boolean includeDyeableArmorItemInfo = true;

    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeItemAlchInfo",
            parserClass = ItemAlchInfoItemDataParser.class
    )
    public boolean includeItemAlchInfo = true;

    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeItemArmorInfo",
            parserClass = ItemArmorInfoItemDataParser.class
    )
    public boolean includeItemArmorInfo = true;

    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeItemLevelInfo",
            parserClass = ItemLevelInfoItemDataParser.class
    )
    public boolean includeItemLevelInfo = true;

    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeItemCustomSkinInfo",
            parserClass = ItemCustomSkinInfoItemDataParser.class
    )
    public boolean includeItemCustomSkinInfo = true;

    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeGeneralItemInfo",
            parserClass = ItemInfoItemDataParser.class
    )
    public boolean includeGeneralItemInfo = true;

    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeFoodInfo",
            parserClass = ItemFoodInfoItemDataParser.class
    )
    public boolean includeFoodInfo = true;
    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeWeaponInfo",
            parserClass = ItemWeaponInfoItemDataParser.class
    )
    public boolean includeWeaponInfo = true;
    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeDurabilityInfo",
            parserClass = ItemDurabilityInfoItemDataParser.class
    )
    public boolean includeDurabilityInfo = true;
    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeSetInfo",
            parserClass = ItemSetInfoItemDataParser.class
    )
    public boolean includeSetInfo = true;
    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeMiscInfo",
            parserClass = MiscItemInfoItemDataParser.class
    )
    public boolean includeMiscInfo = true;

    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includePrayerInfo",
            parserClass = ItemPrayerInfoItemDataParser.class
    )
    public boolean includePrayerInfo = true;

    @MinescapeScrapingModParserConfigOption(
            descriptionTransKey = "minescapescraping.config.itemparser.includeToolTag",
            parserClass = ItemToolInfoItemDataParser.class
    )
    public boolean includeToolTag = true;

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
