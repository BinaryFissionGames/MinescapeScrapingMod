package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.item_scraping.items.ItemSetInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemSetInfoItemDataParser implements ItemDataParser<ItemSetInfo> {
    private static final String NBT_SET_TAG = "stellar_extra_set";

    @Override
    public boolean isItemStackValid(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if (nbt == null) {
            return false;
        }

        return nbt.contains(NBT_SET_TAG);
    }

    @Override
    public ItemSetInfo parse(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();

        ItemSetInfo info = new ItemSetInfo();
        info.belongsToSet = nbt.getString(NBT_SET_TAG);

        return info;
    }
}
