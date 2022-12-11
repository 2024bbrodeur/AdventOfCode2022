import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class MonkeyInTheMiddle {
    public static void part1() throws FileNotFoundException{
        Scanner s1 = new Scanner(new File("input1.txt"));
        
        ArrayList<Monkey> input = new ArrayList<>();
        while(s1.hasNextLine()) {
            Monkey tempMonkey = new Monkey();
            tempMonkey.id = Integer.parseInt(s1.nextLine().substring(7,8));
            tempMonkey.addStartingItemsFromString(s1.nextLine());
            tempMonkey.operation = s1.nextLine().substring(23);
            tempMonkey.test = Integer.parseInt(s1.nextLine().substring(21));
            tempMonkey.ifTrue = Integer.parseInt(s1.nextLine().substring(29));
            tempMonkey.ifFalse = Integer.parseInt(s1.nextLine().substring(30));
            s1.nextLine();


            input.add(tempMonkey);
        }
        s1.close();

        int leastCommonMultiple = 1;
        for(Monkey monkey : input) {
            leastCommonMultiple *= monkey.test;
        }

        System.out.println(part1(input, 10000, leastCommonMultiple, false));

    }

    private static BigInteger part1(ArrayList<Monkey> monkeys, int rounds, int leastCommonMultiple, boolean reduceWorry) {
        while(rounds > 0) {
            for(Monkey monkey : monkeys) {
                while(monkey.items.size() > 0) {
                    monkey.performOperation(0, leastCommonMultiple);
                    if(reduceWorry) monkey.reduceWorryLevel(0);
                    BigInteger temp = monkey.items.get(0);
                    monkey.items.remove(0);
                    if(monkey.performTest(temp)) {
                        monkeys.get(monkey.ifTrue).items.add(temp);
                    } else {
                        monkeys.get(monkey.ifFalse).items.add(temp);
                    }
                    monkey.numberOfInspections = monkey.numberOfInspections.add(BigInteger.ONE);
                }
            }
            rounds--;
        }
        BigInteger[] mostActive = {BigInteger.ZERO,BigInteger.ZERO};
        for(Monkey monkey : monkeys) {
            if(monkey.numberOfInspections.compareTo(mostActive[0]) > 0) {
                mostActive[1] = mostActive[0];
                mostActive[0] = monkey.numberOfInspections;
            } else if(monkey.numberOfInspections.compareTo(mostActive[1]) > 0) {
                mostActive[1] = monkey.numberOfInspections;
            }
        }
        return mostActive[0].multiply(mostActive[1]);
    }

    public static class Monkey {
        public int id;
        public ArrayList<BigInteger> items;
        public String operation;
        public int test;
        public int ifTrue;
        public int ifFalse;
        public BigInteger numberOfInspections;
        public Monkey() {
            items = new ArrayList<>();
            numberOfInspections = BigInteger.valueOf(0);
        }
        public void addStartingItemsFromString(String startingItems) {
            startingItems = startingItems.substring(17);
            String[] itemsAsStrings = startingItems.split(",");
            for(String item : itemsAsStrings) {
                items.add(BigInteger.valueOf(Long.parseLong(item.substring(1))));
            }
        }
        public void performOperation(int index, int leastCommonMultiple) {
            String[] operation = this.operation.split(" ");
            if(operation[1].equals("old")) {
                if(operation[0].equals("*")) items.set(index, items.get(index).multiply(items.get(index)));
                else items.set(index, items.get(index).add(items.get(index)));
            } else if(operation[0].equals("*")) items.set(index, items.get(index).multiply(BigInteger.valueOf(Long.parseLong(operation[1]))));
            else items.set(index, items.get(index).add((BigInteger.valueOf(Long.parseLong(operation[1])))));
            items.set(index, items.get(index).mod(BigInteger.valueOf(leastCommonMultiple)));
        }
        public void reduceWorryLevel(int index) {
            items.set(index, items.get(index).divide(BigInteger.valueOf(3)));
        }
        public boolean performTest(BigInteger number) {
            return number.mod(BigInteger.valueOf(test)).equals(BigInteger.ZERO);
        }
    }
}
