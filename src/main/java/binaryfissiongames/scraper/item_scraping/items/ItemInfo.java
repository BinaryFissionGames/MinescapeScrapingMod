package binaryfissiongames.scraper.item_scraping.items;

import binaryfissiongames.scraper.scraping_steps.ScrapingData;

public class ItemInfo implements ScrapingData {
    public String itemName; // Item name with formatting stripped out
    public String itemNameRaw; // Item name with formatting codes still in-tact
    public String translationKey;
    public String internalId; /* Internal ID used by gameslabs (maybe) */
    public int itemDamage; /*Damage on the item (as in, the internal damage value on the object to make it appear with the model it does)*/
    public int itemMaxDamage; /* max damage value this item can take */
}
