/**
 * Created by huangzhangting on 16/12/28.
 */

/* 定义一些通用的模板助手 */

var _shortDateFormat = 'yyyy-MM-dd';
template.helper('shortDateHelper', function(val){
    if(val===undefined || val===null){
        return '';
    }
    return $.format.date(val, _shortDateFormat);
});

var _longDateFormat = 'yyyy-MM-dd HH:mm:ss';
template.helper('longDateHelper', function(val){
    if(val===undefined || val===null){
        return '';
    }
    return $.format.date(val, _longDateFormat);
});

