# usrJava
## 这是有人透传云平台的第三方API库

### 项目目标
* 为了方便调用有人透传云平台的接口，对相关接口进行二次开发和封装，包括在官方文档没有公开的部分私有接口。
### 项目功能
* 提供平台对接的接口工具类
* 对业务接口返回的数据进行处理和数据类型转换
* 只需要关注业务系统的开发，调用工具类即可获取平台的接口数据。

### 使用指南
* 1、引入pom.xml文件里对应的jar包。
* 2、把src/main/java/com/usrJava/entity和rc/main/java/com/usrJava/util这两个包复制到自己的springboot项目文件夹。
* 3、在UsrAPIUtil.java的定时getToken方法里填入自己的平台帐号和密码。
* 4、使用工具类UsrAPIUtil.java里的方法进行接口调用，类文件位于src/main/java/com/usrJava/util。
* 5、也可以将本项目打包成jar包进行引用。
  
### 注意事项
  部分接口的json解析内容可能跟平台返回的json内容不一致，可自行更改或者提交Issues同步到github。
