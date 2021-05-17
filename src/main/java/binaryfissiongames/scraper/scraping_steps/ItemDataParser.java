package binaryfissiongames.scraper.scraping_steps;

import net.minecraft.item.ItemStack;

// Interface for object that parses data off of an ItemStack.
public interface ItemDataParser<T extends ScrapingData> {
    boolean isItemStackValid(ItemStack stack);
    T parse(ItemStack stack);
}
