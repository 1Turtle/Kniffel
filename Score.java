import greenfoot.UserInfo;
import greenfoot.util.GreenfootStorageException;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a score for the attempts, the user has left to throw the dices.
 * Also handles the highscores.
 *
 * @author Sammy.K
 * @version 1.0
 */

public class Score extends Label {
    public static Integer score = 0;
    public static boolean changed = false;
    private static int offlineScore = 0;

    public Score() {
        super("SCORE", 6);
        setScore(score);
    }

    public void update() {
        setScore(score);
    }

    public static Integer getHighscore() {
        if (UserInfo.isStorageAvailable()) {
            try {
                UserInfo myInfo = UserInfo.getMyInfo();
                return myInfo.getScore();
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return offlineScore;
    }

    public static String getUserName() {
        if (UserInfo.isStorageAvailable()) {
            try {
                UserInfo myInfo = UserInfo.getMyInfo();
                return myInfo.getUserName();
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return "            ";
    }

    public static void setHighscore(int score) {
        if (UserInfo.isStorageAvailable()) {
            try {
                UserInfo myInfo = UserInfo.getMyInfo();
                myInfo.setScore(score);
                myInfo.store();
            } catch (ArrayIndexOutOfBoundsException e) {
                offlineScore = score;
            }
        } else {
            offlineScore = score;
        }
    }

    public static List getTopFive() {
        if (UserInfo.isStorageAvailable()) {
            try {
                UserInfo myInfo = UserInfo.getMyInfo();
                List<UserInfo> users = UserInfo.getTop(5);
                if (users != null)
                    return users;
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return new ArrayList<UserInfo>();
    }
}
