import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Checks the given numbers for combinations
 *
 * @author Sammy.K
 * @version 1.0
 */
public class CheckDices {
    private static String lastType = "";
    private static Integer lastScore = 0;
    private static int chances = 3;


    /**
     * Gives the player 3 chances.
     */
    public static void resetChances() {
        chances = 3;
    }

    /**
     * Saves the last type, that a pattern out of the given dices made.
     *
     * @param type (name)
     */
    public static void setLastType(String type) {
        lastType = type;
    }

    /**
     * Saves the last score, that a pattern out of the given dices made.
     * @param score
     */
    public static void setLastScore(Integer score) {
        lastScore = score;
    }

    /**
     * Returns the last saved pattern, that the dices made.
     *
     * @return type (name)
     */
    public static String getLastType() {
        return lastType;
    }

    /**
     * Returns the last saved score.
     * @return
     */
    public static Integer getLastScore() {
        return lastScore;
    }

    /**
     * Checks for any pattern in the given numbers.
     *
     * @param n1,n2,n3,n4,n5 represents the number on each dice
     * @return Pair<String, Integer> where String represents the type of pattern & Integer the score.
     */
    public static Pair<String, Integer> all(int n1, int n2, int n3, int n4, int n5) {
        String type = "Game Over";
        Integer score = 0;
        if (kniffel(n1, n2, n3, n4, n5)) {
            type = "Kniffel";
            score = 50;
        } else if (fullHouse(n1, n2, n3, n4, n5)) {
            type = "Full House";
            score = 25;
        } else if (bigStreet(n1, n2, n3, n4, n5)) {
            type = "Gro\u00DFe Stra\u00DFe";
            score = 40;
        } else if (smallStreet(n1, n2, n3, n4, n5)) {
            type = "Kleine Stra\u00DFe";
            score = 30;
        } else {
            int iPasch = 0;
            int iTry = 0;
            while (iPasch == 0) {
                if (iTry < 2) {
                    iPasch = pasch(n1, n2, n3, n4, n5, 4 - iTry);
                    if (iPasch > 0)
                        if (iTry == 0) {
                            type = "Viererpasch";
                        } else {
                            type = "Dreierpasch";
                        }
                } else if (chances > 0) {
                    chances--;
                    iPasch = n1 + n2 + n3 + n4 + n5;
                    type = "Chance (" + chances + "/3)";
                } else break;
                iTry++;
                score = iPasch;
            }
        }
        return new Pair<String, Integer>(type, score);
    }

    /**
     * Checks for a Kniffel.
     * Every number must be the same.
     *
     * @param n1,n2,n3,n4,n5 represents the number on each dice
     * @return is Kniffel or not
     */
    private static boolean kniffel(int n1, int n2, int n3, int n4, int n5) {
        List<Integer> dices = List.of(n1, n2, n3, n4, n5);
        for (Integer dice1 : dices) {
            if (dice1 != n1)
                return false;
        }
        return true;
    }

    /**
     * Checks for a Big Street.
     * The numbers 1-5 or 2-6 have to be given (order doesn't matter).
     *
     * @param n1,n2,n3,n4,n5 represents the number on each dice
     * @return is Big Street or not
     */
    private static boolean bigStreet(int n1, int n2, int n3, int n4, int n5) {
        List<Integer> dices = List.of(n1, n2, n3, n4, n5);
        int found = 0;
        for (int i = 2; i < 6; i++) {
            if (dices.contains(i))
                found++;
        }
        return found == 4 && (dices.contains(1) || dices.contains(6));
    }

    /**
     * Checks for a Small Street.
     * The numbers 1-4, 2-5 or 3-6 have to be given (order doesn't matter).
     *
     * @param n1,n2,n3,n4,n5 represents the number on each dice
     * @return is Small Street or not
     */
    private static boolean smallStreet(int n1, int n2, int n3, int n4, int n5) {
        List<Integer> dices = List.of(n1, n2, n3, n4, n5);
        if (dices.contains(3) && dices.contains(4)) {
            if (dices.contains(1) && dices.contains(2)) {
                return true;
            } else if (dices.contains(2) && dices.contains(5)) {
                return true;
            } else return dices.contains(5) && dices.contains(6);
        }
        return false;
    }

    /**
     * Checks for a Full House.
     * In total 2 different numbers have to be given, where one can't have more than 3.
     *
     * @param n1,n2,n3,n4,n5 represents the number on each dice
     * @return is Full House or not
     */
    private static boolean fullHouse(int n1, int n2, int n3, int n4, int n5) {
        List<Integer> dices = List.of(n1, n2, n3, n4, n5);
        int[] times = {0, 0, 0, 0, 0, 0};
        for (Integer dice : dices) {
            times[dice - 1]++;
        }
        List<Integer> index = new ArrayList<>();
        int zeros = 0;
        for (int i = 0; i < 6; i++)
            if (times[i] == 0) {
                zeros++;
            } else {
                index.add(times[i]);
            }
        return (zeros == 4 && (index.get(0) == 2 || index.get(0) == 3));
    }

    /**
     * Checks for a Pasch.
     * Numbers times 'factor' have to be given.
     *
     * @param n1,n2,n3,n4,n5 represents the number on each dice
     * @return score that the numbers made (0 if it's not a pasch)
     */
    private static int pasch(int n1, int n2, int n3, int n4, int n5, int factor) {
        List<Integer> dices = List.of(n1, n2, n3, n4, n5);
        int[] times = {0, 0, 0, 0, 0, 0};
        for (Integer dice : dices) {
            times[dice - 1]++;
        }
        for (int i = 0; i < 6; i++)
            if (times[i] == factor) {
                return n1 + n2 + n3 + n4 + n5;
            }
        return 0;
    }
}