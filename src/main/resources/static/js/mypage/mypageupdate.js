$(document).ready(function() {

    let nicknamecheck = false;
    // 닉네임 유효성 체크
    $("button[name='nickName']").click(function() {
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const nickName = $("input[name='nickName']").val();
        const requestData = { "nickName": nickName }; // 요청 데이터 구성
        if(!nickName.trim()){
            alert("닉네임을 작성해주세요");
            return false;
        }
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
                nicknamecheck = true;
                alert("사용가능한 닉네입니다.");
            },
            error: function(jqXHR, status, error) {
                alert(jqXHR.responseText);
                console.error("인증번호 발송 중 오류가 발생했습니다:", error);
            }
        });
    });

    // 닉네임 수정
    $("#nickNameChange").submit(function(event) {
        // 폼의 기본 동작을 막음 (페이지 새로고침 방지)
        event.preventDefault();
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const nickName = $("input[name='nickName']").val();
        const requestData = { "nickName": nickName }; // 요청 데이터 구성
        if(nicknamecheck) {
            $.ajax({
                url: "/mypage/modify",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(requestData), // JSON 형식으로 데이터 전송
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                cache: false,
                success: function (result, status) {
                    alert("수정이 완료되었습니다.");
                    location.reload(); // 수정 완료 후 페이지 새로고침
                },
                error: function (jqXHR, status, error) {
                    alert(jqXHR.responseText);
                    console.error("수정 중 오류가 발생했습니다:", error);
                }
            });
        }
            else{alert("닉네임 확인을 해주세요")}
            


    });

});
