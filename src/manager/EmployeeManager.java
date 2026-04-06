package manager;

import java.sql.*;
import java.util.Scanner;
import problemdomain.*;

public class EmployeeManager {
	private static final String SERVER = "localhost";
	private static final int PORT = 3303;
	private static final String DATABASE = "cprg211";
	private static final String USERNAME = "root";
	private static final String PASSWORD = "password";

	private Connection conn;
	private Scanner scanner;

	public EmployeeManager() {
		try {
			connect();
			createTable();
			displayMenu();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	private void connect() throws SQLException {
		final String DB_URL = String.format("jdbc:mariadb://%s:%d/%s?user=%s&password=%s", SERVER, PORT, DATABASE,
				USERNAME, PASSWORD);
		conn = DriverManager.getConnection(DB_URL);
		System.out.println("Connection to DB established.");

	}

	private void createTable() throws SQLException {
		String sqlStmt = "CREATE TABLE IF NOT EXISTS Employee (" + "id INT(11) PRIMARY KEY AUTO_INCREMENT,"
				+ "name VARCHAR(50) NOT NULL," + "department VARCHAR(50) NOT NULL," + "email VARCHAR(50),"
				+ "phone VARCHAR(50) NOT NULL," + "address VARCHAR(100), " + "type CHAR NOT NULL,"
				+ "base_salary DOUBLE ," + "bonus DOUBLE ," + "hourly_rate DOUBLE ," + "hours_worked INT(11) )";
		System.out.println(sqlStmt);
		PreparedStatement stmt = conn.prepareStatement(sqlStmt);
		stmt.execute();
	}

	private void displayMenu() {
		scanner = new Scanner(System.in);
		int choice = 0;
		while (choice != 6) {
			System.out.println("**********************Salary Management System*********************");
			System.out.println("1. Add Employee.");
			System.out.println("2. Search Employee by ID.");
			System.out.println("3. Update Employee.");
			System.out.println("4. Remove Employee.");
			System.out.println("5. Print Employee Salary.");
			System.out.println("6. Exit.");
			try {
				choice = Integer.parseInt(scanner.nextLine());
				switch (choice) {
				case 1:
					addEmployee();
					break;
				case 2:
					searchEmployeeByID();
					break;
				case 3:
					updateEmployeeSalary();
					break;
				case 4:
					removeEmployee();
					break;
				case 5:
					printSalaryReport();
					break;
				case 6:
					disconnect();
					break;
				default:
					System.out.println("Incorrect choice. Enter 1 to 6.");

				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter a number for the menu choice.");
			}

		}

	}

	private void addEmployee() {
		System.out.println("Enter Employee's Name : ");
		String name = scanner.nextLine();
		System.out.println("Enter Employee's Department :");
		String department = scanner.nextLine();
		System.out.println("Enter Employee's Email :");
		String email = scanner.nextLine();
		System.out.println("Enter Employee's Phone :");
		String phone = scanner.nextLine();
		System.out.println("Enter Employee's Address :");
		String address = scanner.nextLine();
		System.out.println("Enter Employee's Type(F for Full-time, C for Contractor) :");
		String type = scanner.nextLine().toUpperCase();
		try {
			PreparedStatement stmt;
			if (type.equals("F")) {
				System.out.println("Enter Employee's Base Salary :");
				double base_salary = Double.parseDouble(scanner.nextLine());
				if (base_salary <= 0) {
					throw new InvalidEmployeeDataException("Base Salary cannot be negative or Zero.");
				}
				System.out.println("Enter Employee's Bonus :");
				double bonus = Double.parseDouble(scanner.nextLine());
				if (bonus < 0) {
					throw new InvalidEmployeeDataException("Bonus cannot be negative.");
				}
				String sqlStatement = "INSERT INTO EMPLOYEE (Name, Department, Email, Phone, Address, Type, Base_salary, Bonus) VALUES(?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sqlStatement);
				stmt.setString(1, name);
				stmt.setString(2, department);
				stmt.setString(3, email);
				stmt.setString(4, phone);
				stmt.setString(5, address);
				stmt.setString(6, type);
				stmt.setDouble(7, base_salary);
				stmt.setDouble(8, bonus);

			} else {
				System.out.println("Enter Employee's Hourly Rate :");
				double hourly_rate = Double.parseDouble(scanner.nextLine());
				if (hourly_rate <= 0) {
					throw new InvalidEmployeeDataException("Hourly Rate cannot be negative or Zero.");
				}
				System.out.println("Enter Employee's  Worked Hours :");
				int hours_worked = Integer.parseInt(scanner.nextLine());
				if (hours_worked < 0) {
					throw new InvalidEmployeeDataException("Worked Hours cannot be negative.");
				}
				String sqlStatement = "INSERT INTO EMPLOYEE (Name, Department, Email, Phone, Address, Type, Hourly_rate, Hours_worked) VALUES(?,?,?,?,?,?,?,?)";
				stmt = conn.prepareStatement(sqlStatement);
				stmt.setString(1, name);
				stmt.setString(2, department);
				stmt.setString(3, email);
				stmt.setString(4, phone);
				stmt.setString(5, address);
				stmt.setString(6, type);
				stmt.setDouble(7, hourly_rate);
				stmt.setInt(8, hours_worked);

			}
			int row = stmt.executeUpdate();
			System.out.println(row + " record inserted.");

		} catch (SQLException | InvalidEmployeeDataException e) {

			System.out.println("Insert Error : " + e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("Insert Error: Please enter a valid numeric value.");
		}

	}

	private void searchEmployeeByID() {
		System.out.println("Enter Employee ID to Search : ");
		try {
			int idToSearch = Integer.parseInt(scanner.nextLine());
			String sqlStatement = "SELECT * FROM EMPLOYEE WHERE ID= ?";

			PreparedStatement stmt = conn.prepareStatement(sqlStatement);
			stmt.setInt(1, idToSearch);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("ID");
				String name = rs.getString("name");
				String department = rs.getString("department");
				String email = rs.getString("email");
				String phone = rs.getString("phone");
				String address = rs.getString("address");
				String type = rs.getString("type");

				Employee emp = null;
				if (type.equalsIgnoreCase("F")) {
					double base_salary = rs.getDouble("base_salary");
					double bonus = rs.getDouble("bonus");

					emp = new FullTimeEmployee(id, name, department, email, phone, address, base_salary, bonus);
					System.out.println(emp);

				} else {
					double hourly_rate = rs.getDouble("hourly_rate");
					int hours_worked = rs.getInt("hours_worked");

					emp = new Contractor(id, name, department, email, phone, address, hourly_rate, hours_worked);
					System.out.println(emp);
				}
			} else {
				System.out.println("Not Find the Employee.");
			}

		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a integer for the ID.");
		} catch (SQLException e) {

			System.out.println("Database error: " + e.getMessage());
		}

	}

	private void updateEmployeeSalary() {
		System.out.println("Enter ID to Update Salary Information: ");
		try {
			int idToUpdate = Integer.parseInt(scanner.nextLine());
			String sqlStatement = "SELECT type FROM EMPLOYEE WHERE ID= ?";
			PreparedStatement stmt = conn.prepareStatement(sqlStatement);
			stmt.setInt(1, idToUpdate);

			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String type = rs.getString("type");
				if (type.equalsIgnoreCase("F")) {
					System.out.println("Enter New Base Salary: ");
					double newBase_salary = Double.parseDouble(scanner.nextLine());
					if (newBase_salary <= 0) {
						throw new InvalidEmployeeDataException("New Base Salary cannot be negative or Zero.");

					}
					System.out.println("Enter New Bonus: ");
					double newBonus = Double.parseDouble(scanner.nextLine());
					if (newBonus < 0) {
						throw new InvalidEmployeeDataException("New Bonus cannot be negative.");

					}
					String updateSql = "UPDATE EMPLOYEE SET base_salary= ?, bonus = ?  WHERE id=  ?";
					PreparedStatement pstmt = conn.prepareStatement(updateSql);
					pstmt.setDouble(1, newBase_salary);
					pstmt.setDouble(2, newBonus);
					pstmt.setInt(3, idToUpdate);
					pstmt.executeUpdate();
				} else {
					System.out.println("Enter New Hourly Rate: ");
					double newHourly_rate = Double.parseDouble(scanner.nextLine());
					if (newHourly_rate <= 0) {
						throw new InvalidEmployeeDataException("New HourlyRate cannot be negative or Zero.");
					}
					System.out.println("Enter New Worked Hours : ");
					int newHoursWorked = Integer.parseInt(scanner.nextLine());
					if (newHoursWorked < 0) {
						throw new InvalidEmployeeDataException("New Worked Hours cannot be negative.");
					}

					String updateSql = "UPDATE EMPLOYEE SET hourly_rate= ?, hours_worked= ? WHERE id= ?";
					PreparedStatement pstmt = conn.prepareStatement(updateSql);
					pstmt.setDouble(1, newHourly_rate);
					pstmt.setInt(2, newHoursWorked);
					pstmt.setInt(3, idToUpdate);
					pstmt.executeUpdate();
				}

				System.out.println("Salary Updated Successfully!");
			} else {
				System.out.println("Employee not found.");
			}
		} catch (NumberFormatException e) {
			System.out.println("Update Error: Please enter a valid number.");
		} catch (InvalidEmployeeDataException e) {
			System.out.println("Update Error: " + e.getMessage());
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	private void removeEmployee() {
		System.out.println("Enter ID to Remove: ");
		try {
			int idToRemove = Integer.parseInt(scanner.nextLine());
			String sqlStatement = "DELETE FROM EMPLOYEE WHERE ID=?";
			PreparedStatement stmt = conn.prepareStatement(sqlStatement);
			stmt.setInt(1, idToRemove);
			int row = stmt.executeUpdate();
			System.out.println(row + " record removed");
		} catch (SQLException e) {
			System.out.println(e.getMessage());

		} catch (NumberFormatException e) {
			System.out.println("Invalid input. Please enter a integer for the ID.");
		}

	}

	private void printSalaryReport() {
		System.out.println("Enter ID to view Salary Report: ");
		try {
			int id = Integer.parseInt(scanner.nextLine());
			String sql = "SELECT * FROM EMPLOYEE WHERE ID = ?";

			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Employee emp = null;
				String type = rs.getString("type");

				if (type.equalsIgnoreCase("F")) {
					emp = new FullTimeEmployee(id, rs.getString("name"), rs.getString("department"),
							rs.getString("email"), rs.getString("phone"), rs.getString("address"),
							rs.getDouble("base_salary"), rs.getDouble("bonus"));
				} else {
					emp = new Contractor(id, rs.getString("name"), rs.getString("department"), rs.getString("email"),
							rs.getString("phone"), rs.getString("address"), rs.getDouble("hourly_rate"),
							rs.getInt("hours_worked"));
				}

				double salary = emp.calculateSalary();
				TaxCaculator tc = new TaxCaculator(0.15);
				double tax = tc.applyTax(salary);
				double netPay = salary - tax;

				System.out.println("--------------------Employee Salary Table -------------------------------");
				System.out.println("Employee \t Gross Pay($) \t Tax($) \t Net Pay($) ");
				System.out.println(emp.getName() + "\t\t" + salary + "\t\t" + tax + "\t\t" + netPay);
			} else {
				System.out.println("Employee not found.");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("ID Should be a Integer");
		}
	}

	private void disconnect() {
		try {
			conn.close();
			System.out.println("Connection closed!");
			System.out.println("Goodbye and welcome to use Salary Management System next time!");
			scanner.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}

	}
}
