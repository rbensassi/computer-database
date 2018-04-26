package main.java.com.excilys.cdb.servlet;

import main.java.com.excilys.cdb.dao.DAOFactory;
import main.java.com.excilys.cdb.dto.ComputerDTO;
import main.java.com.excilys.cdb.model.Company;
import main.java.com.excilys.cdb.model.Computer;
import main.java.com.excilys.cdb.service.CompanyService;
import main.java.com.excilys.cdb.service.ComputerService;
import main.java.com.excilys.cdb.utils.Page;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Servlet.
 */
@WebServlet("/")
public class ServletComputer extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private int paging;
    private int offset;
    private DAOFactory dao;
    private ComputerService computerServ;
    private CompanyService companyServ;
    private ServletContext sc;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletComputer() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            if (request.getParameter("page") != null) {
               offset = (Integer.parseInt(request.getParameter("page")) - 1) * paging;
            }

            ArrayList<ComputerDTO> computerDTOs = new ArrayList<>();

            Page<Computer> pComputer = computerServ.getListComputer(offset, paging);

            for (Computer comp : pComputer.elems) {

                ComputerDTO newObj = new ComputerDTO();
                newObj.setComputer(comp);

                Company company = computerServ.getCompany(comp.getId());
                newObj.setCompany(company);

                if (newObj.getCompany() == null) {
                    newObj.setHasCompany(false);
                } else {
                   newObj.setHasCompany(true);
                }

                computerDTOs.add(newObj);
            }

            long nbElem = computerServ.getNumberComputer();

            request.setAttribute("companyWithComputers", computerDTOs);
            request.setAttribute("numberOfElement", nbElem);
            double nbPage = (double) nbElem / (double) paging;

            request.setAttribute("nbPage", Math.ceil(nbPage));

            RequestDispatcher dispatcher = sc.getRequestDispatcher("/Views/dashboard.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws ServletException {
        System.out.println("Servlet " + this.getServletName() + " has started");
        dao = DAOFactory.getInstance();
        try {
            dao.getConnection();
            dao.setConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        computerServ = new ComputerService();
        companyServ = new CompanyService();

        computerServ.init(dao);
        companyServ.init(dao);
        sc = getServletContext();

        paging = Integer.parseInt(getServletContext().getInitParameter("paging"));
        offset = Integer.parseInt(getServletContext().getInitParameter("offset"));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("b1") != null) {
            paging = Integer.parseInt(request.getParameter("b1"));
        } else if (request.getParameter("b2") != null) {
            paging = Integer.parseInt(request.getParameter("b2"));
        } else {
            paging = Integer.parseInt(request.getParameter("b3"));
        }
        doGet(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("Servlet " + this.getServletName() + " has stopped");
    }
}
