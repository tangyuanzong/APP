
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
 * Servlet implementation class singin
 */
@WebServlet("/singin")
public class singin extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/mysql?useUnicode"  //地址
            + "=true&characterEncoding=utf-8&useSSL=false";  
	static final String USER_NAME = "root";
	static final String PASSWORD = "tyz19960912";
	
	private Connection conn;
	private Statement stmt;
	
    /**
     * @throws SQLException 
     * @throws ClassNotFoundException 
     * @see HttpServlet#HttpServlet()
     */
    public singin() throws SQLException, ClassNotFoundException {
    	
    	 super();
    	Class.forName(JDBC_DRIVER);
    	conn = (Connection) DriverManager.getConnection(DB_URL,USER_NAME,PASSWORD);
       
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	//	response.getWriter().append("Served at: ").append(request.getContextPath());
		
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			stmt = (Statement) conn.createStatement();
			String sql = "SELECT * FROM users where username='"+username+"'";
			ResultSet rs = stmt.executeQuery(sql);
			boolean success = false;
			int count = 0;
			while(rs.next()) {
				if(password.equals(rs.getString("pass")))
					success = true;
				count++;
			}
			
			JSONObject result = new JSONObject();
			if(success) {
				rs.close();
				sql = "SELECT * FROM person where username='"+username+"'";
				rs = stmt.executeQuery(sql);
				while(rs.next()) {
					result.put("username", rs.getString("username"));
					result.put("name", rs.getString("name"));
					result.put("age", rs.getInt("age"));
					result.put("teleno", rs.getString("telenum"));
				}
			} else {
				if(count == 0) {
					result.put("error", "用户不存在");
				} else {
					result.put("error", "密码错误");
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
