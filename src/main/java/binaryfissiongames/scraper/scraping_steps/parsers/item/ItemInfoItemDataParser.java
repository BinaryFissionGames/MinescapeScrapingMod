package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.Util;
import binaryfissiongames.scraper.item_scraping.items.ItemInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemInfoItemDataParser implements ItemDataParser<ItemInfo> {
    private static final String ID_NBT_TAG = "stellar_id";

    @Override
    public boolean isItemStackValid(ItemStack stack) {
        Screen currentScreen = Minecraft.getInstance().currentScreen;
        return currentScreen instanceof ChestScreen; // We can only parse if we are in a chestscreen
    }

    @Override
    public ItemInfo parse(ItemStack itemStack) {
        Screen currentScreen = Minecraft.getInstance().currentScreen;
        ChestScreen invScreen = (ChestScreen) currentScreen;

        assert invScreen != null;

        ItemInfo info = new ItemInfo();

        info.itemNameRaw = itemStack.getDisplayName().getString();
        info.itemName = Util.stripFormatting(itemStack.getDisplayName().getUnformattedComponentText());
        info.translationKey = itemStack.getTranslationKey();
        info.itemDamage = itemStack.getDamage();
        info.itemMaxDamage = itemStack.getMaxDamage();

        CompoundNBT nbt = itemStack.getTag();
        if(nbt != null && nbt.contains(ID_NBT_TAG)){
            info.internalId = nbt.getString(ID_NBT_TAG);
        }

        return info;
    }
}
