<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Client</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 30px; }
        label { display: block; margin-top: 10px; }
        input { padding: 6px; width: 300px; }
        button { margin-top: 15px; padding: 6px 14px; }
    </style>
</head>
<jsp:include page="/WEB-INF/views/menu.jsp"/>
<body>
    <h1>
        <c:choose>
            <c:when test="${not empty client}">Modifier le client</c:when>
            <c:otherwise>Nouveau client</c:otherwise>
        </c:choose>
    </h1>

    <form method="post" action="${pageContext.request.contextPath}/clients">
        <input type="hidden" name="action" value="save">
        <input type="hidden" name="id" value="${client.id}">

        <label>Nom
            <input type="text" name="nom" value="<c:out value='${client.nom}'/>" required>
        </label>
        <label>Prénom
            <input type="text" name="prenom" value="<c:out value='${client.prenom}'/>" required>
        </label>
        <label>Email
            <input type="email" name="email" value="<c:out value='${client.email}'/>">
        </label>
        <label>Téléphone
            <input type="text" name="telephone" value="<c:out value='${client.telephone}'/>">
        </label>
        <label>Adresse
            <input type="text" name="adresse" value="<c:out value='${client.adresse}'/>">
        </label>

        <button type="submit">Enregistrer</button>
        <a href="${pageContext.request.contextPath}/clients">Annuler</a>
    </form>
</body>
</html>