package binaryfissiongames.scraper.scraping_steps.parsers.item;

import binaryfissiongames.scraper.item_scraping.items.ItemLevelInfo;
import binaryfissiongames.scraper.item_scraping.sub_info.LevelRequirement;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TextFormatting;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemLevelInfoItemDataParser implements ItemDataParser<ItemLevelInfo> {
    private static final String TYPE_NBT_STRING = "stellar_extra_type";
    private static final String LEVEL_NBT_STRING = "stellar_extra_level";
    private static final String TARGET_NBT_STRING = "stellar_extra_target";
    private static final String TOOL_NBT_STRING = "stellar_extra_tool";

    private static final String SPADE_TOOL_STRING = "spade";

    private static final Pattern ARMOR_REQUIREMENT_PATTERN = Pattern.compile("^Requirement:(.*)");
    private static final Pattern ARMOR_REQUIREMENT_SUB_PATTERN = Pattern.compile("^([0-9]+)([깭깵])$");

    private static final Pattern DEFENDER_REQUIREMENT_PATTERN_ATTACK = Pattern.compile("^Attack level: ([0-9]+)\\s*$");
    private static final Pattern DEFENDER_REQUIREMENT_PATTERN_ATTACK_DEFENSE = Pattern.compile("^Attack/Defence level: ([0-9]+)\\s*$");


    //TODO: MOVE THIS SOMEWHERE NEUTRAL?
    private static final Map<Character, String> SYMBOL_TO_SKILL_NAME;

    static {
        HashMap<Character, String> sym2skill = new HashMap<>();

        sym2skill.put('깭', "defence");
        sym2skill.put('깵', "prayer");

        SYMBOL_TO_SKILL_NAME = Collections.unmodifiableMap(sym2skill);
    }

    @Override
    public boolean isItemStackValid(ItemStack stack) {
        return true;
    }

    @Override
    public ItemLevelInfo parse(ItemStack stack) {
        CompoundNBT nbtData = stack.getTag();
        ItemLevelInfo info = new ItemLevelInfo();

        if (nbtData == null) {
            return null;
        }

        if (nbtData.contains(TYPE_NBT_STRING)) {
            info.type = nbtData.getString(TYPE_NBT_STRING);
        }

        if (nbtData.contains(LEVEL_NBT_STRING)) {
            info.level = nbtData.getInt(LEVEL_NBT_STRING);
        }

        if (nbtData.contains(TARGET_NBT_STRING)) {
            info.target = nbtData.getString(TARGET_NBT_STRING);
        }

        List<LevelRequirement> levelRequirements = new ArrayList<>();

        Optional<String> optionalArmorLevelReq = getStringMatchingPattern(stack, ARMOR_REQUIREMENT_PATTERN);
        Optional<String> optionalDefenderAttackLevelReq = getStringMatchingPattern(stack, DEFENDER_REQUIREMENT_PATTERN_ATTACK);
        Optional<String> optionalDefenderAttackDefLevelReq = getStringMatchingPattern(stack, DEFENDER_REQUIREMENT_PATTERN_ATTACK_DEFENSE);

        if (nbtData.contains(TOOL_NBT_STRING)) {
            /* Tool level requirements (e.g. pickaxe, woodcutting axe, prayer sceptre) */
            String toolTag = nbtData.getString(TOOL_NBT_STRING);
            if (!toolTag.equals(SPADE_TOOL_STRING)) {
                LevelRequirement req = new LevelRequirement();

                req.skill = toolTag;
                req.minLevel = info.level;

                levelRequirements.add(req);
            }
        } else if (optionalArmorLevelReq.isPresent()) {
            /* Armor level requirements */
            String armorReqString = optionalArmorLevelReq.get();
            Matcher patternMatcher = ARMOR_REQUIREMENT_PATTERN.matcher(armorReqString);

            if (!patternMatcher.find()) {
                throw new Error("Armor requirement didn't match");
            }

            String bareRequirements = patternMatcher.group(1);

            String[] reqParts = bareRequirements.split(" ");


            for (String reqPart : reqParts) {
                Matcher matcher = ARMOR_REQUIREMENT_SUB_PATTERN.matcher(reqPart.trim());
                if (matcher.find()) {
                    LevelRequirement req = new LevelRequirement();

                    req.minLevel = Integer.parseInt(matcher.group(1));
                    req.skill = SYMBOL_TO_SKILL_NAME.get(matcher.group(2).charAt(0));

                    levelRequirements.add(req);
                }
            }
        } else if (optionalDefenderAttackLevelReq.isPresent()) {
            /* Defenders */
            String defAtkLevelReqStr = optionalDefenderAttackLevelReq.get();
            Matcher matcher = DEFENDER_REQUIREMENT_PATTERN_ATTACK.matcher(defAtkLevelReqStr);

            if (!matcher.find()) {
                throw new Error("Defender requirement didn't match");
            }
            LevelRequirement req = new LevelRequirement();

            req.skill = "attack";
            req.minLevel = Integer.parseInt(matcher.group(1));

            levelRequirements.add(req);
        } else if (optionalDefenderAttackDefLevelReq.isPresent()) {
            /* Specifically for dragon defender */
            String defAtkLevelReqStr = optionalDefenderAttackDefLevelReq.get();
            Matcher matcher = DEFENDER_REQUIREMENT_PATTERN_ATTACK_DEFENSE.matcher(defAtkLevelReqStr);

            if (!matcher.find()) {
                throw new Error("Defender requirement didn't match");
            }
            LevelRequirement req1 = new LevelRequirement();

            req1.skill = "attack";
            req1.minLevel = Integer.parseInt(matcher.group(1));

            levelRequirements.add(req1);

            LevelRequirement req2 = new LevelRequirement();

            req2.skill = "defence";
            req2.minLevel = Integer.parseInt(matcher.group(1));

            levelRequirements.add(req2);
        } else if (
                nbtData.contains(TARGET_NBT_STRING) &&
                        nbtData.contains(LEVEL_NBT_STRING) &&
                        (nbtData.getString(TARGET_NBT_STRING).equals("ranged") ||
                         nbtData.getString(TARGET_NBT_STRING).equals("melee"))
        ) {
            /* Other level reqs (ranged/melee weapons?)*/
            LevelRequirement req = new LevelRequirement();

            req.minLevel = nbtData.getInt(LEVEL_NBT_STRING);

            if(nbtData.getString(TARGET_NBT_STRING).equals("ranged")) {
                req.skill = "ranged";
            } else {
                req.skill = "attack";
            }

            levelRequirements.add(req);
        }

        info.levelRequirements = levelRequirements;

        return info;
    }

    private Optional<String> getStringMatchingPattern(ItemStack stack, Pattern pattern) {
        Screen currentScreen = Minecraft.getInstance().currentScreen;
        ChestScreen invScreen = (ChestScreen) currentScreen;

        return invScreen.getTooltipFromItem(stack).stream()
                .map(tc -> TextFormatting.getTextWithoutFormattingCodes(tc.getUnformattedComponentText()))
                .filter(s -> pattern.matcher(s).find())
                .findAny();
    }
}
