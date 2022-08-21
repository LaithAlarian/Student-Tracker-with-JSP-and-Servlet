package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class StudentDbUtil {

	private DataSource dataSource;

	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}

	public List<Student> getStudents() throws Exception {

		List<Student> students = new ArrayList<>();

		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;

		try {
			// get a connection
			myConn = dataSource.getConnection();

			// create sql statement
			String sql = "select * from student order by id ASC";

			myStmt = myConn.createStatement();

			// execute query
			myRs = myStmt.executeQuery(sql);

			// process result set
			while (myRs.next()) {

				// retrieve data from result set row
				int id = myRs.getInt("id");
				String firstName = myRs.getString("firstname");
				String lastName = myRs.getString("lastname");
				String email = myRs.getString("email");

				// create new student object
				Student tempStudent = new Student(id, firstName, lastName, email);

				// add it to the list of students
				students.add(tempStudent);
			}

			return students;
		} finally {
			// close JDBC objects
			close(myConn, myStmt, myRs);
		}
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

		try {
			if (myRs != null) {
				myRs.close();
			}

			if (myStmt != null) {
				myStmt.close();
			}

			if (myConn != null) {
				myConn.close(); // doesn't really close it ... just puts back in connection pool
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void addStudent(Student theStudent) throws Exception {

		Connection myConn = null;
		PreparedStatement myStmt = null;

		try {
			// get db connection
			myConn = dataSource.getConnection();

			// create sql for insert
			String sql = "insert into student(firstName,lastName,email) values(?,?,?)";

			myStmt = myConn.prepareStatement(sql);

			// set the param values for the student

			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());

			// execute sql insert

			myStmt.execute();
		} finally {
			close(myConn, myStmt, null);
		}

		// create sql for insert

		// set the param values for the student

		// execute sql insert

		// clean up JDBC object

	}

	public Student getStudents(String theStudentId) throws Exception {

		Student theStudent = null;

		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;

		int studentId;

		try {

			// convert student id to int

			studentId = Integer.parseInt(theStudentId);

			// get connection to database

			myConn = dataSource.getConnection();

			// create sql to get selected stident
			String sql = "Select * from student where id=?";
			// create prepared statement
			myStmt = myConn.prepareStatement(sql);

			// set param
			myStmt.setInt(1, studentId);

			// execute statement
			myRs = myStmt.executeQuery();

			/// retrive data from result set row
			
			if(myRs.next())
			{
				String firstName=myRs.getString("firstName");
				String lastName=myRs.getString("lastName");
				String email=myRs.getString("email");
				
				//use the studentid during construction
				
				theStudent=new Student(studentId, firstName, lastName, email);
			}else
			{
				throw new Exception("Could not find student id :"+studentId);
			}

		} finally {
			// TODO: handle finally clause

			// clean up
			
			close(myConn, myStmt, myRs);
		}

		return theStudent;
	}

	public void updateStudent(Student theStudent) throws Exception {
		
		Connection myConn=null;
			PreparedStatement myStmt=null;
		
		try {
			
			
			
			
			//get db connection
				myConn=dataSource.getConnection();
			
			
			
			//create SQL update statement
			String sql="update student set firstName=?,lastName=?,email=? where id=?";
			
			
			//prepare statement
			
			myStmt=myConn.prepareStatement(sql);
			 
			//set  params
			
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.setInt(4, theStudent.getId());
			
			
			//execute SQL statement
			
			
			myStmt.execute();
			
		} finally {
			//lean up JDBC objects
			
			close(myConn, myStmt, null);
			
		}
		
		
		
		
	}

	public void deleteStudent(Student student) throws Exception {
		
		Connection connection=null;
		PreparedStatement statement=null;
		
		try {
			
		
			//get db connection
			connection=dataSource.getConnection();
			
			//create querey
			
			String sql="delete from student where id=?";
			
			//prepare statement
			statement=connection.prepareStatement(sql);
			
			//set parameter
			
			statement.setInt(1, student.getId());
			
			statement.execute();
			
			
			
		} finally {
			close(connection, statement, null);
		}
		
	}

}
