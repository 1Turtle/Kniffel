import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootSound;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a cup.
 * The player can "roll" all dices with it.
 *
 * @author Sammy.K
 * @version 1.0
 */
public class Cup extends Actor {
    private GreenfootSound clickSound = new GreenfootSound("sounds/wood_click.wav");
    private final List<Dice> DICES = new ArrayList<>();
    private int hold = 0; /* The number of reported dices, that are getting hold */
    private int x, y = 0;

    /**
     * Creates the dices
     */
    public Cup() {
        setImage("images/cup.png");
        for (int i = 0; i < 5; i++) {
            Dice w = new Dice();
            DICES.add(w);
        }
    }

    /**
     * Checks for being clicked. If so, it will roll all known dices,
     * when there are enough attempts though.
     */
    public void act() {
        if (Greenfoot.mouseClicked(this))
            fancyRoll();
    }

    /**
     * Rolls all dices a few times with fancy sounds'n stuff.
     */
    public void fancyRoll() {
        Table t = (Table) getWorld();
        /* Only rolls if there are enough left tries */
        int newScore = t.leftTries.getScore();
        if (newScore > 0) {
            int fac;
            x = getX();
            y = getY();
            t.leftTries.setCensorship(true);
            /* Rolls the dices 10 times */
            for (int i = 0; i < 10; i++) {
                roll();
                /* Sets volume of sounds depending on score of dices */
                int points = getPoints();
                clickSound = new GreenfootSound("sounds/wood_click.wav");
                clickSound.setVolume(points + 70);
                clickSound.play();
                /* Sets direction of cup to move to, depending on 'i' being odd or even */
                if (i % 2 == 0)
                    fac = 1;
                else fac = -1;
                /* 'censoring' left tries & moving cup until rolling is done. */
                if (i < 9) {
                    t.leftTries.setScore(newScore - 1);
                    setLocation(x + fac, y);
                } else {
                    t.leftTries.setCensorship(false);
                    t.leftTries.setScore(newScore - 1);
                    setLocation(x, y);
                }
                Greenfoot.delay(5);
            }
        }
    }

    /**
     * Checks all dices for valid patterns & shows the scoreboard.
     * They can't be used, after that!
     * 'Table.updateValues' will do the rest then.
     */
    public void checkDices() {
        /* Make dices static & save their points in a List */
        List<Integer> p = new ArrayList<>();
        for (Dice dice : DICES) {
            p.add(dice.getPoints());
            dice.setStatic(true);
        }
        /* Checks for patterns & sends them to Table.updateValues, so that they can be further processed there. */
        Pair<String, Integer> values = CheckDices.all(p.get(0), p.get(1), p.get(2), p.get(3), p.get(4));
        Table w = (Table) getWorld();
        w.updateValues(values.getKey(), values.getValue());
    }

    /**
     * Adds the dices
     */
    public void init() {
        /* Removes left dices (should never be the case) */
        List<Dice> rDices = getWorld().getObjects(Dice.class);
        for (Dice dice : rDices)
            getWorld().removeObject(dice);
        /* Adds the dices & resets the values */
        int x = 1;
        hold = 0;
        for (Dice dice : DICES) {
            dice.init();
            getWorld().addObject(dice, x, 3);
            x++;
        }
    }

    /**
     * Gets executed by the dices, when clicked.
     * counts up or down a variable for the dices being hold or not.
     */
    public void getsHold(int fac) {
        hold += fac;
        if (hold >= 5) {
            checkDices();
        }
    }

    /**
     * Gets all the points on the dices summed up
     */
    private int getPoints() {
        int points = 0;
        for (Dice dice : DICES)
            points += dice.getPoints();
        return points;
    }

    /**
     * Rolls the dices
     */
    private void roll() {
        for (Dice dice : DICES)
            dice.roll();
    }
}