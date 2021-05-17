package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.item_scraping.NBTInfo;
import binaryfissiongames.scraper.item_scraping.items.ItemFoodInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemFoodInfoItemDataParser implements ItemDataParser<ItemFoodInfo> {
    public final static String NBT_HEAL_TAG = "stellar_extra_regen";

    @Override
    public boolean isItemStackValid(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();

        if (nbt == null) {
            return false;
        }

        return nbt.contains(NBT_HEAL_TAG);
    }

    @Override
    public ItemFoodInfo parse(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();

        ItemFoodInfo info = new ItemFoodInfo();

        info.healAmount = nbt.getInt(NBT_HEAL_TAG);

        return info;
    }
}
