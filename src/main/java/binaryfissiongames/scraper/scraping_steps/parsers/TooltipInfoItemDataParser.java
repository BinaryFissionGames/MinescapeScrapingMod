package binaryfissiongames.scraper.scraping_steps.parsers;

import binaryfissiongames.scraper.item_scraping.TooltipInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.stream.Collectors;

public class TooltipInfoItemDataParser implements ItemDataParser<TooltipInfo> {

    @Override
    public boolean isItemStackValid(ItemStack stack) {
        Screen currentScreen = Minecraft.getInstance().currentScreen;
        return currentScreen instanceof ChestScreen; // We can only parse if we are in a chestscreen
    }

    @Override
    public TooltipInfo parse(ItemStack stack) {
        Screen currentScreen = Minecraft.getInstance().currentScreen;
        ChestScreen chestScreen = (ChestScreen) currentScreen;
        TooltipInfo info = new TooltipInfo();

        info.tooltips = chestScreen.getTooltipFromItem(stack).stream().map(ITextComponent::getUnformattedComponentText).collect(Collectors.toList());
        return info;
    }
}
