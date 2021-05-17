package binaryfissiongames.scraper.scraping_steps.parsers.mob;

import binaryfissiongames.scraper.item_scraping.mobs.GeneralMobInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import binaryfissiongames.scraper.scraping_steps.parsers.helper.MobRegexes;
import net.minecraft.item.ItemStack;

public class GeneralMobInfoItemDataParser implements ItemDataParser<GeneralMobInfo> {

    @Override
    public boolean isItemStackValid(ItemStack stack) {
        return true;
    }

    @Override
    public GeneralMobInfo parse(ItemStack stack) {
        GeneralMobInfo info = new GeneralMobInfo();

        info.mobName = MobRegexes.getNameFromItemName(stack.getDisplayName().getUnformattedComponentText());

        return info;
    }
}
