## 安装 Markdown Navigator 插件
这个不解释了，安装啥插件都一样，自己谷歌下。

## 破解
将这两个 .class 文件拷贝到 `Markdown Navigator 插件安装目录/lib/idea-multimarkdown.jar` 包中的 `com.vladsch\idea\multimarkdown\license` 目录下，覆盖原有的两个 .class 文件。
### Windows 下推荐使用 Everything 搜索 "idea-multimarkdown.jar"，全盘秒搜

![Everything](http://wx2.sinaimg.cn/mw690/a6e9cb00ly1fggjp1tee6j20oc07smxl.jpg)

### 使用压缩工具打开 `idea-multimarkdown.jar` 按下图所示替换 .class 文件即可

![替换 class 文件](http://wx4.sinaimg.cn/mw690/a6e9cb00ly1fggjp25549j20mr0el40o.jpg)

完成上面的操作后重启 IDEA 即可。

**替换后一定要确认下被替换的 .class 文件修改时间与其他文件修改时间不同。**
![确认替换成功](http://wx2.sinaimg.cn/mw690/a6e9cb00ly1fgglx44looj20ps091abm.jpg)

## 附：破解成功后的截图
![顶部菜单](http://wx3.sinaimg.cn/mw690/a6e9cb00ly1fgglsgj65ej20wg01qmx8.jpg)
解放全部功能！

![破解成功](http://wx1.sinaimg.cn/mw690/a6e9cb00ly1fgglos5opuj20t90jjjtn.jpg)
>这个天数，哈哈哈，设置 Integer.MAX_VALUE 是不是有点高了。。。