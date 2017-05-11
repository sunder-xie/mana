$(document).ready(function () {
    $('#cityArea').hide();
    //加载省份
    initProvince();

    initEditInfo();
});

/**
 * 初始化编辑信息
 */
function initEditInfo() {
    var id = $('#id').val();
    if (id == null || id == '') {
        return false;
    }
    $.ajax({
        url: '/settle/insuranceCompanyRule/getById',
        type: 'GET',
        data: {
            id: id
        },
        dataType: 'json',
        success: function (result) {
            var data = result.data;
            var cityCode = data.cityCodeList[0];
            var cityName = data.cityNameList[0];
            $('#insuranceCompany').val(data.insuranceCompanyId).prop('disabled', true);
            $('#province').val(data.provinceCode).prop('disabled', true);
            $('#cityArea').show();
            $('#checkAll').hide();
            var html = '<div class="list-div"><input type="checkbox" checked="checked" disabled="disabled" class="opa-check checker" data-name="' + cityName + '" value="' + cityCode + '">' + cityName + '</div>';
            $('#cityList').append(html);
            for (var i = 0; i < data.ruleItemList.length; i++) {
                var item = data.ruleItemList[i];
                $('.scenario').each(function () {
                    if ($(this).attr('data-scenario') == item.scenarioType && $(this).attr('data-insuranceType') == item.insuranceType) {
                        if (item.commissionRate == 0) {
                            $(this).val('0.00');
                        } else {
                            $(this).val(item.commissionRate);
                        }
                    }
                });
            }
        }
    })

}

function initProvince() {
    $.ajax({
        async:false,
        url: '/settle/insuranceCompanyRule/regionList',
        type: 'GET',
        data: {
            regionCode: '000000'
        },
        dataType: 'json',
        success: function (result) {
            var data = result.data;
            $('#province').html("");
            $('#province').append('<option value="">请选择</option>>');
            for (var i = 0; i < data.length; i++) {
                var option = '<option value="' + data[i].regionCode + '">' + data[i].regionName + '</option>>';
                $('#province').append(option);
            }
        }
    })
}

$(document).on('change', '#province', function () {
    var provinceCode = $(this).val();
    $('#cityArea').show();
    $.ajax({
        url: '/settle/insuranceCompanyRule/regionList',
        type: 'GET',
        data: {
            regionCode: provinceCode
        },
        dataType: 'json',
        success: function (result) {
            var list = result.data;
            $('#cityList').html('');
            for (var i = 0; i < list.length; i++) {
                var html = '<div class="list-div"><input type="checkbox" class="opa-check checker" data-name="' + list[i].regionName + '" value="' + list[i].regionCode + '">' + list[i].regionName + '</div>';
                $('#cityList').append(html);
            }
        }
    });
});

$(document).on('change', '.checkAll', function () {
    var checked = $(this).prop('checked');
    $('.checker').each(function () {
        $(this).prop('checked', checked);
    })
});

$(document).on('change', '.checker', function () {
    var length = $('.checker').length;
    var checkedLength = $('.checker:checked').length;
    $('.checkAll').prop('checked', length == checkedLength);
});

$(document).on('click', '.sure-button', function () {
    var regular = /^[0]\.([0-9]){1,2}$/;
    var insuranceCompanyId = $('#insuranceCompany').val();
    var companyName = $('#insuranceCompany').find('option:selected').text();
    var provinceCode = $('#province').val();
    var provinceName = $('#province').find('option:selected').text();
    var cityCodeLength = $('.checker:checked').length;
    if (insuranceCompanyId == '') {
        alert('请选择保险公司');
        return false;
    }
    if (provinceCode == '') {
        alert('请选择省份');
        return false;
    }
    if (cityCodeLength == 0) {
        alert('请选择返点配置城市');
        return false;
    }
    var cityCodeList = [];
    var cityNameList = [];
    $('.checker:checked').each(function () {
        cityCodeList.push($(this).val());
        cityNameList.push($(this).attr('data-name'));
    });
    var ruleItemList = [];
    var flag = true;
    $('.scenario').each(function () {
        var element = $(this);
        if (element.val() != '') {
            if (!regular.test($(this).val())) {
                alert($(this).val() + '不符合比例条件，请重新输入在1以内的两位小数');
                flag = false;
                return false;
            }
        }
        var ruleItemVO = {
            scenarioType: $(this).attr('data-scenario'),
            insuranceType: $(this).attr('data-insuranceType'),
            commissionRate: $(this).val() == '' ? '0.00' : $(this).val(),
        };
        ruleItemList.push(ruleItemVO);
    });
    if (!flag) {
        return false;
    }
    var data = {
        id: $('#id').val(),
        insuranceCompanyId: insuranceCompanyId,
        insuranceCompanyName: companyName,
        provinceCode: provinceCode,
        provinceName: provinceName,
        cityCodeList: cityCodeList,
        cityNameList: cityNameList,
        ruleItemList: ruleItemList
    };
    $.ajax({
        url: '/settle/insuranceCompanyRule/saveRule',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(data),
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                alert('保存成功');
                window.location.href = '/settle/insuranceCompanyRule/listPage';
            } else {
                alert(result.message);
            }
        }
    })

});

$(document).on('click', '.cancel', function () {
    window.location.href='/settle/insuranceCompanyRule/listPage';
})

