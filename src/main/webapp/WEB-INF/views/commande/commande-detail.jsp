<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Détail commande</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { border-collapse: collapse; width: 100%; margin-top: 10px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        .erreur { color: #b00020; }
        .total { text-align: right; font-weight: bold; margin-top: 10px; }
        form.inline { display: inline; }
        .ajout { margin-top: 25px; padding: 12px; background: #f7f7f7; }
        select, input { padding: 6px; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/menu.jsp"/>
    <h1>Commande n° ${commande.id}</h1>

    <p>
        <strong>Client :</strong> <c:out value="${commande.client.nom} ${commande.client.prenom}"/><br>
        <strong>Date :</strong> ${commande.dateCommande}<br>
        <strong>Statut :</strong> <c:out value="${commande.statut}"/>
    </p>
    <p>
        <a href="${pageContext.request.contextPath}/commandes?action=edit&id=${commande.id}">Modifier l'en-tête</a> |
        <a href="${pageContext.request.contextPath}/commandes">Retour à la liste</a>
    </p>

    <c:if test="${not empty erreur}">
        <p class="erreur"><c:out value="${erreur}"/></p>
    </c:if>

    <h2>Lignes de la commande</h2>
    <table>
        <tr>
            <th>Produit</th><th>Prix unitaire</th><th>Quantité</th><th>Sous-total</th><th>Action</th>
        </tr>
        <c:forEach var="l" items="${commande.lignes}">
            <tr>
                <td><c:out value="${l.produit.designation}"/></td>
                <td><fmt:formatNumber value="${l.prixUnitaire}" minFractionDigits="2" maxFractionDigits="2"/></td>
                <td>${l.quantite}</td>
                <td><fmt:formatNumber value="${l.sousTotal}" minFractionDigits="2" maxFractionDigits="2"/></td>
                <td>
                    <form class="inline" method="post" action="${pageContext.request.contextPath}/commandes">
                        <input type="hidden" name="action" value="removeLigne">
                        <input type="hidden" name="idCommande" value="${commande.id}">
                        <input type="hidden" name="idProduit" value="${l.produit.id}">
                        <button type="submit">Retirer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty commande.lignes}">
            <tr><td colspan="5">Aucune ligne pour l'instant.</td></tr>
        </c:if>
    </table>

    <p class="total">Total : <fmt:formatNumber value="${commande.total}" minFractionDigits="2" maxFractionDigits="2"/></p>

    <div class="ajout">
        <h3>Ajouter un produit</h3>
        <c:choose>
            <c:when test="${empty produits}">
                <p>Aucun produit disponible.
                    <a href="${pageContext.request.contextPath}/produits?action=new">Crée d'abord un produit.</a></p>
            </c:when>
            <c:otherwise>
                <form method="post" action="${pageContext.request.contextPath}/commandes">
                    <input type="hidden" name="action" value="addLigne">
                    <input type="hidden" name="idCommande" value="${commande.id}">

                    <select name="idProduit" required>
                        <c:forEach var="p" items="${produits}">
                            <option value="${p.id}">
                                <c:out value="${p.designation}"/> (<fmt:formatNumber value="${p.prix}" minFractionDigits="2" maxFractionDigits="2"/>)
                            </option>
                        </c:forEach>
                    </select>
                    <input type="number" name="quantite" min="1" value="1" required>
                    <button type="submit">Ajouter</button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>