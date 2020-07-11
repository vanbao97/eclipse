package org.o7planning.servletexamples;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.o7planning.jdbc.ConnectionUtils;
import org.o7planning.servletexamples.model.Person;

/**
 * Servlet implementation class DispalyImageServlet
 */
@WebServlet(urlPatterns = {"/image"})
public class DispalyImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DispalyImageServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Person getImageInTable(Connection conn, long id) throws SQLException {
		String sql = "Select p.Id,p.Name,p.Image_Data,p.Image_File_Name "//
				+ " from Person p where p.id = ?";
		PreparedStatement pstm = conn.prepareStatement(sql);
		pstm.setLong(1, id);
		ResultSet rs = pstm.executeQuery();
		if (rs.next()) {
			String name = rs.getString("Name");
			byte[] imageData = rs.getBytes("Image_Data");
			String imageFileName = rs.getString("Image_File_Name");
			return new Person(id, name, imageData, imageFileName);
		}
		return null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		try {
			conn = ConnectionUtils.getConnection();
			Long id = null;
			try {
				id = Long.parseLong(request.getParameter("id"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			Person person = getImageInTable(conn, id);
			if (person == null) {
				response.sendRedirect(request.getContextPath() + "/images/noimage.png");
				return;
			}
			String imageFileName = person.getImageFileName();
			String contentType = this.getServletContext().getMimeType(imageFileName);
			System.out.println("Content Type: " + contentType);

			response.setHeader("Content-Type", contentType);

			response.setHeader("Content-Length", String.valueOf(person.getImageData().length));

			response.setHeader("Content-Disposition", "inline; filename=\"" + person.getImageFileName() + "\"");

			// Ghi dữ liệu ảnh vào Response.
			response.getOutputStream().write(person.getImageData());
		} catch (Exception e) {
			throw new ServletException(e);
		}finally {
			ConnectionUtils.closeQuietly(conn);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
