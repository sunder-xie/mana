/**
 * Created by huage on 2017/2/22.
 */

var settleCommon = function () {
    // 页面输入框选择框拿值
    function getData(mode) {
        var data = {};
        $('select,input').each(function () {
            var $this = $(this),
                type_name = $this.data('typename'),
                val;
            if(type_name){
                if($this.is('input')){
                    val = $this.data('id')
                }else if ($this.is('select')){
                    val = $this.find('option:selected').val()
                }
                data[type_name]=val
            }
        });
        if(mode){
            data['cooperationModes'] = mode;
        }
        return data;
    }

    function insureCompany(url,tpl,id) {
        //加载保险公司
        Ajax.get({
            url:url,
            data:{},
            success:function (result) {
                if(result.success){
                    var data = result.data;
                    var html = template(tpl,{data: data});
                    $(id).html(html);
                }
            }
        });
    }

    //ajax得到城市数据
    function getCityData(url,data,IdName,msg,tpl) {
        Ajax.get({
            url:url,
            data:data,
            success:function (result) {
                if(result.success){
                    var data = result.data;
                    var html = template(tpl,{data: data,msg:msg});
                    $(IdName).html(html);
                }
            }
        })
    }
    
    //时间方法
    
    function changeDate(val, defaultVal) {
        var _shortDateFormat = 'yyyy/MM/dd';
        if(val===undefined || val===null){
            if(defaultVal){
                return defaultVal;
            }
            return '';
        }
        return $.format.date(val, _shortDateFormat);
    }

    return {
        getData:function (mode) {
            return getData(mode);
        },

        insureCompany:function (url,tpl,id) {
            return insureCompany(url,tpl,id)
        },

        getCityData:function (url,data,IdName,msg,tpl) {
            return getCityData(url,data,IdName,msg,tpl)
        },

        changeDate:function(val,defaultVal) {
            return changeDate(val,defaultVal);
        }
    }

}();
