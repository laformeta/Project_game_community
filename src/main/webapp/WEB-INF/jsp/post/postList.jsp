<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<h1>포스트 리스트</h1>

<div class="d-flex justify-content-center">
	<div class="">
		<h1>글 목록</h1>
		<br>
		<div id="messageContainer"></div>
		<div>
			<form id="categoryForm" action="/post/post-list-view" method="get">
				<select name="category" id="category">
					<option value="">--태그 선택--</option>
					<option value="전체">전체</option>
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
						<td><fmt:formatDate value="${post.createdAt}"
								pattern="yyyy년 M월 d일 HH:mm:ss" /></td>
						<td><fmt:formatDate value="${post.updatedAt}"
								pattern="yyyy년 M월 d일 HH:mm:ss" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<div class="d-flex justify-content-end">
			<!-- 검색어 입력 폼 -->
			<div class="d-flex">
				<div>
					<input type="text" id="searchKeyword" class="keyword-input"
						name="keyword" placeholder="검색어를 입력하세요">
					<button type="button"
						class="comment-btn btn btn-light searchButton"
						">검색</button>
				</div>
				<div>

					<!-- 글쓰기 버튼 -->
					<button type="button" class="btn btn-info" data-user-id="${userId}" id="writeBtn">글쓰기</button>
				</div>
			</div>
		</div>
	</div>
</div>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
	$(document).ready(function() {
	// 엔터 키 입력 시 검색 수행
		$(".searchButton").on('click', function() {

		//let keyword = $(this).data("keyword");
		let keyword = $(this).siblings("keyword").val();
		console.log("Keyword: ", keyword);
		});
	
	// 글쓰기버튼 눌렀을시
		$("#writeBtn").on('click', function() {
			let userId = $(this).data("user-id");
			if (!userId) {
				// 비로그인이면 로그인 화면 이동
				alert("로그인을 해주세요.");
				location.href = "/user/sign-in-view";
				return;
			}
			else {
                // 로그인되어 있는 경우, 글쓰기 페이지로 이동
                location.href = "/post/post-create-view";
            }
		});


						$("#category").on('change', function() {
							var selectedCategory = $(this).val(); // select 박스에서 선택한 값 가져오기

							if (selectedCategory === "") {
								// 전체를 선택한 경우 페이지를 리로드
								window.location.href = "/post/post-list-view";
							} else {
								// 선택한 카테고리에 해당하는 게시글을 가져오기
								$.ajax({
									url : '/post/post-list-view',
									type : "get",
									cache : false,
									headers : {
										"cache-control" : "no-cache",
										"pragma" : "no-cache"
									},
									data : {
										"category" : selectedCategory
									},
									success : function(data) {
										//console.log(data);
										$('body').html(data);
									},
									error : function(data) {
										alert('error');
									}
								});
							}
						});
						
						// 엔터 키 입력 시 검색 수행
					    $(".searchButton").on('click', function() {
					        // 입력 필드에서 키워드 가져오기
					        let keyword = $("#searchKeyword").val().trim();
					        console.log(keyword);

					        // 키워드가 비어 있지 않은지 확인
					        if (keyword !== "") {
					            // 키워드를 쿼리 매개변수로 사용하여 검색 URL로 리다이렉트
					            window.location.href = "/post/post-list-view?keyword=" + encodeURIComponent(keyword);
					        } else {
					            // 키워드가 비어 있으면 알림 표시
					            showAlert("검색어를 입력하세요.", "alert-danger");
					        }
					    });
					});//document
</script>