var _pageSize = 3;
var _pageNumber = 1;

$(document).ready(function () {
    //加载省份
    initRegion('#province', '000000');

    initQuery(_pageNumber);


});

function initRegion(element, regionParentCode) {
    $.ajax({
        url: '/settle/insuranceCompanyRule/regionListForSelect',
        type: 'GET',
        data: {
            regionParentCode: regionParentCode
        },
        dataType: 'json',
        success: function (result) {
            var data = result.data;
            $(element).html("");
            $(element).append('<option value="">请选择</option>>');
            for (var i = 0; i < data.length; i++) {
                var option = '<option value="' + data[i].regionCode + '">' + data[i].regionName + '</option>>'
                $(element).append(option);
            }
        }
    });
}

$(document).on('change', '#province', function () {
    var regionParentCode = $(this).val();
    if (regionParentCode == '') {
        return false;
    }
    initRegion('#city', regionParentCode);
});

$(document).on('click', '.search-button', function () {
    initQuery(_pageNumber);
});

function initQuery(pageNumber) {
    var index = LayerUtil.load();
    var provinceCode = $('#province').val();
    var cityCode = $('#city').val();
    var data = {
        'pageNumber': pageNumber,
        'pageSize': _pageSize,
        'provinceCode': provinceCode,
        'cityCode': cityCode
    };
    $.ajax({
        url: '/settle/insuranceCompanyRule/list',
        type: 'GET',
        data: data,
        dataType: 'json',
        success: function (result) {
            var offerSet = (pageNumber - 1) * _pageSize;
            var list = result.list;
            if (list != null && list.length > 0) {
                $('.fenye').show();
                $('.companyRuleList').removeClass("car-owner-Info-set");
                if (pageNumber == _pageNumber) {
                    LayerPage.init({
                        cont: ['fenye', 'fenye2'],
                        pages: Math.ceil(result.total / _pageSize)
                    }, function (curr) {
                        //分页查询
                        initQuery(curr);
                    });
                }
                $('.companyRuleList').html(template('companyRuleListTemplate', {
                    list: list,
                    offerSet: offerSet + 1
                }));
                //数据加载完，将表格列表背景变色
                initListBackground();
                changeBackground();
            } else {
                $('.companyRuleList').html("暂无数据");
                $('.fenye').hide();
                $('.companyRuleList').addClass("car-owner-Info-set");
            }

            layer.close(index);
        }
    });
}
//改变列表的背景色
function initListBackground() {
    var table_tr = $('.mana-table tbody tr');
    table_tr.each(function () {
        var module = parseInt($(this).prop('class'),10)%2;
        if(module == 0){
            $(this).css('background','#f9f9f9');
        }else{
            $(this).css('background','#fff');
        }
    })
}
$(document).on('click', '.delete', function () {
    var id = $(this).attr('data-value');
    if(!confirm('确定要删除该条记录吗?')){
        return false;
    }
    $.ajax({
        url: '/settle/insuranceCompanyRule/delete',
        type: 'GET',
        data: {
            'id': id
        },
        dataType:'json',
        success:function(result){
            if(result.success){
                alert('删除成功');
                initQuery(_pageNumber);
            }else{
                alert('删除失败');
            }
        }
    })
});

$(document).on('click','#add',function(){
    window.location.href='/settle/insuranceCompanyRule/rulePage';
});

//鼠标移入移出列表背景变色
function changeBackground() {
    //
    var current_bc = null,
        aaa = null;
    $('.mana-table tbody tr').hover(function () {
        aaa = $(this).prop('class');
        current_bc = $(this).css('background');
        $('.'+aaa).css('background','#e3eced');
    },function()
    {
        $('.'+aaa).css('background',current_bc);
    })
}
