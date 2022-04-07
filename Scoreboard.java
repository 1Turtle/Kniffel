import greenfoot.*;

import java.util.List;

public class Scoreboard extends Actor {
    private final GreenfootImage oddLine = new GreenfootImage("images/line_odd.png");
    private final GreenfootImage evenLine = new GreenfootImage("images/line_even.png");
    private GreenfootImage bg = new GreenfootImage("images/scoreboard.png");
    private static Font font = Fancyfont.loadFont("PublicPixel.ttf", 15.0F);
    private char mode = 't';
    private int posY = 0;
    private boolean lastClick = false;
    public static final int WIDTH = 27;
    public static final int HEIGHT = 9;
    public Color button = Color.PINK;

    public Scoreboard() {
        this.bg.setFont(this.font);
        this.bg.setColor(Color.LIGHT_GRAY);
    }

    /**
     * Handles mouse clicks!
     * Depending on current mode, line 8 will act as a button & reset stuff.
     */
    public void act() {
        boolean isGameOver = false;
        Table w = (Table) getWorld();
        try {
            isGameOver = w.isGameOver;
        } catch (NullPointerException e) {
        }
        /* Trys to handle mouse clicks */
        try {
            MouseInfo m = Greenfoot.getMouseInfo();
            button = Color.PINK;
            /* Set button color depending on mouse pos & reset if clicked */
            if (m.getX() > 4 && m.getX() < 8 && m.getY() == 5 && (mode == 's' || mode == 'r')) {
                button = Color.MAGENTA;
                if (m.getButton() == 1)
                    if (!lastClick) {
                        lastClick = true;
                        Table t = (Table) getWorld();
                        if (mode == 's') {
                            if (isGameOver) {
                                setMode('r');
                                return;
                            }
                            t.reset();
                        } else if (mode == 'r') {
                            t.reset();
                        }
                    } else lastClick = false;
            }
            /* refresh scoreboard */
            if (mode == 's')
                showScore(CheckDices.getLastType(), CheckDices.getLastScore());
            else showRang();
        } catch (NullPointerException e) {
        }
    }

    /**
     * Sets the mode for the score.
     *
     * @param m new mode
     */
    public void setMode(char m) {
        mode = m;
    }

    /**
     * Gets the current mode of the scorebaord.
     *
     * @return current mode
     */
    public char getMode() {
        return mode;
    }

    /**
     * Set current line pos of scoreboard, where it then will write
     *
     * @param y
     */
    public void setLinePos(int y) {
        if (y >= 0 && y < HEIGHT+1) {
            this.posY = y;
        } else if (y >= HEIGHT+1) {
            this.posY = HEIGHT;
        } else {
            this.posY = 0;
        }

    }

    /**
     * Clears the whole scorebaord.
     */
    public void clear() {
        int pos = 2;

        for (int i = 0; i <= HEIGHT; ++i) {
            this.bg.drawImage(this.getLineBG(i), 0, pos);
            pos += 17;
        }

        this.setImage(this.bg);
        setLinePos(0);
        setColor(Color.WHITE);
    }

    /**
     * Writes on the current line the given String.
     *
     * @param str message
     */
    public void write(String str) {
        this.bg.drawImage(this.getLineBG(this.posY), 0, 17 * this.posY + 2);
        this.bg.drawString(str, 8, 17 * (this.posY + 1));
        this.setImage(this.bg);
    }

    /**
     * Writes as 'write' do, but also jumps to the next line.
     *
     * @param str message
     */
    public void print(String str) {
        this.write(str);
        int pos = this.posY + 1;
        if (pos > HEIGHT) {
            pos = 0;
        }

        this.setLinePos(pos);
    }

    /**
     * Gets the background of a line depending on posY being odd or even.
     *
     * @param posY line pos
     * @return 'evenLine' || 'oddLine'
     */
    public GreenfootImage getLineBG(int posY) {
        return posY % 2 != 0 ? this.evenLine : this.oddLine;
    }

    /**
     * Sets the current color for the font. (Doesn't change the already drawn ones)
     *
     * @param c
     */
    public void setColor(Color c) {
        this.bg.setColor(c);
    }

    /**
     * Lets the scoreboard fade in slowly & smoothly.
     */
    public void fadeIn() {
        new Thread(() -> {
            int a = 0;
            int fac = 1;
            while (a <= 255) {
                getImage().setTransparency(a);
                a += fac;
                fac += 1;
                Greenfoot.delay(1);
            }
        }).start();
    }

    /**
     * Hides the scoreboard (nearly).
     */
    public void hide() {
        getImage().setTransparency(0);
    }

    /**
     * Create the window decoration.
     *
     * @param title of window.
     */
    public void newWindow(String title) {
        clear();
        setColor(Color.LIGHT_GRAY);
        setLinePos(0);
        String line = ("=").repeat((WIDTH - title.length() - 4) / 2);
        write(line + "[ " + title + " ]=" + line);
        setLinePos(HEIGHT);
        write(("=").repeat(WIDTH));
    }

    /**
     * Shows the rang list.
     */
    public void showRang() {
        /* Layout */
        String name = "           ";
        String[] buffer = new String[10];
        List<UserInfo> users = Score.getTopFive();
        newWindow("TOP 10");
        buffer[2] = " RANG_ SCORE_ NAME________ ";
        buffer[8] = (" ").repeat(WIDTH - HEIGHT) + "[Nochmal]";
        /* Fill with data */
        int i = 3;
        for (UserInfo user : users) {
            /* RANG */
            buffer[i] = " " + (i - 2);
            buffer[i] += (" ").repeat(7 - buffer[i].length());
            /* SCORE */
            String score = ((Integer) user.getScore()).toString();
            score = ("0").repeat(6 - score.length()) + score;
            buffer[i] += score + " ";
            /* NAME */
            name = user.getUserName();
            if (name.length() > 12)
                name = name.substring(0, 11) + "..";
            buffer[i] += name;
            i++;
            if (i > 5) break;
        }
        name = Score.getUserName();
        if (name.length() > 12)
            name = name.substring(0, 11) + "..";
        /* Write */
        for (i = 0; i <= HEIGHT; i++) {
            setLinePos(i);
            if (buffer[i] != null) {
                if (i == 0 || i == HEIGHT) {
                    setColor(Color.LIGHT_GRAY);
                } else if (i == 8) {
                    setColor(button);
                } else setColor(Color.WHITE);
                if (buffer[i].contains(name))
                    setColor(new Color(255, 235, 101));
                print(buffer[i]);
            }
        }
    }

    /**
     * Shows the new (high)score.
     */
    public void showScore(String type, Integer score) {
        /* Def */
        boolean isGameOver = false;
        Table w = (Table) getWorld();
        Integer highscore = Score.getHighscore();
        String comment = "";
        String message;
        /* Init */
        setMode('s');
        if (Score.changed)
            comment = "NEU!";
        try {
            isGameOver = w.isGameOver;
        } catch (NullPointerException e) {
        }
        if (type.contains("Chance")) {
            message = " Besser als nix.";
        } else if (type == "Game Over") {
            if (Score.changed)
                message = " Gut gew\u00FCrfelt!";
            else message = " Wie schade!";
        } else {
            message = " Hurra! Was ein Gl\u00FCck!";
        }

        /* Print type & decoration */
        newWindow("ERGEBNIS");
        setColor(Color.WHITE);
        setLinePos(2);
        if (isGameOver)
            print(" --- " + type + " ---");
        else print(" +++ " + type + " +++");
        write(message);
        setLinePos(5);
        /* Setup (high)score */
        String spaces1 = " ";
        int len1 = Score.score.toString().length();
        String spaces2 = " ";
        int len2 = highscore.toString().length();
        if (len1 < len2) {
            spaces1 = spaces1.repeat(len2 - len1 + 1);
        } else if (len1 > len2) {
            spaces2 = spaces2.repeat(len1 - len2 + 1);
        }
        /* Write (high)score */
        setColor(new Color(255, 235, 101));
        print("     Score:" + spaces1 + Score.score + " (" + score + ")");
        write(" Highscore:" + spaces2 + highscore + " " + comment);

        setLinePos(8);
        setColor(button);
        String btn = "[Weiter]";
        if (isGameOver)
            btn = "[Ranking]";
        print((" ").repeat(WIDTH - btn.length()) + btn);
    }

    /**
     * Draws the title screen on the 'Scoreboard'
     */
    public void createTitlescreen() {
        /* buffer */
        setMode('t');
        String[] lines = {
                "=Ein========================", "K K NN N III FFF FFF EEE L  ", "KK  NN N  I  FF  FF  EE  L  ", "K K N NN  I  F   F   E   L  ", "K K N  N III F   F   EEE LLL", "-\u00E4hnliches-Spiel------------", "", "", "", "============================="};
        /* Writes the buffer */
        int y = 0;
        for (String line : lines) {
            if (y == 0 || y == 5 || y == HEIGHT)
                setColor(new Color(100, 100, 100));
            else
                setColor(new Color(255, 255, 255));
            setLinePos(y);
            y++;
            write(line);
        }
        /* Writes the most important part: the credits :) */
        setMode('t');
        setColor(new Color(255, 144, 89));
        setLinePos(7);
        print((" ").repeat((28 - 17) / 2) + "von Sammy L. Koch");
    }
}