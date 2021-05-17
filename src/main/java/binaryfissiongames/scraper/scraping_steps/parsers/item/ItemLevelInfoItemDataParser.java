package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.item_scraping.items.ItemLevelInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemLevelInfoItemDataParser implements ItemDataParser<ItemLevelInfo> {
    private static final String TYPE_NBT_STRING = "stellar_extra_type";
    private static final String LEVEL_NBT_STRING = "stellar_extra_level";

    @Override
    public boolean isItemStackValid(ItemStack stack) {
        CompoundNBT nbtData = stack.getTag();
        if (nbtData == null) {
            return false;
        }
        return nbtData.contains(TYPE_NBT_STRING) && nbtData.contains(LEVEL_NBT_STRING);
    }

    @Override
    public ItemLevelInfo parse(ItemStack stack) {
        CompoundNBT nbtData = stack.getTag();
        ItemLevelInfo info = new ItemLevelInfo();

        info.type = nbtData.getString(TYPE_NBT_STRING);
        info.levelReq = nbtData.getInt(LEVEL_NBT_STRING);

        return info;
    }
}
