<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="d-flex justify-content-center">
    <div class="w-50">
        <h1>글 상세</h1>

		<span>${lolPost.subject}</span>
        <p>${lolPost.content}</p>
        <c:if test="${not empty lolPost.imagePath}">
            <div class="my-3">
                <img alt="업로드 된 이미지" src="${lolPost.imagePath}" width="300">
            </div>
        </c:if>

        <div class="d-flex justify-content-end my-2">

            <div>
                <a href="/post/post-list-view" class="btn btn-dark">목록</a>
            </div>
        </div>
    </div>
</div>
<script>
    
</script>