package repository;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepositoryDB {

	String userName = "tomek";
	String userPassword = "starterkit";

	Connection myConn = null;
	PreparedStatement myStmt = null;
	ResultSet myRs = null;

	public List<List<String>> getDataFromDBTable(String tableName, int fromRowIdx, int toRowIdx) throws SQLException {

		List<List<String>> outputData = new ArrayList<List<String>>();
		int maxRowNumber = 0;

		// check name of the table
		if (!tableName.matches("\\w+")) {
			return null;
		}

		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbcdatabase?useSSL=false", userName,
					userPassword);

			// get size of the table
			myStmt = myConn.prepareStatement(String.format("SELECT COUNT( * ) FROM %s;", tableName));
			myRs = myStmt.executeQuery();
			int sizeOfTable = 0;
			while (myRs.next()) {
				sizeOfTable = myRs.getInt(1);
			}

			// check row numbers
			maxRowNumber = toRowIdx - fromRowIdx + 1;
			if (fromRowIdx <= 0) {
				fromRowIdx = 1;
			}
			if (toRowIdx < 0 || toRowIdx > sizeOfTable) {
				maxRowNumber = sizeOfTable - fromRowIdx + 1;
			}

			// get rows form DB
			myStmt = myConn.prepareStatement(String.format("SELECT * FROM %s LIMIT ?,?;", tableName));
			myStmt.setInt(1, fromRowIdx - 1);
			myStmt.setInt(2, maxRowNumber);
			myRs = myStmt.executeQuery();

			// get number of columns
			ResultSetMetaData rsMetaData = myRs.getMetaData();
			int columnCount = rsMetaData.getColumnCount();

			// names of columns
			outputData.add(new ArrayList<String>(columnCount + 1));
			outputData.get(0).add("row");
			for (int column = 1; column <= columnCount; column++) {
				outputData.get(0).add(rsMetaData.getColumnName(column));
			}

			// values
			int i = 1;
			while (myRs.next()) {
				outputData.add(new ArrayList<String>(i + fromRowIdx - 1));
				outputData.get(i).add(myRs.getString("id"));
				for (int column = 1; column <= columnCount; column++) {
					outputData.get(i).add(myRs.getString(rsMetaData.getColumnName(column)));
				}
				i++;
			}

		} catch (Exception exc) {
			exc.printStackTrace();
		} finally {
			close(myConn, myRs);
		}
		return outputData;
	}

	public void writeDataToTable(String tableName, List<List<String>> inputData) throws SQLException {

		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jdbcdatabase?useSSL=false", userName,
					userPassword);

			// create table
			// check name of the table
			if (!tableName.matches("\\w+")) {

				myStmt = myConn.prepareStatement(String.format(
						"CREATE TABLE IF NOT EXISTS jdbcdatabase.%s " + "(%s INT NOT NULL AUTO_INCREMENT, "
								+ "%s INT NOT NULL, " + "%s VARCHAR(25) NOT NULL, " + "PRIMARY KEY (%s), "
								+ "UNIQUE INDEX id_UNIQUE (%s ASC));",
						tableName, inputData.get(0).get(0), inputData.get(0).get(1), inputData.get(0).get(2),
						inputData.get(0).get(0), inputData.get(0).get(0)));
				myStmt.executeUpdate();

				// delete from the table
				myStmt = myConn.prepareStatement(String.format("DELETE FROM  %s;", tableName));
				myStmt.executeUpdate();

				// insert data into the table
				myStmt = myConn.prepareStatement(String.format("INSERT INTO %s VALUES (?,?,?)", tableName));

				for (int i = 1; i < inputData.size(); i++) {
					myStmt.setInt(1, Integer.parseInt(inputData.get(i).get(0)));
					myStmt.setInt(2, Integer.parseInt(inputData.get(i).get(1)));
					myStmt.setString(3, inputData.get(i).get(2));
					myStmt.executeUpdate();
				}
			}

		} catch (

		Exception exc)

		{
			exc.printStackTrace();
		} finally

		{
			close(myConn, myRs);
		}
	}

	private static void close(Connection myConn, ResultSet myRs) throws SQLException {

		if (myRs != null) {
			myRs.close();
		}
		if (myConn != null) {
			myConn.close();
		}
	}

}
