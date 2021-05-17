package binaryfissiongames.scraper.config.screen;

import binaryfissiongames.scraper.MinescapeScrapingMod;
import binaryfissiongames.scraper.Util;
import binaryfissiongames.scraper.config.MinescapeScrapingModConfigController;
import binaryfissiongames.scraper.config.annotation.MinescapeScrapingModParserConfigCategoryAnnotation;
import binaryfissiongames.scraper.config.annotation.MinescapeScrapingModParserConfigOption;
import binaryfissiongames.scraper.config.MinescapeScrapingModConfig;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.util.text.TranslationTextComponent;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MinescapeScrapingModConfigScreen extends Screen {
    private static final int TITLE_HEIGHT = 8;
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int DONE_BUTTON_TOP_OFFSET = 26;

    private ScrapingOptionsRowList scrapingOptionsRowList;

    private final Screen parentScreen;

    public MinescapeScrapingModConfigScreen(Screen parentScreen) {
        super(new TranslationTextComponent("minescapescraping.config.title"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        this.scrapingOptionsRowList = new ScrapingOptionsRowList(
                this.minecraft, this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        //Add all out category options
        MinescapeScrapingModConfig config = MinescapeScrapingModConfigController.getConfig();
        for (Field f : MinescapeScrapingModConfig.class.getFields()) {
            MinescapeScrapingModParserConfigCategoryAnnotation configAnnotation = f.getAnnotation(MinescapeScrapingModParserConfigCategoryAnnotation.class);
            if (configAnnotation != null) {
                List<AbstractOption> options = new ArrayList<>();
                if (!configAnnotation.enableFieldName().equals("none")) {
                    /* Add enable option to enable/disable this scraper. */
                    options.add(new BooleanOption(
                            "minescapescraping.config.enable",
                            (gs) -> Util.getBooleanField(config, configAnnotation.enableFieldName()),
                            (gs, val) -> Util.setBooleanField(config, configAnnotation.enableFieldName(), val)
                    ));
                }
                Object categoryOptionObject;
                try {
                    categoryOptionObject = f.get(config);
                } catch (IllegalAccessException e) {
                    MinescapeScrapingMod.LOGGER.error(e);
                    return;
                }

                /* Check for scraper configs in the sub-fields of this field */
                for(Field parserEnableField : f.getType().getFields()){
                    MinescapeScrapingModParserConfigOption parserConfigAnnotation = parserEnableField.getAnnotation(MinescapeScrapingModParserConfigOption.class);
                    if(parserConfigAnnotation != null && (parserEnableField.getType().equals(boolean.class) || parserEnableField.getType().equals(Boolean.class))){
                        /* Valid annotation; We should add this as a boolean option.*/
                        options.add(new BooleanOption(
                                parserConfigAnnotation.descriptionTransKey(),
                                (gs) -> Util.getBooleanField(categoryOptionObject, parserEnableField.getName()),
                                (gs, val) -> Util.setBooleanField(categoryOptionObject, parserEnableField.getName(), val)
                        ));
                    }
                }

                //Add label, then add options!
                scrapingOptionsRowList.addCategoryLabel(new TranslationTextComponent(configAnnotation.categoryTransKey()));
                scrapingOptionsRowList.addOptions(options.toArray(new AbstractOption[0]));
            }
        }

        this.children.add(this.scrapingOptionsRowList);

        //Done button
        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                new TranslationTextComponent("gui.done"),
                button -> this.closeScreen()
        ));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);

        this.scrapingOptionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);

        drawCenteredString(matrixStack, this.font, I18n.format(((TranslationTextComponent) this.title).getKey()),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);

        //Draw buttons ("Done" button)
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        //Save changed settings
        try {
            MinescapeScrapingModConfigController.saveConfig();
        } catch (IOException e) {
            MinescapeScrapingMod.LOGGER.error("Failed to save config!", e);
        }
    }

    @Override
    public void closeScreen() {
        this.minecraft.displayGuiScreen(parentScreen);
    }
}
