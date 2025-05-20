<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<div class="carousel">
    <%
        String[] tags = { "#커플", "#맛집", "#놀거리", "#10대" };

        for (int i = 0; i < tags.length; i++) {
    %>
        <div class="carousel-item">
            <div><%= tags[i] %></div>
        </div>
    <% } %>
</div>
