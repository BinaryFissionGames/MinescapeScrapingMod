package binaryfissiongames.scraper.scraping_steps;

import binaryfissiongames.scraper.Util;
import binaryfissiongames.scraper.scraping_steps.exception.ScrapingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

public class Scraper {
    private final NavigationScrapingStep[] navigationSteps;
    private final MainScrapingStep scrapingStep;
    private final Path outFilePath;

    private ScraperState state = ScraperState.IDLE;
    private int curNavStep = 0;

    public Scraper(NavigationScrapingStep[] navigationSteps, MainScrapingStep scrapingStep, Path outFilePath) {
        this.navigationSteps = navigationSteps;
        this.scrapingStep = scrapingStep;
        this.outFilePath = outFilePath;
    }

    public void tick() throws IOException, ScrapingException {
        switch (state) {
            case IDLE:
                break;
            case NAVIGATING:
                if (curNavStep >= navigationSteps.length) {
                    state = ScraperState.SCRAPING;
                    break;
                }

                if (navigationSteps[curNavStep].isDone()) {
                    curNavStep++;
                    break;
                }

                navigationSteps[curNavStep].tick();
                break;
            case SCRAPING:
                if (scrapingStep.isDone()) {
                    writeScrapedData(scrapingStep.result());
                    state = ScraperState.DONE;
                    break;
                }

                if (scrapingStep.screenValidator().isValidScreen()) {
                    scrapingStep.tick();
                } else {
                    throw new ScrapingException("Screen changed while scraping!");
                }
                break;
            case DONE:
                break;
        }
    }

    public boolean startScraping() {
        if (isScraping()) {
            return false;
        }

        //Reset all navigation steps
        curNavStep = 0;

        for (NavigationScrapingStep step : navigationSteps) {
            step.reset();
        }

        scrapingStep.reset();

        state = ScraperState.NAVIGATING;
        return true;
    }

    public void reset() {
        state = ScraperState.IDLE;
    }

    public boolean isScraping() {
        return state != ScraperState.IDLE && state != ScraperState.DONE;
    }

    public boolean isDone(){
        return state == ScraperState.DONE;
    }

    private void writeScrapedData(List<Map<String, Object>> data) throws IOException {
        //TODO: Throw this onto a second thread, so it doesn't cause a pause while writing data.
        Files.createDirectories(outFilePath.normalize().getParent());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data);
        Files.write(outFilePath, json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
    }
}
