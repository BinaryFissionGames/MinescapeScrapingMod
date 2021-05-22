package binaryfissiongames.scraper.item_scraping.items;

import binaryfissiongames.scraper.scraping_steps.ScrapingData;

public class ItemWeaponInfo implements ScrapingData {
    public Double damageDealt;
    public String strengthFormula;
    public Double attackSpeed;
    public String projectile; /* arrow or bolt*/
    public boolean twoHanded;
    public String transformsInto; /*What a weapon degrades into (might only be used on kalphite whip???)*/
}
