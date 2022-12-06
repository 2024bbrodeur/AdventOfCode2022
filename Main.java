import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static void main(String[] args) throws FileNotFoundException{
        Scanner s1 = new Scanner(new File("input1.txt"));
        Scanner s2 = new Scanner(new File("input2.txt"));
        ArrayList<Integer[]> input1 = new ArrayList<>();
        ArrayList<Stack<Character>> input2 = new ArrayList<>();

        while(s1.hasNextLine()) {
        // for(int i = 0; i < 6; i++) {
            String temp = s1.nextLine();
            String[] temp1 = temp.split(" ");
            input1.add(new Integer[] {Integer.parseInt(temp1[1]), Integer.parseInt(temp1[3]), Integer.parseInt(temp1[5])});
        }

        ArrayList<String> t = new ArrayList<>();
        while(s2.hasNextLine()) {
            t.add(s2.nextLine());
        }

        for(int i = 1; i < t.get(0).length(); i+= 4) {
            Stack<Character> tempStack = new Stack<>();
            for(int j = 0; j < t.size()-1; j++) {
                Character temp = t.get(j).charAt(i);
                if(!temp.equals(' ')) tempStack.push(temp);
            }
            Stack<Character> finalStack = new Stack<>();
            while(!tempStack.empty()) {
                finalStack.push(tempStack.pop());
            }
            input2.add(finalStack);
        }

        System.out.println(supplyStacks2(input1, input2));

    }

    public static String supplyStacks2(
        ArrayList<Integer[]> instructions, ArrayList<Stack<Character>> stacks
        ) {
            String ans = "";
            for(Integer[] instr : instructions) {
                Stack<Character> temp = new Stack<>();
                for(int i = 0; i < instr[0]; i++) {
                    temp.push(stacks.get(instr[1]-1).pop());
                }
                while(!temp.empty()) stacks.get(instr[2]-1).push(temp.pop());
            }
            for(Stack<Character> stack : stacks) {
                ans += stack.pop();
            }
            return ans;
        }

    public static String supplyStacks1(
        ArrayList<Integer[]> instructions, ArrayList<Stack<Character>> stacks
        ) {
            String ans = "";
            for(Integer[] instr : instructions) {
                for(int i = 0; i < instr[0]; i++) {
                    stacks.get(instr[2]-1).push(stacks.get(instr[1]-1).pop());
                }
            }
            for(Stack<Character> stack : stacks) {
                ans += stack.pop();
            }
            return ans;
        }

    public static int campCleanup2(ArrayList<String> input) {
        int total = 0;
        for(String s : input) {
            String[] ranges = s.split(",");
            String[] range1 = ranges[0].split("-");
            String[] range2 = ranges[1].split("-");
            if(cleanupOverlap(range1, range2)) total++;
        }
        return total;
    }

    //redundant checking because it was easier to copy and paste
    public static boolean cleanupOverlap(String[] r1, String[] r2) {
        return (Integer.parseInt(r1[0]) >= Integer.parseInt(r2[0])
        && Integer.parseInt(r1[0]) <= Integer.parseInt(r2[1])
        || Integer.parseInt(r1[1]) >= Integer.parseInt(r2[0])
        && Integer.parseInt(r1[1]) <= Integer.parseInt(r2[1]))
        || (Integer.parseInt(r2[0]) >= Integer.parseInt(r1[0])
        && Integer.parseInt(r2[0]) <= Integer.parseInt(r1[1])
        || Integer.parseInt(r2[1]) >= Integer.parseInt(r1[0])
        && Integer.parseInt(r2[1]) <= Integer.parseInt(r1[1]));
        
    }

    public static int campCleanup1(ArrayList<String> input) {
        int total = 0;
        for(String s : input) {
            String[] ranges = s.split(",");
            String[] range1 = ranges[0].split("-");
            String[] range2 = ranges[1].split("-");
            if(Integer.parseInt(range1[0]) <= Integer.parseInt(range2[0])
            && Integer.parseInt(range1[1]) >= Integer.parseInt(range2[1]) 
            || Integer.parseInt(range1[0]) >= Integer.parseInt(range2[0])
            && Integer.parseInt(range1[1]) <= Integer.parseInt(range2[1])) total++;
        }
        return total;
    }

    public static int rucksackReorganization2(ArrayList<String> input) {
        int total = 0;
        HashMap<Character, Integer> charToPriority = new HashMap<>();
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for(int i = 0; i < letters.length(); i++) {
            charToPriority.put(letters.charAt(i), i+1);
        }

        for(int i = 0; i < input.size() - 2; i+=3) {
            total += charToPriority.get(rucksackReorganizationHelper2(input.get(i), input.get(i+1), input.get(i+2)));
        }

        return total;
    }

    public static char rucksackReorganizationHelper2(String s1, String s2, String s3) {
        for(int i = 0; i < s1.length(); i++) {
            for(int j = 0; j < s2.length(); j++) {
                for(int k = 0; k < s3.length(); k++) {
                    if(s1.charAt(i) == s2.charAt(j) && s2.charAt(j) == s3.charAt(k)) return s1.charAt(i);
                }
            }
        }
        return '1';
    }

    public static int rucksackReorganization1(ArrayList<String> input) {
        int total = 0;
        HashMap<Character, Integer> charToPriority = new HashMap<>();
        String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for(int i = 0; i < letters.length(); i++) {
            charToPriority.put(letters.charAt(i), i+1);
        }

        for(String s : input) {
            String s1 = s.substring(0, s.length()/2);
            String s2 = s.substring(s.length()/2, s.length());
            total += (charToPriority.get(rucksackReorganizationHelper1(s1, s2)));
        }

        return total;
    }

    public static char rucksackReorganizationHelper1(String s1, String s2) {
        for(int i = 0; i < s1.length(); i++) {
            for(int j = 0; j < s2.length(); j++) {
                if(s1.charAt(i) == s2.charAt(j)) return s1.charAt(i);
            }
        }

        return '1';
    }

    public static int rockPaperScissors2(ArrayList<String> input) {
        int total = 0;
        HashMap<Character, Integer> map = new HashMap<>();
        map.put('X', 0);
        map.put('Y', 3);
        map.put('Z', 6);

        for(String s : input) {
            char opponent = s.charAt(0);
            char outcome = s.charAt(2);
            total += (map.get(outcome) + rpsOutcomeValue2(opponent, outcome));
        }

        return total;
    }

    public static int rpsOutcomeValue2(char opp, char out) {
        if(opp == 'A'){
            if(out == 'X') return 3;
            if(out == 'Y') return 1;
            return 2;
        } else if(opp == 'B') {
            if(out == 'X') return 1;
            if(out == 'Y') return 2;
            return 3;
        }
        if(out == 'X') return 2;
        if(out == 'Y') return 3;
        return 1;
    }

    public static int rockPaperScissors1(ArrayList<String> input) {
        int total = 0;
        HashMap<Character, Integer> map = new HashMap<>();
        map.put('X', 1);
        map.put('Y', 2);
        map.put('Z', 3);

        for(String s : input) {
            char opponent = s.charAt(0);
            char me = s.charAt(2);

            total += (map.get(me) + rpsOutcomeValue1(opponent, me));
        }

        return total;
    }

    public static int rpsOutcomeValue1(char o, char m) {
        if(o == 'A'){
            if(m == 'X') return 3;
            if(m == 'Y') return 6;
            return 0;
        } else if(o == 'B') {
            if(m == 'X') return 0;
            if(m == 'Y') return 3;
            return 6;
        }
        if(m == 'X') return 6;
        if(m == 'Y') return 0;
        return 3;
        
    }

    public static int calorieCounting2(ArrayList<String> input) {
        ArrayList<Integer> ans = new ArrayList<>();
        for(int i = 0; i < 3; i++) {
            ans.add(0);
        }
        int tempSum = 0;

        for(String s : input) {
            if(s.equals("")) {
                if(tempSum > ans.get(0)) {
                    ans.remove(0);
                    ans.add(0, tempSum);
                } else if(tempSum > ans.get(1)) {
                    ans.remove(1);
                    ans.add(1, tempSum);
                } else if(tempSum > ans.get(2)) {
                    ans.remove(2);
                    ans.add(2, tempSum);
                }
                tempSum = 0;
                continue;
            }
            tempSum += Integer.parseInt(s);
        }

        return ans.get(0) + ans.get(1) + ans.get(2);
    }

    public static int calorieCounting1(ArrayList<String> input) {
        int max = 0;
        int tempSum = 0;

        for(String s : input) {
            if(s.equals("")) {
                if(tempSum > max) max = tempSum;
                tempSum = 0;
                continue;
            }
            tempSum += Integer.parseInt(s);
        }

        return max;
    }

}