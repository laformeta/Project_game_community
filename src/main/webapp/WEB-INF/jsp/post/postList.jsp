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
						<td><fmt:formatDate value="${post.createdAt}"
								pattern="yyyy년 M월 d일 HH:mm:ss" /></td>
						<td><fmt:formatDate value="${post.updatedAt}"
								pattern="yyyy년 M월 d일 HH:mm:ss" /></td>
						<td class="recommandCount">${post.recommandCount}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<div class="d-flex justify-content-end">
			 <!-- 검색어 입력 폼 -->
            <form id="searchForm">
                <input type="text" id="searchKeyword" name="keyword" placeholder="검색어를 입력하세요">
                <button type="button" id="searchButton">검색</button>
            </form>

			<!-- 글쓰기 버튼 -->
			<a href="/post/post-create-view" class="btn btn-info">글쓰기</a>
		</div>
	</div>
</div>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script>
	$(document).ready(function() {
		 // 엔터 키 입력 시 검색 수행
        $("#searchKeyword").keypress(function (e) {
            if (e.which === 13) {
                e.preventDefault();
                performSearch();
            }
        });
		 
        // 팝업을 띄우는 함수
        function showAlert(message, alertClass) {
            var alertHtml = '<div class="alert ' + alertClass + '" role="alert">' + message + '</div>';
            $("#messageContainer").html(alertHtml);

            // 일정 시간이 지난 후에 팝업을 숨깁니다.
            setTimeout(function () {
                $("#messageContainer").empty();
            }, 2000); // 2초 후에 숨김
        }
		 
        // 검색 폼 제출 시 검색 수행
        $("#searchForm").submit(function (e) {
            e.preventDefault();
            performSearch();
        });
        
        function performSearch() {
        	var keyword = encodeURIComponent($("#searchKeyword").val().trim());

            if (keyword !== "") {
                // 검색어가 있으면 검색 기능 활용
                $.ajax({
                    url: '/post/post-list-view',
                    type: "get",
                    cache: false,
                    headers: { "cache-control": "no-cache", "pragma": "no-cache" },
                    data: { "keyword": keyword },
                    success: function (data) {
                        //console.log(data);
                        $('body').html(data);
                    },
                    error: function (data) {
                        alert('error');
                    }
                });
            }
        }
		
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
	});
</script>