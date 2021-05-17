package binaryfissiongames.scraper.scraping_steps.parsers.mob;

import binaryfissiongames.scraper.item_scraping.mobs.MobCombatInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import binaryfissiongames.scraper.scraping_steps.parsers.helper.MobRegexes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.item.ItemStack;

public class MobCombatInfoItemDataParser implements ItemDataParser<MobCombatInfo> {
    @Override
    public boolean isItemStackValid(ItemStack stack) {
        return Minecraft.getInstance().currentScreen instanceof ChestScreen;
    }

    @Override
    public MobCombatInfo parse(ItemStack stack) {
        MobCombatInfo info = new MobCombatInfo();

        Screen currentScreen = Minecraft.getInstance().currentScreen;
        ChestScreen chestScreen = (ChestScreen) currentScreen;

        chestScreen.getTooltipFromItem(stack)
                .forEach(tt -> {
                    MobRegexes.tryFillCombatStats(tt, info);
                    MobRegexes.tryFillArmorStats(tt, info);
                });

        Integer hp = MobRegexes.getHitpointsFromItemName(stack.getDisplayName().getUnformattedComponentText());
        if(hp != null){
            info.hitpoints = hp;
        }

        return info;
    }
}
