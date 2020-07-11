package Controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.restfb.types.User;

import BEAN.Member;
import BEAN.RestFB;
import DAO.LoginDAO;
import DAO.RegisterDAO;
import DB.DBConnection;

/**
 * Servlet implementation class LoginFacebookServlet
 */
@WebServlet("/login-facebook")
public class LoginFacebookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public LoginFacebookServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String code = request.getParameter("code");
		if (code == null || code.isEmpty()) {
			RequestDispatcher dis = request.getRequestDispatcher("login.jsp");
			dis.forward(request, response);
		} else {
			String accessToken = RestFB.getToken(code);
			User user = RestFB.getUserInfo(accessToken);
			HttpSession session = request.getSession(true);
			session.setAttribute("sessionuser", user.getName());
			
//			request.setAttribute("sessionmemberid", user.getId());
//			request.setAttribute("sessionuser", user.getName());
			Connection conn = DBConnection.CreateConnection();
			String membername = user.getId();
			String memberpass = "fb";
			Member member = new Member();
			member.setMembername(membername);
			member.setMemberpass(memberpass);

			int memberid = LoginDAO.Exportmemberid(request, conn, member);
			session.setAttribute("sessionmemberid", memberid);
			String authentication = LoginDAO.Authenticationmember(request, conn, member);
			if (authentication.equals("success")) {
					RequestDispatcher rd = request.getRequestDispatcher("Homeforward");
					rd.forward(request, response);
			} else {
				if (authentication.equals("fail")) {
					RegisterDAO.InsertAccount(request, conn, member);
					memberid = LoginDAO.Exportmemberid(request, conn, member);
					session.setAttribute("sessionmemberid", memberid);
					RequestDispatcher dis = request.getRequestDispatcher("Homeforward");
					dis.forward(request, response);
				}

			}
			
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
