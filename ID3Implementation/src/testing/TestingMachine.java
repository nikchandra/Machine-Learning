package testing;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import helper.Data;
import helper.Node;
import id3.ID3;

public class TestingMachine {

	private static int correctlyClassified;
	private static int falselyClassified;
	private static int dataCount;
	private ID3 tree;

	public static double accuracy;
	public static double validationAccuracy;
	public static double testingAccuracy;
	public static double trainingAccuracy;

	public TestingMachine() {

	}

	public void run(ID3 tree, LinkedList<Data> data) {
		dataCount = data.size();
		accuracy = 0.0;
		correctlyClassified = 0;
		falselyClassified = 0;
		this.tree = tree;
		tree.labelNode();

		for (Data d : data) {
			boolean iscorrect = traverseTree(tree.getRootNode(), d);
			if (iscorrect) {
				correctlyClassified++;
			} else {
				falselyClassified++;
			}
		}

		if (correctlyClassified + falselyClassified == dataCount) {
			accuracy = (double) correctlyClassified / dataCount;
		} else {
			System.out.print("ALERT!DATA IS NOT TRAINED/TEST COMPLETELY!!");
		}
	}

	public static boolean traverseTree(Node node, Data d) {
		if (node.isLeaf()) {
			if (d.classLabel == node.getLabel()) {
				return true;
			} else {
				return false;
			}
		}
		if (d.attributes.get(node.getAttributeName()) == 1) {
			return traverseTree(node.getAttributeValue1(), d);
		} else {
			return traverseTree(node.getAttributeValue0(), d);
		}
	}

	public void getTestOnTrainingResults() {

		int nonLeafNodeCount = nonLeafNodesCount(tree.getRootNode());
		int leafNodeCount = leafNodesCount(tree.getRootNode());
		int totalCount = leafNodeCount + nonLeafNodeCount;

		System.out.println("Number of training instances = " + tree.getRootNode().data.size());
		System.out.println("Number of training attributes = " + tree.getRootNode().getAttributesAvailable().size());
		System.out.println("Number of non-leaf nodes in the tree = " + nonLeafNodeCount);
		System.out.println("Number of leaf nodes in the tree = " + leafNodeCount);
		System.out.println("Number of nodes in the tree = " + totalCount);
		System.out.println("Accuracy of the model on the training dataset = " + accuracy);
		System.out.println();
	}

	public void getTestOnValidationResults() {

		validationAccuracy = accuracy;
		System.out.println("Number of validation instances = " + dataCount);
		System.out.println("Number of validation attributes = " + tree.getRootNode().getAttributesAvailable().size());
		System.out.println("Accuracy of the model on the training dataset = " + accuracy);
		System.out.println();

	}

	public void getTestOnTestingResults() {

		testingAccuracy = accuracy;
		System.out.println("Number of testing instances = " + dataCount);
		System.out.println("Number of testing attributes = " + tree.getRootNode().getAttributesAvailable().size());
		System.out.println("Accuracy of the model on the testing dataset = " + accuracy);
		System.out.println();
	}

	public int nonLeafNodesCount(Node node) {
		if (node == null || node.isLeaf()) {
			return 0;
		}
		return nonLeafNodesCount(node.getAttributeValue1()) + nonLeafNodesCount(node.getAttributeValue0()) + 1;

	}

	public int allNodesCount(Node node) {
		if (node == null) {
			return 0;
		}
		return allNodesCount(node.getAttributeValue1()) + allNodesCount(node.getAttributeValue0()) + 1;
	}

	public int leafNodesCount(Node node) {
		if (node == null) {
			return 0;
		}
		if (node.isLeaf()) {
			return 1;
		}
		return leafNodesCount(node.getAttributeValue1()) + leafNodesCount(node.getAttributeValue0());
	}

	public ID3 pruneTree(int numberOfPrunedNode) {
		Random random = new Random();
		LinkedList<Integer> prunedNodes = new LinkedList<>();

		for (int i = 0; i < numberOfPrunedNode; i++) {
			int pruned = random.nextInt(tree.getNonLeafNodeCount() + 1);

			while (pruned == 0 || prunedNodes.contains(pruned)) {
				pruned = random.nextInt(tree.getNonLeafNodeCount() + 1);
			}

			prunedNodes.add(pruned);
		}
		prune(prunedNodes);
		return this.tree;
	}

	public void prune(LinkedList<Integer> list) {
		Queue<Node> node = new LinkedList<>();
		node.add(tree.getRootNode());
		while (!node.isEmpty()) {
			Node currentNode = node.poll();
			if (currentNode.isLeaf())
				continue;
			else if (list.contains(currentNode.getNodeId())) {
				currentNode.setClass();
				continue;
			} else {
				node.add(currentNode.getAttributeValue0());
				node.add(currentNode.getAttributeValue1());
			}
		}
	}

	public static double getAccuracy() {
		return accuracy;
	}

}
