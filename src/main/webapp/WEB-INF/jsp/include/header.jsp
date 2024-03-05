<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="h-100 d-flex align-items-center">
<!-- logo -->
	<img alt="로고" src="/img/logo.png" width="100">
	<div class="mr-auto">
		<h1>게이머즈</h1>
	</div>
	
	<!-- 로그인 정보 -->
	<div>
	<!-- 로그인 시 -->
			
		<c:if test="${not empty userId}">
		<c:if test="${lolPost.userId == loggedInUserId}">
				<a href="#">회원정보수정</a>
			</c:if>
			<span>${userNickname}님 안녕하세요!</span>
			<a href="/user/sign-out">로그아웃</a>
		</c:if>
		<!-- 로그인 안했을 시 -->
		<c:if test="${empty userId}">
			<span><a href="/user/sign-in-view">로그인</a> 필요</span>
		</c:if>
	</div>
</div>