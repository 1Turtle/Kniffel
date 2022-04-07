import greenfoot.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;

import static greenfoot.util.GreenfootUtil.getURL;

/**
 * Represents the table, where everything happens!
 *
 * @author Sammy.K
 * @version 1.0
 */
public class Table extends World {
    /* Sounds */
    private static String oohFile = "ooh.wav";
    private static GreenfootSound ooh = new GreenfootSound("sounds/" + oohFile);
    private static final String clapSound = "sounds/clap.wav";
    private static final String tada = "sounds/tada.wav";
    private static final String ahh = "sounds/ahh.wav";
    private static final String hey = "sounds/hey.wav";
    /* Actors */
    public static Scoreboard scoreboard = new Scoreboard();
    public Cup cup = new Cup();
    public Label attempts = new Label(" Nr", 2);
    public Label leftTries = new Label("");
    public Score score = new Score();
    public boolean isGameOver = false;


    /**
     * Sets the background & adds a cup & the score for the left attempts.
     * Also draws the title screen.
     */
    public Table() {
        super(7, 7, 60);
        setBackground("images/corkboard.png");
        setPaintOrder(Scoreboard.class, Dice.class, Shadow.class);
        attempts.setScore(1);

        init();
        addObject(new Shadow(false), 3, 3);
        scoreboard.createTitlescreen();
        addObject(scoreboard, 3, 3);
    }

    /**
     * Adds the needed objects & resets them (more or less).
     */
    public void init() {
        addObject(cup, 3, 5);
        addObject(attempts, 7, 0);
        addObject(score, 1, 0);
        addObject(leftTries, 4, 4);

        leftTries.setScore(3);
        attempts.hide();
        score.hide();
        leftTries.hide();
        cup.init();
    }

    /**
     * Hides the scoreboard & plays some animations.
     */
    public void started() {
        char mode = scoreboard.getMode();
        if (mode == 't') {
            removeObject(scoreboard);
            List s = getObjects(Shadow.class);
            if (s.size() > 0)
                removeObject((Shadow) s.get(0));
            attempts.displayScore();
            attempts.slideInAnimation();
            score.displayScore();
            score.slideInAnimation();
            leftTries.displayScore();
            leftTries.slideInAnimation();
        }
    }

    /**
     * Shows the scoreboard before getting stopped.
     */
    public void stopped() {
        if (getObjects(Scoreboard.class).size() == 0) {
            addObject(new Shadow(false), 3, 3);
            scoreboard.setMode('t');
            scoreboard.showRang();
            scoreboard.setLinePos(8);
            scoreboard.write((" ").repeat(scoreboard.WIDTH / 2 - 2) + "PAUSE");
            addObject(scoreboard, 3, 3);
            attempts.hide();
            Score.score = 0;
            score.hide();
        }
    }

    /**
     * Hard resets the Actors.
     */
    public void reset() {
        if (isGameOver) {
            Score.score = 0;
            score.update();
            attempts.setScore(0);
            Score.changed = false;
            isGameOver = false;
            CheckDices.resetChances();
        }
        List remove = getObjects(Actor.class);
        for (Object objects : remove)
            removeObject((Actor) objects);

        cup = new Cup();
        int tmp = attempts.getScore();
        attempts = new Label(" Nr", 2);
        attempts.setScore(tmp + 1);
        leftTries = new Label("");
        tmp = score.getScore();
        score = new Score();
        score.setScore(tmp);

        init();
        scoreboard.setMode('t');
        started();
    }

    /**
     * Updates all the values.
     */
    public void updateValues(String type, Integer score) {
        /* Set score */
        Score.score += score;
        Integer highscore = Score.getHighscore();
        if (Score.score > highscore) {
            Score.setHighscore(Score.score);
            Score.changed = true;
        }
        /*  Update Score Labels & hide labels */
        CheckDices.setLastType(type);
        CheckDices.setLastScore(score);
        this.score.update();
        attempts.fadeOut();
        this.score.fadeOut();
        if (type == "Game Over")
            isGameOver = true;
        /* Shows the scorebaord with the result & some sounds & animations */
        Shadow s = new Shadow(true);
        addObject(s, 3, 3);
        scoreboard.setMode('s');
        scoreboard.hide();
        addObject(scoreboard, 3, 4);
        s.fadeIn();
        scoreboard.newWindow("ERGEBNIS");
        scoreboard.fadeIn();
        if (isGameOver) {
            ooh.play();
            Greenfoot.delay(60);
            if (Score.changed)
                Greenfoot.playSound(hey);
            else Greenfoot.playSound(ahh);
        } else playResultSound(type);
        scoreboard.showScore(type, score);
    }

    /**
     * Depending on the results name, it will play a fitting sound (or not).
     *
     * @param type
     */
    public void playResultSound(String type) {
        if (("Kniffel Viererpasch Full House Gro\u00DFe Stra\u00DFe").contains(type)) {
            Greenfoot.playSound(clapSound);
        } else if (("Dreierpasch Kleine Kleine Stra\u00DFe").contains(type)) {
            Greenfoot.playSound(tada);
        }
    }
}
