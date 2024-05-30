
$(document).ready(function () {

    $("#replyWrite").submit(function (event) {
        // 폼의 기본 동작을 막음 (페이지 새로고침 방지)
        event.preventDefault();
        // 리뷰 내용 가져오기
        const reply =  $("textarea[name='reply']").val();

        const boardCommunityId = $("#a").val();
        // CSRF 토큰 가져오기
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        // AJAX 요청 보내기
        $.ajax({
            url: "/user/reply/insert/" + boardCommunityId, // boardCommunityId를 URL에 포함시킴
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({"reply": reply}), // boardCommunityId는 URL에, reply만 JSON으로 전송
            beforeSend: function (xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function (result, status) {
                alert("입력이 완료되었습니다.");
                location.reload(); // 페이지 새로고침
            },
            error: function (jqXHR, status, error) {
                alert(jqXHR.responseText);
                console.error("입력 중 오류가 발생했습니다:", error);
            }
        });
    });

    $("#replyUpdate").click(function() {
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const reply = $("#reply-content").val();
        const boardCommunityId = $("#a").val();
        const replyId = $("input[name='replyId']").val();
        const requestData = {
            "reply": reply,
            "boardCommunityId": boardCommunityId,
            "replyId": replyId
        }; // 요청 데이터 구성
        $.ajax({
            url: "/user/reply/update/"+boardCommunityId,  // 현재 위치를 돌아오기위해서 사용
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(requestData), // JSON 형식으로 데이터 전송
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            cache: false,
            success: function(result, status){
                alert("수정이 완료되었습니다..");
                $('#exampleModal').modal('hide');
                location.reload(); // 페이지 새로고침
            },
            error: function(jqXHR, status, error) {
                alert(jqXHR.responseText);
                console.error("인증번호 발송 중 오류가 발생했습니다:", error);
            }
        });
    });





    //  댓글 삭제
    $(".replyDelete").click(function() {
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const replyId = $(this).attr("data-reply-id");

        const requestData = {
            "replyId": replyId
        }; // 요청 데이터 구성
        $.ajax({
            url: "/user/reply/delete/"+replyId,  // 삭제
            type: "DELETE",
            contentType: "application/json",
            data: JSON.stringify(requestData), // JSON 형식으로 데이터 전송
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            },
            cache: false,
            success: function(result, status){
                alert("삭제가 완료되었습니다..");
                $('#exampleModal').modal('hide');
                location.reload(); // 페이지 새로고침
            },
            error: function(jqXHR, status, error) {
                alert(jqXHR.responseText);
                console.error("인증번호 발송 중 오류가 발생했습니다:", error);
            }
        });
    });

    $('.reply-update-btn').click(function () {
        const replyId = $(this).data('reply-id');
        const replyCreatedBy = $(this).data('reply-created-by');
        const replyContent = $(this).data('reply-content');
        $('#exampleModalLabel').text(replyCreatedBy);
        $('#reply-content').val(replyContent);
        $('#reply-id').val(replyId);
        $('#exampleModal').modal('show');
    });

    $("#boardDelete").click(function() {
        if (confirm('정말로 삭제하시겠습니까?')) {
            var token = $("meta[name='_csrf']").attr("content");
            var header = $("meta[name='_csrf_header']").attr("content");
            const id = $("#boardId").val();

            $.ajax({
                url: '/community/user/board/delete/' + id,
                type: 'DELETE',
                beforeSend: function(xhr) {
                    xhr.setRequestHeader(header, token);
                },
                success: function(result) {
                    alert('게시물이 삭제되었습니다.');
                    window.location.href = '/community/board/list';
                },
                error: function(xhr, status, error) {
                    alert('게시물 삭제에 실패했습니다.');
                    console.error('Error:', error);
                }
            });
        }
    });

    $('#replyClose').click(function (){
        $('#exampleModal').modal('hide');
    });

    function selectROLE(){

    }
});


