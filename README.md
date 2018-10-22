# kit
java精华工具类集锦：

- anno 通用注解支持类
- audio 音频播放器，例如播放mp3
- bytes 字节工具类：字节数组转二进制、字节数组转十六进制、十六进制转字节数组、字节转换字符串
- cache 缓存工具：内存型缓存、文件型缓存、memcache缓存工具、redis缓存工具
- charset 字符集工具，获取任意文本文件的字符编码
- common.model 通用型RESTFUL接口返回对象
- compress 字节压缩和解压缩工具
- controllerpattern Controller模式框架的通用型脚手架，包扫描注入模式的利器
- converter 通用对象值转换器，可将字符串值类型转换成其他常用值类型（boolean,double,float,long,int,byte,short等）
- datastructure 二元组（Pair）、三元组（Triple）对象容器，当你想同时返回两个或三个对象，但又不想新建一个对象容器的时候，可以使用这两个通用型容器
- date 日期工具包。日期计算、转换、求时间差、获取当前时间的所有信息
- db 数据库分页工具类
- describe 一个对象描述工具，更好地toString()
- document pdf生成器
- email 电子邮件工具，可方便地发送带邮件以及附件的邮件
- excel  excel数据读取、导出为excel、excel跨浏览器兼容下载工具类
- executor 一个强大的线程池工具。  
无限制线程池、单任务线程池、指定任务数线程池、流量可控制的线程池、
sqlite单线程写线程池、swing调度线程池、
可运行定时任务的线程池、forkJoin运行池、启动一个后台线程工具、任务管理器
- file 文件工具。检查配置目录、获取全平台配置目录、重复文件清理（文件夹内文件根据hash值去重）、从txt文件读取格式化数据
- filepack 文件打包器&解包器支持。可将一系列文件打包压缩成一个单独的文件，支持5种内置的打包类型  
	支持文件名的打包器  
	支持内容gzip压缩的打包解包器（用于减少包体积）  
	支持内容对称加密的打包解包器（用于加密包数据）  
	支持内容非对称加密的打包解包器（用于强化加密数据）  
	支持层级结构的打包解包器（对文件名进行扩展，支持路径结构）  
- freemarker Freemarker模板配置工具、Freemarker模板格式化输出工具
- gson 强大的json与对象互转工具，json美化格式化输出
- handlerseq 一个任务串行化（顺序）处理工具
- http 本机IP、域名、MAC地址获取，查找本机的空闲端口工具类，URL编解码，URL参数解析、URL参数提取、去参数，文件下载，GET、POST请求工具，系统代理设置工具
- io 优雅地关闭任意对象的工具类
- javac.in.memory 内存内代码编译器，热编译Java代码并执行
- job
    JobInitServlet 简单的servlet形式的job执行引擎
    JobLoader  并根据类注解信息，进行包扫描并执行job
- js java动态调用js代码
- json 以json的形式来存取配置信息
- list 快速生成一个list，list空判断
- log 一个精简的日志工具。可用于swing控制台打印
- map 快速生成一个（有序）的map，map空判断
- mime http所有类型的mime信息映射工具类
- object 对象引用工具
- parallel fork/join并行计算
- platform 关于系统设定，平台信息的变量
- property 强大的属性文件读写工具，支持注释，可网络加载。支持多属性组合文件读取、支持单属性值数组读取
- proxy cglib动态代理工具、字节增强工具
- pubsub 发布订阅模式
- qrcode 二维码生成工具
- queue  超大混合型内存磁盘Queue
- random 随机数工具，可随机生成字符串
- reflect 高效的java反射工具类。
包扫描获取类列表、高性能反射调用、类加载实例化、代码热编译执行、map自动转对象、
对象引用、远程类加载、远程jar包调用、TypeReference任意泛型类型引用
- robot.turing 图灵机器人客户端
- rss rss解析工具
- runkit 获取运行时类、方法名、行号信息
- security 一系列安全工具。http请求参数过滤检测工具，根据秘钥文件生成密码的工具，
md5加密、base64加解密、aes加解密、des加解密、计算某个文件的MD5值
- serialization 高性能protostuff序列化以及反序列化工具、高性能FST序列化及反序列化工具
- servicecenter 服务注册与获取中心
- shell  命令行工具。执行command、shell命令并监听返回值
- singleton 类spring单例工厂的实现，BeanMgr单例管理器
- sort map排序工具
- stopwatch 更好用的秒表
- str 人民币显示工具、字符工具、中文字符判断工具、
- swing swing工具类
swing快捷键注解、颜色工具箱、图片尺寸获取、验证码识别、图片黑白化、
屏幕截图、自定义Swing组件（智能按钮、无框按钮、无聚焦按钮、图形卷动器）、
验证码生成器、图片宽高自适应、R资源管理器、弹窗工具、文件选择框工具
- task  任务管理工具。siri小助手、任务调度工具
- taskman 任务管理器
- testframework 单元测试框架。单元测试、benchmark性能压测工具（TPS压测）
- text 字符集，csv工具类，编码工具集、转义工具集、各种hash算法封装、字符工具类扩充、文件校验器、通配符工具类
- type Java基本类型与包装类型转换工具
- validate 验证工具类。 验证手机号格式、人名格式、邮箱格式、整形、长整形、金额、邮编、
身份证、ip地址、url格式、汉字、UUID格式是否合法，正则校验
- xml 对象与xml互转，（w3c）xml转Document对象，（dom4j）xml解析工具
- yml yml配置文件读取工具

### TODO: 利用类加载器进行命名空间与执行环节隔离支持
- 可支持但项目多命名空间不冲突（像tomcat那样）