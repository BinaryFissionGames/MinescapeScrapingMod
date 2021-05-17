package binaryfissiongames.scraper.scraping_steps.validators;

import binaryfissiongames.scraper.MinescapeScrapingMod;
import binaryfissiongames.scraper.Util;
import binaryfissiongames.scraper.scraping_steps.ScreenValidator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.util.text.TextFormatting;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.regex.Pattern;

public class GenericChestValidator implements ScreenValidator {
    private final int rows;
    private final Pattern titlePattern;

    public GenericChestValidator(int rows, Pattern titlePattern) {
        this.rows = rows;
        this.titlePattern = titlePattern;
    }

    @Override
    public boolean isValidScreen() {
        Screen currentScreen = Minecraft.getInstance().currentScreen;

        if (!(currentScreen instanceof ChestScreen)){
            if(currentScreen != null){
                MinescapeScrapingMod.LOGGER.warn("Screen '" + currentScreen.getTitle().getUnformattedComponentText() + "' is not a ChestScreen!");
            } else {
                MinescapeScrapingMod.LOGGER.warn("No screen is up!");
            }
            return false;
        }

        ChestScreen chestScreen = (ChestScreen) currentScreen;
        String title = TextFormatting.getTextWithoutFormattingCodes(currentScreen.getTitle().getUnformattedComponentText());

        if(!this.titlePattern.matcher(title).matches()){
            MinescapeScrapingMod.LOGGER.warn("Screen '" + title + "' does not match pattern '" + titlePattern.pattern() + "'");
            return false;
        }

        if(!(chestScreen.getContainer().getNumRows() == rows)){
            MinescapeScrapingMod.LOGGER.warn("Screen '" + title + "' has " + chestScreen.getContainer().getNumRows() + " rows, but should have " + rows + ".");
            return false;
        }

        return true;
    }
}
