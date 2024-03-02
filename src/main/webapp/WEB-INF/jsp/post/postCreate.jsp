<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="d-flex justify-content-center">
	<div class="w-50">
		<h1></h1>
		
		<select name="category" id="category">
			<option value="일반">일반</option>
			<option value="질문">질문</option>
			<option value="자랑">자랑</option>
		</select>
		<input type="text" id="subject" class="form-control" placeholder="제목을 입력하세요">
		<textarea id="content" class="form-control" placeholder="내용을 입력하세요" rows="10"></textarea>
		
		
		<div class="d-flex justify-content-end my-	3">
			<input type="file" id="file" accept=".jpg, .png, .gif, .jpeg" multiple>
		</div>
		
		<img id="imagePreview" src="#" alt="Image Preview" width="200px" height="200px">
		<div class="d-flex justify-content-end my-3">
			
			
			
			<div>
				<button type="button" id="postListBtn" class="btn btn-dark">목록</button>
				<button type="button" id="saveBtn" class="btn btn-info">저장</button>
			</div>
		</div>
	</div>
</div>
<script>
	$(document).ready(function() {
		// 파일 선택 시 미리보기
	    $("#file").on('change', function() {
	        previewImage(this);
	    });

	    function previewImage(input) {
	        if (input.files && input.files[0]) {
	            var reader = new FileReader();

	            reader.onload = function(e) {
	                // 미리보기 이미지를 화면에 표시
	                $('#imagePreview').attr('src', e.target.result);
	            }

	            reader.readAsDataURL(input.files[0]);
	        }
	    }
	    // 이미지 미리보기를 더블 클릭하면 초기화
	    function resetImagePreview() {
	        $("#file").val("");
	        $("#imagePreview").attr("src", "#");
	    }
		
		// 목록 버튼 클릭 >> 목록 화면으로 이동
		$("#postListBtn").on('click', function() {
			location.href = "/post/post-list-view";
		});
		
		
		// 글 저장 버튼
		$("#saveBtn").on('click', function() {
// 			alert("확인");
			let subject = $("#subject").val().trim();
			let content = $("#content").val();
			let category = $("#category").val();  // 카테고리 값 가져오기
			let fileName = $("#file").val(); // C:\fakepath\girl-8435329_640.png
// 			alert(fileName);
			
			// validation check
			 if (!category) {
		        alert("카테고리를 선택하세요.");
		        return;
		    }
			
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
				let extension = fileName.split(".").pop().toLowerCase();
// 				alert(extension);
				if ($.inArray(extension, ['jpg', 'jpeg', 'gif', 'png']) == -1) {
					alert("이미지 파일만 업로드 할 수 있습니다.");
					$("#file").val("");
					return;
				}

			}
			
			
			// form 태그를 js에서 만든다
			// 이미지를 업로드 할 때는 반드시 form 태그가 있어야 한다.
			let formData = new FormData();
			formData.append("subject", subject); // key는 name 속성과 같다. Request Parameter명
			formData.append("content", content);
			formData.append("category", category);  // 카테고리 추가
			formData.append("nickname", "${sessionScope.userNickname}"); // 세션에서 사용자 닉네임 가져오기
			formData.append("file", $("#file")[0].files[0]);
			
			// AJAX
			$.ajax({
				// request
				type:"POST"
				, url:"/post/create"
				, data:formData
				, enctype:"multipart/form-data" // 파일 업로드를 위한 필수 설정
				, processData:false // 파일 업로드를 위한 필수 설정
				, contentType:false // 파일 업로드를 위한 필수 설정
				
				// response
				, success:function(data) {
					if (data.code == 200) {
						alert("저장되었습니다.");
						location.href = "/post/post-list-view";
					} else {
						alert(data.error_message);
					}
				}
				, error: function(request, status, error){
						
				}
			});
			
			
		});
	});
</script>
