<#--基础资料新增组界面-->
<#include "/mana/view/common/header.ftl">
<link href="/mana/css/service/addItem.css" type="text/css" rel="stylesheet">
<input type="hidden" id="itemId" value="${itemId}">
<div class="">
    <p class="p-title">车险服务包 > 服务项目管理 > 编辑服务项目</p>
    <div class="search-box">
        <button class="sure-button">提交</button>
        <button class="cancel-button">取消</button>
    </div>
    <div class="add-box">
        <div class="div-left float-left">
            <div class="div-left-box">
                <div class="div-title">项目基础信息</div>
                <ul class="div-left-ul js-top-ul">
                    <li>
                        <span class="must-mark">项目名称</span>
                        <input type="text" name="itemName" placeholder="请输入,最多20个字" class="input-width" maxlength="20"
                               value="${insuranceItemBO.itemName}"/>
                    </li>
                    <li>
                        <span class="must-mark">项目市场单价</span>
                        <input type="text" name="itemPrice" placeholder="请输入" value="${insuranceItemBO.itemPrice}"/>
                    </li>
                    <li>
                        <span class="must-mark">项目单位</span>
                        <input type="text" name="itemUnit" maxlength="20" class="input-width" placeholder="请输入"
                               value="${insuranceItemBO.itemUnit}"/>
                    </li>
                    <li>
                        <span class="must-mark">项目物料费</span>
                        <input type="text" name="itemMaterialPrice" placeholder="请输入"
                               value="${insuranceItemBO.itemMaterialPrice}"/>
                    </li>
                    <li>
                        <span class="must-mark">项目工时费</span>
                        <input type="text" name="itemWorkFee" placeholder="请输入"
                               value="${insuranceItemBO.itemWorkPrice}"/>
                    </li>
                    <li>
                        <span class="must-mark">购买次数范围</span>
                        <input type="text" name="buyMinTime" class="div-left-input" value="${insuranceItemBO.buyMinNum}"
                               placeholder="最低次数"/>
                        <label> - </label>
                        <input type="text" name="buyMaxTime" class="div-left-input" value="${insuranceItemBO.buyMaxNum}"
                               placeholder="最高次数"/>
                    </li>
                    <li>
                        <span class="must-mark">上架状态</span>
                        <input type="checkbox" <#if insuranceItemBO.itemStatus == 1>checked</#if>
                               class="div-left-checkbox" name="shelfStatus" value="1"/>
                        <i>上架</i>
                        <input type="checkbox" <#if insuranceItemBO.itemStatus == 0>checked</#if>
                               class="div-left-checkbox" name="shelfStatus" value="0"/>
                        <i>下架</i>
                    </li>
                    <li>
                    <span>
                        <label>物料商品</label>
                    </span>
                        <input type="checkbox" class="div-left-checkbox" name="matchModel" value=""/>
                        <i>商品型号需匹配车型</i>
                    </li>
                    <li>
                        <span></span>
                        <span class="li-title js-li-title">商品编码</span>
                        <select class="select-width display-none">
                            <option selected value="0">其他</option>
                            <option value="1">机滤</option>
                        </select>
                        <input type="text" class="goods-input js-code-input"/>
                        <div class="mdsug"></div>
                    </li>
                    <li>
                        <span></span>
                        <span class="li-title">商品单价</span>
                        <input type="text" class="goods-input js-price-input"/>
                    </li>
                    <li>
                        <span></span>
                        <span class="li-title">商品数量</span>
                        <button class="subtract-one-button">-</button>
                        <input type="text" class="num-input" value="0.5"/>
                        <button class="add-one-button">+</button>
                        <input type="checkbox" checked class="div-left-checkbox" name="multiplyItem"/>
                        <label>商品数量需乘以项目次数</label>
                    </li>
                    <li>
                        <span></span>
                        <button class="save-button js-save-goods">添加</button>
                    </li>
                <#if (insuranceItemBO.materialTemplateParams?size > 0)>
                    <li>
                        <span></span>
                        <div class="item-box">
                            <div class="div-title">已填商品</div>
                            <table class="mana-table">
                                <#list insuranceItemBO.materialTemplateParams as goods>
                                    <tr>
                                        <td>
                                            <#if goods.goodsSn ??>
                                                <a href="javascript:void 0;" class="js-goodsSn">${goods.goodsSn}</a>
                                            <#else>
                                                <#switch goods.materialType>
                                                    <#case 0>
                                                        <a href="javascript:void 0;" class="js-goodsSn"
                                                           data-type="0">其他</a><#break>
                                                    <#case 1>
                                                        <a href="javascript:void 0;" class="js-goodsSn"
                                                           data-type="1">机滤</a><#break>
                                                </#switch>
                                            </#if>
                                        </td>
                                        <td class="js-material-price">${goods.materialPrice}</td>
                                        <td class="js-goods-num">${goods.goodsNum}</td>
                                        <#if goods.goodsSn??>
                                            <td class="js-match-model" data-match="0">不需匹配车型</td>
                                        <#else>
                                            <td class="js-match-model" data-match="1" data-type="${goods.materialType}">
                                                需匹配车型
                                            </td>
                                        </#if>
                                        <td class="js-multiply-item"
                                            data-multiply="${goods.needMultiplyItemNums?string("1","0")}">
                                            <#if goods.needMultiplyItemNums == true>
                                                需乘以项目次数
                                            <#else>
                                                不乘以项目次数
                                            </#if>
                                        <td>
                                            <a href="javascript:void 0;" data-id="${goods.id}" class="js-goods-delete">删除</a>
                                        </td>
                                    </tr>
                                </#list>
                            </table>
                        </div>
                    </li>
                </#if>
                </ul>
            </div>
            <div>
                <div class="div-right-box background-white">
                    <div class="div-title">项目培训信息</div>
                    <ul class="div-left-ul js-down-ul">
                        <li>
                            <span>视频名称</span>
                            <input type="text" class="down-ul-input js-video-name" maxlength="30" placeholder="请输入"/>
                        </li>
                        <li>
                            <span>视频地址</span>
                            <input type="text" class="down-ul-input js-video-address" maxlength="80" placeholder="请输入"/>
                        </li>
                        <li>
                            <span></span>
                            <button class="save-button js-save-video">添加</button>
                        </li>
                    <#if (insuranceItemBO.serviceItemVideoParams?size > 0)>
                        <li>
                            <span></span>
                            <div class="item-box">
                                <div class="div-title">已填视频</div>
                                <table class="mana-table">
                                    <#list insuranceItemBO.serviceItemVideoParams as video>
                                        <tr>
                                            <td>
                                                <a href="${video.videoTutorial}" target="_blank"
                                                   class="js-video-name">${video.videoName}</a>
                                            </td>
                                            <td>
                                                <a href="javascript:void 0;" style="text-decoration: none;color: #0C0C0C"
                                                   class="js-video-address">${video.videoTutorial}</a>
                                            </td>
                                            <td>
                                                <a href="javascript:void 0;" data-id="${video.id}"
                                                   class="js-video-delete">删除</a>
                                            </td>
                                        </tr>
                                    </#list>
                                </table>
                            </div>
                        </li>
                    </#if>
                    </ul>
                </div>
            </div>
        </div>
        <div class="div-right float-right">
            <div class="div-right-box">
                <div>
                    <p class="div-title">适用车型描述</p>
                    <div class="inner-box">
                    <#--页面编辑器-->
                        <script id="suitableModelDescription" class="ueditor-outer" type="text/html">${insuranceItemBO.itemCarModel}</script>
                    </div>
                </div>
            </div>
            <div class="div-right-box">
                <div>
                    <p class="div-title">内容与工序描述</p>
                    <div class="inner-box">
                    <#--页面编辑器-->
                        <script id="contentDescription" class="ueditor-outer" type="text/html">${insuranceItemBO.itemContentProcedure}</script>
                    </div>
                </div>
            </div>
            <div class="div-right-box">
                <div>
                    <p class="div-title">作用与优点描述</p>
                    <div class="inner-box">
                    <#--页面编辑器-->
                        <script id="workDescription" class="ueditor-outer" type="text/html">
                                ${insuranceItemBO.itemActionMerit}
                        </script>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script id="goodsTableTemplate" type="text/html">
    <li>
        <span></span>
        <div class="item-box">
            <div class="div-title">已填商品</div>
            <table class="mana-table"></table>
        </div>
    </li>
</script>
<script id="goodsTableTrTemplate" type="text/html">
    <tr>
        <td>
            {{if goods.goodsSn == null}}
            <a href="javascript:void 0;" class="js-goodsSn" data-type="{{goods.materialType}}">{{goods.materialTypeName}}</a>
            {{else}}
            <a href="javascript:void 0;" class="js-goodsSn">{{goods.goodsSn}}</a>
            {{/if}}
        </td>
        <td class="js-material-price">{{goods.materialPrice}}</td>
        <td class="js-goods-num">{{goods.goodsNum}}</td>
        {{if goods.goodsSn == null}}
        <td class="js-match-model" data-match="1">需匹配车型</td>
        {{else}}
        <td class="js-match-model" data-match="0">不需匹配车型</td>
        {{/if}}
        <td class="js-multiply-item" data-multiply="{{goods.needMultiplyItemNums}}">
            {{if goods.needMultiplyItemNums == 1}}
            需乘以项目次数
            {{else}}
            不乘以项目次数
            {{/if}}
        <td>
            <a href="javascript:void 0;" class="js-goods-delete">删除</a>
        </td>
    </tr>
</script>
<script id="videoTableTemplate" type="text/html">
    <li>
        <span></span>
        <div class="item-box">
            <div class="div-title">已填视频</div>
            <table class="mana-table">
            </table>
        </div>
    </li>
</script>
<script id="videoTableTrTemplate" type="text/html">
    <tr>
        <td>
            <a href="{{videoAddress}}" target="_blank" class="js-video-name">{{videoName}}</a>
        </td>
        <td>
            <a href="javascript:void 0;" class="js-video-address">{{videoAddress}}</a>
        </td>
        <td>
            <a href="javascript:void 0;" class="js-video-delete">删除</a>
        </td>
    </tr>
</script>
<!-- 配置文件 -->
<script type="text/javascript" src="/static/ueditor/ueditor.config1.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="/static/ueditor/ueditor.all.js"></script>

<script type="text/javascript" src="/mana/js/service/editItem.js"></script>
<#include "/mana/view/common/footer.ftl">

