<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Clients</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f0f0f0; }
        .erreur { color: #b00020; margin-bottom: 10px; }
        form.inline { display: inline; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/menu.jsp"/>
    <h1>Espace Client</h1>

    <c:if test="${not empty erreur}">
        <p class="erreur"><c:out value="${erreur}"/></p>
    </c:if>

    <p><a href="${pageContext.request.contextPath}/clients?action=new">+ Nouveau client</a></p>

    <table>
        <tr>
            <th>ID</th><th>Nom</th><th>Prénom</th><th>Email</th>
            <th>Téléphone</th><th>Adresse</th><th>Actions</th>
        </tr>
        <c:forEach var="c" items="${clients}">
            <tr>
                <td>${c.id}</td>
                <td><c:out value="${c.nom}"/></td>
                <td><c:out value="${c.prenom}"/></td>
                <td><c:out value="${c.email}"/></td>
                <td><c:out value="${c.telephone}"/></td>
                <td><c:out value="${c.adresse}"/></td>
                <td>
                    <a href="${pageContext.request.contextPath}/clients?action=edit&id=${c.id}">Modifier</a>
                    <form class="inline" method="post" action="${pageContext.request.contextPath}/clients"
                          onsubmit="return confirm('Supprimer ce client ?');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${c.id}">
                        <button type="submit">Supprimer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty clients}">
            <tr><td colspan="7">Aucun client enregistré.</td></tr>
        </c:if>
    </table>
</body>
</html>