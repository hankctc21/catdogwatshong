
const check = (value, pattern, message, element)=>{
	if(value=="") {
		element.text("필수 입력입니다").attr("class", "fail");
		return false;
	}	
	if(pattern.test(value)==false) {
		element.text(message).attr("class", "fail");
		return false;
	}
	return true;
}

// Convert Korean keyboard input (2-beolsik) into QWERTY letters.
const hangulToQwerty = (text) => {
	const compatMap = {
		"ㄱ":"r","ㄲ":"R","ㄴ":"s","ㄷ":"e","ㄸ":"E","ㄹ":"f","ㅁ":"a","ㅂ":"q","ㅃ":"Q","ㅅ":"t","ㅆ":"T","ㅇ":"d","ㅈ":"w","ㅉ":"W","ㅊ":"c","ㅋ":"z","ㅌ":"x","ㅍ":"v","ㅎ":"g",
		"ㅏ":"k","ㅐ":"o","ㅑ":"i","ㅒ":"O","ㅓ":"j","ㅔ":"p","ㅕ":"u","ㅖ":"P","ㅗ":"h","ㅘ":"hk","ㅙ":"ho","ㅚ":"hl","ㅛ":"y","ㅜ":"n","ㅝ":"nj","ㅞ":"np","ㅟ":"nl","ㅠ":"b","ㅡ":"m","ㅢ":"ml","ㅣ":"l"
	};

	const choseong = ["r","R","s","e","E","f","a","q","Q","t","T","d","w","W","c","z","x","v","g"];
	const jungseong = ["k","o","i","O","j","p","u","P","h","hk","ho","hl","y","n","nj","np","nl","b","m","ml","l"];
	const jongseong = ["","r","R","rt","s","sw","sg","e","f","fr","fa","fq","ft","fx","fv","fg","a","q","qt","t","T","d","w","c","z","x","v","g"];

	let converted = "";
	for (const ch of text) {
		const code = ch.charCodeAt(0);
		// Hangul syllables
		if (code >= 0xac00 && code <= 0xd7a3) {
			const sIndex = code - 0xac00;
			const l = Math.floor(sIndex / 588);
			const v = Math.floor((sIndex % 588) / 28);
			const t = sIndex % 28;
			converted += choseong[l] + jungseong[v] + jongseong[t];
			continue;
		}
		// Compatibility jamo
		if (compatMap[ch]) {
			converted += compatMap[ch];
			continue;
		}
		converted += ch;
	}
	return converted;
};

const bindQwertyConversion = (selector) => {
	let composing = false;
	$(selector).on("compositionstart", function() {
		composing = true;
	});
	$(selector).on("compositionend", function() {
		composing = false;
	});
	// Avoid converting while IME composition is active; convert on blur for stability.
	$(selector).on("blur", function() {
		if (composing)
			return;
		const converted = hangulToQwerty($(this).val());
		if ($(this).val() !== converted) {
			$(this).val(converted);
		}
	});
};


const usernameCheck = ()=>{
	const $username = $("#username").val().toUpperCase();
	$("#username").val($username);
	const pattern = /^[0-9A-Z]{8,10}$/;
	return check($username, pattern, "아이디는 영문대문자와 숫자 8~10자입니다", $("#username_msg"));
}

const irumCheck = ()=>{
	$("#irum_msg").text("");
	const $value = $("#irum").val();
	const pattern = /^[가-힣]{2,10}$/; 
	return check($value, pattern, "이름은 한글 2~10자입니다", $("#irum_msg"));
}

const passwordCheck = () => {
	$("#password_msg").text("");
	const $password = $("#password").val();

	const pattern = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,10}$/;
	return check($password, pattern, "비밀번호는 영문/숫자/특수문자(!@#$%^&*)를 포함한 8~10자입니다", $("#password_msg"));	
}
const password2Check = () => {
	$("#password2_msg").text("");
	const $password2 = $("#password2").val();
	if($password2=="") {
		$("#password2_msg").text("필수입력입니다").attr("class","fail");
		return false;
	} 
	if($password2!==$("#password").val()) {
		$("#password2_msg").text("비밀번호가 일치하지 않습니다").attr("class","fail");
		return false;
	}
	return true;
}
const emailCheck = ()=>{
	const $email = $("#email").val();
	const pattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	return check($email, pattern, "정확한 이메일을 입력하세요", $("#email_msg"))
}

// 실제로 회원 가입을 수행하는 함수 : 사용자가 입력한 데이터를 가지고 FormData 객체를 만들어 ajax 요청을 보낸다
const join = () =>{
	const formData = new FormData($("#join_form")[0]);
	
	$.ajax({
		url: "/members/new",
		method: "post",
		data: formData,
		processData: false,
		contentType: false
	}).done(()=>Swal.fire("가입신청 완료","이메일을 확인하세요", "success"))
	.fail((msg)=>Swal.fire('가입신청 실패', msg,'error'));
}

$(document).ready(()=>{
	// Keep Korean allowed only in name; force QWERTY-compatible chars for id/password fields.
	bindQwertyConversion("#username");
	bindQwertyConversion("#password");
	bindQwertyConversion("#password2");

	$("#username").on("blur", ()=>{
		if(usernameCheck()==false)
			return false;
		$.ajax("/members/username/check?username=" + $("#username").val())
			.done(()=>$("#username_msg").text("좋은 아이디네요").attr("class", "success"))
			.fail(()=>$("#username_msg").text("사용중인 아이디입니다").attr("class", "fail"));
	});
	
	$("#email").on("blur", ()=>{
		if(emailCheck()==false)
			return false;
		$.ajax("/members/email/check?email=" + $("#email").val())
			.done(()=>$("#email_msg").text("사용할 수 있는 이메일입니다").attr("class", "success"))
			.fail(()=>$("#email_msg").text("사용중인 이메일입니다").attr("class", "fail"));
	});
	
	$("#email").on("blur", emailCheck);
	$("#irum").on("blur", irumCheck);
	$("#password").on("blur", passwordCheck);
	$("#password2").on("blur", password2Check);
	
	$("#join").on("click", ()=>{
		console.log("aaa");
		const r1 = usernameCheck();
		const r2 = passwordCheck();
		const r3 = password2Check();
		const r4 = irumCheck();
		const r5 = emailCheck();
		if((r1 && r2 && r3 && r4 && r5) == false)
			return false;

		$.when($.ajax("/members/username/check?username="+$("#username").val()), 
			$.ajax("/members/email/check?email="+$("#email").val()))
			.done(()=>join())
			.fail(()=>Swal.fire("실패", "아이디나 이메일이 사용중입니다", "error"));		
	});
});
