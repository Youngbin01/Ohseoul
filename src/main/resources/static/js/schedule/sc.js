
$(document).ready(function () {
  $('.side').click(function () {
    $('.side').removeClass('active'); // 모든 side 요소의 active 클래스 제거
    $(this).addClass('active');
  });
});

function aa() {
  alert("ddddd");
  console.log("dddd");
}
document.querySelectorAll('li').forEach(li => {
  li.addEventListener('click', () => {
    const radio = li.querySelector('input[type="radio"]');
    if (radio) {
      radio.checked = true;
    }
  });
});