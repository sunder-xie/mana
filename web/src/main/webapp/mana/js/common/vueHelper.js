/**
 * Created by zhouheng on 17/3/27.
 */

var VueHelper = {
    longDateHelper: function (date) {
        var _longDateFormat = 'yyyy-MM-dd HH:mm:ss';
        if (date === undefined || date === null) {
            return '';
        }
        return $.format.date(date, _longDateFormat);
    },
    shortDateHelper: function (date) {
        var _shortDateFormat = 'yyyy-MM-dd';
        if (date === undefined || date === null) {
            return '';
        }
        return $.format.date(date, _shortDateFormat);
    }
};
