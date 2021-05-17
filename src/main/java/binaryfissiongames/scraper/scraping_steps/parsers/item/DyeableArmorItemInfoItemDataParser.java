package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.Util;
import binaryfissiongames.scraper.item_scraping.items.ItemDyeableArmorInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DyeableArmorItemInfoItemDataParser implements ItemDataParser<ItemDyeableArmorInfo> {
    @Override
    public boolean isItemStackValid(ItemStack stack) {
        return stack.getItem() instanceof DyeableArmorItem;
    }

    @Override
    public ItemDyeableArmorInfo parse(ItemStack stack) {
        ItemDyeableArmorInfo info = new ItemDyeableArmorInfo();
        Item item = stack.getItem();

        if (item instanceof DyeableArmorItem) {
            DyeableArmorItem armorItem = (DyeableArmorItem) item;
            info.colorString = Util.colorIntToString(armorItem.getColor(stack));
        }

        return info;
    }
}
