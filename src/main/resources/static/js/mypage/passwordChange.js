$(document).ready(function() {

    $("#passwordChange").submit(function(event) {
        // 폼의 기본 동작을 막음 (페이지 새로고침 방지)
        event.preventDefault();
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const basicpassword = $("input[name='basicpassword']").val();
        const password = $("input[name='password']").val();
        const password1 = $("input[name='password1']").val();
        const requestData = {
            "basicpassword": basicpassword ,
            "password" : password,
            "password1" : password1
        }; // 요청 데이터 구성
        alert("requestData"+JSON.stringify(requestData))

            $.ajax({
                url: "/mypage/passwordmodify",
                type: "POST",
                contentType: "application/json",
                data: JSON.stringify(requestData), // JSON 형식으로 데이터 전송
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(header, token);
                },
                cache: false,
                success: function (result, status) {
                    alert("변경이 완료되었습니다.");
                    location.reload(); // 수정 완료 후 페이지 새로고침
                },
                error: function (jqXHR, status, error) {
                    alert(jqXHR.responseText);
                    console.error("수정 중 오류가 발생했습니다:", error);
                }
            });


    });





});
