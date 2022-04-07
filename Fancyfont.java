import greenfoot.Font;
import greenfoot.util.GreenfootUtil;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;

/**
 * Loads a .ttf font file from the 'resources' folder
 *
 * @author Sammy.K
 * @version 1.0
 */
public class Fancyfont {
    public static Font loadFont(String path, float size) {
        Font font = new Font((int) size);
        URL url;
        /* Trys to get the file */
        try {
            url = GreenfootUtil.getURL(path, "resources");
        } catch (FileNotFoundException var3) {
            /* Shows error & returns default font due to missing file */
            var3.printStackTrace();
            return font;
        }
        /* Trys to load the font & replace the font in 'font' with reflections */
        try {
            Field pFont = Font.class.getDeclaredField("font");
            pFont.setAccessible(true);
            java.awt.Font uniFont = java.awt.Font.createFont(0, url.openStream());
            uniFont = uniFont.deriveFont(size);
            pFont.set(font, uniFont);
            pFont.setAccessible(false);
        } catch (IllegalAccessException | FontFormatException | IOException | NoSuchFieldException var6) {
            var6.printStackTrace();
        }

        return font;
    }
}
