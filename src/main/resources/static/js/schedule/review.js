
$(document).ready(function () {
    $("#reviewWrite").submit(function (event) {
        // Prevent the default form submission behavior
        event.preventDefault();

        // Get the review content and event ID from the form
        const review = $("textarea[name='review']").val();
        const eventId = $("input[name='eventId']").val();

        // Get the CSRF token and header
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");

        // Send AJAX request
        $.ajax({
            url: "/user/review/insert/" + eventId,
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({"review": review}),
            beforeSend: function(xhr) {
                xhr.setRequestHeader(header, token);
            },
            success: function(result, status, xhr) {
                if (xhr.status === 200) { // If the status is OK
                    alert("입력이 완료되었습니다.");
                    // Check if the response contains a redirect URL
                    if (result && result.startsWith('/')) {
                        // Redirect to the specified URL
                        window.location.href = result;
                    } else {
                        // Reload the current page
                        location.reload();
                    }
                } else if (xhr.status === 302) { // If the status is FOUND (302)
                    // Redirect to the specified URL
                    window.location.href = result;
                }
            },
            error: function(jqXHR, status, error) {
                // Handle errors
                alert("입력 중 오류가 발생했습니다: " + error);
                console.error("입력 중 오류가 발생했습니다:", error);
            }
        });
    });




    $("#reviewUpdate").click(function() {
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const review = $("#review-content").val();
        const eventId = $("input[name='eventId']").val();
        const reviewId = $("input[name='reviewId']").val();
        const requestData = { "review": review,
            "eventId": eventId,
            "reviewId": reviewId
        }; // 요청 데이터 구성

        $.ajax({
            url: "/user/review/update/"+reviewId,  // 현재 위치를 돌아오기위해서 사용
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
    $(".reviewDelete").click(function() {
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        const reviewId = $(this).attr("data-review-id");

        const requestData = {
            "reviewId": reviewId
        }; // 요청 데이터 구성
        alert("reviewId"+JSON.stringify(requestData));
        $.ajax({
            url: "/user/review/delete/"+reviewId,  // 삭제
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




        $('.review-update-btn').click(function () {
            const reviewId = $(this).data('review-id');
            const reviewCreatedBy = $(this).data('review-created-by');
            const reviewContent = $(this).data('review-content');

            $('#exampleModalLabel').text(reviewCreatedBy);
            $('#review-content').val(reviewContent);
            $('#review-id').val(reviewId);
            $('#exampleModal').modal('show');
        });








});
