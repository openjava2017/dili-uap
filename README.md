dili-uap
  uap-shared        基础设施和共享模块（解决循环依赖问题，工具类引入，中间件服务mq/redis/mysql等配置，第三方框架的封装和配置，全局异常拦截配置）
  uap-rpc           远程调用模块(openfeign集成，访问其他平台服务)，可以利用远程调用完成外部系统的用户认证；
  uap-boss          管理模块，维护用户/角色/资源/权限等
  uap-auth          用户认证/授权模块，用于用户登陆和权限控制，集成uap-security权限框架；该模块不仅仅可以授权内部系统用户，还可以通过RPC模块调用远程服务完成用户认证
  uap-security      参考spring-security自研的一套权限验证框架，用于uap及其他子系统进行用户认证授权，类似于uap sdk
  uap-boot          父工程（springboot打包，系统对外提供开放接口）

模块依赖
uap-shared  ->  uap-rpc  ->  uap-boss  ->  uap-auth  ->  uap-boot
uap-security

项目结构
  com.diligrp.uap.xxxx - 模块spring配置xxxxConfiguration（Spring组件扫描配置/MybatisMapper扫描配置）ErrorCode Constants
  com.diligrp.uap.xxxx.controller - 后台接口
  com.diligrp.uap.xxxx.api - 移动端接口
  com.diligrp.uap.xxxx.service
  com.diligrp.uap.xxxx.dao
  com.diligrp.uap.xxxx.exception
  com.diligrp.uap.xxxx.domain
  com.diligrp.uap.xxxx.model
  com.diligrp.uap.xxxx.type
  com.diligrp.uap.xxxx.util
  resource/com.diligrp.uap.dao.mapper - mybatis mapper文件
  
  系统对第三方系统提供接口通过uap-boot controller包
  所有数据模型类放入com.diligrp.uap.xxxx.model下，所有域模型类（VO DTO）放入com.diligrp.uap.xxxx.domain下
  所有数据模型类须继承BaseDO类，进一步规范数据表设计：需包含id version created_time modified_time
  所有枚举类型放入com.diligrp.uap.xxxx.type下，枚举类定义请提供code/name属性，参见com.diligrp.uap.shared.type.Gender
  所有自定义工具类放入com.diligrp.uap.xxxx.util下，如果大家都能公用请放uap-shared模块下
  所有异常类继承PlatformServiceException（提供了错误码和是否打印异常栈信息功能），并放入com.diligrp.uap.xxxx.exception下
  每个模块的常量类请放在模块根目录下，如通用常量请放入uap-shared模块下
  错误码为6位，每个模块的错误类ErrorCode且放入模块根目录，错误码应唯一且独特如前三位为模块标识，公共错误码参见com.diligrp.uap.shared.ErrorCode

工具类
  参见：com.diligrp.uap.shared.util.* com.diligrp.uap.shared.security.*
  包括：JsonUtils CurrencyUtils DateUtils RandomUtils AssertUtils HexUtils AesCipher RsaCipher ShaCipher KeyStoreUtils等等

技术要求
  JDK17 SpringCould SpringBoot 3版本
  编译工具：gradle
  第三方库尽量使用springboot默认推荐，如：Jackson Lettuce；springboot工具集中没有推荐的第三方库，引入时请在合适模块中进行
  已在uap-shared中完成Jackson配置，包括Spring DataBinding，且额外提供了Jackson工具类JsonUtils
  已在uap-shared中已完成Redis基础配置Lettuce，可直接使用StringRedisTemplate，如需进行进一步封装配置请在合适的模块中配置，如需Redis分布式锁，可考虑引入Redission
  已在uap-shared中已完成Mybatis基础配置，使用MapperScan完成mapper文件的扫描，不用plus，可用mybatis分页插件
  已在uap-shared中完成MQ基础配置RabbitMQ，可直接进行使用RabbitTemplate且可进行Queue Exchange和消息监听器的配置
  外部第三方jar放入dili-uap/libs
  新技术框架的引入不以个人熟悉为重点考量标准，以技术框架的通用型和稳定性为考量标准

数据库脚本要求
  维护全量（dili-uap/scripts）和增量脚本（scripts/upgrade）
  维护增量脚本，需同时修改全量脚本
  所有建表SQL，每个字段需填写备注
  通常情况下，每个表都需要包含三个字段id，version，created_time，modified_time
  每个模块的数据表，建议统一的前缀uap_****

重点逻辑
  用户类型：超级管理员 系统管理员 系统用户(后期可根据业务需求定义，比如：财务人员)
  超级管理员不归属商户，系统初始化通过脚本创建，只能访问模块为"Platform"的菜单权限: 商户管理 管理员管理 模块管理 资源管理
  系统管理员归属于商户，可访问除"Platform"模块外的菜单权限，比如：Native模块的功能: 用户管理 部门管理 角色管理 权限管理
  系统用户可访问除"Platform"模块外的所有菜单权限
  
  Platform和Native模块的添加通过数据库脚本初始化，模块管理只管理除Platform和Native外的模块
  权限分配只针对于末级资源；被分配权限的末级资源下不能创建下一级资源
  