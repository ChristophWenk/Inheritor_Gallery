package presentationmodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class manages colors mappings and provides colors to the view.
 * The colors are based on the Material Design 400 colors.
 * https://material.io/
 */
public class ColorPM {
    private int maxColors;
    private int currentColor;
    private List<String> colorList = new ArrayList<>();
    private HashMap<String,String> objectColorMap = new HashMap<>();

    /**
     * Create the ColorPM
     */
    public ColorPM() {
        colorList.add("#EF5350");
        colorList.add("#EC407A");
        colorList.add("#AB47BC");
        colorList.add("#7E57C2");
        colorList.add("#5C6BC0");
        colorList.add("#42A5F5");
        colorList.add("#29B6F6");
        colorList.add("#26C6DA");
        colorList.add("#26A69A");
        colorList.add("#66BB6A");
        maxColors = colorList.size();
        currentColor = 0;
    }

    /**
     * Retrieves a yet unused color or resets after the last color has been provided.
     * @return The next unused color.
     */
    public String getNextColor() {
        if (currentColor < maxColors) {
            int color = currentColor;
            currentColor += 1;
            return colorList.get(color);
        }
        else {
            currentColor = 0;
            return colorList.get(currentColor);
        }
    }

    /**
     * Map an ObjectName to a color. Automatically retrieve the next color.
     * @param objectName The ObjectName that should be mapped.
     */
    public void mapColor(String objectName) {
        objectColorMap.put(objectName,getNextColor());
    }

    /**
     *
     * @return A map of ObjectNames and colors.
     */
    public HashMap<String, String> getObjectColorMap() {
        return objectColorMap;
    }
}
