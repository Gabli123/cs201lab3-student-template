import java.util.*;

public class Q1Test {
    public static void main(String[] args) {

        String result, traversal1, traversal2, traversal3;

        traversal1 = "1-2";
        traversal2 = "2-1-3";
        traversal3 = "1-3-2";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println("Expected Result : Invalid traversals");
        System.out.println("  Actual Result : " + result);
        System.out.println();

        traversal1 = "1-2-3";
        traversal2 = "2-1-3";
        traversal3 = "1-3-2";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println(
                  "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
        System.out.println("  Actual Result : " + result);
        System.out.println();

        traversal1 = "1-2-3";
        traversal2 = "3-2-1";
        traversal3 = "2-3-1";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println("Expected Result : Invalid traversals");
        System.out.println("  Actual Result : " + result);
        System.out.println();

        traversal1 = "3-1-2-5-4";
        traversal2 = "2-1-4-5-3";
        traversal3 = "1-2-3-4-5";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println(
                  "Expected Result : Traversal 1 - Preorder, Traversal 2 - Postorder, Traversal 3 - Inorder");
        System.out.println("  Actual Result : " + result);
        System.out.println();

        traversal1 = "10-20-30-40-50";
        traversal2 = "20-10-30-40-50";
        traversal3 = "10-50-40-30-20";
        result = verify(traversal1, traversal2, traversal3);
        System.out.println("Traversal 1 : " + traversal1);
        System.out.println("Traversal 2 : " + traversal2);
        System.out.println("Traversal 3 : " + traversal3);
        System.out.println(
                  "Expected Result : Traversal 1 - Inorder, Traversal 2 - Preorder, Traversal 3 - Postorder");
        System.out.println("  Actual Result : " + result);
        System.out.println();
    } 

    private static class TreeNode {
        int val;
        TreeNode left, right;
        TreeNode(int val) { this.val = val; }
    }

    public static String verify(String traversal1, String traversal2, String traversal3) {
        String[] inputs = {traversal1, traversal2, traversal3};
        
        // Permutations of indices mapping to (Preorder, Inorder, Postorder)
        int[][] permutations = {
            {0, 1, 2}, {0, 2, 1},
            {1, 0, 2}, {1, 2, 0},
            {2, 0, 1}, {2, 1, 0}
        };

        for (int[] p : permutations) {
            String preStr = inputs[p[0]];
            String inStr  = inputs[p[1]];
            String postStr = inputs[p[2]];

            if (isValidBST(preStr, inStr, postStr)) {
                String[] roles = new String[3];
                roles[p[0]] = "Preorder";
                roles[p[1]] = "Inorder";
                roles[p[2]] = "Postorder";

                return String.format("Traversal 1 - %s, Traversal 2 - %s, Traversal 3 - %s",
                        roles[0], roles[1], roles[2]);
            }
        }

        return "Invalid traversals";
    }

    private static boolean isValidBST(String preStr, String inStr, String postStr) {
        int[] pre = parse(preStr);
        int[] in  = parse(inStr);
        int[] post = parse(postStr);

        if (pre.length != in.length || in.length != post.length || pre.length == 0) {
            return false;
        }

        // 1. Inorder must be strictly sorted ascending for a valid BST with unique elements
        int[] sortedIn = pre.clone();
        Arrays.sort(sortedIn);
        if (!Arrays.equals(in, sortedIn)) {
            return false;
        }

        // 2. Build BST from Preorder and Inorder
        Map<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < in.length; i++) {
            inMap.put(in[i], i);
        }

        preIndex = 0;
        TreeNode root = buildTree(pre, inMap, 0, in.length - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        
        if (root == null || preIndex != pre.length) {
            return false;
        }

        // 3. Verify that generated Postorder matches candidate Postorder
        List<Integer> generatedPost = new ArrayList<>();
        getPostorder(root, generatedPost);

        if (generatedPost.size() != post.length) {
            return false;
        }

        for (int i = 0; i < post.length; i++) {
            if (generatedPost.get(i) != post[i]) {
                return false;
            }
        }

        return true;
    }

    private static int preIndex = 0;

    private static TreeNode buildTree(int[] pre, Map<Integer, Integer> inMap, int inStart, int inEnd, int min, int max) {
        if (inStart > inEnd || preIndex >= pre.length) {
            return null;
        }

        int val = pre[preIndex];
        
        // Validate BST property
        if (val <= min || val >= max) {
            return false ? null : null;
        }

        if (!inMap.containsKey(val)) {
            return null;
        }

        int inIndex = inMap.get(val);
        if (inIndex < inStart || inIndex > inEnd) {
            return null;
        }

        preIndex++;
        TreeNode root = new TreeNode(val);
        root.left = buildTree(pre, inMap, inStart, inIndex - 1, min, val);
        root.right = buildTree(pre, inMap, inIndex + 1, inEnd, val, max);

        return root;
    }

    private static void getPostorder(TreeNode node, List<Integer> list) {
        if (node == null) return;
        getPostorder(node.left, list);
        getPostorder(node.right, list);
        list.add(node.val);
    }

    private static int[] parse(String s) {
        if (s == null || s.trim().isEmpty()) return new int[0];
        String[] parts = s.split("-");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            arr[i] = Integer.parseInt(parts[i]);
        }
        return arr;
    }
}