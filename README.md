#基于javaEEScaffold脚手架的讲座预约系统搭建
##技术选型
* 容器框架：spring
* mvc：spring mvc
* 持久层:：spring data jpa+hibernate jpa
* 数据库：mysql
* 前端：bootstrap，ZTree，Jquery，主题adminLTE(https://www.almsaeedstudio.com/themes/AdminLTE/documentation/index.html#dependencies)
* json框架：fastjson
* 数据校验框架：jquery-validate
* 日志系统：门面slf4j,实现log4j（生产环境rootlogger=info,其他的也都设置为info）
* 代码构建工具：gradle
* 版本控制：git
* excel解析:jxl

##功能设计
* 实现用户角色权限管理(基于资源的角色管理)✔
* java mail功能
* 系统监控日志 druid的监控功能,github上有文档  ✔
* 动态生成侧边栏（即资源,只使用单层目录）✔
* 管理端：讲座管理，考勤管理，学生管理，excel批量导入、导出，下载预约清单，上传考勤，新增考勤等
* 学生端的讲座预约，查询，修改信息等

##数据库表设计


##todo list
将parent_id改为pId，这样才能与ztree一致,或者写一个工具类来进行转换✔
* 表单验证✔
* admin作为超管，不能被删除，不能被修改，拥有所有权限✔
* criteria 条件查询✔
* 分页✔
* 在数据库中添加root资源根节点
* 删除角色时，若角色被用户绑定，会导致出现外键约束的错误：解决方法使用级联 cascade=CascadeType.remove✔
* 数据校验，name唯一性✔
* 服务端权限认证 需要添加spring-shiro-mvc✔
* 上传下载文件✔
* excel校验是否文件中是否有重复项
* 优先级是越低，排在越前面✔
* 将用户名保存到session中，以便于监控中显示✔
* 删除谷歌字体库，提高页面加载速度✔
* 删除sysout，提高速度，修改logger级别，提高速度✔
* shiro要将静态资源设置成允许访问，要不然登录界面获取不了这些静态资源✔
* 默认角色是admin2，应该改为admin✔
* resource add界面不管怎么改都不会生效✔
* 改用ajax来生成页面！！✔
* tld要放在web-inf目录下✔
* select没有在表单中提交✔
* 登录后，再进入login界面登录会导致无反应
* download改为get方法
* 上传下载中文乱码
* 增加删除提醒✔
* 添加修改密码✔
* modal使用触发式✔
* 解决部分update
* 解决登录时间
* 解决多种用户登录问题

##问题
* 要不要全使用ajax在前端生成页面，后端只提供json数据
modal框多次提交请求，解决方法：
1、做好你写死的那个模态框
2、在按钮上注册click事件
3、click事件触发的同时调用load方法往.modal-content里塞你的数据
4、最后在load方法的回调里把模态框显示出来。

