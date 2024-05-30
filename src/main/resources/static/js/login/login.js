$(document).ready(function () {


    error();

    // 이메일 인증 발송 여부
    let verificationCodeSent = false;
    // 인증번호 일치여부
    let verificationCodeConfirmed = false;
    // 이메일 중복체크
    let emailcheck = false;
    //  닉네임 중복체큰
    let nicknamecheck = false;


    // 회원가입 폼이 제출될 때의 동작
    $("#joinform").submit(function (event) {
        // 폼의 기본 동작을 막음 (페이지 새로고침 방지)
        event.preventDefault();
        // 입력된 값을 가져옴
        var nickName = $("input[name='nickName']").val();
        var email = $("input[name='email']").val();
        var password = $("input[name='password']").val();
        // CSRF 토큰 가져오기
        var csrfToken = $("meta[name='_csrf']").attr("content");
        // CSRF 헤더 이름 가져오기
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        // 회원가입 요청을 위한 데이터 객체 생성
        var userData = {
            nickName: nickName,
            email: email,
            password: password
        };

        var password = $("input[name='password']").val();
        var confirmPassword = $("input[name='password2']").val(); // 비밀번호 확인 입력란의 값

        // 비밀번호와 비밀번호 확인 값이 일치하는지 확인
        if (password !== confirmPassword) {
            alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return; // 일치하지 않으면 폼 제출을 중지
        }

        // 서버로 회원가입 요청을 보냄
        if (verificationCodeSent && verificationCodeConfirmed && emailcheck && nicknamecheck) {
            // 서버로 회원가입 요청을 보냄
            $.ajax({
                type: "POST",
                url: "/member/join", // 회원가입 API
                contentType: "application/json",
                data: JSON.stringify(userData),
                // CSRF 헤더와 토큰 설정
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function (response) {
                    // 회원가입 성공 시 처리
                    alert("회원가입이 완료되었습니다.");
                    // 원하는 동작 (예: 로그인 페이지로 리디렉션)
                    window.location.href = "/member/login";
                },
                error: function (xhr, status, error) {
                    // 회원가입 실패 시 처리
                    var errorMessage = xhr.responseText;
                    alert("회원가입에 실패했습니다: " + errorMessage);
                }
            });
        } else {
            // 인증이 완료되지 않았음을 사용자에게 알림
            alert("인증이 완료되지 않았습니다.");
        }
    });



    // 이메일 유효성 체크
    $("button[name='emailcheck']").click(function () {
        verificationCodeSent = false;
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const email = $("input[name='email']").val();
        const requestData = { "email": email }; // 요청 데이터 구성


        $.ajax({
            url: "/member/emailCheck",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestData), // JSON 형식으로 데이터 전송
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            cache: false,
            success: function (result, status) {
                emailcheck = true;
                alert("사용가능한 이메일입니다.");
                $(".email-key").css("display", "flex");
            },
            error: function (jqXHR, status, error) {
                alert(jqXHR.responseText);
                console.error("인증번호 발송 중 오류가 발생했습니다:", error);
            }
        });
    });

    // 이메일(input name="email")이 변경될 때마다 실행
    $("input[name='email']").change(function () {
        // 이메일이 변경될 때마다 인증번호 발송 여부를 false로 설정
        verificationCodeSent = false;
        // 이메일이 변경될 때마다 인증번호 입력란을 숨김
        resetAndHideTimer(); // 타이머 초기화 및 숨기기
        // 이메일이 변경될 때마다 인증번호 입력란의 값을 초기화
        $("input[name='verificationCode']").val("");

        // $("#confirmVerificationCode").css("display", "none");
        $(".email-key").css("display", "none");
    });

    // 이메일 유효성 검사 함수
    function validateEmail(email) {
        // 이메일 유효성 검사 로직을 여기에 추가하세요.
        // 간단한 유효성 검사 예시:
        const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return regex.test(email);
    }



    // 닉네임 유효성 체크
    $("button[name='nickName']").click(function () {
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const nickName = $("input[name='nickName']").val();
        const requestData = { "nickName": nickName }; // 요청 데이터 구성
        if(!nickName.trim()){
            alert("닉네임을 작성해주세요")
            return false;
        }
        $.ajax({
            url: "/member/nickNameCheck",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestData), // JSON 형식으로 데이터 전송
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            cache: false,
            success: function (result, status) {
                nicknamecheck = true;
                alert("사용가능한 닉네입니다.");
            },
            error: function (jqXHR, status, error) {
                alert(jqXHR.responseText);
                console.error("인증번호 발송 중 오류가 발생했습니다:", error);
            }
        });
    });







    $("button[name='sendVerificationCode']").click(function () {
        resetTimer();
        timer();
        $("input[name='verificationCode'], #timer").css("display", "flex");


    });









    // 인증번호 발송 버튼 클릭 시 이벤트 처리
    $("button[name='sendVerificationCode']").click(function () {
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const email = $("input[name='email']").val();
        const requestData = { "email": email }; // 요청 데이터 구성
        $.ajax({
            url: "/member/key",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestData), // JSON 형식으로 데이터 전송
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            cache: false,
            success: function (result, status) {
                verificationCodeSent = true;
                alert("인증번호가 성공적으로 발송되었습니다.");
            },
            error: function (jqXHR, status, error) {
                alert(jqXHR.responseText);
                console.error("인증번호 발송 중 오류가 발생했습니다:", error);
            }
        });
    });

    $("button[name='sendVerificationCode']").click(function () {
        resetTimer();
        timer();
        $("input[name='verificationCode'], #timer").css("display", "flex");

    });





    // "인증번호 확인" 버튼 클릭 시 이벤트 처리
    $("button[name='confirmVerificationCode']").click(function () {
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const verificationCode = $("input[name='verificationCode']").val(); // 입력된 인증번호 가져오기
        const requestData = {
            "verificationCode": verificationCode
        }; // 요청 데이터 구성
        $.ajax({
            url: "/member/verify", // 인증번호 확인을 처리하는 RESTful 엔드포인트 URL
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestData), // JSON 형식으로 데이터 전송
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            cache: false,
            success: function (result, status) {
                alert("인증이 성공적으로 완료되었습니다.");
                verificationCodeConfirmed = true;
                handleVerificationSuccess();
            },
            error: function (jqXHR, status, error) {
                alert("인증번호가 일치하지 않습니다.");
                console.error("인증 중 오류가 발생했습니다:", error, jqXHR.responseText);
            }
        });
    });



    //  비밀번호 찾기
    $("#findpassword").submit(function (event) {
        // 폼의 기본 동작을 막음 (페이지 새로고침 방지)
        event.preventDefault();
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const email = $("input[name='email']").val();
        const password = $("input[name='password']").val();
        const password1 = $("input[name='password1']").val();
        const requestData = {
            "email": email,
            "password": password,
            "password1": password1
        }; // 요청 데이터 구성
        $.ajax({
            url: "/member/passwordmodify",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestData), // JSON 형식으로 데이터 전송
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            cache: false,
            success: function (result, status) {
                alert("변경이 완료되었습니다.");
                location.href = "/member/login"; // 변경 완료 하면 로그인 페이지로
            },
            error: function (jqXHR, status, error) {
                alert(jqXHR.responseText);
                console.error("수정 중 오류가 발생했습니다:", error);
            }
        });


    });





    // 이메일 유효성 체크
    $("button[name='emailconfirm']").click(function () {
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const email = $("input[name='email']").val();
        const requestData = { "email": email }; // 요청 데이터 구성
        $.ajax({
            url: "/member/emailconfirm",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestData), // JSON 형식으로 데이터 전송
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            cache: false,
            success: function (result, status) {
                emailcheck = true;
                alert("등록된 이메일입니다..");
                $(".email-key").css("display", "flex");
            },
            error: function (jqXHR, status, error) {
                alert("존재하지 않는 이메일 입니다.");
                console.error("인증번호 발송 중 오류가 발생했습니다:", error);
            }
        });
    });












});



function handleVerificationSuccess() {
    // 이메일 입력 필드를 읽기 전용으로 변경
    $("input[name='email']").prop('readonly', true);
    // 인증번호 입력 필드와 확인 버튼이 있는 부모 요소를 숨깁니다.
    $("input[name='verificationCode'], button[name='confirmVerificationCode']").closest('.input-box').hide();
    $(".email-key").hide();
    $("#timer")
}


function resetAndHideTimer() {
    clearInterval(timerInterval); // 타이머 중지
    $("#timer").text(""); // 타이머 텍스트 초기화
    $("#timer").hide(); // 타이머 숨기기
}









let timerInterval;

function resetTimer() {
    clearInterval(timerInterval); // 타이머 중지
    $("#timer").text(""); // 타이머 텍스트 초기화
}


function timer() {
    // 시작 시간 설정
    var startTime = new Date();
    startTime.setMinutes(startTime.getMinutes() + 3); // 현재 시간에서 3분을 더함

    // 타이머 업데이트 함수
    function updateTimer() {
        var currentTime = new Date();
        var timeDiff = startTime - currentTime;

        if (timeDiff <= 0) {
            clearInterval(timerInterval); // 타이머 중지
            $("#timer").text("인증시간이 만료되었습니다.");
        } else {
            var minutes = Math.floor((timeDiff / (1000 * 60)) % 60);
            var seconds = Math.floor((timeDiff / 1000) % 60);
            $("#timer").text(minutes + "분 " + seconds + "초 남음");
        }
    }
    // 초기 호출 후 1초마다 updateTimer() 호출
    updateTimer();
    timerInterval = setInterval(updateTimer, 1000);
}

function error() {
    const errorElement = $("#error").val();
    if (errorElement && errorElement.trim() !== "") {
        alert(errorElement);
    }

}