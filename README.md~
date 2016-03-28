# GangliaController
##Ganglia简介

Ganglia是一个用于分布式计算机网络的检测系统

Ganglia的组成：gmond,gmetad,gweb。以上三者彼此独立，可在缺少其他二者的情况下独立运作

Gmond:
gomnd安装在每一台需要监控的主机上，与操作系统交互以获取监控者所需的指标（包括cpu负载，io负载等等）。多台搭载gmond的主机可以组成集群（cluster），在默认条件下，集群内所有节点广播自己的监控信息并收集全网段监控信息，从而所有节点都持有整个集群的监控信息；监控者可以为每个gmond节点设置发送ip和接收ip，从而自定义网络拓扑结构

Gmetad：
Gmetad即ganglia系统中的轮询器。对于网络中的每个集群，仅向其中一个节点询问即可。同时一旦某一结点故障，访问其他节点亦可获取整个集群的信息。对于采集到的数据，gmetad使用轮询数据库（Round Robin Database）RRDtool存储。对于自定义的gmond拓扑结构，gmetad访问最上级gmond节点即可

Gweb：
Gweb即ganglia的可视化工具。Gweb无需设置即可访问网络中任意主机的任意指标数据；同时gweb可将整个集群的数据采用图标化的方式显示；gweb是一种php程序，一般而言部署在apache服务器上；由于需要与RRD交互，gweb通常与gmetad安装在相同的物理硬件上

![Ganglia png](https://github.com/EscapingChocolate/GangliaController/blob/master/Ganglia.png)

##GangliaController简介

![GangliaControllerFrame png](https://github.com/EscapingChocolate/GangliaController/blob/master/GangliaController.png)

GangliaController整体结构如上图所示
