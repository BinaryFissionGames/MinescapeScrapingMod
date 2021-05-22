package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.item_scraping.items.ItemPrayerInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemPrayerInfoItemDataParser implements ItemDataParser<ItemPrayerInfo> {
    static private final String MAX_PRAYER_POINT_INCREASE_NBT_TAG = "stellar_extra_prayerMaxPointBonus";
    static private final String PRAYER_POINT_REDUCTION_NBT_TAG = "stellar_extra_prayerPointBonus";
    @Override
    public boolean isItemStackValid(ItemStack stack) {
        CompoundNBT nbtData = stack.getTag();
        if (nbtData == null) {
            return false;
        }

        return nbtData.contains(MAX_PRAYER_POINT_INCREASE_NBT_TAG) ||
                nbtData.contains(PRAYER_POINT_REDUCTION_NBT_TAG);
    }

    @Override
    public ItemPrayerInfo parse(ItemStack stack) {
        CompoundNBT nbtData = stack.getTag();
        if (nbtData == null) {
            return null;
        }

        ItemPrayerInfo info = new ItemPrayerInfo();

        if(nbtData.contains(MAX_PRAYER_POINT_INCREASE_NBT_TAG)){
            info.maxPrayerPointsBonus = nbtData.getDouble(MAX_PRAYER_POINT_INCREASE_NBT_TAG);
        }

        if(nbtData.contains(PRAYER_POINT_REDUCTION_NBT_TAG)){
            info.prayerPointReduction = nbtData.getDouble(PRAYER_POINT_REDUCTION_NBT_TAG);
        }

        return info;
    }
}
