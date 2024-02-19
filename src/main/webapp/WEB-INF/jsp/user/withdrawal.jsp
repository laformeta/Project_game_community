<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="d-flex justify-content-start">
	<div class="sign-up-box ml-5">
		<h1 class="mb-4">회원관리 및 탈퇴</h1>
		<form id="signUpForm" method="post" action="/user/sign-up">
			<table class="sign-up-table table table-bordered">
				<tr>
					<th>* 아이디<br></th>
					<td>
						<%-- 인풋박스 옆에 중복확인을 붙이기 위해 div를 하나 더 만들고 d-flex --%>
						<div class="d-flex">
							<input type="text" id="loginId" name="loginId" class="form-control col-9" placeholder="아이디를 입력하세요.">
							
						</div>
						
						<%-- 아이디 체크 결과 --%>
						<%-- d-none 클래스: display none (보이지 않게) --%>
<!-- 						<div id="idCheckLength" class="small text-danger d-none">ID를 6자 이상 입력해주세요.</div> -->
<!-- 						<div id="idCheckDuplicated" class="small text-danger d-none">이미 사용중인 ID입니다.</div> -->
<!-- 						<div id="idCheckOk" class="small text-success d-none">사용 가능한 ID 입니다.</div> -->
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
				
			</table>
			<br>
		
			<button type="submit" id="withdrawalBtn" class="btn btn-danger float-right">회원탈퇴</button>
		</form>
	</div>
</div>