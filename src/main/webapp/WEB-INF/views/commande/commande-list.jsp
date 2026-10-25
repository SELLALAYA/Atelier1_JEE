<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Commandes</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        form.inline { display: inline; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/menu.jsp"/>
    <h1>Espace Commande</h1>

    <p><a href="${pageContext.request.contextPath}/commandes?action=new">+ Nouvelle commande</a></p>

    <table>
        <tr>
            <th>N°</th><th>Date</th><th>Client</th><th>Statut</th><th>Total</th><th>Actions</th>
        </tr>
        <c:forEach var="cmd" items="${commandes}">
            <tr>
                <td>${cmd.id}</td>
                <td>${cmd.dateCommande}</td>
                <td><c:out value="${cmd.client.nom} ${cmd.client.prenom}"/></td>
                <td><c:out value="${cmd.statut}"/></td>
                <td><fmt:formatNumber value="${cmd.total}" minFractionDigits="2" maxFractionDigits="2"/></td>
                <td>
                    <a href="${pageContext.request.contextPath}/commandes?action=detail&id=${cmd.id}">Détail</a> |
                    <a href="${pageContext.request.contextPath}/commandes?action=edit&id=${cmd.id}">Modifier</a>
                    <form class="inline" method="post" action="${pageContext.request.contextPath}/commandes"
                          onsubmit="return confirm('Supprimer cette commande et ses lignes ?');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${cmd.id}">
                        <button type="submit">Supprimer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty commandes}">
            <tr><td colspan="6">Aucune commande enregistrée.</td></tr>
        </c:if>
    </table>
</body>
</html>