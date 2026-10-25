package ma.fstt.controleur;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.fstt.Modele.dao.ProduitDao;
import ma.fstt.Modele.entities.Produit;

import java.io.IOException;

@WebServlet("/produits")
public class ProduitServlet extends HttpServlet {

    private static final String LISTE = "/WEB-INF/views/produit/produit-list.jsp";
    private static final String FORM  = "/WEB-INF/views/produit/produit-form.jsp";

    @Inject
    private ProduitDao produitDao;

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
                Produit p = produitDao.findById(Integer.parseInt(req.getParameter("id")));
                if (p == null) {
                    resp.sendRedirect(req.getContextPath() + "/produits");
                    return;
                }
                req.setAttribute("produit", p);
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
                produitDao.delete(Integer.parseInt(req.getParameter("id")));
            } catch (RuntimeException e) {
                afficherListe(req, resp, "Suppression impossible : ce produit figure dans des commandes.");
                return;
            }
            resp.sendRedirect(req.getContextPath() + "/produits");
            return;
        }

        // action = save
        Produit p = new Produit();
        p.setDesignation(req.getParameter("designation"));
        String id = req.getParameter("id");
        if (id != null && !id.isBlank()) {
            p.setId(Integer.parseInt(id));
        }

        try {
            p.setPrix(Double.parseDouble(req.getParameter("prix").replace(',', '.')));
            p.setStock(Integer.parseInt(req.getParameter("stock")));
        } catch (NumberFormatException e) {
            req.setAttribute("erreur", "Le prix et le stock doivent être des nombres valides.");
            req.setAttribute("produit", p);
            req.getRequestDispatcher(FORM).forward(req, resp);
            return;
        }

        if (p.getPrix() < 0 || p.getStock() < 0) {
            req.setAttribute("erreur", "Le prix et le stock ne peuvent pas être négatifs.");
            req.setAttribute("produit", p);
            req.getRequestDispatcher(FORM).forward(req, resp);
            return;
        }

        if (p.getId() == 0) {
            produitDao.save(p);
        } else {
            produitDao.update(p);
        }
        resp.sendRedirect(req.getContextPath() + "/produits");
    }

    private void afficherListe(HttpServletRequest req, HttpServletResponse resp, String erreur)
            throws ServletException, IOException {
        req.setAttribute("produits", produitDao.findAll());
        req.setAttribute("erreur", erreur);
        req.getRequestDispatcher(LISTE).forward(req, resp);
    }
}
