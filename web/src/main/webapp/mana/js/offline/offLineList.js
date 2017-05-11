/**
 * Created by zhouheng on 17/3/22.
 */
var vm, vmAlert;
var _pageNumber = 1;
var _pageSize = 10;
$(function () {
    vm = new Vue({
        el: '#vue',
        data: {
            first: true,
            idList: [],
            searchParam: {
                agentName: null,
                agentMobile: null,
                vehicleSn: null,
                gmtCreateStart: null,
                gmtCreateEnd: null,
                auditStatus: "-1",
                pageNum: _pageNumber,
                pageSize: _pageSize
            },
            content: []
        },
        mixins: [window.vueUtils],
        computed: {
            noData: function () {
                return (!this.first && !this.content.length) ? true : false;
            }
        },
        filters: {
            dateHelper: function (date) {
                return VueHelper.longDateHelper(date);
            }
        },
        methods: {
            queryList: function (pageNumber) {
                var _this = this;
                var param = $.extend({}, _this.filterData(_this.searchParam));
                param['pageNum'] = pageNumber;
                var auditStatus = _this.searchParam.auditStatus;
                var mobile = _this.searchParam.agentMobile;
                if(mobile != null && !MaNa.isMobile(mobile)){
                    layer.msg("请填写正确手机号!");
                    return false;
                }
                if (parseInt(auditStatus) < 0) {
                    param.auditStatus = null;
                }
                var index = LayerUtil.load();
                $.ajax({
                    url: "/offline/getTempInfoPageList",
                    data: param
                }).then(function (res) {
                    layer.close(index);
                    if (res.success) {
                        $('.fenye').show();
                        var list = res.list;
                        _this.content = list;
                        _this.first = false;
                        if (!pageNumber || pageNumber == _pageNumber) {
                            LayerPage.init({
                                cont: ['fenye', 'fenye2'],
                                pages: Math.ceil(res.total / _pageSize)
                            }, function (curr) {
                                _this.queryList(curr);
                            });
                        }

                    } else {
                        layer.msg(res.message);
                    }
                })
            },
            selectedItem: function () {
                var $dom = $(event.target);
                $dom.parents("table").find('input[type=checkbox]').not(this).attr("checked", false);
            },
            auditTg: function (item) {
                var _this = this;
                //页面层
                layer.open({
                    title: "请完善保单信息,方便结算对账",
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['520px', '640px'], //宽高
                    content: $('#auditTg').html(),
                    success: function () {
                        vmAlert = initAlert(item);
                        // vmAlert.content = item;
                    }
                });
            },
            auditBh: function (item) {
                var _this = this;
                //页面层
                layer.open({
                    title: "请填写驳回原因",
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['320px', '340px'], //宽高
                    content: $('#auditBh').html(),
                    success: function () {
                        vmAlert = initAlert(item);
                    }
                });
            },
            exportData: function () {
                var _this = this;
                var param = $.extend({}, _this.filterData(_this.searchParam));
                var auditStatus = _this.searchParam.auditStatus;
                if (parseInt(auditStatus) < 0) {
                    param.auditStatus = null;
                }
                var url = "/offline/exportData?" + $.param(param);
                location.href = url;
            }
        },
        mounted: function () {
            this.$nextTick(function () {
                var _this = this;

                _this.queryList(1);

                //重写
                String.prototype.format = function () {
                    for (var i = 0, val = this, len = arguments.length; i < len; i++)
                        val = val.replace(new RegExp('\\{' + i + '\\}', 'gm'), arguments[i]);
                    return val;
                };
            })
        }
    });
    function initAlert(item) {
        return new Vue({
            el: '#vueAudit',
            data: {
                item: {
                    id: item.id,
                    agentId: item.agentId,
                    agentName: item.agentName,
                    insuranceCompanyId: null,
                    auditStatus: 1,
                    vehicleSn: item.vehicleSn,
                    commercialFee: item.commercialFee,
                    commercialFormNo: item.commercialFormNo,
                    gmtCommercialStart: null,
                    forcibleFee: item.forcibleFee,
                    forcibleFormNo: item.forcibleFormNo,
                    gmtForcibleStart: null,
                    gmtPay: null,
                    insuredProvinceCode: null,
                    insuredCityCode: null,
                    insuredProvince: null,
                    insuredCity: null,
                    vesselTaxFee: item.vesselTaxFee
                },
                rejectReason: null,
                content: []
            },
            mixins: [window.vueUtils],
            mounted: function () {
                this.init();
                this.$nextTick(function () {
                    this.getArea($('#province'), $('#city'));
                })
            },
            methods: {
                init: function () {
                    $.ajax({
                        url: "/insurance/getAllCompanyList"
                    }).then(function (result) {
                        var companyList = result.data;

                        var _h = '<option value="">请选择</option>';
                        for (var i = 0; i < companyList.length; i++) {
                            _h += '<option value="{0}">{1}</option>'.format(companyList[i].id, companyList[i].companyName);
                        }
                        $('#insureCompanyId').html(_h);
                    });
                },
                validateData: function (param) {
                    var insuranceCompanyId = param.insuranceCompanyId;
                    if (insuranceCompanyId == null || insuranceCompanyId == -1) {
                        layer.msg("请选择保险公司!");
                        return false;
                    }
                    var vehicleSn = param.vehicleSn;
                    if (vehicleSn == null || vehicleSn == "") {
                        layer.msg("车牌号不能为空!");
                        return false;
                    }
                    var insuredProvinceCode = param.insuredProvinceCode;
                    var insuredCityCode = param.insuredCityCode;
                    if (insuredProvinceCode == null || insuredCityCode == null) {
                        layer.msg("请选择投保省市信息!");
                        return false;
                    }
                    var gmtPay = param.gmtPay;
                    if (gmtPay == null || gmtPay == "") {
                        layer.msg("请选择签单日期!");
                        return false;
                    }
                    var commercialFee = param.commercialFee;
                    var commercialFormNo = param.commercialFormNo;
                    var gmtCommercialStart = param.gmtCommercialStart;
                    var forcibleFee = param.forcibleFee;
                    var forcibleFormNo = param.forcibleFormNo;
                    var gmtForcibleStart = param.gmtForcibleStart;
                    var vesselTaxFee = param.vesselTaxFee;
                    if(commercialFee==""&&forcibleFee==""){
                        layer.msg("抱歉,商业险和交强险至少完善其中一项,请完善必填项后再提交!");
                        return false;
                    }
                    if(commercialFee!=null && commercialFee!=""){
                        var bool1 = true;
                        if((commercialFormNo==null||commercialFormNo=="")&&(gmtCommercialStart==null||gmtCommercialStart=="")){
                            layer.msg("请完善商业险后再提交!");
                            bool1 = false;
                        }else{
                            var flag1 = this.validateNum('商业险保费', commercialFee);
                            if (!flag1) {
                                bool1 = false;
                            }
                            if(commercialFormNo == ""){
                                layer.msg("请完善商业险后再提交!");
                                bool1 = false;
                            }
                            if(gmtCommercialStart== null || gmtCommercialStart== ""){
                                layer.msg("请完善商业险后再提交!");
                                bool1 = false;
                            }
                        }
                        if(!bool1){
                            return false;
                        }
                    }else{
                        if(commercialFormNo!=null && commercialFormNo!=""){
                            layer.msg("请完善商业险后再提交!");
                            return false;
                        }
                        if(gmtCommercialStart!= null && gmtCommercialStart!= ""){
                            layer.msg("请完善商业险后再提交!");
                            return false;
                        }
                        if(commercialFee != null && commercialFee != ""){
                            layer.msg("请完善商业险后再提交!");
                            return false;
                        }
                    }
                    if(forcibleFee!=null && forcibleFee!=""){
                        var bool2 = true;
                        if((forcibleFormNo==null||forcibleFormNo=="")&&(gmtForcibleStart==null||gmtForcibleStart=="")&&(vesselTaxFee==null||vesselTaxFee=="")){
                            layer.msg("请完善交强险后再提交!");
                            bool2 = false;
                        }else{
                            var flag2 = this.validateNum('交强险保费',forcibleFee);
                            if(!flag2){
                                bool2 = false;
                            }
                            var flag3 = this.validateNum('车船税',vesselTaxFee);
                            if(!flag3){
                                bool2 = false;
                            }
                            if(forcibleFormNo == ""){
                                layer.msg("请完交强险后再提交!");
                                bool2 = false;
                            }
                            if(gmtForcibleStart==null || gmtForcibleStart==""){
                                layer.msg("请完交强险后再提交!");
                                bool2 = false;
                            }
                        }
                        if(!bool2){
                            return false;
                        }
                    }else{
                        if(forcibleFormNo!=null && forcibleFormNo!=""){
                            layer.msg("请完善交强险后再提交!");
                            return false;
                        }
                        if(gmtForcibleStart!= null && gmtForcibleStart!= ""){
                            layer.msg("请完善交强险后再提交!");
                            return false;
                        }
                        if(forcibleFee != null && forcibleFee != ""){
                            layer.msg("请完善交强险后再提交!");
                            return false;
                        }
                        if(vesselTaxFee!=null && vesselTaxFee!= ""){
                            layer.msg("请完善交强险后再提交!");
                            return false;
                        }
                    }
                    return true;
                },
                validateNum: function (tip, num) {
                    if (!isNaN(num)) {
                        if (num <=0) {
                            layer.msg('请完善交强险');
                            return false;
                        }
                        if (num > 99999999) {
                            layer.msg(tip + '必须小于99999999');
                            return false;
                        }
                    } else {
                        layer.msg(tip + '必须为数字');
                        return false;
                    }
                    return true;
                },
                submit: function () {
                    var _this = this;
                    var param = _this.getItemObj();
                    var flag = this.validateData(param);
                    if (!flag) {
                        return false;
                    }
                    var index = LayerUtil.load();
                    $.post("/offline/modifyEnteringFormAuditStatus",
                        param,
                        function (result) {
                            layer.close(index);
                            if (result.success) {
                                layer.closeAll();
                                layer.msg("操作成功!保单已审核通过");
                                setTimeout(function () {
                                    location.reload()
                                },2000)
                            } else {
                                layer.msg(result.message);
                            }
                        })
                },
                cancel: function () {
                    layer.closeAll();
                },
                submit2: function () {
                    var _this = this;
                    var param = _this.getItemObj();
                    if(_this.rejectReason == null || _this.rejectReason == undefined
                        || _this.rejectReason == ""){
                        layer.msg("请填写审核驳回原因");
                        return false;
                    }
                    param['rejectReason'] = _this.rejectReason;
                    param['auditStatus'] = 2;
                    var index = LayerUtil.load();
                    $.post("/offline/modifyEnteringFormAuditStatus",
                        param,
                        function (result) {
                            layer.close(index);
                            if (result.success) {
                                layer.closeAll();
                                layer.msg("驳回成功!");
                                setTimeout(function () {
                                    location.reload()
                                },2000)

                            } else {
                                layer.msg(result.message);
                            }
                        })
                },
                cancel2: function () {
                    layer.closeAll();
                },
                getItemObj: function () {
                    var _this = this;
                    var itemObj = $.extend({}, _this.item);
                    itemObj['insuredCity'] = $('#city :selected').text();
                    itemObj['insuredProvince'] = $('#province :selected').text();
                    return itemObj;
                }
            }
        })
    }
});
