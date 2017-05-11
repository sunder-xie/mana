var _pageSize = 15;
var _pageNumber = 1;

$(document).ready(function () {


    initQuery(_pageNumber);


});



$(document).on('click', '.search-button', function () {
    initQuery(_pageNumber);
});


$(document).on('click', '.new-add-button', function () {
    location.href = "/cashCouponConfig/addConfigInit";
});


function initQuery(pageNumber) {
    var index = LayerUtil.load();
    var provinceCode = $('#province').val();
    var cityCode = $('#city').val();

    var data = {
        'pageNumber': pageNumber,
        'pageSize': _pageSize,
        'provinceName': provinceCode,
        'cityName': cityCode
    };
    $.ajax({
        url: '/cashCouponConfig/getConfigInfo',
        type: 'GET',
        data: data,
        dataType: 'json',
        success: function (result) {
            var offerSet = (pageNumber - 1) * _pageSize;
            var list = result.list;
            if (list != null && list.length > 0) {
                $('.fenye').show();
                $('.cashCouponRuleList').removeClass("car-owner-Info-set");
                if (pageNumber == _pageNumber) {
                    LayerPage.init({
                        cont: ['fenye', 'fenye2'],
                        pages: Math.ceil(result.total / _pageSize)
                    }, function (curr) {
                        //分页查询
                        initQuery(curr);
                    });
                }
                $('.cashCouponRuleList').html(template('cashCouponRuleListTemplate', {
                    list: list,
                    offerSet: offerSet + 1
                }));
                //数据加载完，将表格列表背景变色
                initListBackground();
                changeBackground();
            } else {
                $('.cashCouponRuleList').html("暂无数据");
                $('.fenye').hide();
                $('.cashCouponRuleList').addClass("car-owner-Info-set");
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
    var id = $(this).attr('data-id');

    if(!confirm('确定要删除该条记录吗?')){
        return false;
    }
    $.ajax({
        url: '/cashCouponConfig/deleteConfigInfo',
        type: 'GET',
        data: {
            'regionConfigId': id
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
