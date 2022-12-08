import java.io.IOException;
import java.util.Scanner;

/* 2022 - 12 - 07: All poängräkning funkar */

public class BowlingScore {
    public static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        /* Create array of Rounds */
        Round[] allRounds = new Round[10];
        for (int i = 0; i < 10; i++) {
            allRounds[i] = new Round();
            allRounds[i].FirstScore = 0;
            allRounds[i].SecondScore = 0;
        }
        boolean isFirst = true;
        Round.thirdActive = false;

        /* Ask for first ball */
        for (int round = 0; round < 10; round++) {
            System.out.print("Round " + (round + 1) + " - ");
            System.out.print("1st ball: ");
            allRounds[round].FirstScore = input(10);
            if (allRounds[round].FirstScore == 10) {
                allRounds[round].isStrike = true;
            }
            checkAndSetScore(allRounds, round, true, false);
            printScore(allRounds, round, true, false);


            /* Ask for second ball if first not Strike or is 10th round */
            if (!allRounds[round].isStrike || round == 9) {
                int pinsLeft;
                System.out.print("Round " + (round + 1) + " - ");
                System.out.print("2nd ball: ");

                if (allRounds[round].isStrike) {
                    pinsLeft = 10;
                } else {
                    pinsLeft = 10 - allRounds[round].FirstScore;
                }
                allRounds[round].SecondScore = input(pinsLeft);
                if (allRounds[round].FirstScore + allRounds[round].SecondScore == 10) {
                    allRounds[round].isSpare = true;
                }

                allRounds[round].isStrike = false;
                if (round == 9 && (allRounds[round].SecondScore == 10)) {
                    allRounds[round].isStrike = true;
                }

                checkAndSetScore(allRounds, round, false, false);
                printScore(allRounds, round, false, false);
            }

            /* Ask for third ball if Round 10 - First+Second ball >= 10 (strike or spare) */
            if (round == 9 && allRounds[round].FirstScore + allRounds[round].SecondScore >= 10) {
                Round.thirdActive = true;
                System.out.print("Round " + (round + 1) + " - ");
                System.out.print("3rd ball: ");
                int pinsLeft = 10;

                /* If 1st ball = 10 and 2nd is not - means that 3rd ball has 10-second pins */
                if (allRounds[9].FirstScore == 10 && allRounds[9].SecondScore != 10)
                    pinsLeft = 10 - allRounds[9].SecondScore;

                allRounds[round].ThirdScore = input(pinsLeft);

                checkAndSetScore(allRounds, round, false, true);
                printScore(allRounds, round, false, true);
            }
        }
    }


    /**
     *
     * @param r			round object
     * @param cr		current round
     * @param isFirst	is first or second ball?
     *
     * Method to continously print score for each ball, and total
     */
    public static void printScore(Round[] r, int cr, boolean isFirst, boolean isThird) {
        clrscr();
        String first;
        String second;
        String third;

        System.out.println();
        for (int i = 0; i < 10; i++) {
            first = " ";
            second = " ";

            if (i <= cr) {
                first = Integer.toString(r[i].FirstScore);
                second = Integer.toString(r[i].SecondScore);
                if (r[i].FirstScore == 0) {
                    first = "-";
                }
                if (r[i].SecondScore == 0) {
                    second = "-";
                }
                if (r[i].isSpare) {
                    second = "/";
                }
                if (i == cr && isFirst) {
                    second = " ";
                }
                if (r[i].isStrike && i != 9) {
                    first = " ";
                    second = "X";
                }

               if (r[i].isStrike && i == 9 && isFirst) {
                   first = "X";
                   second = " ";
               }

               if (r[i].isStrike && i == 9 && !isFirst) {
                   first = "X";
                   second = "X";
               }

               if (!r[i].isStrike && i == 9 && !isFirst && r[i].FirstScore == 10) {
                   first = "X";
               }



                if (i == 9 && r[cr].isStrike && !isFirst) {
                    first = "X";
                    second = "X";
                }

            }

            System.out.print(first + "|" + second + "  ");

        }

        if (isThird) {
            third = Integer.toString(r[9].ThirdScore);
            if (r[9].ThirdScore == 10) {
                third = "X";
            }
            System.out.print("|" + third + "  ");
        }

        System.out.println();
        for (int i = 0; i < 10; i++) {
            System.out.print(r[i].Score + "\t");
        }
        System.out.println("\n");
    }


    /**
     * @param pinsLeft	Maximum possible pins to input
     * @return			Integer of number of pins knocked down
     */
    public static int input (int pinsLeft) {
        //boolean valid = false;
        int numPinsKnocked;
        while (true) {
            if (input.hasNextInt()) {
                numPinsKnocked = input.nextInt();
                if (numPinsKnocked >= 0 && numPinsKnocked <= pinsLeft) {
                    return numPinsKnocked;
                } else {
                    System.out.print(" - Try again: ");
                }
            } else {
                System.out.print(" - Try again: ");
            }
        }
    }


    /**
     *
     * @param r			round object
     * @param cr		current round
     * @param isFirst	is it the first or second ball?
     *
     * Method to keep track of strikes and spares and set the scores correctly
     */
    public static void checkAndSetScore(Round[] r, int cr, boolean isFirst, boolean isThird) {
        if (isFirst) {
            if (cr >= 1) {
                if (r[cr - 1].isSpare) {
                    r[cr - 1].Score = 10 + r[cr].FirstScore;
                    if (cr >= 2) {
                        r[cr -1].Score += r[cr - 2].Score;
                    }
                }
            }
            if (cr >= 2) {
                if (r[cr - 2].isStrike && r[cr - 1].isStrike) {
                    r[cr - 2].Score = 20 + r[cr].FirstScore;
                    if (cr >= 3) {
                        r[cr - 2].Score += r[cr - 3].Score;
                    }
                }
            }
        }

        if (!isFirst) {
            if (cr >= 1) {
                if (r[cr - 1].isStrike) {
                    r[cr - 1].Score = 10 + r[cr].FirstScore + r[cr].SecondScore;
                    if (cr >= 2) {
                        r[cr - 1].Score += r[cr - 2].Score;
                    }
                }
            }
            if (!r[cr].isStrike && !r[cr].isSpare) {
                r[cr].Score = r[cr].FirstScore + r[cr].SecondScore;
                if (cr >= 1) {
                    r[cr].Score += r[cr - 1].Score;
                }
            }
        }
        if (isThird) {
            r[cr].Score = r[cr].FirstScore + r[cr].SecondScore + r[cr].ThirdScore + r[cr-1].Score;
        }
    }

    public static void clrscr(){
        //Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {
            System.out.println("Error: " + ex);
        }
    }
}
