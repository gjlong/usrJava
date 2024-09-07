# usrJava
## 这是有人透传云平台的第三方API库

### 项目目标：
* 为了方便调用有人透传云平台的接口，对相关接口进行二次开发和封装，包括在官方文档没有公开的部分私有接口。
### 项目功能：
* 提供平台对接的接口工具类
* 对业务接口返回的数据进行类型转换

### 使用指南
* 1、引入pom.xml文件对应的jar包。
* 2、把src/main/java/com/usrJava/entity和rc/main/java/com/usrJava/util这两个包复制到自己的项目文件夹目录。
* 3、在UsrAPIUtil.java的getToken方法里填入自己的平台帐号和密码。
* 4、使用工具类UsrAPIUtil.java进行接口调用，
* src/main/java/com/usrJava/util/
### 部署细节
