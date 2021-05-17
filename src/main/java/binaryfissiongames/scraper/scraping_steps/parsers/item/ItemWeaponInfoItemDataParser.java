package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.item_scraping.items.ItemWeaponInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemWeaponInfoItemDataParser implements ItemDataParser<ItemWeaponInfo> {
    private static final String NBT_DAMAGE_TAG = "stellar_extra_damage";
    private static final String NBT_FORMULA_TAG = "stellar_extra_strengthFormula";
    private static final String NBT_SPEED_TAG = "stellar_extra_genericAttackSpeed";
    private static final String NBT_PROJECTILE_TAG = "stellar_extra_projectile";
    private static final String NBT_TWO_HANDS_TAG = "stellar_extra_twoHands";
    private static final String NBT_TRANSFORM_TAG = "stellar_extra_transformTo";

    @Override
    public boolean isItemStackValid(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        if (nbt == null) {
            return false;
        }
        return nbt.contains(NBT_DAMAGE_TAG) ||
                nbt.contains(NBT_FORMULA_TAG) ||
                nbt.contains(NBT_SPEED_TAG) ||
                nbt.contains(NBT_PROJECTILE_TAG) ||
                nbt.contains(NBT_TWO_HANDS_TAG) ||
                nbt.contains(NBT_TRANSFORM_TAG);
    }

    @Override
    public ItemWeaponInfo parse(ItemStack stack) {
        CompoundNBT nbt = stack.getTag();
        ItemWeaponInfo info = new ItemWeaponInfo();

        if (nbt.contains(NBT_DAMAGE_TAG)) {
            info.damageDealt = nbt.getInt(NBT_DAMAGE_TAG);
        } else {
            info.damageDealt = 0;
        }

        if (nbt.contains(NBT_FORMULA_TAG)) {
            info.strengthFormula = nbt.getString(NBT_FORMULA_TAG);
        }

        if (nbt.contains(NBT_SPEED_TAG)) {
            info.attackSpeed = nbt.getFloat(NBT_SPEED_TAG);
        } else {
            info.attackSpeed = 0;
        }

        if (nbt.contains(NBT_PROJECTILE_TAG)) {
            info.projectile = nbt.getString(NBT_PROJECTILE_TAG);
        }

        if (nbt.contains(NBT_TWO_HANDS_TAG)) {
            info.twoHanded = nbt.getBoolean(NBT_TWO_HANDS_TAG);
        } else {
            info.twoHanded = false;
        }

        if (nbt.contains(NBT_TRANSFORM_TAG)) {
            info.transformsInto = nbt.getString(NBT_TRANSFORM_TAG);
        }

        return info;
    }
}
