<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Commande</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        label { display: block; margin-top: 10px; }
        input, select { padding: 6px; width: 320px; }
        button { margin-top: 15px; padding: 6px 14px; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/menu.jsp"/>
    <h1>
        <c:choose>
            <c:when test="${not empty commande}">Modifier la commande n° ${commande.id}</c:when>
            <c:otherwise>Nouvelle commande</c:otherwise>
        </c:choose>
    </h1>

    <c:if test="${empty clients}">
        <p>Aucun client disponible.
            <a href="${pageContext.request.contextPath}/clients?action=new">Crée d'abord un client.</a></p>
    </c:if>

    <c:if test="${not empty clients}">
    <form method="post" action="${pageContext.request.contextPath}/commandes">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="id" value="${commande.id}">

        <label>Client
            <select name="idClient" required>
                <c:forEach var="cl" items="${clients}">
                    <option value="${cl.id}" ${cl.id == commande.client.id ? 'selected' : ''}>
                        <c:out value="${cl.nom} ${cl.prenom}"/>
                    </option>
                </c:forEach>
            </select>
        </label>

        <label>Date
            <input type="date" name="dateCommande"
                   value="${empty commande ? aujourdhui : commande.dateCommande}" required>
        </label>

        <label>Statut
            <select name="statut">
                <option value="EN_COURS" ${commande.statut == 'EN_COURS' ? 'selected' : ''}>En cours</option>
                <option value="VALIDEE"  ${commande.statut == 'VALIDEE'  ? 'selected' : ''}>Validée</option>
                <option value="LIVREE"   ${commande.statut == 'LIVREE'   ? 'selected' : ''}>Livrée</option>
                <option value="ANNULEE"  ${commande.statut == 'ANNULEE'  ? 'selected' : ''}>Annulée</option>
            </select>
        </label>

        <button type="submit">Enregistrer</button>
        <a href="${pageContext.request.contextPath}/commandes">Annuler</a>
    </form>
    </c:if>
</body>
</html>