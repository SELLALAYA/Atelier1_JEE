<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des commandes</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        ul { line-height: 2; font-size: 18px; }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/views/menu.jsp"/>
    <h1>Gestion des commandes</h1>
    <p>Choisis un espace :</p>
    <ul>
        <li><a href="${pageContext.request.contextPath}/clients">Espace Client</a></li>
        <li><a href="${pageContext.request.contextPath}/produits">Espace Produit</a></li>
        <li><a href="${pageContext.request.contextPath}/commandes">Espace Commande</a></li>
    </ul>
</body>
</html>