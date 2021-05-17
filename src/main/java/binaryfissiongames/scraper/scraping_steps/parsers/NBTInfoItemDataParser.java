package binaryfissiongames.scraper.scraping_steps.parsers;

import binaryfissiongames.scraper.item_scraping.NBTInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class NBTInfoItemDataParser implements ItemDataParser<NBTInfo> {
    @Override
    public boolean isItemStackValid(ItemStack stack) {
        return stack.getTag() != null;
    }

    @Override
    public NBTInfo parse(ItemStack stack) {
        NBTInfo info = new NBTInfo();
        info.nbtInfo = stack.getTag().toString();
        return info;
    }
}
