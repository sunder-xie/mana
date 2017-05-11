<!--服务包比例管理-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/settleAdd.css" type="text/css" rel="stylesheet">
<div class="common-vue">

    <p class="p-title">服务包模式 > 服务包模式 > <span>{{statusStr}}</span></p>
    <div class="select-condition-box">
        <div class="select-condition-item">
            <span>保险公司</span>
            <select v-model="ld.insuranceCompanyId" class="insuranceCompanyId">
                <option :value="item.id" v-for="item in companyData">{{item.companyName}}</option>
            </select>
        </div>
        <div class="select-condition-item">
            <span>保险模式</span>
            <select v-model="ld.cooperationMode">
                <option value="2">买保险送服务包</option>
                <option value="3">买服务包送保险</option>
            </select>
        </div>
    </div>
    <div class="add-box">
        <div class="inner-box">
            <div class="insurance-box-outer">
                <div class="insurance-box-row">
                    <p class="insurance-title title-margin">请选择配置条件</p>
                    <div class="add-item-box" v-if="!ld.id">
                        <input class="opa-radio" type="radio" name="applyRange" v-model="ld.applyRange" value="1">地区配置
                        <input class="opa-radio" type="radio" name="applyRange" v-model="ld.applyRange" value="2">门店配置
                    </div>
                    <div class="area-dispose-box" v-show="ld.applyRange == 1">
                        <div class="area-dispose-item">
                            <select class="province" v-model="provinceChangeMark">
                                <option>选择省份</option>
                                <option :value="item.regionCode" v-for="item in provinceData">{{item.regionName}}</option>
                            </select>
                        </div>
                        <div class="area-dispose-item">
                            <label v-show="cityData.length">
                                <input class="all-check opa-radio" type="checkbox" v-model="allCityMark">
                                全选
                            </label>
                            <ul class="area-dispose-item">
                                <li v-for="item in cityData">
                                    <label>
                                        <input type="checkbox" class="opa-radio" :value="item" v-model="ld.regionConfigVOList">{{item.cityName}}
                                    </label>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="store-dispose-box" v-show="ld.applyRange == 2">
                        <div class="area-dispose-item">
                            <input type="text" class="storeInput" placeholder="请输入门店名称">
                            <div class="selectQueryArea"></div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="insurance-box-outer">
                <div class="insurance-box-row">
                    <p class="insurance-title title-margin">
                        服务包工时费
                        <input type="checkbox" class="opa-radio" value="true"
                               v-model="package.show">
                    </p>
                    <div class="add-item-box" v-show="package.show">
                        <div class="add-item">
                            <span class="margin-r-10">生效起止时间</span>
                            <input type="text"  v-model="timeReport('YYYY-MM-DD', package.startTime)" disabled="disabled">
                            <span class="margin-r-10">至</span>
                            <input type="text" class="Wdate" v-model="package.endTime" @blur="saveTime" @change="saveTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" >
                        </div>
                        <div class="add-item clearFix">
                            <span class="second-level-title">结算条件</span>
                            <ul class="second-level-box">
                                <li v-if="ld.cooperationMode == 3">
                                    <input type="radio" name="packCondition" class="opa-radio" value="3"
                                           v-model="package.settleCondition">服务包支付日期
                                </li>
                                <li>
                                    <input type="radio" name="packCondition"  class="opa-radio" value="1"
                                           v-model="package.settleCondition">签单日期
                                </li>
                                <li>
                                    <input type="radio" name="packCondition"  class="opa-radio" value="2"
                                           v-model="package.settleCondition">起保日期
                                </li>
                            </ul>
                        </div>
                        <div class="add-item clearFix">
                            <span class="second-level-title">资金类型</span>
                            <ul class="second-level-box">
                                <li>
                                    <input type="radio" name="packFundType" class="opa-radio" value="1"
                                           v-model="package.fundType">现金
                                </li>
                            </ul>
                        </div>
                        <div class="add-item clearFix">
                            <span class="second-level-title">返点类型</span>
                            <ul class="second-level-box">
                                <li>
                                    <input type="radio" name="packRebateType" class="opa-radio" value="2"
                                           v-model="package.rebateType">服务包工时费
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <div class="insurance-box-outer">
                <div class="insurance-box-row">
                    <p class="insurance-title title-margin">
                        商业险
                        <input type="checkbox" class="opa-radio" value="true"
                               v-model="bussiness.show">
                    </p>
                    <div class="add-item-box" v-show="bussiness.show">
                        <#--<div class="add-item">-->
                            <#--<div class="select-condition-item">-->
                                <#--<span>计算方式</span>-->
                                <#--<select v-model="bussiness.calculateMode">-->
                                    <#--<option value="1">单笔</option>-->
                                    <#--<option value="2">月累计</option>-->
                                <#--</select>-->
                            <#--</div>-->
                        <#--</div>-->
                        <div class="add-item clearFix">
                            <span class="second-level-title">结算条件</span>
                            <ul class="second-level-box">
                                <li>
                                    <input type="radio" name="bussinessCondition"  class="opa-radio" value="1"
                                           v-model="bussiness.settleCondition">签单日期
                                </li>
                                <li>
                                    <input type="radio" name="bussinessCondition"  class="opa-radio" value="2"
                                           v-model="bussiness.settleCondition">起保日期
                                </li>
                            </ul>
                        </div>
                        <div class="add-item clearFix">
                            <span class="second-level-title">资金类型</span>
                            <ul class="second-level-box">
                                <li>
                                    <input type="radio" name="bussinessFundType" class="opa-radio" value="1"
                                           v-model="bussiness.fundType">现金
                                </li>
                            </ul>
                        </div>
                        <div class="add-item clearFix">
                            <span class="second-level-title">返点类型</span>
                            <ul class="second-level-box">
                                <li>
                                    <input type="radio" name="bussinessRebateType" class="opa-radio" value="1"
                                           v-model="bussiness.rebateType">比例
                                </li>
                            </ul>
                        </div>
                        <template v-for="(item, index) in bussiness.configBasicVOList">
                            <div class="add-item time-area" :key="item.settleConfigBasicId">
                                <div class="dispose-condition-item">
                                    <span class="margin-r-10">生效起止时间</span>
                                    <input type="text"  v-model="timeReport('YYYY-MM-DD', item.startTime)" disabled="disabled">
                                    <span class="margin-r-10">至</span>
                                    <input type="text" class="Wdate endTime" :disabled="index != bussiness.configBasicVOList.length - 1" v-model="item.endTime" @blur="saveTime" @change="saveTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" >
                                </div>
                                <div class="dispose-condition-item">
                                    <span>保费区间分组</span>
                                    <select v-model="item.settleConfigBasicId" @change="getConfig(2, item.settleConfigBasicId, index)">
                                        <option :value="area.id" v-for="area in filterSelect(bussinessSelect, bussiness.configBasicVOList)">{{area.groupName}}</option>
                                    </select>
                                </div>
                                <button class="operate-btn" @click="deleteConfig(index, 2)" v-if="index == bussiness.configBasicVOList.length - 1">-</button>
                                <button class="operate-btn" @click="addConfig(index, 2)" v-if="index == bussiness.configBasicVOList.length - 1">+</button>
                            </div>
                            <div class="add-item clearFix" :key="item.settleConfigBasicId">
                                <table class="mana-table add-table-style">
                                    <thead>
                                    <tr>
                                        <td>保费区间分组名</td>
                                        <td>保险公司</td>
                                        <td>险种</td>
                                        <td>保费区间</td>
                                        <td>比例</td>
                                    </tr>
                                    </thead>
                                    <tbody>
                                        <tr v-for="(area, i) of item.settleConfigBasicVO.itemBOList">
                                            <template v-if="i== 0">
                                                <td :rowspan="item.settleConfigBasicVO.itemBOList.length">{{item.settleConfigBasicVO.groupName}}</td>
                                                <td :rowspan="item.settleConfigBasicVO.itemBOList.length">{{item.settleConfigBasicVO.insuranceCompanyName}}</td>
                                                <td :rowspan="item.settleConfigBasicVO.itemBOList.length">{{item.settleConfigBasicVO.insuranceTypeName}}</td>
                                                <td>{{area.itemStartValue}}-{{area.itemEndValue||'∞'}}</td>
                                                <td>{{area.itemRate}}</td>
                                            </template>
                                            <template v-else>
                                                <td>{{area.itemStartValue}}-{{area.itemEndValue||'∞'}}</td>
                                                <td>{{area.itemRate}}</td>
                                            </template>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </template>
                    </div>
                </div>
            </div>
            <div class="insurance-box-outer">
                <div class="insurance-box-row">
                    <p class="insurance-title title-margin">
                        交强险
                        <input type="checkbox" class="opa-radio" value="true"
                               v-model="strong.show">
                    </p>
                    <div class="add-item-box" v-show="strong.show">
                        <#--<div class="add-item">-->
                            <#--<div class="select-condition-item">-->
                                <#--<span>计算方式</span>-->
                                <#--<select v-model="strong.calculateMode">-->
                                    <#--<option value="1">单笔</option>-->
                                    <#--<option value="2">月累计</option>-->
                                <#--</select>-->
                            <#--</div>-->
                        <#--</div>-->
                        <div class="add-item clearFix">
                            <span class="second-level-title">结算条件</span>
                            <ul class="second-level-box">
                                <li>
                                    <input type="radio" name="strongCondition"  class="opa-radio" value="1"
                                           v-model="strong.settleCondition">签单日期
                                </li>
                                <li>
                                    <input type="radio" name="strongCondition"  class="opa-radio" value="2"
                                           v-model="strong.settleCondition">起保日期
                                </li>
                            </ul>
                        </div>
                        <div class="add-item clearFix">
                            <span class="second-level-title">资金类型</span>
                            <ul class="second-level-box">
                                <li>
                                    <input type="radio" name="strongFundType" class="opa-radio" value="1"
                                           v-model="strong.fundType">现金
                                </li>
                            </ul>
                        </div>
                        <div class="add-item clearFix">
                            <span class="second-level-title">返点类型</span>
                            <ul class="second-level-box">
                                <li>
                                    <input type="radio" name="strongRebateType" class="opa-radio" value="1"
                                           v-model="strong.rebateType">比例
                                </li>
                            </ul>
                        </div>
                        <template v-for="(item, index) in strong.configBasicVOList" >
                            <div class="add-item time-area" :key="item.settleConfigBasicId">
                                <div class="dispose-condition-item">
                                    <span class="margin-r-10">生效起止时间</span>
                                    <input type="text" v-model="timeReport('YYYY-MM-DD', item.startTime)" disabled="disabled">
                                    <span class="margin-r-10">至</span>
                                    <input type="text" class="Wdate endTime" :disabled="index != strong.configBasicVOList.length - 1"  v-model="item.endTime" @blur="saveTime" @change="saveTime" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})">
                                </div>
                                <div class="dispose-condition-item">
                                    <span>保费区间分组</span>
                                    <select v-model="item.settleConfigBasicId" @change="getConfig(1, item.settleConfigBasicId, index)">
                                        <option :value="area.id" v-for="area in filterSelect(strongSelect, strong.configBasicVOList)">{{area.groupName}}</option>
                                    </select>
                                </div>
                                <button class="operate-btn" @click="deleteConfig(index, 1)" v-if="index == strong.configBasicVOList.length -1">-</button>
                                <button class="operate-btn" @click="addConfig(index, 1)" v-if="index == strong.configBasicVOList.length -1" >+</button>
                            </div>
                            <div class="add-item clearFix" :key="item.settleConfigBasicId">
                                <table class="mana-table add-table-style">
                                    <thead>
                                    <tr>
                                        <td>保费区间分组名</td>
                                        <td>保险公司</td>
                                        <td>险种</td>
                                        <td>保费区间</td>
                                        <td>比例</td>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr  v-for="(area, i) of item.settleConfigBasicVO.itemBOList">
                                        <template v-if="i== 0">
                                            <td :rowspan="item.settleConfigBasicVO.itemBOList.length">{{item.settleConfigBasicVO.groupName}}</td>
                                            <td :rowspan="item.settleConfigBasicVO.itemBOList.length">{{item.settleConfigBasicVO.insuranceCompanyName}}</td>
                                            <td :rowspan="item.settleConfigBasicVO.itemBOList.length">{{item.settleConfigBasicVO.insuranceTypeName}}</td>
                                            <td>{{area.itemStartValue}}-{{area.itemEndValue||'∞'}}</td>
                                            <td>{{area.itemRate}}</td>
                                        </template>
                                        <template v-else>
                                            <td>{{area.itemStartValue}}-{{area.itemEndValue||'∞'}}</td>
                                            <td>{{area.itemRate}}</td>
                                        </template>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </template>
                    </div>
                </div>
            </div>
        </div>
        <div class="btn-div">
            <button class="sure-button" @click="saveBill(ld.id? 1 : 2)">确定</button>
            <button class="cancel-button" @click="returnList">取消</button>
        </div>
    </div>
</div>
</div>
<script>
    window.vv = new Vue({
        el: '.common-vue',
        data: {
            start: false,
            backDetail: '',
            getDetailMark: false,
            provinceData: [],
            cityData: [],
            companyData: [],
            ld: {
                insuranceCompanyId: 0,
                insuranceCompanyName: '',
                cooperationMode: 2,
                applyRange: 1,
                regionConfigVOList: []
            },
            package: {
                show: false,
                startTime: '',
                endTime: '',
                fundType: 1,
                rebateType: 2
            },
            bussiness: {
                show: false,
                calculateMode: 1,
                configBasicVOList: [],
                fundType: 1,
                rebateType: 1
            },
            bussinessSelect: [],
            bussinessConfig: {
                
            },
            strong: {
                show: false,
                calculateMode: 1,
                configBasicVOList: [],
                fundType: 1,
                rebateType: 1
            },
            strongSelect: [],
            strongConfig: {
                
            },
            provinceChangeMark: false,
            allCityMark: false
        },
        mixins: [window.vueUtils],
        computed: {
            'statusStr': function() {
                return this.ld.id ? '详情': '新建';
            },
            'nowTime': function() {
                return this.timeReport('YYYY-MM-DD');
            }
        },
        watch: {
            'provinceChangeMark': function(n, o) {
                if(!n) return;
                var _this = this, data = {
                    regionParentCode: n,
                    insuranceCompanyId: this.ld.insuranceCompanyId,
                    regionLevel: 0,
                    cooperationMode: this.ld.cooperationMode,
                    settleRuleId: this.ld.id
                };
                this.allCityMark = false;
                this.getCity(data);
            },
            'allCityMark': function(n, o) {
                this.$set(this.ld, 'regionConfigVOList', n?JSON.parse(JSON.stringify(this.cityData)): []);
            },
            'companyInput': function(n, o) {
                var url = '/usShop/getShopListInfo';
                this.selectQuery(url, n, 'companyName');
            },

            'ld.insuranceCompanyId': function(n, o) {
                var _this = this, pData = {
                    regionParentCode: null,
                    insuranceCompanyId: n,
                    regionLevel: 0,
                    cooperationMode: n,
                    settleRuleId: this.ld.id
                }, cData = {
                    regionParentCode: this.provinceChangeMark,
                    insuranceCompanyId: n,
                    regionLevel: 0,
                    cooperationMode: n,
                    settleRuleId: this.ld.id
                };
                this.getProvince(pData);
                this.getCity(cData);
                this.ld.insuranceCompanyName = $('.insuranceCompanyId').text();
                this.getSettleInfo(n, this.ld.cooperationMode, 1, this.strong.calculateMode);
                this.getSettleInfo(n, this.ld.cooperationMode, 2, this.bussiness.calculateMode);
            },
            'ld.cooperationMode': function(n, o) {
                var _this = this, pData = {
                    regionParentCode: null,
                    insuranceCompanyId: this.ld.insuranceCompanyId,
                    regionLevel: 0,
                    cooperationMode: n,
                    settleRuleId: this.ld.id
                }, cData = {
                    regionParentCode: this.provinceChangeMark,
                    insuranceCompanyId: this.ld.insuranceCompanyId,
                    regionLevel: 0,
                    cooperationMode: n,
                    settleRuleId: this.ld.id
                };
                this.getProvince(pData);
                this.getCity(cData);
                if(!this.getDetailMark) {
                    this.$set(this.strong, 'configBasicVOList', []);
                    this.$set(this.bussiness, 'configBasicVOList', []);
                }

                this.$nextTick(function() {
                    this.getSettleInfo(this.ld.insuranceCompanyId, n, 1, this.strong.calculateMode);
                    this.getSettleInfo(this.ld.insuranceCompanyId, n, 2, this.bussiness.calculateMode);
                })
            },
            'strong.calculateMode': function(n, o) {
                this.$set(this.strong, 'configBasicVOList', []);
                this.$nextTick(function() {
                    this.getSettleInfo(this.ld.insuranceCompanyId, this.ld.cooperationMode, 1, n);
                })
            },
            'bussiness.calculateMode': function(n, o) {
                this.$set(this.bussiness, 'configBasicVOList', []);
                this.$nextTick(function() {
                    this.getSettleInfo(this.ld.insuranceCompanyId, this.ld.cooperationMode, 2, n);
                });
            }
        },
        methods: {
            init: function(id, back) {
                var _this = this, companyUrl = '/insurance/getAllCompanyList', detailUrl = '/settle/shopRule/getShopRuleById', xhr;
                if(id) this.getDetailMark = true;
                if(id) {
                    xhr = $.when(
                            Ajax.get({
                                url: companyUrl
                            }),
                            Ajax.get({
                                url: detailUrl,
                                data: {
                                    id: id
                                }
                            })
                    );
                } else {
                    xhr = Ajax.get({
                        url: companyUrl
                    });
                }
                xhr.done(function(r1, r2) {
                    var res = r1 && r1.length ? r1[0]: r1;
                    if(res.success) {
                        _this.companyData = res.data;
                        res.data.length ? _this.ld.insuranceCompanyId = res.data[0].id : '';
                    }
                    if(r2 && r2[0].success) {
                        _this.start = true;
                        _this.backDetail = r2[0].data;
                        _this._setDetail(r2[0].data);
                    }
                    _this.$nextTick(function() {
                        back();
                    })
                })
            },
            _setDetail: function(data) {
                var _this = this, ld = $.extend({}, _this.ld), pack = $.extend({}, _this.package),
                        strong = $.extend({}, _this.strong), bussiness = $.extend({}, _this.bussiness);
                ld.id = data.id;
                ld.insuranceCompanyId = data.insuranceCompanyId;
                ld.insuranceCompanyName = data.insuranceCompanyName;
                ld.applyRange = data.applyRange;
                ld.cooperationMode = data.cooperationMode;
                if(data.regionConfigVOList) {
                    ld.regionConfigVOList = data.regionConfigVOList;
                    _this.provinceChangeMark = data.regionConfigVOList[0].provinceCode;
                } else {
                    $('.storeInput').val(data.shopConfigVO.shopName);
                    ld.shopConfigVO = data.shopConfigVO;
                }
                data.rebateStandardVOs.forEach(function(v) {
                    switch(v.rebateStandard) {
                        case 1:
                            v.configBasicVOList.forEach(function (val) {
                                val.endTime = _this.timeReport('YYYY-MM-DD', val.endTime, true);
                                val.settleConfigBasicVO.itemBOList = $.extend([], val.settleConfigBasicVO.itemVOList);
                            });
                            delete strong.configBasicVOList;
                            $.extend(strong, v, {show: true});
                            break;
                        case 2:
                            v.configBasicVOList.forEach(function (val) {
                                val.endTime = _this.timeReport('YYYY-MM-DD', val.endTime, true);
                                val.settleConfigBasicVO.itemBOList = $.extend([], val.settleConfigBasicVO.itemVOList);
                            });
                            delete bussiness.configBasicVOList;
                            $.extend(bussiness, v, {show: true});
                            break;
                        case 3:
                            $.extend(pack, v, {show: true});
                            pack.startTime = v.configBasicVOList[0].startTime;
                            pack.endTime = v.configBasicVOList[0].endTime;
                            break;
                    }
                });
                _this.ld = ld, _this.strong = strong, _this.bussiness = bussiness, _this.package = pack;
                _this.$nextTick(function() {
                    this.getDetailMark = false;
                })
            },
            getAvailable: function(data, back) {
                var _this = this, url = '/settle/shopRule/getAvailableRegionList';
                Ajax.get({
                    type: 'get',
                    url: url,
                    data: data
                }).done(function(res) {
                    if(res.success) {
                        back && back(res.data);
                    }
                });
            },
            getProvince: function(data, back) {
                var _this = this;
                this.getAvailable(data, function(data) {
                    _this.provinceData = data;
                    _this.$nextTick(function() {
                        back && back(data);
                    })
                });
            },
            getCity: function(data, back) {
                var _this = this;
                this.getAvailable(data, function(data) {
                    _this.cityData = data.map(function(v) {
                        var a = {};
                        a.provinceCode = v.regionParentCode;
                        a.provinceName = $('.province :selected').text();
                        a.cityCode = v.regionCode;
                        a.cityName = v.regionName;
                        return a;
                    });
                    _this.$nextTick(function() {
                        back && back(data);
                    })
                })
            },
            getSettleInfo: function(companyId, model, type, calcType) {
                var _this = this, url = '/settle/getBasicBORedisList';
                if(!Array.prototype.every.call(arguments, function(v) {
                    return !!v? true: false;
                })) return ;
                Ajax.get({
                    url: url,
                    data: {
                        insuranceCompanyId: companyId,
                        cooperationMode: model,
                        insuranceType: type,
                        calculateMode: calcType
                    }
                }).done(function(res) {
                    if(res.success) {
                        if(!res.data) res.data = [];
                        if(type == 2) {
                            _this.bussinessSelect = res.data;
                            if(res.data.length && !_this.start) _this.pushConfigList(type, _this.bussiness.configBasicVOList, res.data[0].id);
                        } else {
                            _this.strongSelect = res.data;
                            if(res.data.length && !_this.start) _this.pushConfigList(type,_this.strong.configBasicVOList, res.data[0].id);
                        }
                    }
                })
            },
            pushConfigList: function(type, list, id) {
                var l = list.length, time = l? this.timeReport('YYYY-MM-DD', new Date(list[l-1].endTime).getTime() + 24 * 60 * 60 * 1000) : this.timeReport('YYYY-MM-DD');
                list.push({
                    settleConfigBasicId: id,
                    startTime: time,
                    endTime: '',
                    settleConfigBasicVO: {}
                });
                this.$nextTick(function() {
                    this.getConfig(type, id, l);
                })
            },
            getConfig: function(type, id, index) {
                var _this = this, url = '/settle/getSettleConfigById', cacheList = type == 2? this.strongConfig: this.bussinessConfig;
                if(cacheList[id]) {
                    return type == 2?
                            this.bussiness.configBasicVOList[index].settleConfigBasicVO =  cacheList[id] :
                            this.strong.configBasicVOList[index].settleConfigBasicVO = cacheList[id];
                }
                Ajax.get({
                    url:  url,
                    data: {
                        basicId: id
                    }
                }).done(function(res) {
                    if(res.success) {
                        if(type == 2) {
                            _this.bussiness.configBasicVOList[index].settleConfigBasicVO = _this.bussinessConfig[id] = res.data;
                        } else {
                            _this.strong.configBasicVOList[index].settleConfigBasicVO =  _this.strongConfig[id] = res.data;
                        }
                    }
                })
            },
            filterSelect: function(allList, list) {
                if(true) return allList;
                allList = JSON.parse(JSON.stringify(allList));
                return allList.filter(function(v1) {
                    if(list.every(function(v2) {
                        return v1.id == v2.settleConfigBasicId? false: true;
                    })) {
                        return true;
                    }
                });
            },
            deleteConfig: function(index, type) {
                var $lastList =  $(event.target).closest('.time-area').prevAll('.time-area');
                if(type == 2) {
                    this.bussiness.configBasicVOList.splice(index, 1);
                    if(index > 0) this.bussiness.configBasicVOList[index-1].endTime = '';
                } else {
                    this.strong.configBasicVOList.splice(index, 1);
                    if(index > 0) this.strong.configBasicVOList[index-1].endTime = '';
                }
            },
            addConfig: function(index, type) {
                var list = type == 2? this.bussiness.configBasicVOList : this.strong.configBasicVOList, id =
                        (type == 2?
                                this.bussiness.configBasicVOList :
                                this.strong.configBasicVOList
                        )[index].settleConfigBasicId, $endTime = $(event.target).closest('.add-item').find('.endTime');
                if(!list[index].endTime) return layer.tips('必须填入结束时间', $endTime);
                this.pushConfigList(type, list, id);
            },
            saveTime: function() {
                $(event.target).mouse({
                    type: 'input'
                })
            },
            _convert2bo: function(d) {
                var b = $.extend({}, this.bussiness), s = $.extend({}, this.strong), p = $.extend({}, this.package);
                p.show && (d.packageRuleItem = (delete p.show, p));
                b.show && (d.bizRuleItemList = b.configBasicVOList.map(function(v) {
                    return {
                        settleCondition: b.settleCondition,
                        fundType: b.fundType,
                        rebateType: b.rebateType,
                        calculateMode: b.calculateMode,
                        settleConfigBasicId: v.settleConfigBasicId,
                        startTime: v.startTime,
                        endTime: v.endTime
                    }
                }));
                s.show && (d.forceRuleItemList = s.configBasicVOList.map(function(v) {
                    return {
                        settleCondition: s.settleCondition,
                        fundType: s.fundType,
                        rebateType: s.rebateType,
                        calculateMode: s.calculateMode,
                        settleConfigBasicId: v.settleConfigBasicId,
                        startTime: v.startTime,
                        endTime: v.endTime
                    }
                }));
                d.applyRange == 1? delete d.shopConfigVO : delete d.regionConfigVOList;
                d.insuranceCompanyName = $('.insuranceCompanyId').text();
                return d;
            },
            saveBill: function(type) {
                var _this = this, url = type == 1? '/settle/shopRule/modifyShopRule' : '/settle/shopRule/addShopRule', d = this._convert2bo($.extend({}, this.ld));
                Ajax.postJson({
                    url: url,
                    data: d
                }).done(function(res) {
                    if(res.success) {
                        layer.msg(type ==1 ?'保存成功' : '新建成功', function() {
                            open('servicePackageList?menuId=30');
                        });
                    } else {
                        layer.msg(res.message);
                    }
                });
            },
            returnList: function() {
                open('servicePackageList?menuId=30', '_self');
            }
        },
        mounted: function() {
            this.$nextTick(function() {
                var _this = this, urlData = _this.parseUrl(), avaData;
                this.init(urlData['id'], function() {
                    _this.getProvince({
                        cooperationMode: _this.ld.cooperationMode,
                        insuranceCompanyId: _this.ld.insuranceCompanyId,
                        regionParentCode: null,
                        regionLevel: 0,
                        settleRuleId: _this.ld.id
                    }, function(data) {
                        _this.ld.id && _this._setDetail(_this.backDetail);
                        _this.$nextTick(function() {
                            _this.start = false;
                        })
                    });
                });
                $(document)
                    .on('focus', '.storeInput', function() {
                        if($(this).val()) $(this).trigger('input');
                    })
                    .on('input', '.storeInput', function(e) {
                            var str = $.trim($(this).val()), $area = $(this).next('.selectQueryArea');
                            if(!str.length) return _this.ld.shopConfigVO = {}, $area.empty().hide();
                            Ajax.get({
                                url: "/ucShop/getSimpleShopList",
                                data: {
                                    queryStr: str,
                                    cooperationMode: _this.ld.cooperationMode
                                },
                                success: function (result) {
                                    if (result.success) {
                                        $area.append(
                                            $('<ul class="selectQuery">').append(
                                                result.list.map(function(v) {
                                                    return $('<li>').data('d', v).text(v.companyName);
                                                })
                                            )
                                        ).show();
                                    } else {
                                        $area.empty().hide();
                                    }
                                }
                            })
                    })
                    .on('click', '.selectQuery>li', function (e) {
                        var $div =  $(e.target).closest('ul').parent(), $input = $div.prev(), d = $(e.target).data('d');
                        $div.empty().hide();
                        if($input.hasClass('storeInput')) {
                            $input.val(d.companyName);
                            _this.ld.shopConfigVO = {
                                shopId: d.id,
                                shopName: d.companyName,
                                shopAccount: d.defaultAccountMobile
                            }
                        }
                    })
                    .on('click', function(e) {
                        var $e = $(e.target);
                        if(!$e.hasClass('storeInput') && !$e.closest('.selectQuery').length) {
                            $('.selectQueryArea:visible').empty().hide();
                        }
                    })
            })
        }
    })
</script>
<#include "/mana/view/common/footer.ftl">