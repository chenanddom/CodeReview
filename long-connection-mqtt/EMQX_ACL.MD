# EMQX的ACL插件
EMQX支持使用配置文件，外部组合六书库和自定义的HTTP API作为ACL数据源

连接数据源，进行访问控制功能是通过插件实现的，使用前需要启用相应的插件。

客户端订阅主题、发布消息是插件通过检查目标主题(TOPIC)是否在指定数据源
允许/禁止列表内来实现对客户端的发布，订阅管权限管理。



* ACL配置的类型
    * 配置文件
        * 内置ACL
    * 外部数据库
        * MySQL ACL
        * PostgreSQL ACL
        * Redis ACL
        * MongoDB ACL
        外部数据库可以存储大量数据、动态管理 ACL，方便与外部设备管理系统集成。
    
    * 其他
        * HTTP ACL
        
            HTTP ACL能够实现复杂的ACL管理。      
            
            
* ACL规则详解
ACL 是允许与拒绝条件的集合，EMQ X 中使用以下元素描述 ACL 规则：
```shell script
## Allow-Deny Who Pub-Sub Topic
"允许(Allow)/拒绝(Deny)" "谁(Who)" "订阅(Subscribe)/发布(Publish)" "主题列表(Topics)"
```              

同时具有多条ACL规则时，EMQX讲按照规则排序进行合并，以ACL文件中的默认ACL为例，ACL文件中配置了默认的ACL规则明规则
规则从下至上的加载:
    1. 第一条规则允许客户端发布订阅所有的主题
    2. 第二条规则禁止全部客户端订阅$SYS/#与#主题
    3. 第三条规则允许ip地址为127.0.0.1的客户端发布订阅$SYS/#与#主题，为第二条开了特里
    4. 第四条规则允许用户为dashboard的客户端订阅$SYS/#主题，为第二条开了特例。

* 授权结果
任何一次ACL授权最终都会返回一个结果
    * 允许:经过检查允许客户端进行操作
    * 禁止:经过检查禁止客户端操作
    * 忽略(ignore):尾插找到ACL权限信息(no match)，无法显式判断结果时允许还是禁止，交友下一个ACL插件或者默认ACL规则来判断。
      
      
 
### ACL全局配置
/etc/emqx/emqx.conf
```shell script
## Allow or deny if no ACL rules matched.
##
## Value: allow | deny
#如果不匹配规则，默认是是让通过的，
acl_nomatch = allow

## Value: File Name
#ACL默认配置规则
acl_file = /etc/emqx/acl.conf
## Whether to enable ACL cache.
##
## If enabled, ACLs roles for each client will be cached in the memory
##
## Value: on | off
#开启acl校验的缓冲区，提高emqx服务器的性能
enable_acl_cache = on

## The maximum count of ACL entries can be cached for a client.
##
## Value: Integer greater than 0
## Default: 32
#最大的缓冲acl规则的条数
acl_cache_max_size = 32

## The time after which an ACL cache entry will be deleted
##
## Value: Duration
## Default: 1 minute
# 缓存acl规则的时长
acl_cache_ttl = 1m

## The action when acl check reject current operation
##
## Value: ignore | disconnect
## Default: ignore
#如果规则不匹配，那么mqtt服务会做出的反映，mqtt3.1是忽略(此处是缺陷)，但是mqtt5.0会做出错误的提示
acl_deny_action = ignore


``` 
acl规则的配置文件
/etc/emqx/acl.conf
```shell script
[root@kafka-node4 emqx]# vim acl.conf 

%%--------------------------------------------------------------------
%% [ACL](https://docs.emqx.io/broker/v3/en/config.html)
%%
%% -type(who() :: all | binary() |
%%                {ipaddr, esockd_access:cidr()} |
%%                {client, binary()} |
%%                {user, binary()}).
%%
%% -type(access() :: subscribe | publish | pubsub).
%%
%% -type(topic() :: binary()).
%%
%% -type(rule() :: {allow, all} |
%%                 {allow, who(), access(), list(topic())} |
%%                 {deny, all} |
%%                 {deny, who(), access(), list(topic())}).
%%--------------------------------------------------------------------

{allow, {user, "dashboard"}, subscribe, ["$SYS/#"]}.

{allow, {ipaddr, "127.0.0.1"}, pubsub, ["$SYS/#", "#"]}.

{deny, all, subscribe, ["$SYS/#", {eq, "#"}]}.

{allow, all}.

```
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
       
    
    
    
    
