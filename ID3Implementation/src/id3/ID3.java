package id3;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import helper.Data;
import helper.Node;

public class ID3 {
	private Node rootNode;
	private int nonLeafNodeCount;

	ID3(LinkedList<Data> data, HashSet<String> attributesAvailable) {

		rootNode = new Node(data, attributesAvailable);
		nonLeafNodeCount = 0;
	}

	public void buildTree() {
		buildTree(rootNode);
	}

	private void buildTree(Node node) {

		if (node.getAttributesAvailable().size() == 0
				|| (node.getNegativeData().size() == 0 || node.getPositiveData().size() == 0)) {
			node.setClass();
			return;
		}

		nonLeafNodeCount++;

		node.findBestAttribute();

		if (node.getAttributeName() == null
				|| (node.getAttributeValue0() == null && node.getAttributeValue1() == null)) {
			node.setClass();
			nonLeafNodeCount--;
		}
		if (node.getAttributeValue0() != null) {
			buildTree(node.getAttributeValue0());
		}
		if (node.getAttributeValue1() != null) {
			buildTree(node.getAttributeValue1());
		}
	}

	public int labelNode() {
		int nodeNumber = 0;
		Queue<Node> node = new LinkedList<>();
		node.add(this.rootNode);
		while (!node.isEmpty()) {

			Node current = node.poll();
			current.setNodeId(nodeNumber++);

			if (current.getAttributeValue1() != null && !current.getAttributeValue1().isLeaf()) {
				node.add(current.getAttributeValue1());
			}
			if (current.getAttributeValue0() != null && !current.getAttributeValue0().isLeaf()) {
				node.add(current.getAttributeValue0());
			}
		}
		return (nodeNumber - 1);
	}

	public void printTree() {
		printTree(rootNode, "");
	}

	public void printTree(Node node, String s) {
		if (node.isLeaf()) {
			System.out.println(node.getLabel());
			return;
		}
		System.out.println();
		System.out.print(s + " " + node.getAttributeName() + " = 0 : ");
		printTree(node.getAttributeValue0(), s + "| ");
		System.out.print(s + " " + node.getAttributeName() + " = 1 : ");
		printTree(node.getAttributeValue1(), s + "| ");
	}

	public Node getRootNode() {
		return rootNode;
	}

	public int getNonLeafNodeCount() {
		return nonLeafNodeCount;
	}
}
