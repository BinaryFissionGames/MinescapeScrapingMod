package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.item_scraping.items.MiscItemInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class MiscItemInfoItemDataParser implements ItemDataParser<MiscItemInfo> {
    private static final String NBT_GO_TO_BANK_TAG = "stellar_extra_goToBankOnDeath";
    private static final String NBT_ALLOWED_IN_BP_TAG = "stellar_extra_canBeStoredInPouch";
    private static final String NBT_UNTRADEABLE_TAG = "stellar_extra_untradable";
    private static final String NBT_SELECTABLE_TAG = "stellar_extra_isSelectable";
    private static final String NBT_STACKABLE_TAG = "stellar_extra_customStacking";
    private static final String NBT_GLOWING_TAG = "stellar_extra_glowing";

    @Override
    public boolean isItemStackValid(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if (nbt == null) {
            return false;
        }
        return nbt.contains(NBT_GO_TO_BANK_TAG) ||
                nbt.contains(NBT_ALLOWED_IN_BP_TAG) ||
                nbt.contains(NBT_UNTRADEABLE_TAG) ||
                nbt.contains(NBT_SELECTABLE_TAG) ||
                nbt.contains(NBT_STACKABLE_TAG) ||
                nbt.contains(NBT_GLOWING_TAG);
    }

    @Override
    public MiscItemInfo parse(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        MiscItemInfo info = new MiscItemInfo();

        if (nbt.contains(NBT_GO_TO_BANK_TAG)) {
            info.goesToBankOnDeath = nbt.getBoolean(NBT_GO_TO_BANK_TAG);
        } else {
            info.goesToBankOnDeath = false;
        }

        if (nbt.contains(NBT_ALLOWED_IN_BP_TAG)) {
            info.canBeStoredInPouch = nbt.getBoolean(NBT_ALLOWED_IN_BP_TAG);
        } else {
            info.canBeStoredInPouch = true;
        }

        if (nbt.contains(NBT_UNTRADEABLE_TAG)) {
            info.untradeable = nbt.getBoolean(NBT_UNTRADEABLE_TAG);
        } else {
            info.untradeable = false;
        }

        if (nbt.contains(NBT_SELECTABLE_TAG)) {
            info.selectable = nbt.getBoolean(NBT_SELECTABLE_TAG);
        } else {
            info.selectable = false;
        }

        if (nbt.contains(NBT_STACKABLE_TAG)) {
            info.stackable = nbt.getBoolean(NBT_STACKABLE_TAG);
        } else {
            info.stackable = false;
        }

        if (nbt.contains(NBT_GLOWING_TAG)) {
            info.glowing = nbt.getBoolean(NBT_GLOWING_TAG);
        } else {
            info.glowing = false;
        }

        return info;
    }
}
