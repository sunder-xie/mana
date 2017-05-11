<#--基础资料新增组界面-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/settle/settleAdd.css" type="text/css" rel="stylesheet">
<div class="">
    <p class="p-title">基础管理 > 返点比例配置</p>
    <div class="layout-div">
        <div class="inner-box clearFix">
            <div class="insurance-box">
                <input type="hidden" id="id" value="${id}">
                <div class="add-item">
                    <div class="select-div">
                        <span class="select-span-title">保险公司</span>
                        <select id="insuranceCompany">
                            <#list allCompanyList as item>
                                <option value="${item.id}">${item.companyName}</option>
                            </#list>
                        </select>
                    </div>
                </div>
                <div class="add-item">
                    <div class="select-div">
                        <span class="select-span-title">省份</span>
                        <select id="province">
                            <option>请选择</option>
                        </select>
                    </div>
                </div>
                <div class="add-item" id="cityArea">
                    <div id="checkAll">
                        <input type="checkbox" class="opa-check checkAll" > 全选
                    </div>
                    <div id="cityList">
                    </div>
                </div>
                <div class="add-item">
                    <div>
                        <span>投保场景:</span>
                        <span>单保交强险</span>
                    </div>
                    <div>
                        <span>计算基数:</span>
                        <span>交强险</span>
                        <div class="scenario-div">
                            <label>比例</label>
                            <input type="text" placeholder="0.00" class="scenario scenario-input" data-scenario="3" data-insuranceType="1">
                        </div>
                    </div>
                </div>
                <div class="add-item">
                    <div>
                        <span>投保场景:</span>
                        <span>单保商业险</span>
                    </div>
                    <div>
                        <span>计算基数:</span>
                        <span>商业险</span>
                        <div class="scenario-div">
                            <label>比例</label>
                            <input type="text" placeholder="0.00" class="scenario scenario-input" data-scenario="1" data-insuranceType="2">
                        </div>
                    </div>
                </div>
                <div class="add-item">
                    <div>
                        <span>投保场景:</span>
                        <span>交强险+商业险</span>
                    </div>
                    <div>
                        <span>计算基数:</span>
                        <span>商业险</span>
                        <div class="scenario-div">
                            <label>比例</label>
                            <input type="text" placeholder="0.00" class="scenario scenario-input" data-scenario="2" data-insuranceType="2">
                        </div>
                    </div>
                    <div>
                        <span>计算基数:</span>
                        <span>交强险</span>
                        <div class="scenario-div">
                            <label>比例</label>
                            <input type="text" placeholder="0.00" class="scenario scenario-input" data-scenario="2" data-insuranceType="1">
                        </div>
                    </div>
                </div>
                <div class="add-item">
                    <div>
                        <span>投保场景:</span>
                        <span>单保第三方责任险</span>
                    </div>
                    <div>
                        <span>计算基数:</span>
                        <span>商业险</span>
                        <div class="scenario-div">
                            <label>比例</label>
                            <input type="text" placeholder="0.00" class="scenario scenario-input" data-scenario="5" data-insuranceType="2">
                        </div>
                    </div>
                </div>
                <div class="add-item">
                    <div>
                        <span>投保场景:</span>
                        <span>第三方责任险+交强险</span>
                    </div>
                    <div>
                        <span>计算基数:</span>
                        <span>商业险</span>
                        <div class="scenario-div">
                            <label>比例</label>
                            <input type="text" placeholder="0.00" class="scenario scenario-input" data-scenario="4" data-insuranceType="2">
                        </div>
                    </div>
                    <div>
                        <span>计算基数:</span>
                        <span>交强险</span>
                        <div class="scenario-div">
                            <label>比例</label>
                            <input type="text" placeholder="0.00" class="scenario scenario-input" data-scenario="4" data-insuranceType="1">
                        </div>
                    </div>
                </div>
                <div class="add-item">
                    <div>
                        <span>投保场景:</span>
                        <span>第三方责任+车上人员责任险（司机或乘客）</span>
                    </div>
                    <div>
                        <span>计算基数:</span>
                        <span>商业险</span>
                        <div class="scenario-div">
                            <label>比例</label>
                            <input type="text" placeholder="0.00" class="scenario scenario-input" data-scenario="6" data-insuranceType="2">
                        </div>
                    </div>
                </div>
                <div class="add-item">
                    <div>
                        <span>投保场景:</span>
                        <span>第三方责任+车上人员责任险（司机或乘客）+交强险</span>
                    </div>
                    <div>
                        <span>计算基数:</span>
                        <span>商业险</span>
                        <div class="scenario-div">
                            <label>比例</label>
                            <input type="text" placeholder="0.00" class="scenario scenario-input" data-scenario="7" data-insuranceType="2">
                        </div>
                    </div>
                    <div>
                        <span>计算基数:</span>
                        <span>交强险</span>
                        <div class="scenario-div">
                            <label>比例</label>
                            <input type="text" placeholder="0.00" class="scenario scenario-input" data-scenario="7" data-insuranceType="1">
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="btn-div">
            <button class="sure-button">确定</button>
            <button class="cancel-button cancel">取消</button>
        </div>
    </div>
</div>
<script type="text/javascript" src="/mana/js/settle/insuranceCompanyRulePage.js"></script>
<#include "/mana/view/common/footer.ftl">