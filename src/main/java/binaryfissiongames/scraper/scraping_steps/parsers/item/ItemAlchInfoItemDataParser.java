package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.item_scraping.items.ItemAlchInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemAlchInfoItemDataParser implements ItemDataParser<ItemAlchInfo> {
    private static final String ALCH_NBT_TAG = "stellar_extra_alc";


    @Override
    public boolean isItemStackValid(ItemStack stack) {
        CompoundNBT nbtData = stack.getTag();
        if(nbtData == null) return false;

        return nbtData.contains(ALCH_NBT_TAG);
    }

    @Override
    public ItemAlchInfo parse(ItemStack stack) {
        CompoundNBT nbtData = stack.getTag();
        ItemAlchInfo info = new ItemAlchInfo();

        info.value = nbtData.getInt(ALCH_NBT_TAG);

        return info;
    }
}
