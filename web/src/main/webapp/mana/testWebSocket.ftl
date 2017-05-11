<#include "/mana/view/common/header.ftl">

<h2>你愁啥 (#‵′)凸</h2>
<h2>瞅你咋地 <(￣3￣)></h2>

<br>
<br>

<div>
    <div>
        <button id="connect" onclick="connect();">Connect</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button>
    </div>
    <div id="conversationDiv">
        <p>
            <label>notice content?</label>
        </p>
        <p>
            <textarea id="name" rows="5"></textarea>
        </p>
        <button id="sendName" onclick="sendName();">Send</button>
        <p id="response"></p>
    </div>
</div>

<#include "/mana/view/common/footer.ftl">

<script src="//cdn.bootcss.com/sockjs-client/1.1.1/sockjs.min.js"></script>
<script src="//cdn.bootcss.com/stomp.js/2.3.3/stomp.min.js"></script>
<script>
    var stompClient = null;
    function setConnected(connected) {
        document.getElementById('connect').disabled = connected;
        document.getElementById('disconnect').disabled = !connected;
        document.getElementById('conversationDiv').style.visibility = connected ? 'visible' : 'hidden';
        document.getElementById('response').innerHTML = '';
    }
    // 开启socket连接
    function connect() {
        var socket = new SockJS('/socket');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            setConnected(true);
        });
    }
    // 断开socket连接
    function disconnect() {
        if (stompClient != null) {
            stompClient.disconnect();
        }
        setConnected(false);
        console.log("Disconnected");
    }
    // 向‘/app/change-notice’服务端发送消息
    function sendName() {
        var value = document.getElementById('name').value;
        stompClient.send("/app/change-notice", {}, value);
    }

//    connect();

</script>
