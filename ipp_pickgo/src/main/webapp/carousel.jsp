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


/* 현재는 그냥 이미지만 생성하게 만들어놨습니다. 안에 이미지를 추가하고 싶으시면 밑에 이미지 쓰시면 됩니다. */
/*

<div class="category-content" id="banner-carousel">
            <div class="banner-card">
                <img src="<%= request.getContextPath() %>/images/cafe1.jpeg" alt="배너1">
                <div class="banner-text">대전으로 떠나는 여행!<br>ADCAFE</div>
            </div>
            <div class="banner-card">
                <img src="<%= request.getContextPath() %>/images/cafe2.jpeg" alt="배너2">
                <div class="banner-text">지금 커플들이 많이 찾는곳!</div>
            </div>
            <div class="banner-card">
                <img src="<%= request.getContextPath() %>/images/cafe3.jpeg" alt="배너3">
                <div class="banner-text">어르신들이 많이 찾는 힐링</div>
            </div>
            <div class="banner-card">
                <img src="<%= request.getContextPath() %>/images/cafe3.jpeg" alt="배너3">
                <div class="banner-text">어르신들이 많이 찾는 힐링</div>
            </div>
            <div class="banner-card">
                <img src="<%= request.getContextPath() %>/images/cafe3.jpeg" alt="배너3">
                <div class="banner-text">어르신들이 많이 찾는 힐링</div>
            </div>
        </div>
*/
