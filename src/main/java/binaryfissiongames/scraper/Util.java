package binaryfissiongames.scraper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ChestScreen;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.Range;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Util {

    public static <T> boolean areListsEqual(List<T> l1, List<T> l2) {
        if (l1.size() != l2.size()) {
            return false;
        }

        for (int i = 0; i < l1.size(); i++) {
            if (!l1.get(i).equals(l2.get(i))) {
                return false;
            }
        }

        return true;
    }

    public static <T> boolean areListsEqual(List<T> l1, List<T> l2, Comparator<T> comparator) {
        if (l1.size() != l2.size()) {
            return false;
        }

        for (int i = 0; i < l1.size(); i++) {
            if (comparator.compare(l1.get(i), l2.get(i)) != 0) {
                return false;
            }
        }

        return true;
    }

    /*
    * Checks to make sure every element l1[n] != l2[n].
    * */
    public static <T> boolean areAllListElementsUnequal(List<T> l1, List<T> l2, Comparator<T> comparator) {
        for (int i = 0; i < l1.size(); i++) {
            if(i >= l2.size()){
                // Test held for all elements, but we reached the end of l2.
                // That means that all the elements in the list are, indeed, unequal.
                break;
            }
            if (comparator.compare(l1.get(i), l2.get(i)) == 0) {
                return false;
            }
        }

        return true;
    }

    public static String stripNonAscii(String s) {
        return s == null ? null : s.replaceAll("[^\\x00-\\x7F]", "");
    }

    public static String stripFormatting(String s) {
        return stripNonAscii(TextFormatting.getTextWithoutFormattingCodes(s))
                .replace(" x1", "")
                .replaceAll(" [0-9]+ $", "")
                .replace(" (unknown)", "");
    }

    public static String colorIntToString(int color) {
        return String.format("#%06X", color);
    }

    public static Map<String, Object> transformDataObjectToMap(Object o) {
        Map<String, Object> map = new HashMap<>();
        Class<?> oClass = o.getClass();
        Field[] fields = oClass.getFields();

        for (Field f : fields) {
            //Ignore transient fields.
            if (!Modifier.isTransient(f.getModifiers())) {
                try {
                    map.put(f.getName(), f.get(o));
                } catch (IllegalAccessException e) {
                    MinescapeScrapingMod.LOGGER.error("Got an exception while transforming a data into a map.");
                    MinescapeScrapingMod.LOGGER.error(e);
                }
            }
        }

        return map;
    }

    public static void printToChatBox(String s) {
        Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent(s));
    }

    public static boolean getBooleanField(Object obj, String fieldName) {
        try {
            return obj.getClass().getField(fieldName).getBoolean(obj);
        } catch (IllegalAccessException e) {
            MinescapeScrapingMod.LOGGER.error("Field " + fieldName + " on class " + obj.getClass().getCanonicalName() + " is unaccessible, defaulting to false.", e);
            return false;
        } catch (NoSuchFieldException e) {
            MinescapeScrapingMod.LOGGER.error("Field " + fieldName + " on class " + obj.getClass().getCanonicalName() + " does not exist, defaulting to false.", e);
            return false;
        }
    }

    public static void setBooleanField(Object obj, String fieldName, boolean value) {
        try {
            obj.getClass().getField(fieldName).setBoolean(obj, value);
        } catch (IllegalAccessException e) {
            MinescapeScrapingMod.LOGGER.error("Field " + fieldName + " on class " + obj.getClass().getCanonicalName() + " is unaccessible.", e);
        } catch (NoSuchFieldException e) {
            MinescapeScrapingMod.LOGGER.error("Field " + fieldName + " on class " + obj.getClass().getCanonicalName() + " does not exist.", e);
        }
    }

    public static <T> T instantiateNoArgsConstructor(Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return clazz.getConstructor().newInstance();
    }

    public static void clickSlot(Container container, int slotNumber) {
        Minecraft.getInstance().playerController
                .windowClick(container.windowId, slotNumber, 0, ClickType.PICKUP, Minecraft.getInstance().player);
    }

    public static List<ItemStack> getCurrentContainerItemStacks(Range<Integer> includeRange) {
        Screen screen = Minecraft.getInstance().currentScreen;
        ChestScreen chestScreen = (ChestScreen) screen;
        return chestScreen.getContainer().inventorySlots
                .stream()
                .filter((s) -> s.getHasStack() && includeRange.contains(s.slotNumber))
                .map(Slot::getStack)
                .collect(Collectors.toList());
    }

    public static Range<Integer> getContainerRangeExcludingLastRow(int rows){
        return Range.between(0, rows * 9 - 1);
    }
}
