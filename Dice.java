import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

import java.util.List;

/**
 * This class represents a dice.
 * The player can "throw" & (like in the original Kniffel game)
 * hold the dice.
 *
 * @author Sammy.K
 * @version 1.0
 */
public class Dice extends Actor {
    private int points = 0; /* Number of points on the current visible side */
    private boolean isHold, isStatic = false; /* isHold = player holding it; isStatic = is interactable or not */


    public Dice() {
        setPicture();
    }

    /**
     * (Un)Holds the dice with mouse click. (when not static)
     */
    public void act() {
        if (Greenfoot.mouseClicked(this) && points > 0 && !isStatic)
            toggleHold(!isHold);
    }

    /**
     * Sets the value for the dice being hold or not.
     *
     * @param state new state
     */
    private void toggleHold(boolean state) {
        int iHold = 0;
        if (isHold != state) {
            List<Cup> cup = getWorld().getObjects(Cup.class);

            setRotation(90);
            if (state && !isHold) {
                /* Moves the dice up if not already hold */
                move(-1);
                iHold++;
            } else if (!state && isHold) {
                /* Moves the dice down if not already released */
                move(1);
                iHold--;
            }
            setRotation(0);
            /* Updates hold counter in 'Cup' */
            isHold = state;
            setPicture();
            if (cup.size() > 0 && iHold != 0)
                cup.get(0).getsHold(iHold);
        }
    }

    /**
     * Sets the value for the dice being static (not interactable) or not.
     *
     * @param status new state
     */
    public void setStatic(boolean status) {
        isStatic = status;
    }

    /**
     * Sets the picture depending on number of 'points' on the dice.
     * (Also depends on the state being hold or not)
     */
    private void setPicture() {
        if (!isHold) {
            setImage(new GreenfootImage("W" + points + ".PNG"));
        } else {
            setImage(new GreenfootImage("F" + points + ".PNG"));
        }
    }

    /**
     * Throws a new number of 'points' on the dice from 1-6.
     */
    public void roll() {
        if (!isHold) {
            points = new java.util.Random().nextInt(6) + 1;
            setPicture();
        }
    }

    /**
     * Returns the current number of points on the dice.
     *
     * @return current number
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the dice to its original state
     */
    public void init() {
        points = 0;
        toggleHold(false);
        setStatic(false);
        setPicture();
    }
}
