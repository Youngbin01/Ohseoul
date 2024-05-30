
function showSubmenu(category) {
  // 모든 서브메뉴를 숨김
  $('.sub').hide();

  // 해당 카테고리의 서브메뉴만 보여줌
  const submenu = $('#' + category.toLowerCase());
  if (submenu.length) {
    submenu.show();
  }
}

function sendData(data) {
  // 여기서 데이터를 자바로 보내는 작업을 수행합니다.
  // 이 예제에서는 간단하게 콘솔에 데이터를 출력합니다.
  console.log('전송할 데이터:', data);
  // 자바로 데이터를 보내는 작업을 추가하세요.
}

$(document).ready(function () {
  // 여기에 실행하고자 하는 JavaScript 코드를 작성합니다.
  console.log('문서가 준비되었습니다.');
});