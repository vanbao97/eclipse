package com.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {
	private DataSource dataSouce;

	public StudentDbUtil(DataSource theDataSource) {
		dataSouce = theDataSource;
	}

	public List<Student> getStudents() throws Exception {
		List<Student> students = new ArrayList<>();
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = dataSouce.getConnection();
			myStmt = myConn.createStatement();
			String sql = "select * from student order by last_name";
			myRs = myStmt.executeQuery(sql);
			while (myRs.next()) {
				int id = myRs.getInt("id");
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");

				Student stempStudent = new Student(id, firstName, lastName, email);
				students.add(stempStudent);
			}
			return students;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if (myConn != null) {
				myConn.close();
			}
			if (myStmt != null) {
				myStmt.close();
			}
			if (myRs != null) {
				myRs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addStudent(Student theStudent) throws SQLException {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
			myConn = dataSouce.getConnection();
			String sql = "insert into student" + "(first_Name, last_Name, email)" + "value(?, ?,?)";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());

			myStmt.execute();
		} finally {
			// m: handle finally clause
			close(myConn, myStmt, null);
		}

	}

	public Student getStudent(String theStudentID) throws Exception {
		Student theStudent = null;
		Connection myConn = null;
		PreparedStatement myStmt = null;
		ResultSet myRs = null;
		int studentId;
		try {
			studentId = Integer.parseInt(theStudentID);
			myConn = dataSouce.getConnection();
			String sql = "select * from student where id=?";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setInt(1, studentId);
			myRs = myStmt.executeQuery();
			if (myRs.next()) {
				String firstName = myRs.getString("first_name");
				String lastName = myRs.getString("last_name");
				String email = myRs.getString("email");
				theStudent = new Student(studentId, firstName, lastName, email);
			} else {
				throw new Exception("Could not find student id: " + studentId);
			}
			return theStudent;
		} finally {
			close(myConn, myStmt, myRs);
		}
	}

	public void updateStudent(Student theStudent) throws Exception {
		Connection myConn = null;
		PreparedStatement myStmt = null;
		try {
			myConn = dataSouce.getConnection();
			String sql = "update student set first_name=?, last_name=?, email=? where id=?";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setInt(4, theStudent.getId());
			myStmt.setString(1, theStudent.getFirstName());
			myStmt.setString(2, theStudent.getLastName());
			myStmt.setString(3, theStudent.getEmail());
			myStmt.execute();
		} finally {
			close(myConn, myStmt, null);
		}
	}

	public void deleteStudent(String theStudentID) throws Exception{
		Connection myConn = null;
		PreparedStatement myStmt = null;
		int id = Integer.parseInt(theStudentID);
		try {
			myConn = dataSouce.getConnection();
			String sql = "delete from student where id=?";
			myStmt = myConn.prepareStatement(sql);
			myStmt.setInt(1, id);
			myStmt.execute();
		} finally {
			close(myConn, myStmt, null);
		}
	}
}
