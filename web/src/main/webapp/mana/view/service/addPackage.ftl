<#--车险服务包管理新建服务包界面-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/service/addPackage.css" type="text/css" rel="stylesheet">
<div class="">
    <p class="p-title">车险服务包 > 服务包管理 > 新建服务包</p>
    <div class="search-box">
        <button class="sure-button">提交</button>
        <button class="cancel-button">取消</button>
    </div>
    <div class="add-box">
        <div class="div-left float-left">
            <div class="div-title">服务包基础信息</div>
            <ul class="div-left-ul">
                <li>
                    <span class="must-mark">服务包名称</span>
                    <input type="text" name="packageName" maxlength="20" placeholder="请输入"/>
                </li>
                <li>
                    <span class="must-mark">服务包市场价</span>
                    <input type="text" name="packagePrice" placeholder="请输入"/>
                </li>
                <li>
                    <span class="must-mark">适用保费区间</span>
                    <input type="text" name="minFee" class="div-left-input" placeholder="最低保费"/>
                    <label class="label-text-color"><=</label>
                    <label>保费</label>
                    <label class="label-text-color"><</label>
                    <input type="text" name="maxFee" class="div-left-input" placeholder="最高保费"/>
                </li>
                <li>
                    <span class="must-mark">机油用量</span>
                    <input type="text" name="oilAmount" maxlength="10" placeholder="请输入"/>
                </li>
                <#--<li>-->
                    <#--<span>机油商品编码</span>-->
                    <#--<input type="text" name="oilGoodsSn" maxlength="20" placeholder="请输入"/>-->
                <#--</li>-->
                <li>
                    <span class="must-mark">门店可得返利</span>
                    <input type="text" name="rewardAmount" placeholder="请输入"/>
                </li>
                <li>
                    <span class="must-mark">上架状态</span>
                    <input type="checkbox" checked class="div-left-checkbox" name="shelfStatus" value="1"/>
                    <i>上架</i>
                    <input type="checkbox" class="div-left-checkbox" name="shelfStatus" value="0"/>
                    <i>下架</i>
                </li>
                <li>
                    <span class="must-mark">服务项目</span>
                    <span class="li-title">项目名称</span>
                    <div class="item-select-box">
                        <select class="item-select"></select>
                    </div>
                </li>
                <li>
                    <span></span>
                    <span class="li-title">项目次数</span>
                    <button class="subtract-one-button">-</button>
                    <input type="text" class="num-input" value="1"/>
                    <button class="add-one-button">+</button>
                </li>
                <li>
                    <span></span>
                    <button class="save-button">添加</button>
                </li>
            </ul>
        </div>
        <div class="div-right float-right">
            <div class="div-right-box">
                <div>
                    <p class="div-title must-mark">服务包描述</p>
                    <div class="inner-box">
                    <#--页面编辑器-->
                        <script id="packageDescription" class="ueditor-outer"></script>
                    </div>
                </div>
            </div>
            <div class="div-right-box">
                <div>
                    <p class="div-title">营销提示语</p>
                    <div class="inner-box">
                    <#--页面编辑器-->
                        <script id="marketTip" class="ueditor-outer"></script>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script id="manaTableParentTemplate" type="text/html">
    <li>
        <span></span>
        <div class="item-box">
        <div class="div-title">已填项目</div>
            <table class="mana-table"></table>
        </div>
    </li>
</script>
<script id="manaTableTemplate" type="text/html">
    <tr>
        <td>
            <a href="/insuranceItem/editItemInit?id={{itemId}}" target="_blank" class="js-item-name" data-id="{{itemId}}">{{itemName}}</a>
        </td>
        <td class="js-item-num">{{itemNum}}</td>
        <td>
            <a href="javascript:void 0;" class="js-delete">删除</a>
        </td>
    </tr>
</script>
<!-- 配置文件 -->
<script type="text/javascript" src="/static/ueditor/ueditor.config1.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="/static/ueditor/ueditor.all.js"></script>

<script type="text/javascript" src="/mana/js/service/addPackage.js"></script>
<#include "/mana/view/common/footer.ftl">

