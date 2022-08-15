# EMQX的安装和配置
rpm安装
准备rpm安装包

```shell script
# 安装的时候由于确认openssl的依赖库，所以报错
[root@kafka-node4 emqx]# rpm -ivh emqx-centos7-v4.0.5.x86_64.rpm 
error: Failed dependencies:
	libcrypto.so.10(OPENSSL_1.0.2)(64bit) is needed by emqx-4.0.5-1.el7.x86_64
[root@kafka-node4 emqx]# yum install http://mirror.centos.org/centos/7/updates/x86_64/Packages/openssl-libs-1.0.2k-25.el7_9.x86_64.rpm
[root@kafka-node4 emqx]# rpm -ivh emqx-centos7-v4.0.5.x86_64.rpm 
Preparing...                          ################################# [100%]
Updating / installing...
   1:emqx-4.0.5-1.el7                 ################################# [100%]
Created symlink from /etc/systemd/system/multi-user.target.wants/emqx.service to /usr/lib/systemd/system/emqx.service.
[root@kafka-node4 emqx]# emqx start
EMQ X Broker v4.0.5 is started successfully!

```
![](./files/emqx-install-success-1.PNG)





## 配置

是否允许匿名登录，默认true是允许的,false就表示不允许
allow_anonymous = false




