var wsObj = null;
var wsUri = null;
var userId = -1;
var lockReconnect = false;
var wsCreateHandler = null;

function createWebSocket() {
    var host = window.location.host; // 带有端口号
    userId = GetQueryString("userId");
    wsUri = "ws://" + host + "/websocket/" + userId;

    try {
        wsObj = new WebSocket(wsUri);
        initWsEventHandle();
    } catch (e) {
        writeToScreen("执行关闭事件，开始重连");
		reconnect();
    }
}

function initWsEventHandle() {
    try {
        wsObj.onopen = function (evt) {
          heartCheck.reset().start();
            onWsOpen(evt);
        };

        wsObj.onmessage = function (evt) {
            heartCheck.reset().start();
            onWsMessage(evt);
        };

        wsObj.onclose = function (evt) {
            writeToScreen("执行关闭事件，开始重连");
            reconnect();
            onWsClose(evt);
        };
        wsObj.onerror = function (evt) {
            writeToScreen("执行error事件，开始重连");
            onWsError(evt);
        };
    } catch (e) {
        writeToScreen("绑定事件没有成功");
		reconnect();
    }
}

function onWsOpen(evt) {
    writeToScreen("CONNECTED");
}

function onWsClose(evt) {
    writeToScreen("DISCONNECTED");
}

function onWsError(evt) {
    writeToScreen(evt.data);
}

function writeToScreen(message) {
    if (DEBUG_FLAG) {
        $("#debuggerInfo").val($("#debuggerInfo").val() + "\n" + message);
    }
}

function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg); //获取url中"?"符后的字符串并正则匹配
    var context = "";
    if (r != null)
        context = r[2];
    reg = null;
    r = null;
    return context == null || context == "" || context == "undefined" ? "" : context;
}
// /*
// 设置重连
//  */
function reconnect() {
    if (lockReconnect) {
        return;
    }
    ;
    writeToScreen("1秒后重连");
    //没连接上会一直重连，设置延迟避免请求过多
    wsCreateHandler && clearTimeout(wsCreateHandler);
    wsCreateHandler = setTimeout(function () {
        writeToScreen("重连..." + wsUri);
        createWebSocket();
        lockReconnect = false;
        writeToScreen("完成重连");
    }, 1000);
}
//心跳检测
var heartCheck = {
    timeout: 1000,        //1分钟发一次心跳
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function(){
        clearTimeout(this.timeoutObj);
        clearTimeout(this.serverTimeoutObj);
        return this;
    },
    start: function(){
        var self = this;
        this.timeoutObj = setTimeout(function(){
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            wsObj.send("ping");
            console.log("ping!")
            self.serverTimeoutObj = setTimeout(function(){//如果超过一定时间还没重置，说明后端主动断开了
                ws.close();     //如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
            }, self.timeout)
        }, this.timeout)
    }
}



