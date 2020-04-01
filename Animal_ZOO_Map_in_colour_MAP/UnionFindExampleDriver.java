/*
COMP 2230_02
Data Structure, Algorithm Analysis, and Program Design
Johnny Cuzzola
 March 22th 2020
 Midterm 2
 Name : Amlan Nag ( T00605732)
*/

import java.util.*;

public class UnionFindExampleDriver {


    public static UnionFind<String> uf;

    public static Maze grid;

    public static Scanner sc = new Scanner(System.in);

    private static int rows = 25;
    private static int cols = 25;
    private static double prob = 0.5;

    private static Map<Integer, String> occupied = new HashMap<>();

    public static void main(String[] args) {
        //init
        grid = new Maze(rows, cols, prob);
        uf = new UnionFind<>();
        findRegions();

        //dialog
        while (true) {
            int choice = printMenu();

            if (choice == 5) break;

            if (choice == 1) {
                grid.drawMazeSetsInColor(uf);
                continue;
            }

            if (choice == 2) {
                String name = askString("What's the name of the animal being placed in the zoo? (Capital Letter) ");

                int quantity = askInteger("How many " + name + "s  are you housing? ");
                printFreeRegions(quantity);

                int region = askInteger("Place " + quantity + " " + name + "s in what region? ");
                if (canPlace(region, quantity)) {
                    place(name, quantity, region);
                    occupied.put(region, name);
                } else {
                    System.out.println("Can't place there");
                }
                grid.drawMazeSetsInColor(uf);
                continue;
            }

            if (choice == 3) {
                int cap = askInteger("How many spots do you need? ");
                printFreeRegions(cap);
                continue;
            }

            if (choice == 4) {
                int region = askInteger("Which region do you want to move? ");
                String name = occupied.get(region);
                if (name == null) {
                    System.out.println("There is no any animal there.");
                    continue;
                }
                System.out.println("Region #" + region + " currently housing " + name + "s");
                int quantity = countAnimals(region);
                System.out.println("You need an area capable of holding " + quantity + " of them.");
                printFreeRegions(quantity);


                int region2 = askInteger("Move them to what region? ");

                if (canPlace(region2, quantity)) {
                    place(name, quantity, region2);
                    clearPlace(region);
                    occupied.remove(region);

                    grid.drawMazeSetsInColor(uf);
                    occupied.put(region2, name);
                } else {
                    System.out.println("Can't place there");
                }
                continue;
            }
        }
    }

    private static int printMenu() {
        System.out.println("*******************");
        System.out.println("*    ZOOKEEPER  *");
        System.out.println("*******************");
        System.out.println();
        System.out.println("1. Show map of ZOO");
        System.out.println("2. Place a ZOO animal in the map");
        System.out.println("3. Show region capacity");
        System.out.println("4. Move an animal to another enclosure");
        System.out.println("5. Exit");
        System.out.println("Choose >>");
        return  sc.nextInt();
    }

    private static int askInteger(String q) {
        System.out.println(q + ">>");
        return sc.nextInt();
    }

    private static String askString(String q) {
        System.out.println(q + ">>");
        return sc.next();
    }

    private static boolean canPlace(int region, int quantity) {
        if (region >= 0 && region < uf.sets.size()) {
            if (uf.sets.get(region).size() >= quantity && !occupied.containsKey(region)) return true;
        }
        return false;
    }

    private static void printFreeRegions(int min) {
        System.out.println("#REGION  CAPACITY  ANIMAL>>");
        for (int i = 0; i < uf.sets.size(); i++) {
            if (canPlace(i, min)) {
                System.out.printf("#%3d  %3d  \n", i, uf.sets.get(i).size());
            }
        }
    }

    private static void place(String name, int quantity, int region) {
        Random random = new Random();

        if (name.length() > 3) name = name.substring(0, 3);
        name = name.toUpperCase();
        Set<String> set = uf.sets.get(region);
        String[] placed = new String[set.size()];

        for (int i = 0; i < placed.length; i++) {
            placed[i] = "";
        }
        for (int k = 0; k < quantity; k++) {
            while (true) {
                int r = random.nextInt(set.size());
                if (placed[r].length() == 0) { //unoccupied space
                    placed[r] = name;
                    break;
                }
            }
        }
        int n = 0;
        for (String cell: set.toArray(new String[0])) {
            if (placed[n++].length() != 0) {
                String[] indexes = cell.split(",");
                int i = Integer.parseInt(indexes[0]);
                int j = Integer.parseInt(indexes[1]);
                grid.names[i][j] = name;
            }
        }
    }


    private static void clearPlace(int region) {

        Set<String> set = uf.sets.get(region);
        for (String cell: set.toArray(new String[0])) {
            String[] indexes = cell.split(",");
            int i = Integer.parseInt(indexes[0]);
            int j = Integer.parseInt(indexes[1]);
            grid.names[i][j] = "";
        }
    }

    private static int countAnimals(int region) {
        int num = 0;

        Set<String> set = uf.sets.get(region);
        for (String cell: set.toArray(new String[0])) {
            String[] indexes = cell.split(",");
            int i = Integer.parseInt(indexes[0]);
            int j = Integer.parseInt(indexes[1]);
            if (grid.names[i][j].length() > 0) num++;
        }
        return num;
    }
    ////////////////////
    public static void findRegions() {

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid.maze[i][j]) continue;
                int currentSet = uf.add(i + "," + j);

                if (j > 0 && !grid.maze[i][j - 1]) {
                    Integer leftSet = uf.find(i + "," + (j - 1));
                    int unionSet = uf.union(currentSet, leftSet);
                    if (currentSet != unionSet) uf.sets.remove(currentSet);
                    if (leftSet != unionSet) uf.sets.remove((int) leftSet);
                }

                currentSet = uf.find(i + "," + j);
                if (i > 0 && !grid.maze[i - 1][j]) {
                    Integer upperSet = uf.find((i - 1) + "," + j);
                    int unionSet = uf.union(currentSet, upperSet);
                    if (currentSet != unionSet) uf.sets.remove(currentSet);
                    if (upperSet != currentSet && upperSet != unionSet) {
                        uf.sets.remove((int) upperSet);
                    }
                }
            }
        }
    }
}
/* End of program ! Thanks for your concern. */