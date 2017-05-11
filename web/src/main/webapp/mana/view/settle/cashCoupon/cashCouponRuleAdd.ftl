<!--代金券规则新增-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/settleAdd.css" type="text/css" rel="stylesheet">
<link href="/mana/css/settle/cashCoupon/cashCouponRuleAdd.css" type="text/css" rel="stylesheet">
<div class="common-vue">
    <p class="p-title">代金券 > 规则管理 > <span>{{statusStr}}规则</span></p>
    <div class="cash-box">
        <p class="box-title">{{statusStr}}规则</p>
        <div class="add-cash-box">
            <p class="box-second-title">请选择规则适用地区</p>
            <div class="select-rule">
                <div class="addCity-box" v-if="jsonData.regionConfigBizParams.length>0">
                    <p>地区：</p>
                    <div class="addCityUl">
                        <ul class="cityUl">
                            <li v-for="list in jsonData.regionConfigBizParams">
                                [{{list.provinceName}}省 -{{list.cityName}}市]
                            </li>
                        </ul>
                    </div>
                </div>

                <button class="select-area-button" @click="addUseArea" v-if="jsonData.regionConfigBizParams.length<=0">
                    + 新增地区
                </button>
                <button class="select-area-button" @click="addUseArea" v-if="jsonData.regionConfigBizParams.length>0 && !id">
                    重新选择
                </button>
            </div>
            <p class="box-second-title">填写现金券生成规则</p>
            <div class="select-rule">
                <div class="add-item">
                    <div class="dispose-condition-item">
                        <span class="option-title">送券商品类型</span>
                        <select v-model="goodsType">
                            <option :value="0">请选择</option>
                            <option :value="1">自营产品</option>
                            <option :value="2">非自营产品</option>
                        </select>
                    </div>
                    <div class="dispose-condition-item">
                        <span class="option-title">商品金额折算系数</span>
                        <input type="text" placeholder="1：2折算，输0.5" v-model="goodsCoefficient"
                               @blur="decimalCheck(goodsCoefficient,$event)">
                    </div>
                    <div class="dispose-condition-item">
                        <button class="create-rule-btn" @click="createRule">添加</button>
                    </div>
                </div>
                <div class="add-item goodsConfig-box" v-show="jsonData.goodsConfigBizParams.length">
                    <p>已填规则：</p>
                    <ul>
                        <li v-for="(goodsConfig,i) of jsonData.goodsConfigBizParams">
                            <span v-if="goodsConfig.goodsType==1" class="goodsConfig">自营产品</span>
                            <span v-if="goodsConfig.goodsType==2" class="goodsConfig">非自营产品</span>
                            <span class="goodsConfig">{{goodsConfig.goodsCoefficient}}</span>
                            <a href="javascript:void 0;" class="removeRule" @click="removeRule(i)">删除</a>
                        </li>
                    </ul>
                </div>
                <div class="add-item">
                    <span class="option-title">送券面值</span>
                    <input type="text" v-model="jsonData.faceAmount" @blur="integerCheck(jsonData.faceAmount,$event)">
                </div>
                <div class="add-item">
                    <span class="option-title">券有效期</span>
                    <input type="text" placeholder="请输入有效天数" v-model="jsonData.cashCouponValidateDays"
                           @blur="integerCheck(jsonData.cashCouponValidateDays,$event)">
                </div>
                <div class="add-item">
                    <span class="option-title">开启状态</span>
                    <ul class="open-state">
                        <li><input type="radio" name="open-state" v-model="jsonData.ruleStatus" value="1">开启</li>
                        <li><input type="radio" name="open-state" v-model="jsonData.ruleStatus" value="0">关闭</li>
                    </ul>
                </div>
            </div>
            <p class="box-second-title">填写现金券使用规则</p>
            <div class="select-rule">
                <p>系统出单的保单</p>
                <div class="add-item">
                    <span class="option-title">商业险最低投保金额</span>
                    <input type="text" class="input-width" v-model="jsonData.onlineCommercialMinfee"
                           @blur="decimalCheck(jsonData.onlineCommercialMinfee,$event)">
                </div>
                <div class="add-item">
                    <span class="option-title">交强险最低投保金额</span>
                    <input type="text" class="input-width" v-model="jsonData.onlineForcibleMinfee"
                           @blur="decimalCheck(jsonData.onlineForcibleMinfee,$event)">
                </div>
                <div class="add-item">
                    <span class="option-title">保单缴费时间</span>
                    <input type="text" class="input-width" placeholder="输0代表当天，输1代表当天及昨天"
                           v-model="jsonData.onlineFormValidateDays"
                           @blur="integerCheck(jsonData.onlineFormValidateDays,$event)">
                </div>
                <div class="add-item">
                    <span class="option-title">业务模式</span>
                    <select v-model="jsonData.onlineCooperationMode">
                        <option :value="1">奖励金</option>
                    </select>
                </div>
                <p>门店补录的保单</p>
                <div class="add-item">
                    <span class="option-title">商业险最低投保金额</span>
                    <input type="text" class="input-width" v-model="jsonData.offlineCommercialMinfee"
                           @blur="decimalCheck(jsonData.offlineCommercialMinfee,$event)">
                </div>
                <div class="add-item">
                    <span class="option-title">交强险最低投保金额</span>
                    <input type="text" class="input-width" v-model="jsonData.offlineForcibleMinfee"
                           @blur="decimalCheck(jsonData.offlineForcibleMinfee,$event)">
                </div>
                <div class="add-item">
                    <span class="option-title">保单缴费时间</span>
                    <input type="text" class="input-width" placeholder="输0代表当天，输1代表当天及昨天"
                           v-model="jsonData.offlineFormValidateDays"
                           @blur="integerCheck(jsonData.offlineFormValidateDays,$event)">
                </div>
                <div class="add-item">
                    <span class="option-title">业务模式</span>
                    <select v-model="jsonData.offlineCooperationMode">
                        <option :value="1">奖励金</option>
                    </select>
                </div>
            </div>
            <p class="box-second-title">填写现金券结算规则</p>
            <div class="select-rule">
                <div class="add-item">
                    <span class="option-title">用券后商业险返点比例</span>
                    <input type="text" class="input-width" v-model="jsonData.commercialRebateRatio"
                           @blur="blurInput(jsonData.commercialRebateRatio,$event)">
                </div>
                <div class="add-item">
                    <span class="option-title">用券后交强险返点比例</span>
                    <input type="text" class="input-width" v-model="jsonData.forcibleRebateRatio"
                           @blur="blurInput(jsonData.forcibleRebateRatio,$event)">
                </div>
            </div>
            <div class="btn-box">
                <button class="cancel-button" @click="cancelInput">取消</button>
                <button class="sure-button" @click="submitInput">提交</button>
            </div>
        </div>
    </div>
    <div id="area-box">
        <div>
            <div class="cityBox" v-for="(item,i) of cityList">
                <p class="regionName">{{item.parentRegion.regionName}}</p>
                <ul class="cityUl">
                    <li>
                        <input type="checkbox" :id="i" @change="selectProAllCity(item,$event,i)">全选
                    </li>
                    <li v-for="(city,j) of item.subRegionList">
                        <input type="checkbox" v-model="item.selected" :value="city" class="checkeBox"
                               @change="isTitleChecked(item,$event,i)">{{city.regionName}}
                    </li>
                </ul>
            </div>
        </div>
        <div class="btn-box">
            <button class="cancel-button" @click="cancelChooseArea">取消</button>
            <button class="sure-button" @click="sureChooseArea">确定</button>
        </div>
    </div>
</div>


<script>
    window.vv = new Vue({
        el: '.common-vue',
        data: {
            jsonData: {
                regionConfigBizParams: [],           //城市列表
                //生成规则
                goodsConfigBizParams: [],        //规则列表
                faceAmount: '',       //面值
                cashCouponValidateDays: '',        //券有效期
                ruleStatus: 1,         //开启状态   1是开启，0关闭
                //使用规则
                onlineForcibleMinfee: '',  //    /** 线上交强险最低保费 **/
                onlineCommercialMinfee: '',//    /**线上商业险最低保费**/
                onlineFormValidateDays: '', //    /**线上保单缴费有效期限(单位:天)**/
                onlineCooperationMode: '1',      //    /**线上业务模式 1:奖励金**/
                offlineForcibleMinfee: '',          // /**线下交强险最低保费**/
                offlineCommercialMinfee: '',//    /**线下商业险最低保费**/
                offlineFormValidateDays: '',            //    /**线下保单缴费有效期限(单位:天)**/
                offlineCooperationMode: '1',         //    /**线下业务模式 1:奖励金**/
                //结算规则
                commercialRebateRatio: '',
                forcibleRebateRatio: ''
            },
            cityList: [],
            goodsType: 0,
            goodsCoefficient: '',
            id:''
        },
        mixins: [window.vueUtils],
        computed: {
            'statusStr': function () {
                return this.jsonData.id ? '编辑' : '新增';
            }
        },
        watch: {
            'goodsType': function (n, o) {
                if (!n) return;
                var _this = this, send = n;
            }
        },
        methods: {
            //ajax方法
            getJson: function (url, data,type, backFun) {
                Ajax.get({
                    url: url,
                    type: type,
                    contentType : 'application/json',
                    data: data
                }).done(function (result) {
                    if (result.success) {
                        backFun && backFun(result);
                    }else{
                        layer.alert(result.message, {
                            icon: 2
                        });
                    }
                })
            },
            //新增地区
            addUseArea: function () {
                var _url = '/cashCouponConfig/getOpenedRegionList',
                        _this = this;
                this.getJson(_url, {},'get', function (result) {
                    _this.cityList = result.data;
                    var _regionConfigBizParams = _this.jsonData.regionConfigBizParams;
                    _this.cityList.forEach(function (list) {
                        _this.$set(list, 'selected', []);
                    });
                    if (_regionConfigBizParams.length > 0) {
                        for (var i = 0; i < _regionConfigBizParams.length; i++) {
                            for (var j = 0; j < _this.cityList.length; j++) {
                                if(_this.cityList[j].parentRegion.id ==  _regionConfigBizParams[i].provinceCode){
                                    _this.$set(_this.cityList[j],'selected', JSON.parse(JSON.stringify(_regionConfigBizParams[i].selected)));
                                }
                            }
                        }
                    }
                });
                layer.open({
                    type: 1,
                    title: '请选择规则适用地区',
                    content: $('#area-box'),
                    area: ['800px', '425px'],
                    shade: 0.5,
                    shadeClose: true
                });
            },
            //全选
            selectProAllCity: function (data, event, index) {
                var _checked = event.target.checked,
                        flag = false;
                data.selected = [];
                if (_checked) {
                    data.subRegionList.forEach(function (item) {
                        data.selected.indexOf(item) === -1 && data.selected.push(item);
                    });
                    if (data.subRegionList.length == data.selected.length) {
                        flag = true;
                    }
                }
                $('#' + index).parent('li').siblings('li').find('input').prop('checked', flag);
            }
            ,
            isTitleChecked: function (data, event, index) {
                index = parseInt(index, 10);
                var _select = data.selected,
                        flag = data.subRegionList.every(function (item) {
                            return _select.indexOf(item) != -1;
                        });
                $('#' + index).prop('checked', flag)
            }
            ,
            //取消选择地区
            cancelChooseArea: function () {
                layer.closeAll()
            }
            ,
            //确定选择地区
            sureChooseArea: function () {
                var _this = this,
                        _regionConfigBizParams = _this.jsonData.regionConfigBizParams = [];

                for(var i = 0;i<_this.cityList.length;i++){
                    var _cityList = _this.cityList[i];
                    for(var j = 0;j<_cityList.selected.length;j++){
                        var _selected = _cityList.selected.length > 0 ? _cityList.selected:[];
                        _regionConfigBizParams.push({
                            provinceCode:_cityList.parentRegion.id,
                            cityCode:_cityList.selected[j].id,
                            cityName:_cityList.selected[j].regionName,
                            provinceName:_cityList.parentRegion.regionName,
                            selected:_selected
                        });
                    }
                }
                if(_regionConfigBizParams.length <=0){
                    layer.alert('请选择配置地区', {
                        icon: 2
                    });
                   return;
                }
                layer.closeAll()
            }
            ,
            createRule: function () {
                var _this = this,
                        _goodsConfigBizParams=_this.jsonData.goodsConfigBizParams;
                if (_this.goodsType == 0 || !_this.goodsCoefficient) return layer.alert('请把必填项填写完整后再添加', {
                    icon: 2
                });
                for(var i = 0;i<_goodsConfigBizParams.length;i++){
                    if(_this.goodsType==_goodsConfigBizParams[i].goodsType){
                        return layer.alert('不能重复添加送券商品类型', {
                            icon: 2
                        });
                    }
                }
                _this.jsonData.goodsConfigBizParams.push({
                    goodsType: _this.goodsType,
                    goodsCoefficient: _this.goodsCoefficient
                })
            }
            ,
            removeRule: function (index) {
                this.jsonData.goodsConfigBizParams.splice(index, 1);
            }
            ,
            cancelInput: function () {
                window.location.href='/cashCouponConfig/config/list'
            }
            ,
            submitInput: function () {
                var _this = this,
                        data = _this.jsonData,
                        url = '/cashCouponConfig/addConfig',
                        flag = true;
                $('input[type="text"]').each(function () {
                    var $this = $(this);
                    if ($this.hasClass('add-red-border')) {
                        layer.alert('请把数据类型填正确', {
                            icon: 7
                        });
                        flag = false;
                        return false;
                    }
                });
                if (!flag) return false;
                for (var key in data) {
                    if (Array.isArray(data[key])) {
                        if (data[key].length <= 0) {
                            return layer.alert('请把必填项填写完整后再提交', {
                                icon: 2
                            });
                        }
                    } else {
                        if ($.trim(data[key])=='') {
                            return layer.alert('请把必填项填写完整后再提交', {
                                icon: 2
                            });
                        }
                    }
                }
                data = JSON.stringify(data);
                _this.getJson(url, data,'post', function (result) {
                    if (result.success) {
                        layer.alert('提交成功', {
                            icon: 1
                        });
                        window.location.reload();
                    }
                })
            },
            'blurInput':function (data,event) {
                var condition =  /^\b0(\.\d{1,2})?\b$/,
                    _target = $(event.target);
                if(!data) {
                    _target.removeClass('add-red-border');
                    return false;
                }
                if(!condition.test(data)){
                    layer.alert('必须输入两位小数', {
                        icon: 7
                    });
                    _target.addClass('add-red-border');
                    return ;
                }
                _target.removeClass('add-red-border');

            }
        },
        mounted: function () {
            this.$nextTick(function () {
                var _this = this, urlData = _this.parseUrl(),
                        Id = urlData['id'],
                        data = {
                            regionConfigId: Id
                        },
                        url='/cashCouponConfig/getConfigInfoById';
                if(!Id) return;
                _this.getJson(url, data,'get', function (result) {
                    if(result.success){
                        _this.jsonData = result.data;
                        _this.id = result.data.id;
                    }
                })
            });
        }
    })
</script>
<#include "/mana/view/common/footer.ftl">