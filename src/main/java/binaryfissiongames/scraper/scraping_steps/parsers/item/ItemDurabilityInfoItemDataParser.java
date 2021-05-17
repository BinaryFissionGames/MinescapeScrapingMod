package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.item_scraping.items.ItemDurabilityInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemDurabilityInfoItemDataParser implements ItemDataParser<ItemDurabilityInfo> {
    private static final String NBT_DURABILITY_TAG = "stellar_extra_durability";
    private static final String NBT_CHARGE_TAG = "stellar_extra_charge";

    @Override
    public boolean isItemStackValid(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if(nbt == null){
            return false;
        }

        return nbt.contains(NBT_DURABILITY_TAG) || nbt.contains(NBT_CHARGE_TAG);
    }

    @Override
    public ItemDurabilityInfo parse(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if(nbt == null){
            return null;
        }

        ItemDurabilityInfo info = new ItemDurabilityInfo();

        if(nbt.contains(NBT_DURABILITY_TAG)){
            info.durability = nbt.getInt(NBT_DURABILITY_TAG);
        }

        if(nbt.contains(NBT_CHARGE_TAG)){
            info.charges = nbt.getInt(NBT_CHARGE_TAG);
        }

        return info;
    }
}
