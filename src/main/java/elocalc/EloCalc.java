package elocalc;

/**
 * @author zp4rker
 */
public class EloCalc {

    public static final double K_CONSTANT = 5;

    public int[] calculateElos(int winnerElo, int loserElo) {
        double p1 = 1.0 / (1.0 + Math.pow(10.0, (loserElo - winnerElo) / 400.0));
        double p2 = 1.0 / (1.0 + Math.pow(10.0, (winnerElo - loserElo) / 400.0));

        int winner = (int) Math.round(winnerElo + K_CONSTANT * (1 - p1));
        int loser = (int) Math.round(loserElo + K_CONSTANT * (0 - p2));

        return new int[]{winner, loser};
    }

}
