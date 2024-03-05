<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1>글 수정</h1>

		<form id="editForm" enctype="multipart/form-data">
			<select name="category" id="category">
				<option value="일반">일반</option>
				<option value="질문">질문</option>
				<option value="자랑">자랑</option>
			</select> <input type="text" id="subject" class="form-control"
				placeholder="제목을 입력하세요" value="${lolPost.subject}">
			<textarea id="content" class="form-control" placeholder="내용을 입력하세요"
				rows="10">${lolPost.content}</textarea>

			<%-- 이미지가 있을 때만 영역 노출 --%>
			<c:if test="${not empty lolPost.imagePath}">
				<div class="my-3">
					<img alt="업로드 된 이미지" src="${lolPost.imagePath}" width="300">
				</div>
			</c:if>

			<div class="d-flex justify-content-end my-3">
				<input type="file" id="file" accept=".jpg, .png, .gif, .jpeg">
			</div>

			<div class="my-3">
				<span> 교체될 이미지</span> <img id="preview" alt="미리보기 이미지" width="300">
			</div>

			<div class="d-flex justify-content-end">
				<button type="button" id="clearBtn" class="btn btn-secondary">파일
					지우기</button>
				<div>
					<a href="/post/post-list-view" class="btn btn-dark">목록</a>
					<button type="button" id="saveBtn" class="btn btn-info"
						data-post-id="${lolPost.id}">수정</button>
				</div>
			</div>
		</form>
	</div>
</div>
<script>
	$(document)
			.ready(
					function() {
						// 글 수정 버튼
						$("#saveBtn")
								.on(
										'click',
										function() {
											let postId = $(this)
													.data("post-id");
											let subject = $("#subject").val()
													.trim();
											let content = $("#content").val();
											let category = $("#category").val();
											let fileName = $("#file").val();
											//alert(postId);

											// validation check
											if (!subject) {
												alert("제목을 입력하세요.");
												return; // submit 이벤트 일때만 return false;
											}

											if (!content) {
												alert("내용을 입력하세요.");
												return;
											}

											// 파일이 업로드 된 경우에만 확장자 체크
											if (fileName) {
												// 				alert("파일이 있다.");
												// C:\fakepath\girl-8435329_640.png
												// 확장자만 뽑은 후 소문자로 변경해서 검사한다.
												let extension = fileName.split(
														".").pop()
														.toLowerCase();
												// 				alert(extension);
												if ($.inArray(extension, [
														'jpg', 'jpeg', 'gif',
														'png' ]) == -1) {
													alert("이미지 파일만 업로드 할 수 있습니다.");
													$("#file").val("");
													return;
												}

											}

											// 이미지를 업로드 할 때는 반드시 form 태그로 구성한다

											let formData = new FormData();
											formData.append("postId", postId);
											formData.append("subject", subject);
											formData.append("content", content);
											formData.append("category",
													category);
											formData.append("file",
													$("#file")[0].files[0]);

											$
													.ajax({
														// request
														type : "PUT",
														url : "/post/update",
														data : formData,
														enctype : "multipart/form-data" // 파일 업로드를 위한 필수 설정
														,
														processData : false // 파일 업로드를 위한 필수 설정
														,
														contentType : false // 파일 업로드를 위한 필수 설정

														// response
														,
														success : function(data) {
															if (data.code == 200) {
																alert("글이 수정되었습니다.");
																window.location.href = "/post/post-detail-view?postId="
																		+ postId;
															} else {
																alert(data.error_message);
															}
														}

														,
														error : function(
																request,
																status, error) {
															alert("ajax 오류. 글 수정 실패");
														}

													}); // ajax

										}); // saveBtn
						// 파일 업로드 후 이미지 미리보기
						$("#file").change(function() {
							let file = this.files[0];
							let reader = new FileReader();

							reader.onload = function(e) {
								$('#preview').attr('src', e.target.result);
							};

							reader.readAsDataURL(file);
						});

						// 모두 지우기 버튼 클릭
						$("#clearBtn").on('click', function() {
							// 			alert("모두 지우기");
							$("#file").val("");
							$("#preview").attr("src", "#");
						});

					}); // document
</script>