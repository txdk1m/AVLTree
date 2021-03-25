// Code by Ted Kim

import java.util.ArrayList;
import java.util.Collections;

public class Lab2_AVL{

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		// initialize 5000 integer array
		ArrayList<Integer> values = new ArrayList<Integer>();
		for(int i = 0; i < 5000; i++) {
			values.add(i);
		}
		// shuffle array
		Collections.shuffle(values);
		// call first AVL tree initialization
		AVLTree tree_1 = new AVLTree();
		// AVL insert
		for(int i = 0; i < values.size(); i++) {
			tree_1.root = tree_1.insert(tree_1.root, values.get(i));
		}
		// call second AVL tree empty
		AVLTree tree_2 = new AVLTree();
		// forever while loop to find, remove, insert, and switch
		while(true){
			for(int j = 0; j < values.size(); j++) {
				Node min = tree_1.findMin(tree_1.root);
				tree_1.root = tree_1.remove(tree_1.root, min.x);
				System.out.printf("The process with a priority of %d is now scheduled to run!", min.x); System.out.println();
				values.set(j, min.x);
				tree_2.root = tree_2.insert(tree_2.root, values.get(j));
				System.out.printf("The process with a priority of %d has run out of its timeslice!", min.x); System.out.println();
			}
			long endTime = System.currentTimeMillis();
			long totalrunTime = endTime - startTime;
			System.out.println("Runtime: " + totalrunTime + " milliseconds");
			System.out.println("Every process has got a chance to run; Please press “Enter” to start the next round!");
			pressEnterToContinue();
			AVLTree blank_tree = new AVLTree();
			tree_1 = tree_2;
			tree_2 = blank_tree;
		}	
	}

	static void pressEnterToContinue()
	 { 
	    try
	    {
	    	System.in.read();
	    } catch(Exception e)
	    	{}  
	 }
}

class Node{
	int x, height;
	Node left, right;
	public int value;
	
	Node(int n){
		x = n;
		height = 1;
	}
}

class AVLTree{
	Node root;
	
	int value;	
	public void Node(int v) {
		this.value = v;
	}
	
	int height(Node t) {
		if (t == null)
			return 0;
		return t.height;
	}
	
	void newHeight(Node t) {
		t.height = 1 + Math.max(height(t.left), height(t.right));
	}
	
	int balance(Node t) {
		if(t == null) {
			return 0;
		} else return height(t.right) - height(t.left);
	}
	
	// rotateRight
	Node rotateToRight(Node k2) {
		Node k1 = k2.left;
		Node k3 = k1.right;
		k1.right = k2;
		k2.left = k3;
		newHeight(k2);
		newHeight(k1);
		return k1;
	}
	// rotateLeft
	Node rotateToLeft(Node k1) {
		Node k2 = k1.right;
		Node k3 = k2.left;
		k2.left = k1;
		k1.right = k3;
		newHeight(k1);
		newHeight(k2);
		return k2;
	}
	// findMin
	Node findMin(Node t) {
		if (t == null)
			return null;
		if (t.left == null)
			return t;
		return findMin(t.left);
	}
	// insert
	Node insert(Node t, int x){
		if (t == null) {
			return (new Node(x));
		}
		
		if (x < t.x)
			t.left = insert(t.left, x);
		
		else if (x > t.x) {
			t.right = insert(t.right, x);
		} else return t;
		
		//balancing
		int b = balance(t);
		
		if (b > 1 && x < t.left.x) {
			return rotateToRight(t);
		}
		
		if (b < -1 && x > t.right.x) {
			return rotateToLeft(t);
		}
		
		if (b > 1 && x > t.left.x) {
			t.left = rotateToLeft(t.left);
			return rotateToRight(t);
		}
		
		if (b < -1 && x < t.right.x) {
			t.right = rotateToRight(t.right);
			return rotateToLeft(t);
		}
		
		return t; 
	}
	
	// remove
	Node remove(Node t, int x) {
		if (t == null) {
			return t;
		}
		
		else if (x < t.x) {
			t.left = remove(t.left, x);
		}
		
		else if (x > t.x) {
			t.right = remove(t.right, x);
		}
		
		else {
			if ((t.left == null) || (t.right == null)) {
				Node temp = null;
				if (temp == t.left)
					temp = t.right;
				else
					temp = t.left;
				
				if (temp == null) {
					temp = t;
					t = null;
				} else
					t = temp;
			} else {
				Node temp = findMin(t.right);
				t.x = temp.x;
				t.right = remove(t.right, temp.x);
			}
		}
		if (t == null) {
			return t;
		}
		
		newHeight(t);
		
		int b = balance(t);
		// left left
		if (b > 1 && balance(t.left) >= 0)
			return rotateToLeft(t);
		// left right
		if (b > 1 && balance(t.left) < 0) {
			t.left = rotateToRight(t.left);
			return rotateToRight(t);
		}
		// right right
		if (b < -1 && balance(t.right) <= 0)
			return rotateToRight(t);
		// right left
		if (b < -1 && balance(t.right) > 0) {
			t.right = rotateToLeft(t.right);
			return rotateToLeft(t);
		}
		
		return t;
	}
}

