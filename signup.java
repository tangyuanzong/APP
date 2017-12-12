

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * Servlet implementation class signup
 */
@WebServlet("/signup")
public class signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/mysql?useUnicode"  //地址
            + "=true&characterEncoding=utf-8&useSSL=false";  
	static final String USER_NAME = "root";
	static final String PASSWORD = "tyz19960912";
	
	private Connection conn;
	private Statement stmt;
       
    /**
     * @throws ClassNotFoundException 
     * @throws SQLException 
     * @see HttpServlet#HttpServlet()
     */
    public signup() throws ClassNotFoundException, SQLException {
        super();
        // TODO Auto-generated constructor stub
        
        Class.forName(JDBC_DRIVER);
    	conn = (Connection) DriverManager.getConnection(DB_URL,USER_NAME, PASSWORD);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
    try {
	     String username = request.getParameter("username");
	     String password = request.getParameter("password");
         String name = request.getParameter("name");
	     String age = request.getParameter("age");
	     String telenum = request.getParameter("telenum");
	     
		 stmt = (Statement) conn.createStatement();
			
			String sql = "SELECT * FROM users where username='"+username+"'";
			ResultSet rs = stmt.executeQuery(sql);
			int size = 0;
			int size1 = 0;
			while(rs.next()) {
				size++;
			}
			sql = "SELECT * FROM person where name='"+name+"'";
			ResultSet rs1 = stmt.executeQuery(sql);
			while(rs1.next()) {
				size1++;
			}
			JSONObject result = new JSONObject();
			if(size != 0)
				result.put("error", "用户名已被注册 ");
			else if(size1!=0) {
				result.put("error", "姓名已被注册 ");
			}
			
			else {
				
				String sql1 = "INSERT INTO users VALUES('"+username+"', '"+password+"')";
				stmt.executeUpdate(sql1);
				String sql2 = "INSERT INTO person VALUES('"+username+"', '"+name+"', "+age+", '"+telenum+"')";
				stmt.executeUpdate(sql2);
				
				
				result = new JSONObject();	
				sql = "SELECT * FROM person where username='"+username+"'";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					result.put("username", rs.getString("username"));
					result.put("name", rs.getString("name"));
					result.put("age", rs.getInt("age"));
					result.put("teleno", rs.getString("telenum"));
				}
			}
			
			response.setContentType("text/json;charset=UTF-8");
			
            PrintWriter out = response.getWriter();
			out.println(result.toJSONString());
			rs.close();
			out.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
