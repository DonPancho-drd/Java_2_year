import java.util.*;

public class BullsAndCows {

    private static final int NUMBER_LENGTH = 4;
    private static final int MAX_STEPS_EASY = 25;
    private static final int MAX_STEPS_MEDIUM = 17;
    private static final int MAX_STEPS_HARD = 10;
    private static final int MAX_DEFAULT = 50;


    private static final Map<String, Integer> leaderboard = new HashMap<>();

    public static void main(String[] args) {
        printUsage();
        Scanner scanner = new Scanner(System.in);
        String input;
        label:
        while (true) {
            System.out.print("Enter command: ");
            input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "play":
                    playGame(scanner);
                    break;
                case "help":
                    printUsage();
                    break;
                case "results":
                    showLeaderboard();
                    break;
                case "quit":
                    System.out.println("Goodbye!");
                    break label;
                default:
                    System.out.println("Unknown command. Type 'help' for instructions.");
                    break;
            }
        }
        scanner.close();
    }


    private static void playGame(Scanner scanner) {
        System.out.print("Enter your name: ");
        String playerName = scanner.nextLine().trim();
        if (playerName.isEmpty()) {
            playerName = "Anonymous";
        }

        System.out.print("Choose mode (easy/medium/hard/default): ");
        String mode = scanner.nextLine().trim().toLowerCase();
        int maxSteps = getMaxSteps(mode);

        String secretNumber = generateSecretNumber();
        System.out.println("A secret 4-digit number has been generated. Let's start!");

        int steps = 0;
        while (steps < maxSteps) {
            System.out.print("Enter your guess: ");
            String guess = scanner.nextLine().trim();
            if (guess.equals("yield")) {
                System.out.println("You gave up. The secret number was: " + secretNumber);
                return;
            }
            if (!isValidGuess(guess)) {
                System.out.println("Invalid guess. Please enter a 4-digit number with unique digits.");
                continue;
            }
            steps++;
            if (guess.equals(secretNumber)) {
                System.out.println("Congratulations, " + playerName + "! You've guessed the number in " + steps + " steps.");
                int score = calculateScore(mode, steps);
                updateLeaderboard(playerName, score);
                return;
            } else {
                int[] result = checkGuess(secretNumber, guess);
                System.out.println("Bulls: " + result[0] + ", Cows: " + result[1]);
            }
        }
        System.out.println("You've reached the maximum number of steps. The secret number was: " + secretNumber);
        updateLeaderboard(playerName, 0);
    }

    private static int[] checkGuess(String secret, String guess) {
        int cows = 0;
        int bulls = 0;
        for (int i = 0; i < NUMBER_LENGTH; i++) {
            if (secret.charAt(i) == guess.charAt(i)) {
                bulls++;
            } else if (secret.contains(String.valueOf(guess.charAt(i)))) {
                cows++;
            }
        }
        return new int[]{bulls, cows};
    }

    private static String generateSecretNumber() {
        Random random = new Random();
        Set<Character> digits = new HashSet<>();
        StringBuilder number = new StringBuilder();
        while (number.length() < NUMBER_LENGTH) {
            char digit = (char) ('0' + random.nextInt(10));
            if (!digits.contains(digit)) {
                digits.add(digit);
                number.append(digit);
            }
        }
        return number.toString();
    }

    private static boolean isValidGuess(String guess) {
        if (guess.length() != NUMBER_LENGTH) {
            return false;
        }
        Set<Character> digits = new HashSet<>();
        for (char c : guess.toCharArray()) {
            if (!Character.isDigit(c) || digits.contains(c)) {
                return false;
            }
            digits.add(c);
        }
        return true;
    }

    private static int getMaxSteps(String mode) {
        return switch (mode) {
            case "easy" -> MAX_STEPS_EASY;
            case "medium" -> MAX_STEPS_MEDIUM;
            case "hard" -> MAX_STEPS_HARD;
            default -> MAX_DEFAULT; // No limit (limit = 50)
        };
    }

    private static int calculateScore(String mode, int steps) {
        int baseScore = switch (mode) {
            case "easy" -> 10;
            case "medium" -> 20;
            case "hard" -> 30;
            default -> 5;
        };

        return baseScore * (MAX_DEFAULT - steps + 1);
    }

    private static void updateLeaderboard(String playerName, int score) {
        leaderboard.put(playerName, leaderboard.getOrDefault(playerName, 0) + score);
        System.out.println(playerName + " earned " + score + " points!");
        showLeaderboard();
    }

    private static void showLeaderboard() {
        if (leaderboard.isEmpty()) {
            System.out.println("The leaderboard is empty.");
            return;
        }


        System.out.println("\n<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
        System.out.println("                         Leaderboard :");

        Comparator<Map.Entry<String, Integer>> comparator = new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        };

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(leaderboard.entrySet());
        sortedEntries.sort(comparator);


        for (Map.Entry<String, Integer> entry : sortedEntries) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " points");
        }
        System.out.println("\n<><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><><>\n");
    }

    private static void printUsage() {
        System.out.println("To start the game, please enter 'play'");
        System.out.println("You will be asked to choose the mode");
        System.out.println("           - easy   -  25 steps and less");
        System.out.println("           - medium -  17 steps and less");
        System.out.println("           - hard   -  10 steps and less");
        System.out.println("           by default - no limit = 50 steps");
        System.out.println("To stop the game enter 'yield'");
        System.out.println("For help enter 'help' - you will see this text");
        System.out.println("To know the rankings please enter 'results'");
        System.out.println("To finish enter 'quit'");
    }
}