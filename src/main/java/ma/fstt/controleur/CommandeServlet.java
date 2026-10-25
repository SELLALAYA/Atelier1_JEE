package ma.fstt.controleur;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.fstt.Modele.dao.ClientDao;
import ma.fstt.Modele.dao.CommandeDao;
import ma.fstt.Modele.dao.ProduitDao;
import ma.fstt.Modele.entities.client;
import ma.fstt.Modele.entities.Commande;
import ma.fstt.Modele.entities.ligne_commande;
import ma.fstt.Modele.entities.Produit;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/commandes")
public class CommandeServlet extends HttpServlet {

    private static final String LISTE  = "/WEB-INF/views/commande/commande-list.jsp";
    private static final String FORM   = "/WEB-INF/views/commande/commande-form.jsp";
    private static final String DETAIL = "/WEB-INF/views/commande/commande-detail.jsp";

    @Inject
    private CommandeDao commandeDao;
    @Inject
    private ClientDao clientDao;
    @Inject
    private ProduitDao produitDao;

    // ====================== GET ======================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                req.setAttribute("clients", clientDao.findAll());
                req.setAttribute("aujourdhui", LocalDate.now());
                req.getRequestDispatcher(FORM).forward(req, resp);
                break;

            case "edit": {
                Commande c = commandeDao.findById(Integer.parseInt(req.getParameter("id")));
                if (c == null) {
                    resp.sendRedirect(req.getContextPath() + "/commandes");
                    return;
                }
                req.setAttribute("commande", c);
                req.setAttribute("clients", clientDao.findAll());
                req.getRequestDispatcher(FORM).forward(req, resp);
                break;
            }

            case "detail":
                afficherDetail(req, resp, Integer.parseInt(req.getParameter("id")), null);
                break;

            default:
                req.setAttribute("commandes", commandeDao.findAll());
                req.getRequestDispatcher(LISTE).forward(req, resp);
        }
    }

    // ====================== POST ======================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        String ctx = req.getContextPath();

        switch (action) {
            case "delete":
                commandeDao.delete(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect(ctx + "/commandes");
                break;

            case "save":
                enregistrerEnTete(req, resp);
                break;

            case "addLigne":
                ajouterLigne(req, resp);
                break;

            case "removeLigne": {
                int idCommande = Integer.parseInt(req.getParameter("idCommande"));
                int idProduit = Integer.parseInt(req.getParameter("idProduit"));
                Commande c = commandeDao.findById(idCommande);
                if (c != null) {
                    c.getLignes().removeIf(l -> l.getProduit().getId() == idProduit);
                    commandeDao.update(c);
                }
                resp.sendRedirect(ctx + "/commandes?action=detail&id=" + idCommande);
                break;
            }

            default:
                resp.sendRedirect(ctx + "/commandes");
        }
    }

    // ---------- création / modification de l'en-tête ----------
    private void enregistrerEnTete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String ctx = req.getContextPath();
        try {
            client client = clientDao.findById(Integer.parseInt(req.getParameter("idClient")));
            LocalDate date = LocalDate.parse(req.getParameter("dateCommande"));
            String statut = req.getParameter("statut");
            String id = req.getParameter("id");

            if (client == null) {
                resp.sendRedirect(ctx + "/commandes");
                return;
            }

            Commande cmd;
            if (id == null || id.isBlank()) {
                cmd = new Commande();
                cmd.setClient(client);
                cmd.setDateCommande(date);
                cmd.setStatut(statut);
                commandeDao.save(cmd);
            } else {
                cmd = commandeDao.findById(Integer.parseInt(id));   // on garde les lignes existantes
                cmd.setClient(client);
                cmd.setDateCommande(date);
                cmd.setStatut(statut);
                commandeDao.update(cmd);
            }
            resp.sendRedirect(ctx + "/commandes?action=detail&id=" + cmd.getId());

        } catch (NumberFormatException | DateTimeParseException e) {
            resp.sendRedirect(ctx + "/commandes");
        }
    }

    // ---------- ajout d'une ligne ----------
    private void ajouterLigne(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int idCommande = Integer.parseInt(req.getParameter("idCommande"));
        Commande cmd = commandeDao.findById(idCommande);
        if (cmd == null) {
            resp.sendRedirect(req.getContextPath() + "/commandes");
            return;
        }

        Produit produit;
        int quantite;
        try {
            produit = produitDao.findById(Integer.parseInt(req.getParameter("idProduit")));
            quantite = Integer.parseInt(req.getParameter("quantite"));
        } catch (NumberFormatException e) {
            afficherDetail(req, resp, idCommande, "Produit ou quantité invalide.");
            return;
        }

        if (produit == null || quantite <= 0) {
            afficherDetail(req, resp, idCommande, "Choisis un produit et une quantité supérieure à 0.");
            return;
        }

        // Si le produit est déjà dans la commande, on augmente la quantité
        ligne_commande existante = null;
        for (ligne_commande l : cmd.getLignes()) {
            if (l.getProduit().getId() == produit.getId()) {
                existante = l;
            }
        }
        if (existante != null) {
            existante.setQuantite(existante.getQuantite() + quantite);
        } else {
            cmd.getLignes().add(new ligne_commande(0, quantite, produit.getPrix(), produit));
        }

        commandeDao.update(cmd);
        resp.sendRedirect(req.getContextPath() + "/commandes?action=detail&id=" + idCommande);
    }

    // ---------- affichage de la page de détail ----------
    private void afficherDetail(HttpServletRequest req, HttpServletResponse resp,
                                int idCommande, String erreur)
            throws ServletException, IOException {
        Commande cmd = commandeDao.findById(idCommande);
        if (cmd == null) {
            resp.sendRedirect(req.getContextPath() + "/commandes");
            return;
        }
        req.setAttribute("commande", cmd);
        req.setAttribute("produits", produitDao.findAll());
        req.setAttribute("erreur", erreur);
        req.getRequestDispatcher(DETAIL).forward(req, resp);
    }
}
