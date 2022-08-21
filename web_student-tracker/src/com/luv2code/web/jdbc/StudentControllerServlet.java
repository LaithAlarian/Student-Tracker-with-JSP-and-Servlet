package com.luv2code.web.jdbc;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private StudentDbUtil studentDbUtil;
	@Resource(name = "jdbc/web_student_tracker")//to inject 
	private DataSource dataSource;
	
	
	

	@Override
	public void init() throws ServletException {//call by the java ee server or by tomcat when the server first load or instioalize 
		// TODO Auto-generated method stub
		super.init();
		
		//create out student db util .....asd pass in the conn pool /datasource
		try {
			
			studentDbUtil=new StudentDbUtil(dataSource);
			
		} catch (Exception e) {
			throw new ServletException(e);
		}
		
	}




	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		 //list the student ....in MVC fashion 
			
		try {
			
			//read the "command" parameter
			
			String theCommand=request.getParameter("command");
			
			//if the command is missing , then default to listing student
			if(theCommand==null)
			{
				theCommand="LIST";
			}
			
			
			//route to the appropriate method
			
			switch(theCommand)
			{
			case "LIST":
				listStudents(request, response);
				break;
			case "ADD":
				addStudent(request, response);
				break;
			case "LOAD":
					loadStudent(request, response);
				break;
			case "UPDATE":
				updateStudent(request, response);
				break;
			case "DELETE":
				deleteStudent(request,response);
				break;
				
				default:
					listStudents(request, response);
			
			}
			
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}




	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//read student info from the form data
		int id=Integer.parseInt(request.getParameter("studentId"));
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		//create a new student object
		
		Student student=new Student(id, firstName, lastName, email);
		
		//perform update on database
		studentDbUtil.deleteStudent(student);
		
		//send them back to the "list student "page
		
				listStudents(request, response);
		
	}




	private void updateStudent(HttpServletRequest request, HttpServletResponse response)throws Exception {

		//read student info from the form data
		int id=Integer.parseInt(request.getParameter("studentId"));
		
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		//create a new student object
		
		Student theStudent=new Student(id, firstName, lastName, email);
		
		
		
		//perform update on database
		
		studentDbUtil.updateStudent(theStudent);
		
		
		//send them back to the "list student "page
		
		listStudents(request, response);
		
		
	}




	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
				
				//read student info from form date
				String theStudentId=request.getParameter("studentId");
				
				//get student from database (db util)
				
				Student theStudent=studentDbUtil.getStudents(theStudentId);
				
				//plac student in the request aribute
				
				request.setAttribute("THE_STUDENT", theStudent);
				
				//se to jsp page :update-student-form.jsp
				
				RequestDispatcher dispatcher=
						request.getRequestDispatcher("/update-student-form.jsp");
				dispatcher.forward(request, response);
		
	}




	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
			
		//read student info from form date
		
		String firstName=request.getParameter("firstName");
		String lastName=request.getParameter("lastName");
		String email=request.getParameter("email");
		
		//create a new student object
		
		Student theStudent=new Student(firstName, lastName, email);
		
		//add the student to the database
		
		studentDbUtil.addStudent(theStudent);
		
		//send back to main page(the student list)

		listStudents(request, response);
	}




	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//get student from db util
		
		List<Student> students=studentDbUtil.getStudents();
		
		
		//add student the the request
		
		request.setAttribute("STUDENT_LIST", students);
		
		//send the jsp page (view)
		
		RequestDispatcher dispatcher=request.getRequestDispatcher("/list-student.jsp");
		
		dispatcher.forward(request, response);
		
		
		
	}

}
