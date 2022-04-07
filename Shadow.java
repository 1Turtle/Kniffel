import greenfoot.*;

/**
 * Just makes the screen darker a bit :)
 *
 * @author Sammy.K
 * @version 1.0
 */
public class Shadow extends Actor {
    /**
     * Create the Acotr with being already transparent or not.
     *
     * @param transparent
     */
    public Shadow(boolean transparent) {
        GreenfootImage img = new GreenfootImage("images/shadow.png");
        if (transparent)
            img.setTransparency(1);
        setImage(img);
    }

    /**
     * Lets the shadow fade in slowly & smoothly.
     */
    public void fadeIn() {
        new Thread(() -> {
            int a = 0;
            int fac = 0;
            while (a <= 255) {
                getImage().setTransparency(a);
                a += fac;
                fac++;
                Greenfoot.delay(1);
            }
        }).start();
    }
}
