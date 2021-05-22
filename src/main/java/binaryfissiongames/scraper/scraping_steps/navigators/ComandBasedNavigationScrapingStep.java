package binaryfissiongames.scraper.scraping_steps.navigators;

import binaryfissiongames.scraper.MinescapeScrapingMod;
import binaryfissiongames.scraper.scraping_steps.NavigationScrapingStep;
import binaryfissiongames.scraper.util.IntervalTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;

public class ComandBasedNavigationScrapingStep implements NavigationScrapingStep {
    private final String command;
    private ComandBasedNavigationScrapingStepState state = ComandBasedNavigationScrapingStepState.INITIAL;
    private IntervalTimer intervalTimer;

    //Sends a command and waits for a screen to open.
    public ComandBasedNavigationScrapingStep(String command) {
        this.command = command;
        this.intervalTimer = new IntervalTimer(2500);
    }

    @Override
    public void tick() {
        Screen currentScreen = Minecraft.getInstance().currentScreen;
        switch (state){
            case INITIAL:
                if(currentScreen == null){
                    this.state = ComandBasedNavigationScrapingStepState.ENTERING_COMMAND;
                } else {
                    this.state = ComandBasedNavigationScrapingStepState.CLOSING_CURRENT_SCREEN;
                }
                break;
            case CLOSING_CURRENT_SCREEN:
                if(currentScreen == null){
                    this.state = ComandBasedNavigationScrapingStepState.ENTERING_COMMAND;
                } else {
                    currentScreen.closeScreen();
                }
                break;
            case ENTERING_COMMAND:
                Minecraft.getInstance().player.sendChatMessage(command);
                this.intervalTimer.reset();
                this.state = ComandBasedNavigationScrapingStepState.WAITING_FOR_COMMAND_TO_END;
                break;
            case WAITING_FOR_COMMAND_TO_END:
                if(!this.intervalTimer.isDone()){
                    break;
                }

                if(currentScreen != null){
                    MinescapeScrapingMod.LOGGER.info("Navigated to screen - Title: '" + currentScreen.getTitle().getUnformattedComponentText() + "'");
                    this.state = ComandBasedNavigationScrapingStepState.DONE;
                }
                break;
            case DONE:
                break;
        }
    }

    @Override
    public boolean isDone() {
        return state == ComandBasedNavigationScrapingStepState.DONE;
    }

    @Override
    public void reset() {
        this.state = ComandBasedNavigationScrapingStepState.INITIAL;
    }
}
