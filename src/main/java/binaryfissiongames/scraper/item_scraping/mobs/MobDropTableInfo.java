package binaryfissiongames.scraper.item_scraping.mobs;

import binaryfissiongames.scraper.item_scraping.mobs.etc.MobDropTableEntry;
import binaryfissiongames.scraper.scraping_steps.ScrapingData;

import java.util.ArrayList;
import java.util.List;

public class MobDropTableInfo implements ScrapingData {
    public List<MobDropTableEntry> drops = new ArrayList<>();
}
