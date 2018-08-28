package helper;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

public class LoadData {
	public LinkedList<Data> data = new LinkedList<Data>();
	public HashSet<String> attributeLabel = new HashSet<String>();

	public LoadData(String dataFilePath) {

		try {
			FileReader fileReader = new FileReader(dataFilePath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			// get the attribute names
			String attributelist = bufferedReader.readLine();
			String[] attributes = attributelist.split(",");
			int numOfAttributes = attributes.length - 1;

			for (int i = 0; i < attributes.length - 1; i++) {
				attributeLabel.add(attributes[i]);
			}

			// get the data and class label into data object
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				Data dataValue = new Data();
				String[] values = line.split(",");
				for (int i = 0; i < numOfAttributes; i++) {
					dataValue.attributes.put(attributes[i], Integer.parseInt(values[i]));
				}
				dataValue.classLabel = Integer.parseInt(values[values.length - 1]);
				data.add(dataValue);
				
			}
			bufferedReader.close();

		} catch (FileNotFoundException fileNotFoundException) {
			System.out.println("Unable to find file in the given path" + dataFilePath);
			fileNotFoundException.printStackTrace();
		} catch (IOException iOException) {
			System.out.println("Unable to read data from the file" + dataFilePath);
			iOException.printStackTrace();
		}
		
		

	}
}
