package service.java;

import repository.RepositoryDB;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import pareser.ParserCSV;

public class Service {

	public static void main(String[] args) throws SQLException {
		
		String filename = "insert-MYTAB.csv";
		File inputFile = new File(filename);
		
		printTable("mytab", 1, -1);
		storeDate(inputFile, "mytab3");
	}
	

	public static void printTable(String tableName, int fromRowIdx, int toRowIdx) throws SQLException {

		RepositoryDB repository = new RepositoryDB();
		List<List<String>> inputData = repository.getDataFromDBTable(tableName, fromRowIdx, toRowIdx);

		//print table
		System.out.println("  --------------------------------------------------------------------");
		System.out.printf("%3s %10s %5s %49s", "|", "table: ", tableName, "|");
		System.out.println();
		System.out.println("  --------------------------------------------------------------------");
		System.out.format("%3s %-6s %3s %-6s %3s %-25s %3s %-10s %3s" , "|", inputData.get(0).get(0), "|", inputData.get(0).get(1), "|", 
        		inputData.get(0).get(2), "|", inputData.get(0).get(3), "|");
		System.out.println();
		System.out.println("  --------------------------------------------------------------------");
		for(int i=1; i<inputData.size(); i++){
	        System.out.format("%3s %-6s %3s %-6s %3s %-25s %3s %-10s %3s" , "|", inputData.get(i).get(0), "|", inputData.get(i).get(1), "|", 
	        		inputData.get(i).get(2), "|", inputData.get(i).get(3), "|");
	        System.out.println();
		}
		System.out.println("  --------------------------------------------------------------------");

	}
	
	public static void storeDate(File file, String tableName) throws SQLException{
		
		RepositoryDB repository = new RepositoryDB();
		List<List<String>> inputData = ParserCSV.parseCSV(file);		
		repository.writeDataToTable("mytabCreated", inputData);
		
	}

}
