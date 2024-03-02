<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="d-flex justify-content-center">
    <div class="w-50">
        <h1>글 수정</h1>

        <form id="editForm" enctype="multipart/form-data">
            <input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요" value="${lolPost.subject}">
            <textarea id="content" class="form-control" placeholder="내용을 입력하세요" rows="10">${lolPost.content}</textarea>

            <%-- 이미지가 있을 때만 영역 노출 --%>
            <c:if test="${not empty lolPost.imagePath}">
                <div class="my-3">
                    <img alt="업로드 된 이미지" src="${lolPost.imagePath}" width="300">
                </div>
            </c:if>

            <div class="d-flex justify-content-end my-3">
                <input type="file" id="file" accept=".jpg, .png, .gif, .jpeg">
            </div>

            <div class="d-flex justify-content-between">
                <button type="button" id="deleteBtn" class="btn btn-secondary" data-post-id="${lolPost.id}">삭제</button>

                <div>
                    <a href="/post/post-list-view" class="btn btn-dark">목록</a>
                    <button type="button" id="saveBtn" class="btn btn-info" data-post-id="${lolPost.id}">수정</button>
                </div>
            </div>
        </form>
    </div>
</div>
<script>

</script>