<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="d-flex justify-content-center">
	<div class="sign-up-box">
		<h1 class="mb-4">회원가입</h1>
		<form id="signUpForm" method="post" action="/user/api/sign-up">
		 <!-- reCAPTCHA -->
            <script src="https://www.google.com/recaptcha/api.js" async defer></script>
            <div class="g-recaptcha" data-sitekey="6LfGKnkpAAAAAGRroi3dVQdX4KiMtj0z8YXsa5_e"></div>
            <div class="text-danger">${recaptchaError}</div>
		
			<table class="sign-up-table table table-bordered">
				<tr>
					<th>* 아이디<br></th>
					<td>
						<%-- 인풋박스 옆에 중복확인을 붙이기 위해 div를 하나 더 만들고 d-flex --%>
						<div class="d-flex">
							<input type="text" id="loginId" name="loginId" class="form-control" placeholder="아이디를 입력하세요.">
						</div>
						
						<%-- 아이디 체크 결과 --%>
						<%-- d-none 클래스: display none (보이지 않게) --%>
						<div id="idCheckLength" class="small text-danger d-none">ID를 6자 이상 입력해주세요.</div>
						<div id="idCheckDuplicated" class="small text-danger d-none">이미 사용중인 ID입니다.</div>
						<div id="idCheckOk" class="small text-success d-none">사용 가능한 ID 입니다.</div>
					</td>
				</tr>
				<tr>
					<th>* 비밀번호</th>
					<td><input type="password" id="password" name="password" class="form-control" placeholder="비밀번호를 입력하세요."></td>
				</tr>
				<tr>
					<th>* 비밀번호 확인</th>
					<td><input type="password" id="confirmPassword" class="form-control" placeholder="비밀번호를 입력하세요."></td>
				</tr>
				<tr>
					<th>* 이메일</th>
					<td>
					<div class="d-flex align-items-center">
						<input type="text" id="email" name="email" class="form-control" placeholder="이메일 주소를 입력하세요.">
					</div>
<!-- 					<br> -->
<!-- 					<button type="button" id="sendEmailCertificationBtn" class="btn btn-success">인증번호 전송</button> -->
					</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<th>* 이메일 인증번호</th> -->
<!-- 					<td><input type="text" id="certificationNumber" name="certificationNumber" class="form-control"></td> -->
<!-- 				</tr> -->
				<tr>
					<th>* 이름</th>
					<td><input type="text" id="name" name="name" class="form-control" placeholder="이름을 입력하세요."></td>
				</tr>
				<tr>
					<th>* 닉네임</th>
					<td><input type="text" id="nickname" name="nickname" class="form-control" placeholder="닉네임을 입력하세요."></td>
				</tr>
			</table>
			<br>
		
			<button type="submit" id="signUpBtn" class="btn btn-primary float-right">회원가입</button>
		</form>
	</div>
</div>
<script>
	$(document).ready(function() {
		$("#loginId").on('keyup', function() {
			// alert("클릭");
			// 경고 문구 초기화
			$('#idCheckLength').addClass('d-none');
			$('#idCheckDuplicated').addClass('d-none');
			$('#idCheckOk').addClass('d-none');
			
			let loginId = $('#loginId').val().trim();
			if (loginId.length < 6) {
				$('#idCheckLength').removeClass('d-none');
				return;
			}
			
			$.ajax({
				url:"/user/is-duplicated-id"
				, data: {"loginId":loginId}
			
				, success: function(data) {
					if (data.is_duplicated_id/*true*/) {
						$('#idCheckDuplicated').removeClass('d-none');
					} else {
						// 중복 아님 => 사용 가능
						$('#idCheckOk').removeClass('d-none');
					}
				}
				, error: function(request, status, error) {
					alert(data.error_message);
				}
			}); // loginIdCheckBtn AJAX
			
		}); // loginIdCheckBtn click
		
// 		$("#sendEmailCertificationBtn").on('click', function() {
// 			//alert("클릭");
// 		}); // sendEmailCertificationBtn click
		
		$('#signUpForm').on('submit', function(e) {
			//alert("클릭");
			e.preventDefault();
			
			  // reCAPTCHA 확인
            let recaptchaResponse = grecaptcha.getResponse();
            if (!recaptchaResponse || recaptchaResponse === "") {
                alert("reCAPTCHA를 완료해주세요.");
                return false;
            }
			
			// validation check
			let loginId = $("#loginId").val().trim();
			let password = $("#password").val();
			let confirmPassword = $("#confirmPassword").val();
			let name = $("#name").val().trim();
			let email = $("#email").val().trim();
			
			if (!loginId) {
				alert("아이디를 입력하세요.");
				return false;
			}
			
			if (!password || !confirmPassword) {
				alert("비밀번호를 입력하세요.");
				return false;
			}
			
			if (password != confirmPassword) {
				alert("비밀번호가 일치하지 않습니다.");
				return false;
			}
			
			if (!name) {
				alert("이름을 입력하세요.");
				return false;
			}
			
			if (!email) {
				alert("이메일을 입력하세요.");
				return false;
			}
			
			// 중복확인 후 사용 가능한 아이디인지 확인
			// => idCheckOk 클래스 d-none이 없을 때
			if ($("#idCheckOk").hasClass('d-none')) {
				alert("아이디 중복확인을 다시 해주세요.");
				return false;
			}
			
			//alert("회원가입");
			
			// 1) 서버 전송: submit을 js에서 동작시킴
			//$(this)[0].submit(); // 화면 이동이 된다.
			
			// 2) AJAX: 화면 이동 되지 않음(콜백함수에서 이동) 응답값 JSON
			let url = $(this).attr("action");
			//alert(url);
			let params = $(this).serialize(); // 폼태그에 있는 name 속성과 값으로 파라미터를 구성
			console.log(params);
			
			$.post(url, params)  // request
			.done(function(data) { // response
				// {"code":200, "result":"성공"}
				if (data.code == 200) {
					alert("가입을 환영합니다. 로그인 해주세요.");
					location.href = "/user/sign-in-view"; // 로그인 화면으로 이동
				} else {
					// 로직 실패
					alert(data.error_message);
				}
			});
		}); // signUpBtn click
	}); // document
</script>