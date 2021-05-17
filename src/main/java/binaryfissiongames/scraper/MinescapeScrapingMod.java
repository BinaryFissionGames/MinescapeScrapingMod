package binaryfissiongames.scraper;

import binaryfissiongames.scraper.config.MinescapeScrapingModConfigController;
import binaryfissiongames.scraper.config.screen.MinescapeScrapingModConfigScreen;
import binaryfissiongames.scraper.scraping_steps.Scraper;
import binaryfissiongames.scraper.scraping_steps.exception.ScrapingException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Mod("minescapescrapingmod")
public class MinescapeScrapingMod {
    public static final Logger LOGGER = LogManager.getLogger();

    private static final KeyBinding scrapeKeybind = new KeyBinding(
            "key.scrape.desc",
            KeyConflictContext.UNIVERSAL,
            KeyModifier.NONE,
            InputMappings.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.minescapescraping.category");

    public MinescapeScrapingMod() {
        //Client side only mod
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        //Register keybind(s)
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerKeybind);
        //Register config gui
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> new MinescapeScrapingModConfigScreen(screen));
    }

    private void registerKeybind(final FMLClientSetupEvent event) {
        ClientRegistry.registerKeyBinding(scrapeKeybind);
    }

    @Mod.EventBusSubscriber(
            value = {Dist.CLIENT}
    )
    public static class TickEvents {
        private static List<Scraper> scrapers = new ArrayList<>();

        private static TickEventStates state = TickEventStates.IDLE;
        private static int currentScraper = 0;

        private enum TickEventStates {
            IDLE,
            SCRAPING
        }

        @SubscribeEvent
        public static void onEvent(TickEvent event) {
            if (event.phase.equals(TickEvent.Phase.START)) {
                switch (state) {
                    case IDLE:
                        break;
                    case SCRAPING:
                        if (currentScraper >= scrapers.size()) {
                            state = TickEventStates.IDLE;
                            Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("Finished scraping; Data written to disk!"));
                            break;
                        }

                        if (scrapers.get(currentScraper).isDone()) {
                            currentScraper++;
                            break;
                        }

                        if (!scrapers.get(currentScraper).isScraping()) {
                            scrapers.get(currentScraper).startScraping();
                        }

                        try {
                            scrapers.get(currentScraper).tick();
                        } catch (ScrapingException e) {
                            LOGGER.error("Failed to scrape", e);
                            if (e.getHumanReadableMessage() != null) {
                                Util.printToChatBox("Failed to scrape: " + e.getHumanReadableMessage());
                            } else {
                                Util.printToChatBox("A scraper returned an error. Check logs for details!");
                            }
                            currentScraper++;
                        } catch (Exception | Error e) {
                            LOGGER.error("Failed to scrape", e);
                            Util.printToChatBox("A scraper returned an error. Check logs for details!");
                            currentScraper++;
                        }
                        break;
                }
            }
        }

        public static void startScraping() {
            scrapers = MinescapeScrapingModConfigController.buildScrapersFromConfig();
            currentScraper = 0;
            state = TickEventStates.SCRAPING;
        }

        public static boolean scraping() {
            return state == TickEventStates.SCRAPING;
        }
    }

    @Mod.EventBusSubscriber(
            value = {Dist.CLIENT}
    )
    public static class KeyEvents {
        @SubscribeEvent
        public static void onKeyEvent(InputEvent.KeyInputEvent event) {
            if (event.getAction() == GLFW.GLFW_RELEASE) {
                onKeyReleaseEvent(event.getKey());
            }
        }

        //TODO: Make sure modifiers are pressed/not pressed
        private static void onKeyReleaseEvent(int keyCode) {
            if (keyCode == scrapeKeybind.getKey().getKeyCode() &&
                    !(Minecraft.getInstance().currentScreen instanceof ChatScreen) &&
                    Minecraft.getInstance().player != null) {
                if (!TickEvents.scraping()) {
                    Util.printToChatBox("Starting scraping...");
                    TickEvents.startScraping();
                } else {
                    Util.printToChatBox("Already scraping!");
                }
            }
        }
    }

}
