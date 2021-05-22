package binaryfissiongames.scraper.item_scraping.items;

import binaryfissiongames.scraper.item_scraping.sub_info.LevelRequirement;
import binaryfissiongames.scraper.scraping_steps.ScrapingData;

import java.util.ArrayList;
import java.util.List;

public class ItemLevelInfo implements ScrapingData {
    public String type;
    public Integer level; // RAW "level" nbt field.
    public String target;
    public List<LevelRequirement> levelRequirements;
}
