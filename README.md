# 破解 Markdown Navigator

用了好久的 `2.3.8` 版本，一直懒得更新，今儿不小心把插件删了...然后发现之前参考的链接失效了...

哎，自己重新破解一遍吧...

## 安装 Markdown Navigator 插件

这个不解释了，安装啥插件都一样，自己谷歌下。

## 修改文件

将 [`LicenseAgent.class` 文件](https://github.com/CrazyBunQnQ/multimarkdown/releases/tag/2.7.0)拷贝到 Markdown Navigator 插件安装目录 `/lib/idea-multimarkdown.jar` 包中的 `com.vladsch\idea\multimarkdown\license` 目录下，覆盖原有的 `LicenseAgent.class` 文件

>具体修改方法、内容感兴趣的可以看后面的[详细破解方法](#破解全过程)和 [`LicenseAgent.java` 注释](https://github.com/CrazyBunQnQ/multimarkdown)

### Windows 下推荐使用 Everything 搜索 "idea-multimarkdown.jar"，全盘秒搜

![Everything](http://wx2.sinaimg.cn/mw690/a6e9cb00ly1fggjp1tee6j20oc07smxl.jpg)

### 使用压缩工具打开 `idea-multimarkdown.jar` 按下图所示替换 .class 文件即可

![替换 class 文件](http://wx4.sinaimg.cn/mw690/a6e9cb00ly1fggjp25549j20mr0el40o.jpg)

完成上面的操作后重启 IDEA 即可。

**替换后一定要确认下被替换的 .class 文件修改时间与其他文件修改时间不同。**

![确认替换成功](http://wx2.sinaimg.cn/mw690/a6e9cb00ly1fgglx44looj20ps091abm.jpg)

## Mac 系统破解插件

打开终端输入命令：
```
cd ~/Library/Application\ Support
ls
```
根据查到的 IDEA 版本文件夹，继续输入下面的命令进入插件目录：
```
cd IntelliJIdea2017.1/idea-multimarkdown
open lib
```
输入完上述指令后会弹出插件目录，将修改好的 multimarkdown.jar 文件替换进去，重启 IDEA 即可，如图：

![替换 jar 包](http://wx2.sinaimg.cn/mw690/a6e9cb00ly1fggoxvajfnj21b61hie2s.jpg)

## 附：破解成功后的截图

解锁全部姿势:

![顶部菜单](http://wx1.sinaimg.cn/mw690/a6e9cb00ly1fgh5r3ktnrj21ec0cftc9.jpg)

![破解成功](http://wx1.sinaimg.cn/large/a6e9cb00ly1fzpvwns7oej20u00uwnc2.jpg)

>这个天数，哈哈哈，设置 `Integer.MAX_VALUE` 是不是有点高了。。。

## 破解全过程

>默认你已经安装好插件了
>
>`x` 为你的 IDEA 版本

### 创建项目

>实际直接克隆此项目即可
>以下路径根据自己电脑情况有所变化

创建项目时必须创建 `com.vladsch.idea.multimarkdown.license` 包，与原始 jar 包中的结构一致

### 修改文件

```bash
# 1. 将安装好的插件拷贝出来，并保存为 `idea-multimarkdown.bak.jar` 作为备份
cd 你的项目目录
cp ~/Library/Application\ Support/IntelliJIdea2018.x/idea-multimarkdown/lib/idea-multimarkdown.jar ./releases/2.7.0/idea-multimarkdown.bak.jar
# 2. 解压插件
cd releases/2.7.0
# 解压到 source 文件夹(没找到解压到指定文件夹的参数...)
cp idea-multimarkdown.bak.jar ./source/
cd source
jar xvf idea-multimarkdown.bak.jar && rm -f idea-multimarkdown.bak.jar
# 将要修改的 LicenseAgent.java 拷贝到上面创建的包里
cd 你的项目目录
cp releases/2.7.0/source/com/vladsch/idea/multimarkdown/license/LicenseAgent.java src/com/vladsch/idea/multimarkdown/license/
```


### 编译文件

打开  文件你会发现很多报错，无法编译，是因为没有依赖包

编译前首先需要引入 IDEA 和 multimarkdown 的依赖包

IDEA 依赖包在 IDEA 安装目录中

![IDEA 依赖包](http://wx4.sinaimg.cn/large/a6e9cb00ly1fzpvgamo4nj213w0aidhp.jpg)

multimarkdown 的依赖包在该插件目录中

![multimarkdown 依赖包](http://wx2.sinaimg.cn/large/a6e9cb00ly1fzpvinslyfj21j00a80v1.jpg)

>路径参考图中路径

导入依赖后菜单 `Build` → `Build Project` 编译项目

然后会生成 out 目录，编译好的 `.class` 文件就在这里

### 重新打包

```bash
cd 项目目录
# 将修改后的 LicenseAgent.class 文件拷贝到解压后的 jar 包中
cp out/production/multimarkdown/com/vladsch/idea/multimarkdown/license/LicenseAgent.class ./releases/2.7.0/source/com/vladsch/idea/multimarkdown/license/
# 重新打包并移到上层目录
cd releases/2.7.0/source && jar cvf idea-multimarkdown.jar * && mv idea-multimarkdown.jar ../
# 将打好的包拷贝到 IDEA 插件目录中覆盖掉原文件
cd ..
cp idea-multimarkdown.jar ~/Library/Application\ Support/IntelliJIdea2018.x/idea-multimarkdown/lib/idea-multimarkdown.jar
```

完成以上步骤后重启 idea 即可

## 参考

[JetBrains 官方文档中介绍的各个系统下的 IDEA 路径（配置路径、缓存路径、插件路径和日志路径）](https://www.jetbrains.com/help/idea/2017.1/directories-used-by-intellij-idea-to-store-settings-caches-plugins-and-logs.html)
