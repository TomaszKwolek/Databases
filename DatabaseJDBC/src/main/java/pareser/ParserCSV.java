package pareser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ParserCSV {

	public static List<List<String>> parseCSV(File inputFile) {

		List<List<String>> outputData = new ArrayList<List<String>>();

		try {
			CSVParser parser = CSVParser.parse(inputFile, Charset.defaultCharset(), CSVFormat.newFormat(';'));
			int rowCount = 0;
			for (CSVRecord csvRecord : parser) {
				outputData.add(new ArrayList<String>(csvRecord.size()));
				for (int i = 0; i < csvRecord.size(); i++) {
					outputData.get(rowCount).add(csvRecord.get(i));
				}
				rowCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return outputData;
	}
}
