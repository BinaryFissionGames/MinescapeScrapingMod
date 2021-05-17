package binaryfissiongames.scraper.scraping_steps.navigators;

import binaryfissiongames.scraper.Util;
import binaryfissiongames.scraper.scraping_steps.NavigationScrapingStep;
import binaryfissiongames.scraper.scraping_steps.exception.ScrapingException;
import binaryfissiongames.scraper.util.IntervalTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.inventory.container.ChestContainer;

public class SlotClickBasedNavigationScrapingStep implements NavigationScrapingStep {
    private SlotClickBasedNavigationScrapingStepState state = SlotClickBasedNavigationScrapingStepState.INITIAL;
    private final IntervalTimer beforeTimer;
    private final IntervalTimer afterTimer;
    private final int slotNumber;

    public SlotClickBasedNavigationScrapingStep(int slotNumber) {
        this(slotNumber, 1000);
    }

    public SlotClickBasedNavigationScrapingStep(int slotNumber, long waitMs) {
        this.slotNumber = slotNumber;
        if(waitMs < 500){
            waitMs = 500; // we need to wait at least 500 ms total between commands, as a courtesy
            // (and also to not get kicked for spamming)
        }
        beforeTimer = new IntervalTimer(waitMs / 2);
        // Note about +1 here; This actually doesn't really matter, but it's technically more correct, since otherwise
        // odd numbers would wait one ms less (again, imperceptible, but technically correct )
        afterTimer = new IntervalTimer((waitMs + 1) / 2);
    }

    @Override
    public void tick() throws ScrapingException {
        Screen screen = Minecraft.getInstance().currentScreen;
        if(!(screen instanceof ChestScreen)){
            throw new ScrapingException("Expected ChestScreen, but didn't find one!");
        }
        ChestScreen chestScreen = (ChestScreen) screen;
        ChestContainer container = chestScreen.getContainer();

        if(container.getNumRows() * 9 <= slotNumber){
            throw new ScrapingException("Wanted to press slot " + slotNumber + ", but chest is too small!");
        }

        switch (state){
            case INITIAL:
                beforeTimer.reset();
                state = SlotClickBasedNavigationScrapingStepState.BEFORE_CLICK;
                break;
            case BEFORE_CLICK:
                if(!beforeTimer.isDone()){
                    break;
                }

                //Click slot
                Util.clickSlot(container, slotNumber);
                state = SlotClickBasedNavigationScrapingStepState.AFTER_CLICK;
                afterTimer.reset();
                break;
            case AFTER_CLICK:
                if(!afterTimer.isDone()){
                    break;
                }
                //TODO: make sure screen has changed (title?)
                state = SlotClickBasedNavigationScrapingStepState.DONE;
                break;
            case DONE:
                break;
        }
    }

    @Override
    public boolean isDone() {
        return state == SlotClickBasedNavigationScrapingStepState.DONE;
    }

    @Override
    public void reset() {
        state = SlotClickBasedNavigationScrapingStepState.INITIAL;
    }
}
