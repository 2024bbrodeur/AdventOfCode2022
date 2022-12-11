import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    public static void main(String[] args) throws FileNotFoundException{
        Scanner s1 = new Scanner(new File("input1.txt"));
        
        ArrayList<String> input = new ArrayList<>();

        while(s1.hasNextLine()) input.add(s1.nextLine());

        System.out.println(treetopTreeHouse2(input));

    }

    public static int treetopTreeHouse2(ArrayList<String> input) {
        int largest = 0;
        for(int row = 0; row < input.size(); row++) {
            for(int column = 0; column < input.get(row).length(); column++) {
                int visibility = treeVisibility(row, column, input);
                largest = visibility > largest ? visibility : largest;
            }
        }
        return largest;
    }

    public static int treeVisibility(int row, int column, ArrayList<String> input) {
        int up = 0;
        int down = 0;
        int left = 0;
        int right = 0;
        int currHeight = Integer.parseInt(input.get(row).substring(column, column+1));
        for(int i = row - 1; i >= 0; i--) {
            up++;
            if(Integer.parseInt(input.get(i).substring(column, column+1)) >= currHeight) break;
        }
        for(int i = row + 1; i < input.size(); i++) {
            down++;
            if(Integer.parseInt(input.get(i).substring(column, column+1)) >= currHeight) break;
        }
        for(int i = column - 1; i >= 0; i--) {
            left++;
            if(Integer.parseInt(input.get(row).substring(i, i+1)) >= currHeight) break;
        }
        for(int i = column + 1; i < input.get(row).length(); i++) {
            right++;
            if(Integer.parseInt(input.get(row).substring(i, i+1)) >= currHeight) break;
        }

        return up*down*left*right;
    }

    public static int treetopTreeHouse1(ArrayList<String> input) {
        int ans = 0;
        for(int row = 0; row < input.size(); row++) {
            for(int column = 0; column < input.get(row).length(); column++) {
                if(isTreeVisible(row, column, input)) ans++;
            }
        }
        return ans;
    }

    public static boolean isTreeVisible(int row, int column, ArrayList<String> input) {
            if(row == 0 || row == input.size() - 1
            || column == 0 || column == input.get(row).length() - 1) {
                return true;
            }
            int curr = Integer.parseInt(input.get(row).substring(column, column+1));
            boolean visible = true;

            for(int i = 0; i < row; i++) {
                if(curr <= Integer.parseInt(input.get(i).substring(column, column+1))) {
                    visible = false;
                    break;
                }
            }
            if(visible) return true;
            visible = true;

            for(int i = row+1; i < input.size(); i++) {
                if(curr <= Integer.parseInt(input.get(i).substring(column, column+1))){
                    visible = false;
                    break;
                }
            }
            if(visible) return true;
            visible = true;

            for(int i = 0; i < column; i++) {
                if(curr <= Integer.parseInt(input.get(row).substring(i, i+1))) {
                    visible = false;
                    break;
                }
            }
            if(visible) return true;
            visible = true;

            for(int i = column+1; i < input.get(row).length(); i++) {
                if(curr <= Integer.parseInt(input.get(row).substring(i, i+1))){
                    visible = false;
                    break;
                }
            }
            return visible;
    }

    public static int noSpaceLeftOnDevice2(ArrayList<String> input) {
        directoryTree rootDirectory = createTree(input);
        int smallestSize = rootDirectory.getIndirectFileSize();
        int sizeNeeded = 30000000 - (70000000 - rootDirectory.getIndirectFileSize());
        System.out.println(sizeNeeded);
        System.out.println(smallestSize);
        Stack<directoryTree> stack = new Stack<>();
        stack.push(rootDirectory);
        while(!stack.empty()) {
            directoryTree curr = stack.pop();
            int currSize = curr.getIndirectFileSize();
            if(currSize >= sizeNeeded && currSize <= smallestSize) {
                smallestSize = currSize;
            }
            for(directoryTree d : curr.childNodes) stack.push(d);
        }
        return smallestSize;
    }

    public static int noSpaceLeftOnDevice1(ArrayList<String> input) {
        directoryTree rootDirectory = createTree(input);
        int ans = 0;
        Stack<directoryTree> stack = new Stack<>();
        stack.push(rootDirectory);
        while(!stack.empty()) {
            directoryTree curr = stack.pop();
            int indirectFileSize = curr.getIndirectFileSize();
            if(indirectFileSize <= 100000) ans += indirectFileSize;
            for(directoryTree d : curr.childNodes) stack.push(d);
        }
        return ans;
    }

    public static directoryTree createTree(ArrayList<String> input) {
        directoryTree currDirectory = null;
        directoryTree rootDirectory = null;
        for(String command : input) {
            if(command.charAt(0) == '$'){
                if(command.substring(2,4).equals("cd")) {
                    if(currDirectory!=null) currDirectory.sizeSet = true;
                    if(command.substring(5).equals("..")) currDirectory = currDirectory.parent;
                    else if(currDirectory==null){
                        currDirectory = new directoryTree("/", currDirectory);
                        rootDirectory = currDirectory;
                    }
                    else currDirectory = currDirectory.getChildWithName(command.substring(5));
                }
            } else if(command.substring(0,3).equals("dir")) {
                currDirectory.childNodes.add(new directoryTree(command.substring(4), currDirectory));
            } else if(!currDirectory.sizeSet) {
                String[] temp = command.split(" ");
                currDirectory.directFileSize += Integer.parseInt(temp[0]);
            }
            
        }
        return rootDirectory;
    }

    public static class directoryTree {
        public boolean foundIndirectFileSize;
        private int indirectFileSize;

        public int directFileSize;
        public boolean sizeSet;
        public ArrayList<directoryTree> childNodes;
        public directoryTree parent;
        public String name;
        public directoryTree(String name, directoryTree parent) {
            this.parent = parent;
            this.name = name;
            sizeSet = false;
            directFileSize = 0;
            childNodes = new ArrayList<>();

            foundIndirectFileSize = false;
            indirectFileSize = 0;
        }
        public directoryTree getChildWithName(String name) {
            for(directoryTree d : childNodes) {
                if(d.name.equals(name)) return d;
            }
            return null;
        }
        public int getIndirectFileSize() {
            if(foundIndirectFileSize) {
                return indirectFileSize;
            }
            int size = directFileSize;
            foundIndirectFileSize = true;
            if(childNodes.size() == 0){
                indirectFileSize = size;
                return size;
            }
            for(directoryTree d : childNodes) {
                size+=d.getIndirectFileSize();
            }
            indirectFileSize = size;
            return size;
        }
    }

    public static int tuningTrouble(String input, int windowSize) {
        int ans = windowSize;

        String temp = input.substring(ans-windowSize, ans);
       
        while(tuningTroubleContainsRepeat(temp)) {
            ans++;
            temp = input.substring(ans-windowSize, ans);
        }

        return ans;
    }

    public static boolean tuningTroubleContainsRepeat(String s) {
        for(int i = 0; i < s.length(); i++) {
            for(int j = i+1; j < s.length(); j++) {
                if(s.charAt(i)==s.charAt(j)) {
                    return true;
                }
            }
        }
        return false;
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