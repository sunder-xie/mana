/**
 * Created by huangzhangting on 16/11/30.
 */
var MaNa = function(){

    return {
        /** 替换所有空格 */
        repSpace: function(str){
            return str.replace(/\s/g, '');
        },
        /** 去除右边空格 **/
        trimRight : function(str){
            return str.replace(/(\s*$)/g, "");
        },
        /** 验证是否手机号 */
        isMobile: function (val) {
            return /^1[34578]\d{9}$/.test(val);
        },
        /** 正整数 */
        isPositiveInt: function(val){
            return /^[1-9]+[0-9]*$/.test(val);
        },
        /** 校验车牌 */
        isLicencePlate: function(val){
            return /^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$/.test(val.toUpperCase());
        },
        //表单序列化成对象
        jqSerializeForm: function(form){
            $form = $(form);
            var o = {};
            $.each($form.serializeArray(), function(index) {
                if (o[this['name']]) {
                    o[this['name']] = o[this['name']] + "," + this['value'].trim();
                } else {
                    o[this['name']] = this['value'].trim();
                }
            });
            return o;
        },
        required : function(obj){
            var searchVal = this.repSpace($(obj).val());
            $(obj).val(searchVal);
            if(searchVal==''){
                LayerUtil.msg('请输入车牌号');
                return false;
            }
            return true;
        }
    };
}();
