dili-etrade
  etrade-core        基础设施（工具类引入，中间件服务mq/redis/mysql等配置，第三方框架的封装和配置，全局异常拦截配置-待配置）
  etrade-rpc         远程调用模块(openfeign集成，访问原有平台服务，请合理规划)
  etrade-shared      共享模块（解决循环依赖问题，请合理规划包名）
  etrade-coupon      优惠劵模块
  etrade-sentinel    风控模块
  etrade-stock       商品库存模块（包括商品）
  etrade-shop        店铺模块
  etrade-order       订单模块（包括订单支付）
  etrade-boss        父工程（springboot打包，系统对外提供接口）

项目依赖
etrade-core  ->  etrade-rpc  ->  etrade-shared  ->  etrade-coupon  ->  etrade-boss
                                                    etrade-sentinel
                                                    etrade-stock
                                                    etrade-shop
                                                    etrade-order

项目结构
  com.diligrp.etrade.xxxx - 模块spring配置xxxxConfiguration（Spring组件扫描配置/MybatisMapper扫描配置）ErrorCode Constants
  com.diligrp.etrade.xxxx.controller - 后台接口
  com.diligrp.etrade.xxxx.api - 移动端接口
  com.diligrp.etrade.xxxx.service
  com.diligrp.etrade.xxxx.dao
  com.diligrp.etrade.xxxx.exception
  com.diligrp.etrade.xxxx.domain
  com.diligrp.etrade.xxxx.model
  com.diligrp.etrade.xxxx.type
  com.diligrp.etrade.xxxx.util
  resource/com.diligrp.etrade.dao.mapper - mybatis mapper文件
  
  系统对第三方系统提供接口通过etrade-boss controller包
  所有数据模型类（DO）放入com.diligrp.etrade.xxxx.model下，所有域模型类（VO DTO）放入com.diligrp.etrade.xxxx.domain下
  所有数据模型类（DO）须继承BaseDo类，进一步规范数据表设计：需包含id version created_time modified_time
  所有枚举类型放入com.diligrp.etrade.xxxx.type下，枚举类定义请提供code/name属性，参见com.diligrp.etrade.core.type.Gender
  所有自定义工具类放入com.diligrp.etrade.xxxx.util下，如果大家都能公用请放etrade-core模块下
  所有异常类继承PlatformServiceException（提供了错误码和是否打印异常栈信息功能），并放入com.diligrp.etrade.xxxx.exception下
  每个模块的常量类请放在模块根目录下，如通用常量请放入etrade-core模块下
  错误码为6位，每个模块的错误类ErrorCode且放入模块根目录，错误码应唯一且独特如前三位为模块标识，公共错误码参见com.diligrp.etrade.core.ErrorCode

工具类
  参见：com.diligrp.etrade.core.util.* com.diligrp.etrade.core.security.*
  包括：JsonUtils CurrencyUtils DateUtils RandomUtils AssertUtils HexUtils AesCipher RsaCipher ShaCipher KeyStoreUtils等等

技术要求
  JDK17 SpringCould SpringBoot 3版本
  编译工具：gradle
  第三方库尽量使用springboot默认推荐，如：Jackson Lettuce；springboot工具集中没有推荐的第三方库，引入时请在合适模块中进行
  已在etrade-core中完成Jackson配置，包括Spring DataBinding，且额外在core包中提供了JsonUtils
  已在etrade-core中已完成Redis基础配置Lettuce，可直接使用StringRedisTemplate，如需进行进一步封装配置请在合适的模块中配置，如需Redis分布式锁，可考虑引入Redission
  已在etrade-core中已完成Mybatis基础配置，使用MapperScan完成mapper文件的扫描，不用plus，可用mybatis分页插件
  已在etrade-core中完成MQ基础配置RabbitMQ，可直接进行使用RabbitTemplate且可进行Queue Exchange和消息监听器的配置
  外部第三方jar放入dili-etrade/libs
  新技术框架的引入不以个人熟悉为重点考量标准，以技术框架的通用型和稳定性为考量标准
  分页查询

数据库脚本要求
  维护全量（dili-etrade/scripts）和增量脚本（scripts/upgrade），每个模块的全量脚本文件以模块进行命名；脚本命名规范可参考原有标准
  维护增量脚本，需同时修改权量脚本
  所有建表SQL，每个字段需填写备注
  通常情况下，每个表都需要包含三个字段id，version，created_time，modified_time
  每个模块的数据表，建议统一的前缀

遗留问题
  UAP集成
  Redis序列化-Redission
  分页查询-Mybatis分页插件集成
  定时任务xxl-job配置

mkdir -p src/main/java/com/diligrp/etrade/ModuleName/controller
mkdir -p src/main/java/com/diligrp/etrade/ModuleName/api
mkdir -p src/main/java/com/diligrp/etrade/ModuleName/service
mkdir -p src/main/java/com/diligrp/etrade/ModuleName/dao
mkdir -p src/main/java/com/diligrp/etrade/ModuleName/exception
mkdir -p src/main/java/com/diligrp/etrade/ModuleName/domain
mkdir -p src/main/java/com/diligrp/etrade/ModuleName/model
mkdir -p src/main/java/com/diligrp/etrade/ModuleName/type
mkdir -p src/main/java/com/diligrp/etrade/ModuleName/util
mkdir -p src/main/resources/com/diligrp/etrade/dao/mapper
