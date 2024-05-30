$(document).ready(function() {

    // 닉네임 유효성 체크
    $("button[name='secession']").click(function() {
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const nickName = $("input[name='nickName']").val();
        const requestData = { "nickName": nickName }; // 요청 데이터 구성
        $.ajax({
            url: "/member/nickNameCheck",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(requestData), // JSON 형식으로 데이터 전송
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            cache: false,
            success: function(result, status){
                alert("사용가능한 닉네입니다.");
            },
            error: function(jqXHR, status, error) {
                alert(jqXHR.responseText);
                console.error("인증번호 발송 중 오류가 발생했습니다:", error);
            }
        });
    });


});
