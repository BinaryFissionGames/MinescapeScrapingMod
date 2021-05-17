package binaryfissiongames.scraper.scraping_steps.main_steps;

import binaryfissiongames.scraper.MinescapeScrapingMod;
import binaryfissiongames.scraper.Util;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import binaryfissiongames.scraper.scraping_steps.MainScrapingStep;
import binaryfissiongames.scraper.scraping_steps.ScreenValidator;
import binaryfissiongames.scraper.scraping_steps.exception.ScrapingException;
import binaryfissiongames.scraper.util.IntervalTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.*;
import java.util.regex.Pattern;

public class ItemChestMainScrapingStep implements MainScrapingStep {
    private final ScreenValidator validator;
    private final List<ItemDataParser<?>> parsers;
    private int nextSlotNumber = 0;

    private final IntervalTimer beforeNextTimer;
    private final IntervalTimer afterNextTimer;


    private List<Map<String, Object>> parsedData;
    private List<String> curScreenAllItemNames = new ArrayList<>();
    private ItemChestMainScrapingStepState state = ItemChestMainScrapingStepState.INITIAL;

    private final static Pattern PREV_BUTTON_PATTERN = Pattern.compile("Previous$");
    private final static Pattern NEXT_BUTTON_PATTERN = Pattern.compile("^Next");
    private final static Pattern SORTED_BY_BUTTON_PATTERN = Pattern.compile("^Sorted by");

    //Takes in a validator (should return true throughout the WHOLE SCRAPING PROCESS, a list of parsers to use, and a waitTimeMs between hitting the "next" button.)
    public ItemChestMainScrapingStep(ScreenValidator validator, List<ItemDataParser<?>> parsers, long waitTimeMs) {
        this.validator = validator;
        this.parsers = parsers;

        // Hard-coded minimum, half a second. Don't want to push it with how fast this could send commands to the server.
        if (waitTimeMs < 500) {
            waitTimeMs = 500;
        }

        beforeNextTimer = new IntervalTimer(waitTimeMs / 2);
        afterNextTimer = new IntervalTimer((waitTimeMs + 1) / 2);
    }

    @Override
    public void tick() throws ScrapingException {
        if (!validator.isValidScreen()) {
            //Screen has changed; We need to exit exceptionally.
            this.state = ItemChestMainScrapingStepState.DONE;
            throw new ScrapingException("Screen changed during scraping!");
        }

        Screen screen = Minecraft.getInstance().currentScreen;
        ChestScreen chestScreen = (ChestScreen) screen;
        ChestContainer container = chestScreen.getContainer();

        switch (state) {
            case INITIAL:
                curScreenAllItemNames = new ArrayList<>();
                parsedData = new ArrayList<>();
                state = ItemChestMainScrapingStepState.SCRAPING;
                break;
            case SCRAPING:
                state = ItemChestMainScrapingStepState.DONE;
                int maxSlot = container.getNumRows() * 9;

                for (Slot slot : container.inventorySlots) {
                    if (slot.getHasStack()) {
                        if(slot.slotNumber >= maxSlot){
                            //If this isn't here, parsing goes on into the inventory, which is an issue.
                            MinescapeScrapingMod.LOGGER.info(slot.slotNumber + " >= " + maxSlot + ", Skipping (" + slot.getStack().getDisplayName().getUnformattedComponentText() + ")");
                            continue;
                        }

                        Map<String, Object> compositedParsePasses = new HashMap<>();
                        ItemStack stack = slot.getStack();
                        String name = TextFormatting.getTextWithoutFormattingCodes(stack.getDisplayName().getUnformattedComponentText());
                        if (PREV_BUTTON_PATTERN.matcher(name).find() || SORTED_BY_BUTTON_PATTERN.matcher(name).find()) {
                            continue;
                        }
                        if (NEXT_BUTTON_PATTERN.matcher(name).find()) {
                            //Reached next button; We'll queue to hit it.
                            MinescapeScrapingMod.LOGGER.info("Found next button in slot " + slot.slotNumber);
                            state = ItemChestMainScrapingStepState.WAITING_TO_PRESS_NEXT;
                            continue;
                        }

                        //Go through all parser stuff
                        for (ItemDataParser<?> parser : parsers) {
                            if (parser.isItemStackValid(stack)) {
                                try {
                                    compositedParsePasses.putAll(Util.transformDataObjectToMap(parser.parse(stack)));
                                } catch (Exception | Error e) {
                                    MinescapeScrapingMod.LOGGER.error("Parser pass failed...", e);
                                }
                            }
                        }
                        parsedData.add(compositedParsePasses);
                    }
                }

                if(this.state == ItemChestMainScrapingStepState.WAITING_TO_PRESS_NEXT){
                    beforeNextTimer.reset();
                }
                break;
            case WAITING_TO_PRESS_NEXT:
                //Wait
                if (beforeNextTimer.isDone()) {
                    //Click next
                    MinescapeScrapingMod.LOGGER.info("Clicking next!");
                    Optional<Slot> nextSlot = container.inventorySlots
                            .stream()
                            .filter((s) -> s.getHasStack() && NEXT_BUTTON_PATTERN.matcher(TextFormatting.getTextWithoutFormattingCodes(s.getStack().getDisplayName().getUnformattedComponentText())).find())
                            .findAny();

                    if (!nextSlot.isPresent()) {
                        state = ItemChestMainScrapingStepState.DONE;

                        throw new ScrapingException("Failed to find next slot in WAITING_TO_PRESS_NEXT!", "Couldn't find next button!");
                    }

                    nextSlotNumber = nextSlot.get().slotNumber;
                    state = ItemChestMainScrapingStepState.WAITING_FOR_NEXT_TO_CHANGE;
                    curScreenAllItemNames = Util.getCurrentContainerItemNames(nextSlotNumber);

                    Util.clickSlot(container, nextSlotNumber);

                    afterNextTimer.reset();
                }
                break;
            case WAITING_FOR_NEXT_TO_CHANGE:
                if(!afterNextTimer.isDone()){
                    break;
                }

                //Wait for items to change. When they do change, switch back to the scraping state.
                List<String> actualCurItemNames = Util.getCurrentContainerItemNames(this.nextSlotNumber);

                if (!Util.areListsEqual(curScreenAllItemNames, actualCurItemNames)) {
                    MinescapeScrapingMod.LOGGER.info("Screen changed, continuing scraping.");
                    this.state = ItemChestMainScrapingStepState.SCRAPING;
                }
                break;
            case DONE:
                break;
        }
    }

    @Override
    public boolean isDone() {
        return state == ItemChestMainScrapingStepState.DONE;
    }

    @Override
    public void reset() {
        state = ItemChestMainScrapingStepState.INITIAL;
    }

    @Override
    public List<Map<String, Object>> result() {
        return parsedData;
    }

    @Override
    public ScreenValidator screenValidator() {
        return validator;
    }


}
