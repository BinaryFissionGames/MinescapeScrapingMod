package binaryfissiongames.scraper.scraping_steps.parsers;

import binaryfissiongames.scraper.MinescapeScrapingMod;
import binaryfissiongames.scraper.item_scraping.items.ItemCustomSkinInfo;
import binaryfissiongames.scraper.scraping_steps.ItemDataParser;
import binaryfissiongames.scraper.scraping_steps.parsers.helper.SkinValue;
import com.google.gson.Gson;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TextFormatting;

import java.util.Base64;

public class ItemCustomSkinInfoItemDataParser implements ItemDataParser<ItemCustomSkinInfo> {
    private static final String SKIN_VALUE_NBT_KEY = "stellar_extra_java-edition.skin-value";
    @Override
    public boolean isItemStackValid(ItemStack stack) {
        CompoundNBT nbtData = stack.getTag();
        if(nbtData == null) return false;

        if(nbtData.contains(SKIN_VALUE_NBT_KEY)){
            MinescapeScrapingMod.LOGGER.info("Stack of item (" +
                    TextFormatting.getTextWithoutFormattingCodes(stack.getDisplayName().getUnformattedComponentText()) +
                    ") has NBT tag indicating a downloadable skin.");
            return true;
        }

        return false;
    }

    @Override
    public ItemCustomSkinInfo parse(ItemStack stack) {
        CompoundNBT nbtData = stack.getTag();
        String b64EncodedJson = nbtData.getString(SKIN_VALUE_NBT_KEY);
        String jsonString = new String(Base64.getDecoder().decode(b64EncodedJson));

        MinescapeScrapingMod.LOGGER.info("Skin string: " + jsonString);

        Gson gson = new Gson();
        SkinValue skinValue = gson.fromJson(jsonString, SkinValue.class);

        ItemCustomSkinInfo customSkinInfo = new ItemCustomSkinInfo();
        customSkinInfo.skinUrl = skinValue.textures.SKIN.url;

        return customSkinInfo;
    }
}
