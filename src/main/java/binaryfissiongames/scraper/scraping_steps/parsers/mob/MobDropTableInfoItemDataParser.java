package binaryfissiongames.scraper.scraping_steps.parsers.mob;

import binaryfissiongames.scraper.item_scraping.mobs.MobDropTableInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import binaryfissiongames.scraper.scraping_steps.parsers.helper.MobRegexes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.item.ItemStack;

import java.util.Objects;
import java.util.stream.Collectors;

public class MobDropTableInfoItemDataParser implements ItemDataParser<MobDropTableInfo> {
    @Override
    public boolean isItemStackValid(ItemStack stack) {
        return Minecraft.getInstance().currentScreen instanceof ChestScreen;
    }

    @Override
    public MobDropTableInfo parse(ItemStack stack) {
        MobDropTableInfo info = new MobDropTableInfo();

        Screen currentScreen = Minecraft.getInstance().currentScreen;
        ChestScreen chestScreen = (ChestScreen) currentScreen;

        info.drops = chestScreen.getTooltipFromItem(stack).stream()
                .map(MobRegexes::getDropTableEntryFromTooltipText)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return info;
    }
}
