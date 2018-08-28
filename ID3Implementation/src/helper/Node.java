package helper;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Node {
	private int nodeId;
	private String attributeName;
	private boolean isLeaf;
	private int label;
	private Node attributeValue1;
	private Node attributeValue0;
	private double entropy;
	private HashSet<String> attributesAvailable;
	public LinkedList<Data> data;
	private LinkedList<Data> positiveData;
	private LinkedList<Data> negativeData;

	// Constructors of node class
	Node() {
	}

	public Node(Node origin) {
		this.nodeId = origin.nodeId;
		this.attributeName = origin.attributeName;
		this.isLeaf = origin.isLeaf;
		this.label = origin.label;
		this.attributeValue1 = origin.attributeValue1;
		this.attributeValue0 = origin.attributeValue0;
		this.entropy = origin.entropy;
		this.attributesAvailable = origin.attributesAvailable;
		this.data = origin.data;
		this.positiveData = origin.positiveData;
		this.negativeData = origin.negativeData;
	}

	public Node(LinkedList<Data> data, HashSet<String> attributes) {
		this.data = data;
		nodeId = -1;
		isLeaf = false;
		label = -1;
		attributesAvailable = attributes;

		attributeName = null;
		attributeValue1 = null;
		attributeValue0 = null;
		positiveData = new LinkedList<>();
		negativeData = new LinkedList<>();

		for (Data d : data) {
			if (d.classLabel == 1)
				positiveData.add(d);
			else
				negativeData.add(d);
		}
		entropy = calculateEntropy();

	}

	Node(LinkedList<Data> data) {
		this.data = data;
		isLeaf = false;
		label = -1;
		attributesAvailable = null;

		attributeName = null;
		attributeValue1 = null;
		attributeValue0 = null;
		positiveData = new LinkedList<>();
		negativeData = new LinkedList<>();
		for (Data d : data) {
			if (d.classLabel == 1)
				positiveData.add(d);
			else
				negativeData.add(d);
		}
		entropy = calculateEntropy();
	}

	// Setting class for the node
	public void setClass() {
		this.nodeId = -1;
		isLeaf = true;
		this.setAttributeValue1(null);
		this.setAttributeValue0(null);

		if (negativeData.size() == 0 || negativeData.size() < positiveData.size()) {
			label = 1;
		} else if (positiveData.size() == 0 || negativeData.size() > positiveData.size()) {
			label = 0;
		} else {
			Random label = new Random();
			this.label = label.nextInt(2);
		}

		return;
	}

	// Finding the best attribute for the split
	public void findBestAttribute() {

		LinkedList<Data> decidedDataSetValue1 = null;
		LinkedList<Data> decidedDataSetValue0 = null;
		String selectedAttribute = null;

		double conditionalEntropy = this.entropy;

		for (String attribute : attributesAvailable) {
			LinkedList<Data> value0 = new LinkedList<>();
			LinkedList<Data> value1 = new LinkedList<>();

			for (Data d : data) {
				if (d.attributes.get(attribute) == 1) {
					value1.add(d);
				} else {
					value0.add(d);
				}
			}

			if (value1.size() == 0 || value0.size() == 0) {
				continue;
			}

			double currentEntropy = conditionalEntropy(value1, value0);

			if (currentEntropy < conditionalEntropy) {
				conditionalEntropy = currentEntropy;
				selectedAttribute = attribute;
				decidedDataSetValue1 = value1;
				decidedDataSetValue0 = value0;
			}
		}

		if (decidedDataSetValue1 == null || (decidedDataSetValue0 == null && selectedAttribute == null)) {
			return;
		}

		HashSet<String> attribSetValue1 = new HashSet<>(this.attributesAvailable);
		attribSetValue1.remove(selectedAttribute);
		HashSet<String> attribSetValue0 = new HashSet<>(this.attributesAvailable);
		attribSetValue0.remove(selectedAttribute);

		// update the node
		this.attributeName = selectedAttribute;
		this.attributeValue1 = new Node(decidedDataSetValue1, attribSetValue1);
		this.attributeValue0 = new Node(decidedDataSetValue0, attribSetValue0);
	}

	Double calculateEntropy() {
		int totalCount = data.size();
		int positiveCount = this.positiveData.size();
		int negativeCount = this.negativeData.size();

		double positiveProbability = (double) positiveCount / totalCount;
		double negativeProbability = (double) negativeCount / totalCount;

		if (positiveProbability == 0 || negativeProbability == 0) {
			return (double) 0;
		}
		return -1 * (positiveProbability * Math.log10(positiveProbability) / Math.log10(2.0))
				- (negativeProbability * Math.log10(negativeProbability) / Math.log10(2.0));
	}

	public static double conditionalEntropy(LinkedList<Data> value1, LinkedList<Data> value0) {
		Node nodeEq1 = new Node(value1);
		Node nodeEq0 = new Node(value0);
		return conditionalEntropy(nodeEq1, nodeEq0);
	}

	public static double conditionalEntropy(Node value1, Node value0) {
		int dataSizeValue1 = value1.data.size();
		int dataSizeValue0 = value0.data.size();
		int total = dataSizeValue1 + dataSizeValue0;

		return ((double) dataSizeValue1 / total) * value1.getEntropy()
				+ ((double) dataSizeValue0 / total) * value0.getEntropy();

	}

	public int getNodeId() {
		return nodeId;
	}

	public boolean setNodeId(int nodeId) {
		if (isLeaf)
			return false;
		else {
			this.nodeId = nodeId;
			return true;
		}
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public Node getAttributeValue1() {
		return attributeValue1;
	}

	public void setAttributeValue1(Node attributeValue1) {
		this.attributeValue1 = attributeValue1;
	}

	public Node getAttributeValue0() {
		return attributeValue0;
	}

	public void setAttributeValue0(Node attributeValue0) {
		this.attributeValue0 = attributeValue0;
	}

	public double getEntropy() {
		return entropy;
	}

	public void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public HashSet<String> getAttributesAvailable() {
		return attributesAvailable;
	}

	public void setAttributesAvailable(HashSet<String> attributesAvailable) {
		this.attributesAvailable = attributesAvailable;
	}

	public LinkedList<Data> getData() {
		return data;
	}

	public void setData(LinkedList<Data> data) {
		this.data = data;
	}

	public LinkedList<Data> getPositiveData() {
		return positiveData;
	}

	public void setPositiveData(LinkedList<Data> positiveData) {
		this.positiveData = positiveData;
	}

	public LinkedList<Data> getNegativeData() {
		return negativeData;
	}

	public void setNegativeData(LinkedList<Data> negativeData) {
		this.negativeData = negativeData;
	}

}
