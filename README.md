# 破解 Markdown Navigator
## 安装 Markdown Navigator 插件
这个不解释了，安装啥插件都一样，自己谷歌下。

## 修改文件
将这两个 .class 文件拷贝到 `Markdown Navigator 插件安装目录/lib/idea-multimarkdown.jar` 包中的 `com.vladsch\idea\multimarkdown\license` 目录下，覆盖原有的两个 .class 文件。
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

![破解成功](http://wx3.sinaimg.cn/mw690/a6e9cb00ly1fggoxu26ohj21ks17mn91.jpg)
>这个天数，哈哈哈，设置 `Integer.MAX_VALUE` 是不是有点高了。。。

## 参考
[IntelliJ IDEA-MultiMarkdown破解](http://www.jianshu.com/p/b70e250bed37)

[JetBrains 官方文档中介绍的各个系统下的 IDEA 路径（配置路径、缓存路径、插件路径和日志路径）](https://www.jetbrains.com/help/idea/2017.1/directories-used-by-intellij-idea-to-store-settings-caches-plugins-and-logs.html)