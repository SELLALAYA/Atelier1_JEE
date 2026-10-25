package ma.fstt.controleur;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.fstt.Modele.dao.ClientDao;
import ma.fstt.Modele.entities.client;

import java.io.IOException;

@WebServlet("/clients")
public class ClientServlet extends HttpServlet {

    private static final String LISTE = "/WEB-INF/views/client/client-list.jsp";
    private static final String FORM  = "/WEB-INF/views/client/client-form.jsp";

    @Inject
    private ClientDao clientDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                req.getRequestDispatcher(FORM).forward(req, resp);
                break;
            case "edit":
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("client", clientDao.findById(id));
                req.getRequestDispatcher(FORM).forward(req, resp);
                break;
            default:
                afficherListe(req, resp, null);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            try {
                clientDao.delete(Integer.parseInt(req.getParameter("id")));
            } catch (RuntimeException e) {
                afficherListe(req, resp, "Suppression impossible : ce client a des commandes.");
                return;
            }
        } else { // save
            client c = new client();
            c.setNom(req.getParameter("nom"));
            c.setPrenom(req.getParameter("prenom"));
            c.setEmail(req.getParameter("email"));
            c.setTelephone(req.getParameter("telephone"));
            c.setAdresse(req.getParameter("adresse"));

            String id = req.getParameter("id");
            if (id == null || id.isBlank()) {
                clientDao.save(c);
            } else {
                c.setId(Integer.parseInt(id));
                clientDao.update(c);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/clients");
    }

    private void afficherListe(HttpServletRequest req, HttpServletResponse resp, String erreur)
            throws ServletException, IOException {
        req.setAttribute("clients", clientDao.findAll());
        req.setAttribute("erreur", erreur);
        req.getRequestDispatcher(LISTE).forward(req, resp);
    }
}
