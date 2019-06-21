项目说明
===

[![Build Status](https://travis-ci.org/aiziyuer/SpriteApp.svg?branch=master)](https://travis-ci.org/aiziyuer/SpriteApp)


<img src="https://raw.githubusercontent.com/aiziyuer/SpriteApp/master/Design/FrontDesign/robot.svg" width=100>

## 功能设计

这里大概罗列一下后面可能会做的功能:

- `ssh`隧道的创建和管理
- `etcd`键值得查询预览

### SSH隧道管理

初步用例设计:

![image](https://raw.githubusercontent.com/aiziyuer/SpriteApp/master/Design/SSH隧道交互用例.svg)


### 应用编译

``` bash
# 启动p2的repository
./server -j

# 开始编译
mvn clean verify
```

## 感谢

<div>Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>

### 实现参考

- [Common Navigator Framework初探](https://blog.csdn.net/andywangcn/article/details/8257953)
