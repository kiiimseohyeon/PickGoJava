<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>PickGo</title>
    <%
        String cssPath = request.getContextPath() + "/pickgo.css";
    %>
    <link rel="stylesheet" href="<%= cssPath %>">
</head>
<body>

    <%@ include file="header.jsp" %>
    <%@ include file="mainBanner.jsp" %>
 	<%@ include file="carousel.jsp" %> 
 

    <jsp:include page="category.jsp">
        <jsp:param name="title" value="ee" />
    </jsp:include>

    <jsp:include page="category.jsp">
        <jsp:param name="title" value="dd" />
    </jsp:include>

    <jsp:include page="category.jsp">
        <jsp:param name="title" value="mm" />
    </jsp:include>

    <%@ include file="footer.jsp" %>
</body>
</html>
