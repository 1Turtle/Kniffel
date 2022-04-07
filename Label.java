import greenfoot.*;

/**
 * Used to draw Labels with following scores.
 *
 * @author Sammy.K
 * @version 1.0
 */
public class Label extends Actor {
    private static final Font font = Fancyfont.loadFont("resources/PublicPixel.ttf", 16);
    private boolean censored = false;
    private int zeros = 1; /* How many digits are displayed */
    private Integer score = 0;
    private String message;


    public Label(String msg, int zeros) {
        message = msg;
        if (zeros > 1)
            this.zeros = zeros;
    }

    public Label(String msg) {
        message = msg;
    }

    /**
     * Shows the score.
     *
     * @param y offset
     */
    private void displayScore(int y) {
        String subNum = score.toString();
        /* Generates random number for each digit, if censored */
        if (censored) {
            subNum = "";
            for (int i = 0; i < zeros; i++)
                subNum += new java.util.Random().nextInt(10);
        } else {
            /* Add zeros if number is smaller than expected length of digits */
            if (zeros - subNum.length() > 0)
                subNum = ("0").repeat(zeros - subNum.length()) + subNum;
        }
        /* Setup image */
        GreenfootImage img = new GreenfootImage(60 * 7, 60);
        img.setFont(font);
        /* Draw label with score on image */
        img.setColor(new Color(0, 0, 0, 100)); /* transparent black */
        for (int i = 5; i > -1; i -= 5) {
            img.drawString(message + " " + subNum, 60 * 2 + 8 + i, 24 + i + y);
            img.setColor(new Color(255, 235, 101)); /* nice yellow */
        }
        setImage(img);
    }

    /**
     * Shows the score.
     */
    public void displayScore() {
        displayScore(0);
    }

    /**
     * Lets the Score slide in smoothly.
     */
    public void slideInAnimation() {
        new Thread(() -> {
            int y = -30;
            float fac = 4;
            while (y <= 0) {
                displayScore(y);
                y += (int) fac;
                fac -= 0.2;
                Greenfoot.delay(1);
            }
            displayScore();
        }).start();
    }

    /**
     * Lets the score fade out slowly & smoothly
     */
    public void fadeOut() {
        new Thread(() -> {
            int a = 255;
            int fac = 0;
            while (a >= 0) {
                getImage().setTransparency(a);
                a -= fac;
                fac++;
                Greenfoot.delay(1);
            }
        }).start();
    }

    /**
     * Sets & displays a new given number.
     *
     * @param attempts replaces the old value
     */
    public void setScore(int attempts) {
        score = attempts;
        displayScore();
    }

    /**
     * @return current displayed value
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the state of the score being censored or not.
     * If so, it will always show a different number when drawn.
     *
     * @param state new boolean for being censored or not
     */
    public void setCensorship(boolean state) {
        censored = state;
    }

    public void hide() {
        setImage(new GreenfootImage(1, 1));
    }
}
