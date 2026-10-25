<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Produits</title>
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
    <h1>Espace Produit</h1>

    <c:if test="${not empty erreur}">
        <p class="erreur"><c:out value="${erreur}"/></p>
    </c:if>

    <p><a href="${pageContext.request.contextPath}/produits?action=new">+ Nouveau produit</a></p>

    <table>
        <tr>
            <th>ID</th><th>Désignation</th><th>Prix</th><th>Stock</th><th>Actions</th>
        </tr>
        <c:forEach var="p" items="${produits}">
            <tr>
                <td>${p.id}</td>
                <td><c:out value="${p.designation}"/></td>
                <td><fmt:formatNumber value="${p.prix}" minFractionDigits="2" maxFractionDigits="2"/></td>
                <td>${p.stock}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/produits?action=edit&id=${p.id}">Modifier</a>
                    <form class="inline" method="post" action="${pageContext.request.contextPath}/produits"
                          onsubmit="return confirm('Supprimer ce produit ?');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${p.id}">
                        <button type="submit">Supprimer</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty produits}">
            <tr><td colspan="5">Aucun produit enregistré.</td></tr>
        </c:if>
    </table>
</body>
</html>