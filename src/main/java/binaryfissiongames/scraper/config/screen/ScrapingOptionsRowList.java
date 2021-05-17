package binaryfissiongames.scraper.config.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.AbstractOption;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.list.AbstractOptionList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
/*
* Screen component that displays scraping options (Option blocks separated by section titles.)
* */
public class ScrapingOptionsRowList extends AbstractOptionList<ScrapingOptionsRowList.Entry>{

    public ScrapingOptionsRowList(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int itemHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, itemHeightIn);
        this.centerListVertically = false;
    }

    public void addOption(AbstractOption opt1, @Nullable AbstractOption opt2) {
        this.addEntry(ScrapingOptionsRowList.Row.create(this.minecraft.gameSettings, this.width, opt1, opt2));
    }

    public void addOptions(AbstractOption[] options) {
        for(int i = 0; i < options.length; i += 2) {
            this.addOption(options[i], i < options.length - 1 ? options[i + 1] : null);
        }
    }

    public void addCategoryLabel(ITextComponent label){
        this.addEntry(new CategoryEntry(label));
    }

    public int getRowWidth() {
        return 400;
    }

    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 32;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class Entry extends AbstractOptionList.Entry<ScrapingOptionsRowList.Entry> {
    }

    /* Essentially copied from KeyBindingList.CategoryEntry */
    @OnlyIn(Dist.CLIENT)
    public class CategoryEntry extends ScrapingOptionsRowList.Entry {
        private final ITextComponent labelText;
        private final int labelWidth;

        public CategoryEntry(ITextComponent label) {
            this.labelText = label;
            this.labelWidth = ScrapingOptionsRowList.this.minecraft.fontRenderer.getStringPropertyWidth(this.labelText);
        }

        public void render(MatrixStack matrixStack, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
            ScrapingOptionsRowList.this.minecraft.fontRenderer.func_243248_b(matrixStack, this.labelText, (float)(ScrapingOptionsRowList.this.minecraft.currentScreen.width / 2 - this.labelWidth / 2), (float)(p_230432_3_ + p_230432_6_ - 9 - 1), 16777215);
        }

        public boolean changeFocus(boolean focus) {
            return false;
        }

        public List<? extends IGuiEventListener> getEventListeners() {
            return Collections.emptyList();
        }
    }

    /* Essentially copied from OptionsRowList.Row */
    @OnlyIn(Dist.CLIENT)
    public static class Row extends ScrapingOptionsRowList.Entry {
        private final List<Widget> widgets;

        private Row(List<Widget> widgetsIn) {
            this.widgets = widgetsIn;
        }

        /**
         * Creates an options row with button for the specified option
         */
        public static ScrapingOptionsRowList.Row create(GameSettings settings, int guiWidth, AbstractOption option) {
            return new ScrapingOptionsRowList.Row(ImmutableList.of(option.createWidget(settings, guiWidth / 2 - 155, 0, 310)));
        }

        /**
         * Creates an options row with 1 or 2 buttons for specified options
         */
        public static ScrapingOptionsRowList.Row create(GameSettings settings, int guiWidth, AbstractOption leftOption, @Nullable AbstractOption rightOption) {
            Widget widget = leftOption.createWidget(settings, guiWidth / 2 - 155, 0, 150);
            return rightOption == null ? new ScrapingOptionsRowList.Row(ImmutableList.of(widget)) : new ScrapingOptionsRowList.Row(ImmutableList.of(widget, rightOption.createWidget(settings, guiWidth / 2 - 155 + 160, 0, 150)));
        }

        public void render(MatrixStack p_230432_1_, int p_230432_2_, int p_230432_3_, int p_230432_4_, int p_230432_5_, int p_230432_6_, int p_230432_7_, int p_230432_8_, boolean p_230432_9_, float p_230432_10_) {
            this.widgets.forEach((p_238519_5_) -> {
                p_238519_5_.y = p_230432_3_;
                p_238519_5_.render(p_230432_1_, p_230432_7_, p_230432_8_, p_230432_10_);
            });
        }

        public List<? extends IGuiEventListener> getEventListeners() {
            return this.widgets;
        }
    }
}
