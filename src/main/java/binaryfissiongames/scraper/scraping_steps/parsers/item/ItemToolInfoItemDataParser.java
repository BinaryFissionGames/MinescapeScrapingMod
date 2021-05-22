package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.item_scraping.items.ItemToolInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemToolInfoItemDataParser implements ItemDataParser<ItemToolInfo> {
    private static final String TOOL_NBT_TAG = "stellar_extra_tool";

    @Override
    public boolean isItemStackValid(ItemStack stack) {
        CompoundNBT nbtData = stack.getTag();
        if (nbtData == null) {
            return false;
        }

        return nbtData.contains(TOOL_NBT_TAG);
    }

    @Override
    public ItemToolInfo parse(ItemStack stack) {
        CompoundNBT nbtData = stack.getTag();
        if (nbtData == null) {
            return null;
        }

        ItemToolInfo info = new ItemToolInfo();
        info.toolType = nbtData.getString(TOOL_NBT_TAG);

        return info;
    }
}
