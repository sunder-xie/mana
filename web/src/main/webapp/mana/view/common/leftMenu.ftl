<div class="mana-nav-left">
    <ul>
    <#--<li class="sidebar-toggler">-->
    <#--<div><i></i></div>-->
    <#--</li>-->

        <li class="home-menu used <#if menuId==null || menuId<1>selected</#if>" >
            <div><i class="home-icon"></i><a class="title" href="/home?menuId=0">主页</a><i class="current-icon hidden"></i></div>
        </li>
    <#--<li class="other-menu parentMenu down">-->
        <#--<i class="icon-diamond"></i><span class="title">菜单</span><i class="up-icon hidden"></i><i class="down-icon"></i>-->
    <#--</li>-->
    <#--<ul class="sub-menu hidden">-->
        <#--<li class="used <#if menuId==1>selected</#if>" >-->
            <#--<a href="/priceParity?menuId=1">-->
                <#--保险比价-->
            <#--</a>-->
            <#--<i class="current-icon hidden"></i>-->
        <#--</li>-->
        <#--<li class="used <#if menuId==2>selected</#if>" >-->
            <#--<a href="javascript:" onclick="specialMenu('/coupon?menuId=2');">优惠券发送列表</a><i class="current-icon hidden"></i>-->
        <#--</li>-->
    <#--</ul>-->
    <#--<li class="basicInfo-menu parentMenu down">-->
        <#--<i class="basicInfo-icon"></i><span class="title">基本资料</span><i class="up-icon hidden"></i><i class="down-icon"></i>-->
    <#--</li>-->
    <#--<ul class="sub-menu subBasicInfo-menu hidden">-->
        <#--<li class="used"><a href="/customer/addCustomerPage">新增车主信息</a><i class="current-icon hidden"></i></li>-->
    <#--</ul>-->
    <#--<li class="carOwnerManage-menu parentMenu down">-->
        <#--<i class="carOwner-icon"></i><span class="title">车主管理</span><i class="up-icon hidden"></i><i class="down-icon"></i>-->
    <#--</li>-->
    <#--<ul class="sub-menu subCarOwnerManage-menu hidden">-->
        <#--<li class="used"><a href="">业务信息列表</a><i class="current-icon hidden"></i></li>-->
    <#--</ul>-->

    <#if userMenuList??>
    <#list userMenuList as menu>
        <li class="parentMenu down">
            <i class="basicInfo-icon"></i><span class="title">${menu.resourceName}</span>
            <i class="fa Z-angle fa-angle-down"></i>
        </li>
        <ul class="sub-menu hidden">
            <#if menu.children??>
                <#list menu.children as subMenu>
                    <li class="used <#if subMenu.selected>selected</#if>" >
                        <a href="${subMenu.resourceUrl}?menuId=${subMenu.id}">${subMenu.resourceName}</a>
                        <i class="current-icon hidden"></i>
                    </li>
                </#list>
            </#if>
        </ul>
    </#list>
    </#if>

    </ul>
</div>
