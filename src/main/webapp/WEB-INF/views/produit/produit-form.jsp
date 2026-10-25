<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Produit</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        label { display: block; margin-top: 10px; }
        input { padding: 6px; width: 300px; }
        button { margin-top: 15px; padding: 6px 14px; }
        .erreur { color: #b00020; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/views/menu.jsp"/>
    <h1>
        <c:choose>
            <c:when test="${not empty produit and produit.id != 0}">Modifier le produit</c:when>
            <c:otherwise>Nouveau produit</c:otherwise>
        </c:choose>
    </h1>

    <c:if test="${not empty erreur}">
        <p class="erreur"><c:out value="${erreur}"/></p>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/produits">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="id" value="${produit.id != 0 ? produit.id : ''}">

        <label>Désignation
            <input type="text" name="designation" value="<c:out value='${produit.designation}'/>" required>
        </label>
        <label>Prix
            <input type="number" name="prix" step="0.01" min="0" value="${produit.prix}" required>
        </label>
        <label>Stock
            <input type="number" name="stock" step="1" min="0" value="${produit.stock}" required>
        </label>

        <button type="submit">Enregistrer</button>
        <a href="${pageContext.request.contextPath}/produits">Annuler</a>
    </form>
</body>
</html>