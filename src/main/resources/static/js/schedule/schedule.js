$(document).ready(function() {
    // 페이지 로드 시 초기화
    $('select[name="is_free"]').val('all'); // 첫번째 항목을 'all'로 설정
    setOptionsForSearchType2('all'); // 두 번째 항목 초기화
    setOptionsForSearchType3('all', '전체'); // 세 번째 항목 초기


    var urlParams = new URLSearchParams(window.location.search);
    var is_free = urlParams.get('is_free');
    var mcodename = urlParams.get('mcodename');
    var codename = urlParams.get('codename');
    var searchKeyword = urlParams.get('searchKeyword');



    if (is_free) {
        $('select[name="is_free"]').val(is_free);
        setOptionsForSearchType2(is_free);

    }

    if (mcodename) {
        $('select[name="mcodename"]').val(mcodename);
        setOptionsForSearchType3(is_free, mcodename);

    }

    if (codename) {
        $('select[name="codename"]').val(codename);

    }

    if (searchKeyword) {
        $('input[name="searchKeyword"]').val(searchKeyword);
    }

    function setOptionsForSearchType2(selectedType1) {
        var options = [];
        switch (selectedType1) {

            case 'all':
                options = ['전체', '공연', '교양', '축제', '기타'];
                break;
            case '무료':
                options = ['전체', '공연', '교양', '축제', '기타'];
                break;
            case '유료':
                options = ['전체', '공연', '교양', '축제', '기타'];
                break;
        }

        updateSearchTypeOptions('select[name="mcodename"]', options);
    }

    function setOptionsForSearchType3(selectedType1, selectedType2) {
        var options = [];
        switch (selectedType2) {
            case '전체':
                options = ['전체'];
                break;
            case '공연':
                options = ['전체', '뮤지컬/오페라', '연극', '콘서트'];
                break;
            case '교양':
                options = ['전체', '전시/미술', '클래식', '국악', '무용', '독주/독창회'];
                break;
            case '축제':
                options = ['전체', '시민화합', '전통/역사', '문화/예술', '자연/경관', '기타'];
                break;
            case '기타':
                options = ['전체', '교육/체험', '영화', '기타'];
                break;
        }

        updateSearchTypeOptions('select[name="codename"]', options);
    }

    function updateSearchTypeOptions(selectName, options) {
        var select = $(selectName);
        select.empty();
        $.each(options, function(index, value) {
            select.append('<option value="' + value + '">' + value + '</option>');
        });
    }

    $('select[name="is_free"]').change(function() {
        var selectedType1 = $(this).val();
        setOptionsForSearchType2(selectedType1);
        setOptionsForSearchType3(selectedType1, $('select[name="mcodename"]').val());
        $('input[name="is_free"]').val(selectedType1);
    });

    $('select[name="mcodename"]').change(function() {
        var selectedType2 = $(this).val();
        setOptionsForSearchType3($('select[name="is_free"]').val(), selectedType2);
        $('input[name="mcodename"]').val(selectedType2);
    });

    $('select[name="codename"]').change(function() {
        var selectedType3 = $(this).val();
        $('input[name="codename"]').val(selectedType3);
    });

});
