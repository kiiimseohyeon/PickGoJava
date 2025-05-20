<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String title = request.getParameter("title") != null ? request.getParameter("title") : "Title";
    String id = "carousel_" + System.currentTimeMillis(); 
%>
<div class="category">
    <div class="category-title"><%= title %></div>
    <div class="carousel-wrapper">
        <button class="scroll-btn left" onclick="scrollLeft('<%= id %>')">&#8249;</button>
        <div class="category-content" id="<%= id %>">
            <% for (int i = 0; i < 7; i++) { %>
                <div class="card">
                    <div class="banner-style">
                        <img src="<%= request.getContextPath() %>/images/cafe<%= (i % 3 + 1) %>.jpeg" alt="카페 이미지">
                        <div class="banner-overlay">카페 <%= i + 1 %></div>
                    </div>
                </div>
            <% } %>
        </div>
        <button class="scroll-btn right" onclick="scrollRight('<%= id %>')">&#8250;</button>
    </div>
</div>
