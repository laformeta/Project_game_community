<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글 상세</h1>
		<div class="" id="subject">
			<span>${lolPost.subject}</span>
		</div>
		<div class="d-flex justify-content-between mb-5" id="userAndPostInfo">
			<div>${lolPost.nickname}</div>
			<div class="d-flex">
				<div class="mx-2">추천:${lolPost.recommandCount}</div>
				<div class="mx-2">비추천:${lolPost.noRecommandCount}</div>
				<div class="mx-2">${lolPost.createdAt}</div>
			</div>
		</div>
		<div id="usersContents">
			<p>${lolPost.content}</p>
			<c:if test="${not empty lolPost.imagePath}">
				<div class="my-3">
					<img alt="업로드 된 이미지" src="${lolPost.imagePath}" width="300">
				</div>
			</c:if>
		</div>

		<div class="d-flex justify-content-end my-2">

			
			<div>
				<a href="/post/post-list-view" class="btn btn-dark">목록</a>
			</div>


			<!-- 수정 버튼 -->
			<c:if test="${lolPost.userId == loggedInUserId}">
				<a href="/post/post-edit-view/${lolPost.id}"
					class="btn btn-warning mx-2">수정</a>
			</c:if>

			<!-- 모달 코드 추가 -->
			<div class="modal fade" id="confirmModal" tabindex="-1" role="dialog"
				aria-labelledby="confirmModalLabel" aria-hidden="true">
				<div class="modal-dialog" role="document">
					<div class="modal-content">
						<div class="modal-header">
							<h5 class="modal-title" id="confirmModalLabel">삭제 확인</h5>
							<button type="button" class="close" data-dismiss="modal"
								aria-label="Close">
								<span aria-hidden="true">&times;</span>
							</button>
						</div>
						<div class="modal-body">정말 삭제하시겠습니까?</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-secondary" id="confirmNoBtn">아니오</button>
							<button type="button" class="btn btn-danger" id="confirmYesBtn">예</button>
						</div>
					</div>
				</div>
			</div>

			<!-- 삭제 버튼 -->
			<c:if test="${lolPost.userId == loggedInUserId}">
				<button type="button" id="deleteBtn" class="btn btn-secondary"
					data-post-id="${lolPost.id}">삭제</button>

			</c:if>
		</div>
		<div class="d-flex justify-content-center">
			<!-- 추천 버튼 -->
			<div class="mx-3">
				<button type="button" id="recommandButton"
					data-post-id="${lolPost.id}">추천</button>
				<span id="recommandCount">${lolPost.recommandCount}</span>
			</div>

			<!-- 비추천 버튼 -->
			<div>
				<button type="button" id="noRecommandButton"
					data-post-id="${lolPost.id}">비추천</button>
				<span id="noRecommandCount">${lolPost.noRecommandCount}</span>
			</div>
		</div>



		<%-- 댓글 제목 --%>
		<div class="card-comment-desc border-bottom">
			<div class="ml-3 mb-1 font-weight-bold">댓글</div>
		</div>

		<%-- 댓글 목록 --%>
		<div class="card-comment-list m-2">

			<%-- 댓글 내용들 --%>
			<c:forEach items="${lolPost.commentList}" var="commentView">
				<div class="card-comment m-1">
					<span class="font-weight-bold">${commentView.user.nickname}</span>
					<span>${commentView.comment.content}</span>

					<%-- 댓글 삭제 버튼(자신의 댓글만 삭제 버튼 노출) --%>
					<c:if test="${userId eq commentView.comment.userId}">
						<a href="#" class="comment-del-btn"
							data-comment-id="${commentView.comment.id}"> <img
							src="https://www.iconninja.com/files/603/22/506/x-icon.png"
							width="10" height="10">
						</a>
					</c:if>
				</div>
			</c:forEach>
			<%-- 댓글 쓰기 --%>
			<div class="comment-write d-flex border-top mt-2">
				<input type="text" class="form-control border-0 mr-2 comment-input"
					placeholder="댓글 달기" />
				<button type="button" class="comment-btn btn btn-light"
					data-user-id="${userId}" data-post-id="${lolPost.id}">게시</button>
			</div>
		</div>
		<%--// 댓글 목록 끝 --%>

	</div>
</div>

<script>
	$(document).ready(function() {
		// 추천 버튼 클릭 시
		$("#recommandButton").click(function() {
			var postId = $(this).data("post-id");

			$.ajax({
				url : "/recommand/" + postId,
				success : function(data) {
					// 성공 시 추천 수 업데이트
					if (data.code == 200) {
						$("#recommandCount").text(data.recommandCount);
						location.reload(true); // 새로고침 => post Detail화면
					} else if (data.code == 300) {
						// 비로그인 시 로그인 페이지로 이동
						alert(data.error_message);
						location.href = "/user/sign-in-view";
					}
				},
				error : function(request, status, error) {
					alert("이미 추천을 눌렀습니다.");
				}
			});
		});

		// 비추천 버튼 클릭 시
		$("#noRecommandButton").click(function() {
			var postId = $(this).data("post-id");

			$.ajax({
				url : "/no-recommand/" + postId, // 해당 URL에 실제 컨트롤러 매핑을 해주어야 합니다.
				success : function(data) {
					// 성공 시 비추천 수 업데이트
					if (data.code == 200) {
						$("#noRecommandCount").text(data.noRecommandCount);
						location.reload(true); // 새로고침 => post Detail화면
					} else if (data.code == 300) {
						// 비로그인 시 로그인 페이지로 이동
						alert(data.error_message);
						location.href = "/user/sign-in-view";
					}
				},
				error : function(request, status, error) {
					alert("이미 비추천을 눌렀습니다.");
				}
			});
		});

		$("#deleteBtn").on('click', function() {
			$("#confirmModal").modal('show');
		});

		$("#confirmYesBtn").on('click', function() {
			$("#confirmModal").modal('hide');
			let postId = $("#deleteBtn").data("post-id");

			$.ajax({
				type : "DELETE",
				url : "/post/delete",
				data : {
					"postId" : postId
				},
				success : function(data) {
					if (data.code == 200) {
						location.href = "/post/post-list-view";
					} else {
						alert(data.error_message);
					}
				},
				error : function(request, status, error) {
					alert("ajax 오류. 글 삭제 실패");
				}
			});
		});

		$("#confirmNoBtn").on('click', function() {
			$("#confirmModal").modal('hide');
		});

		// 	 쓰기
		$(".comment-btn").on('click', function() {
			//alert("댓글 쓰기");
			let userId = $(this).data("user-id");
			//alert(userId);
			if (!userId) {
				// 비로그인이면 로그인 화면 이동
				alert("로그인을 해주세요.");
				location.href = "/user/sign-in-view";
				return;
			}

			let lolPostId = $(this).data("post-id");
			//alert(postId);

			// 댓글 내용 가져오기
			// 1) 이전 태그 값 가져오기
			//let content = $(this).prev().val().trim();

			// 2) 형제 태그 중 input 값 가져오기
			let content = $(this).siblings("input").val().trim();
			//alert(content);

			$.ajax({
				type : "post",
				url : "/comment/create",
				data : {
					"lolPostId" : lolPostId,
					"content" : content
				}

				,
				success : function(data) {
					if (data.code == 200) {
						location.reload(true);
					} else if (data.code == 500) {
						alert(data.errorMessage);
						location.href = "/user/sign-in-view";
					}
				},
				error : function(request, status, error) {
					alert("댓글 쓰기 실패했습니다.");
				}
			});
		});

	});//document
</script>