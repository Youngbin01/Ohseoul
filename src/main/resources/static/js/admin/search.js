$(document).ready(function() {

    $("#search").click(function() {
        var email = $('#nickName').val(); // 검색할 이메일 주소 가져오기
        var page = 0; // 페이지는 0부터 시작
        var size = 10; // 페
        var search = $("input[name='search']").val();
        var requestData =
            "email=" + encodeURIComponent(email) +
            "&page=" + page +
            "&size=" + size +"&search"+search;
        alert(email);
        $.ajax({
            url: "/admin/list/"+requestData,
            type: "GET",
            contentType: "application/json",
            data: JSON.stringify(requestData), // JSON 형식으로 데이터 전송
            cache: false,
            success: function(){
                alert("검색 성공");
            },
            error: function(jqXHR, status, error) {
                alert(jqXHR.responseText);
                console.error("오류 확인", error);
            }
        });
    });



});
