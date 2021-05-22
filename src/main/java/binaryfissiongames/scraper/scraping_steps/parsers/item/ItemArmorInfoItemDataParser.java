package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.MinescapeScrapingMod;
import binaryfissiongames.scraper.item_scraping.items.ItemArmorInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ItemArmorInfoItemDataParser implements ItemDataParser<ItemArmorInfo> {
    private static final String MAGIC_PROTECTION_NBT_KEY = "stellar_extra_protectionMagic";
    private static final String MELEE_PROTECTION_NBT_KEY = "stellar_extra_protectionMelee";
    private static final String RANGED_PROTECTION_NBT_KEY = "stellar_extra_protectionRanged";
    private static final String DRAGONFIRE_PROTECTION_NBT_KEY = "stellar_extra_protectionDragonFire";
    private static final Pattern DAMAGE_BUFFER_TOOLTIP_REGEX = Pattern.compile("Damage buffer: ([+-]?\\d+)%깻 ([+-]?\\d+)%깳 ([+-]?\\d+)%깷");

    @Override
    public boolean isItemStackValid(ItemStack stack) {
        Screen currentScreen = Minecraft.getInstance().currentScreen;
        if (!(currentScreen instanceof ChestScreen)) return false;

        ChestScreen invScreen = (ChestScreen) currentScreen;

        CompoundNBT nbtData = stack.getTag();
        if(nbtData == null) return false;

        boolean containsNbtKeys =
                nbtData.contains(MAGIC_PROTECTION_NBT_KEY) &&
                        nbtData.contains(MELEE_PROTECTION_NBT_KEY) &&
                        nbtData.contains(RANGED_PROTECTION_NBT_KEY);

        boolean containsDamageBufferTooltip = invScreen.getTooltipFromItem(stack)
                .stream()
                .anyMatch(
                        (tt) -> DAMAGE_BUFFER_TOOLTIP_REGEX.matcher(TextFormatting.getTextWithoutFormattingCodes(tt.getUnformattedComponentText())).find()
                );

        return containsNbtKeys || containsDamageBufferTooltip;
    }

    @Override
    public ItemArmorInfo parse(ItemStack stack) {
        CompoundNBT nbtData = stack.getTag();
        ItemArmorInfo info = new ItemArmorInfo();
        Screen currentScreen = Minecraft.getInstance().currentScreen;
        ChestScreen invScreen = (ChestScreen) currentScreen;

        boolean containsNbtKeys =
                nbtData.contains(MAGIC_PROTECTION_NBT_KEY) &&
                        nbtData.contains(MELEE_PROTECTION_NBT_KEY) &&
                        nbtData.contains(RANGED_PROTECTION_NBT_KEY);

        if(containsNbtKeys){
            info.magicProtection = nbtData.getInt(MAGIC_PROTECTION_NBT_KEY);
            info.meleeProtection = nbtData.getInt(MELEE_PROTECTION_NBT_KEY);
            info.rangedProtection = nbtData.getInt(RANGED_PROTECTION_NBT_KEY);
        }

        if(nbtData.contains(DRAGONFIRE_PROTECTION_NBT_KEY)){
            info.dragonfireProtection = nbtData.getInt(DRAGONFIRE_PROTECTION_NBT_KEY);
        }

        Optional<ITextComponent> matchedTextOpt = invScreen
                .getTooltipFromItem(stack)
                .stream()
                .filter(
                        (tt) -> DAMAGE_BUFFER_TOOLTIP_REGEX.matcher(TextFormatting.getTextWithoutFormattingCodes(tt.getUnformattedComponentText())).find()
                ).findAny();

        if(matchedTextOpt.isPresent()){
            ITextComponent text = matchedTextOpt.get();
            Matcher matcher = DAMAGE_BUFFER_TOOLTIP_REGEX.matcher(TextFormatting.getTextWithoutFormattingCodes(text.getUnformattedComponentText()));
            matcher.find();
            info.meleeBuffer = Integer.parseInt(matcher.group(1));
            info.magicBuffer = Integer.parseInt(matcher.group(2));
            info.rangedBuffer = Integer.parseInt(matcher.group(3));
        }

        return info;
    }
}
