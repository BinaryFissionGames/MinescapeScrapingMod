package binaryfissiongames.scraper.scraping_steps.parsers.helper;

import binaryfissiongames.scraper.Util;
import binaryfissiongames.scraper.item_scraping.mobs.MobCombatInfo;
import binaryfissiongames.scraper.item_scraping.mobs.etc.MobDropTableEntry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobRegexes {
    private static final Pattern namePattern = Pattern.compile("^(.+) ([0-9]+) ❤ ☠[0-9]+$");

    private static final Pattern dropPatternNoQuantity = Pattern.compile("^• (.+) \\((.+)\\)$");
    private static final Pattern dropPatternSingleQuantity = Pattern.compile("^• (.+) x([0-9]+) \\((.+)\\)$");
    private static final Pattern dropPatternMultiQuantity = Pattern.compile("^• (.+) [x ]?\\[([0-9]+)-([0-9]+)] \\((.+)\\)$");

    private static final Pattern combatStatsRegex = Pattern.compile("^Combat: ([0-9]+)깭 ([0-9]+)깩 ([0-9]+)깻 ([0-9]+)깳 ([0-9]+)깷$");
    private static final Pattern armorStatsRegex = Pattern.compile("^Armor: ([0-9]+)깻 ([0-9]+)깳 ([0-9]+)깷$");

    /*
        Gets mob name (pretty, as in how it displays to the player) from an item name string. Returns null if cannot be parsed.
    */
    public static String getNameFromItemName(String name) {
        name = TextFormatting.getTextWithoutFormattingCodes(name);
        Matcher match = namePattern.matcher(name);
        if (!match.find()) {
            return null;
        }

        return match.group(1);
    }

    /*
        Gets mob HP from an item name string. Returns null if cannot be parsed.
    */
    public static Integer getHitpointsFromItemName(String name) {
        name = TextFormatting.getTextWithoutFormattingCodes(name);
        Matcher match = namePattern.matcher(name);
        if (!match.find()) {
            return null;
        }

        return Integer.parseInt(match.group(2));
    }

    /*
        Get a drop table entry from a tooltip text component. Returns null if the text component cannot be parsed as a drop table entry.
    */
    public static MobDropTableEntry getDropTableEntryFromTooltipText(ITextComponent component) {
        String text = TextFormatting.getTextWithoutFormattingCodes(component.getUnformattedComponentText());
        MobDropTableEntry entry = new MobDropTableEntry();

        Matcher matcher = dropPatternMultiQuantity.matcher(text);
        if (matcher.find()) {
            int minQty = Integer.parseInt(matcher.group(2));
            int maxQty = Integer.parseInt(matcher.group(3));
            entry.item = matcher.group(1).trim();
            entry.minQuantity = minQty;
            entry.maxQuantity = maxQty;
            entry.rarity = matcher.group(4);

            return entry;
        }

        matcher = dropPatternSingleQuantity.matcher(text);
        if (matcher.find()) {
            int qty = Integer.parseInt(matcher.group(2));
            entry.item = matcher.group(1).trim();
            entry.minQuantity = qty;
            entry.maxQuantity = qty;
            entry.rarity = matcher.group(3);

            return entry;
        }

        /*Note: This pattern has to be last because anything matched by the other two patterns will be matched by this pattern*/
        matcher = dropPatternNoQuantity.matcher(text);
        if (matcher.find()) {
            entry.item = matcher.group(1).trim();
            entry.rarity = matcher.group(2);
            entry.minQuantity = 1;
            entry.maxQuantity = 1;

            return entry;
        }

        return null;
    }

    /*Try to fill the combat info with the tooltip. Does nothing if tooltip cannot be parsed*/
    public static void tryFillCombatStats(ITextComponent tooltip, MobCombatInfo combatInfo) {
        Matcher matcher = combatStatsRegex.matcher(TextFormatting.getTextWithoutFormattingCodes(tooltip.getUnformattedComponentText()));
        if (!matcher.find()) {
            return;
        }

        combatInfo.combatDefence = Integer.parseInt(matcher.group(1));
        combatInfo.combatAttack = Integer.parseInt(matcher.group(2));
        combatInfo.combatStrength = Integer.parseInt(matcher.group(3));
        combatInfo.combatMagic = Integer.parseInt(matcher.group(4));
        combatInfo.combatRanged = Integer.parseInt(matcher.group(5));
    }

    /*Try to fill the armor info with the tooltip. Does nothing if tooltip cannot be parsed*/
    public static void tryFillArmorStats(ITextComponent tooltip, MobCombatInfo combatInfo) {
        Matcher matcher = armorStatsRegex.matcher(TextFormatting.getTextWithoutFormattingCodes(tooltip.getUnformattedComponentText()));
        if (!matcher.find()) {
            return;
        }

        combatInfo.armorMelee = Integer.parseInt(matcher.group(1));
        combatInfo.armorMagic = Integer.parseInt(matcher.group(2));
        combatInfo.armorRanged = Integer.parseInt(matcher.group(3));
    }


}
