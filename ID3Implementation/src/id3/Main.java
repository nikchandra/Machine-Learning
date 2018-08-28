package id3;

import helper.LoadData;
import testing.TestingMachine;

public class Main {

	public static void main(String[] args) {
		// Reading the dataset paths
		if (args.length != 4 || (args[0].length() == 0 || args[1].length() == 0) || args[2].length() == 0
				|| args[3].length() == 0) {
			System.out.println(
					"PLEASE ENTER VALID TRAINING DATA PATH, VALIDATION DATA PATH, TESTING DATA PATH AND PRUNING FACTOR");
			return;
		}

		String trainingDataPath = args[0];
		String validationDataPath = args[1];
		String testDataPath = args[2];
		Double prune = Double.parseDouble(args[3]);

		LoadData trainingData = new LoadData(trainingDataPath);
		LoadData testData = new LoadData(testDataPath);
		LoadData validationData = new LoadData(validationDataPath);

		ID3 decisionTree = new ID3(trainingData.data, trainingData.attributeLabel);
		decisionTree.buildTree();
		decisionTree.printTree();

		TestingMachine machine = new TestingMachine();
		System.out.println("\nPre-Pruned Accuracy:");
		System.out.println("----------------------------------");

		// Running Training machine against Training Data
		machine.run(decisionTree, trainingData.data);
		machine.getTestOnTrainingResults();

		// Running Training machine against Validation Data
		machine.run(decisionTree, validationData.data);
		machine.getTestOnValidationResults();

		// Running Training machine against Test Data
		machine.run(decisionTree, testData.data);
		machine.getTestOnTestingResults();

		// Pruning
		System.out.println("\nPost-Pruned Accuracy");
		System.out.println("----------------------------------");
		int numberOfNodeToPrune = (int) (prune * decisionTree.getNonLeafNodeCount());
		ID3 prunedTree = machine.pruneTree(numberOfNodeToPrune);
		System.out.println("Pruned Tree:\n");
		prunedTree.printTree();
		// Running Training machine against Training Data(Pruned Tree)
		machine.run(prunedTree, trainingData.data);
		machine.getTestOnTrainingResults();

		// Running Training machine against Validation Data(Pruned Tree)
		machine.run(prunedTree, validationData.data);
		machine.getTestOnValidationResults();

		// Running Training machine against Test Data(Pruned Tree)
		machine.run(prunedTree, testData.data);
		machine.getTestOnTestingResults();

	}

}
