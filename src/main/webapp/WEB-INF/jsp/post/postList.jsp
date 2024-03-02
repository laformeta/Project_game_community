<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<h1>포스트 리스트</h1>

<div class="d-flex justify-content-center">
    <div class="">
        <h1>글 목록</h1>
        <br>
        <div>
            <form id="categoryForm">
                <select name="category" id="category">
                    <option value="일반">일반</option>
                    <option value="질문">질문</option>
                    <option value="자랑">자랑</option>
                </select>
            </form>
        </div>

        <table class="table">
            <thead>
                <tr>
                    <th>글 번호</th>
                    <th>작성자</th>
                    <th>태그</th>
                    <th>제목</th>
                    <th>등록일</th>
                    <th>수정일</th>
                    <th>추천</th>
                </tr>
            </thead>
            <tbody id="postTable">
                <!-- 여기에 동적으로 생성되는 행이 들어갈 것입니다. -->
                <c:forEach items="${postList}" var="post">
                    <tr>
                        <td>${post.id}</td>
                        <td>${post.nickname}</td>
                        <td>${post.category}</td>
                        <td><a href="/post/post-detail-view?postId=${post.id}">${post.subject}</a></td>
                        <td><fmt:formatDate value="${post.createdAt}" pattern="yyyy년 M월 d일 HH:mm:ss" /></td>
                        <td><fmt:formatDate value="${post.updatedAt}" pattern="yyyy년 M월 d일 HH:mm:ss" /></td>
                        <td>따봉</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div class="d-flex justify-content-end">
            <!-- 글쓰기 버튼 -->
            <a href="/post/post-create-view" class="btn btn-info">글쓰기</a>
        </div>
    </div>
</div>