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

![Ganglia png](https://github.com/EscapingChocolate/GangliaController/blob/master/pic/Ganglia.png)

##GangliaController简介
针对Ganglia系统仅能完成简单的数据采集及展现工作的问题，设计了GangliaController完善Ganglia系统，以实现自动化的对远程gmond的控制，分析实时数据进行相应处理等功能.

![GangliaControllerFrame png](https://github.com/EscapingChocolate/GangliaController/blob/master/pic/GangliaController.png)

GangliaController整体结构如上图所示

其中![Program png](https://github.com/EscapingChocolate/GangliaController/blob/master/pic/program.png)表示一个独立程序;![Module png](https://github.com/EscapingChocolate/GangliaController/blob/master/pic/Module.png)表示程序的一个模块;![doc png](https://github.com/EscapingChocolate/GangliaController/blob/master/pic/doc.png)表示一个文件目录;蓝色字体为Ganglia系统原有架构

###physical MonitorNode

在监测节点中运行两个进程:MonitorNode和gmond

gmond开启后，会根据conf.d中的配置文件定时执行监测指标采集脚本。

MonitorNode开启后，Download模块轮询SettingsDistribute_Module以获取实时配置信息;Control模块得到实时配置信息后，从Dealt中调用相应的方法进行处理:例如获取到的信息中含有"Enable somemetric",则会从conf.a中将相应metric配置文件mv至conf.d中，重启gmond.

###physical SummaryNode

在汇总节点中包含三个进程:gmetad,SummaryNode,SettingsDistribute_Module

gmetad开启后，会定时轮询指定的cluster，获取整个cluster中所有节点的实时监测数据

SummaryNode开启后，会读取HostsConfig中所有节点处理规则配置文件，生成并维护每个节点的Host对象；Control_Module会定时调用DataReader获取实时监测数据，并将数据交由对应的Host对象处理；Host对象获取实时数据后，会依照配置规则调用Dealt_Module中相应的处理方式(尚未完成)，并/或生成新的节点配置更新信息文件，存储于SettingsOutput路径

SettingsDistribute_Module运行于Tomcat容器，可以相应MonitorNode的GET请求，根据uri在SettingsOutput中读取对应的配置更新文件，以Json格式返回(访问权限问题尚未落实).


    hostConfig有效结构：
    {
        “HOST_NAME":"<name>",
        "METRICS":ff
            [
                (metric)
                {
                    "METRIC_NAME":"<name>",
                    "SECTIONS":
                        [
                            (section)
                            {
                                "SECTION_ID":"<sectionID>"
                                "DOMAINS":
                                    [
                                        (domain)
                                        {"MAX":"<max>","MIN":"<min>"},
                                        ......
                                    ]
                                "ACTIONS":
                                [
                                    (action)
                                    {
                                        "ACTION_ID":"<action_ID>"
                                        "ACTION_EVERY":"<action_every>"(if <action_every> == -1 ,action only once)
                                        (if "ACTION_TYPE"=="ALARM")
                                        {
                                            "ACTION_TYPE":"ALARM",

                                        }
                                        (if "ACTION_TYPE"=="SETTINGS_ALTER")
                                        {
                                            "ACTION_TYPE"=="SETTINGS_ALTER",
                                            "SETTINGS":
                                                [
                                                    (setting)
                                                    {
                                                        "METRIC_NAME":"<metric_name>",
                                                        "ACT":"ENABLE|DISABLE",
                                                        "HOST_NAME":"<host_name>"
                                                        "PARAM":"<param>"
                                                    }
                                                    ......
                                                ]
                                        }
                                    },
                                    ......
                                ]
                            }
                            ......
                        ]
                }
                ,
                (metric)
                ......
            ]
    }
    (大括号域内为JSONObject，中括号域内为JSONArray；小括号内为Array的元素，无意义）
    



aaaaaaa