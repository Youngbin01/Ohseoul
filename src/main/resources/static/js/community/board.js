$(document).ready(function () {

    $('#viewPostBtn').on("click", function (e) {
        e.preventDefault();
        var postId = $(this).data('post-id');
        increaseViewCount(postId);
    });
});

// 조회수 증가를 위한 함수
function increaseViewCount(postId) {
    // 서버로 POST 요청 보내기
    $.ajax({
        type: 'POST',
        url: '/community/board/increase-view-count/' + postId,
        success: function (response) {
            // 성공 시 작업
            console.log("조회수가 증가되었습니다.");
        },
        error: function (error) {
            // 에러 처리
            console.error("조회수를 증가하는 중 오류가 발생했습니다.");
        }
    });
}
