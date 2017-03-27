import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Play the game of 20 questions.
 * @author Stina Bridgeman
 * @author Max Brodheim
 * @author Erika Cardenas
 * @author Camera Finn
 */
public class TwentyQuestions {

	/**
	 * Play a game of 20 Questions, and edits the tree if unsuccessful.
	 * 
	 * @param tree
	 *          the 20 Questions tree
	 * @param scanner
	 *          the scanner to read user input from
	 * @preconditions: tree and scanner cannot be null
	 * @postcondition: the tree is updated to included more information (user
	 *                 input is not forgotten)
	 */
	public static void play ( BinaryTree tree, Scanner scanner ) {
		assert tree != null && scanner != null;
		Node runner = tree.getRoot();
		while ( true ) { // keeps the game running
			if ( tree.isLeaf(runner) ) { // guessing an answer
				System.out.println("You're thinking of a " + runner.getElement()
				    + ", right?");

				if ( scanner.nextLine().toLowerCase().equals("yes") ) { // if answer is
				                                                        // yes
					// computer guessed correctly, ends game

					System.out.println("I guessed it!");
					break;

				}

				else { // if answer is no

					// Gathers information:
					System.out.println("What were you thinking of? ");
					String actualAns = scanner.nextLine(); // stores the users answer
					System.out.println("Enter a question that is 'yes' for " + actualAns
					    + " and 'no' for " + runner.getElement()); // queries for question
					String newQues = scanner.nextLine(); // stores new question

					// manipulate tree to include new info
					tree.expandLeaf(runner);
					tree.setElement(tree.getLeftChild(runner),actualAns);
					tree.setElement(tree.getRightChild(runner),newQues);
					tree.swapElements(runner,tree.getRightChild(runner));
					break; // ends play loop

				}

			} else { // asking a question

				System.out.println(runner.getElement());
				String response = scanner.nextLine().toLowerCase();
				if ( response.equals("yes") ) {// if answer is yes
					runner = tree.getLeftChild(runner); // restarts loop with appropriate
					                                    // response for next node
				}

				else if ( response.equals("no") ) {// answer is no
					runner = tree.getRightChild(runner); // restarts loop with appropriate
					                                     // response for next node
				} else {
					System.out.println("please enter only 'yes' or 'no' "); // handles bad
					                                                        // input
				}

			}
		}
	}

	/**
	 * Save the tree to a file.
	 * 
	 * @param tree
	 *          tree to save
	 * @param filename
	 *          name of file to save the tree in
	 * @param node
	 *          the root of the tree to be saved
	 * @throws IOException
	 *           if there is an error opening the file or writing the tree
	 * @precondition: file is writeable, assured through usage of method in main
	 * @postcondition: writes File filename to store tree data and structure,
	 *                 PrintWriter is closed.
	 */
	public static void save ( BinaryTree tree, String filename )
	    throws IOException {

		PrintWriter output = new PrintWriter(new FileWriter(filename)); // writer
		                                                                // for
		                                                                // writing
		                                                                // to file
		save(tree,tree.getRoot(),output);
		output.close(); // close output
	}

	/**
	 * Uses recursion to save a tree to a file in a specific structure, must be
	 * used in conjunction with a public method that closes the output stream.
	 * 
	 * @param tree
	 *          BinaryTree that is read from
	 * @param node
	 *          current location in the reading process
	 * @param output
	 *          PrintWriter being used to write to file
	 * @precondition: node cannot be null, which means tree can't be null. output
	 *                cannot be null.
	 * @postcondition: file is updated, but the output is not closed.
	 */
	private static void save ( BinaryTree tree, Node node, PrintWriter output ) {
		if ( tree.isInternal(node) ) { // if the node is a question:
			output.print("[Q]");
			output.println(node.getElement()); // prints appropriate data
			save(tree,tree.getLeftChild(node),output); // recurses on children
			save(tree,tree.getRightChild(node),output);
		} else { // if node is an answer. this is the base case.
			output.print("[A]");
			output.println(node.getElement());
		}
	}

	/**
	 * Load the tree from a file.
	 * 
	 * @param filename
	 *          name of file to load from, with full filepath and file type if appropriate
	 * @return the loaded tree
	 * @throws IOException
	 *           if there is an error opening the file or reading the tree
	 * @precondition: filename must not be null or empty string, and must have file handle.
	 * @postcondition: creates tree with data from File filename with proper structure.
	 */
	public static BinaryTree load ( String filename ) throws IOException {
		Scanner scanner = new Scanner(new FileReader(filename)); //scanner to read file
		BinaryTree tree = new BinaryTree(); //tree to have information added too
		tree = loadHelper(tree,tree.getRoot(),scanner); //calls helper
		scanner.close(); //closes input
		return tree;

	}
/**
 * helper method for load, used to fill in the BinaryTree with recursion
 * @param tree
 * 					BinaryTree being written to
 * @param node
 * 					current node in the writing process
 * @param scanner
 * 					scanner associated with the correct file, used to read file
 * @return tree with data in the correct spot
 * @precondition: nothing can be null, however this is assured by usage in load().
 * @postcondition: tree with correct information in correct locations
 */
	private static BinaryTree loadHelper ( BinaryTree tree, Node node,
	                                       Scanner scanner ) {
		String next = scanner.nextLine(); //stores the next line
		tree.setElement(node,next); //sets the node's element to the stored line
		if ( node.getElement().charAt(1) == 'Q' ) { //tests if the node should be internal
			tree.expandLeaf(node); //adds two null children
			loadHelper(tree,tree.getLeftChild(node),scanner); //recurses for children
			loadHelper(tree,tree.getRightChild(node),scanner);
		}
		//removes the [Q] or [A] from the string, to keep the tree neat and properly formatted
		String editted = node.getElement(); 
		editted = editted.substring(3,editted.length());
		tree.setElement(node,editted);

		return tree;
	}

	/**
	 * Print the tree.
	 * 
	 * @param tree
	 *          the tree to print
	 */
	public static void print ( BinaryTree tree ) {
		print(tree,tree.getRoot(),"");
	}

	/**
	 * Print the subtree rooted at the specified node.
	 * 
	 * @param tree
	 *          the tree to print
	 * @param node
	 *          the root of the subtree to print
	 */
	private static void print ( BinaryTree tree, Node node, String indent ) {
		System.out.println(indent + node.getElement());
		if ( tree.isInternal(node) ) {
			print(tree,tree.getLeftChild(node),indent + " ");
			print(tree,tree.getRightChild(node),indent + " ");
		}
	}

	public static void main ( String[] args ) {

		Scanner scanner = new Scanner(System.in);
		BinaryTree gametree = new BinaryTree("duck");

		for ( ; true ; ) {
			System.out.print("play, print tree, save, load, or quit? [p/t/s/l/q]  ");
			char choice = scanner.nextLine().charAt(0);
			System.out.println();

			if ( choice == 'q' ) {
				System.out.println("goodbye");
				break;

			} else if ( choice == 't' ) {
				print(gametree);

			} else if ( choice == 's' ) {
				try {
					System.out.print("enter filename to save in: ");
					String filename = scanner.nextLine();
					save(gametree,filename);
				} catch ( IOException e ) {
					System.out.println("error saving");
				}

			} else if ( choice == 'l' ) {
				try {
					System.out.print("enter filename to load from: ");
					String filename = scanner.nextLine();
					gametree = load(filename);
				} catch ( IOException e ) {
					System.out.println("error loading file");
				}

			} else if ( choice == 'p' ) {
				play(gametree,scanner);
			}

			System.out.println();
		}
	}
}