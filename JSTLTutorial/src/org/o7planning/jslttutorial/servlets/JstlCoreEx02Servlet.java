package org.o7planning.jslttutorial.servlets;

import java.io.IOException;
import java.util.List;
 
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.o7planning.jslttutorial.beans.Dept;
import org.o7planning.jslttutorial.utils.DBUtils;
 
@WebServlet("/jstlCoreExample02")
public class JstlCoreEx02Servlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    public JstlCoreEx02Servlet() {
        super();
    }
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        // Chuẩn bị dữ liệu từ DB (Mô phỏng).
        List<Dept> list = DBUtils.queryDepartments();
 
        // Lưu dữ liệu vào thuộc tính (attribute) 'departments' của request.
        request.setAttribute("departments", list);
 
        // Tạo đối tượng RequestDispatcher
        // để forward (chuyển tiếp) yêu cầu tới jstl_core_example02.jsp
        RequestDispatcher dispatcher = getServletContext()
                .getRequestDispatcher("/WEB-INF/jsps/jstl_core_example02.jsp");
 
        // Forward (Chuyển tiếp) yêu cầu, để hiển thị trên trang JSP.
        dispatcher.forward(request, response);
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
 
}
