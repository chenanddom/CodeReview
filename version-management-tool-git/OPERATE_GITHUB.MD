# 操作Github

## 常见操作git远程仓库的命令

| 命令名称                   | 作用                            |
|------------------------|-------------------------------|
| git remote -v          | 查看当前所有远程地址的别名                 |
| git remote add 别名 远程地址 | 起别名                           |
| git push 别名 分支         | 推送本地分支上的内容到远程仓库               |
| git clone 远程地址         | 将远程仓库的内容克隆到本地                 |
| git pull 远程仓库地址别名 远程分支 | 将远程仓库对于分支最新的内容拉下来后与当前本地分支直接合并 |

* 添加别名字
```shell
$ git remote add gitdemo git@github.com:chenanddom/gitdemo.git
$ git remote -v
gitdemo git@github.com:chenanddom/gitdemo.git (fetch)
gitdemo git@github.com:chenanddom/gitdemo.git (push)
```

* 推送本地分支上的内容到远程仓库
```shell
# 推送master分支的代码到远程代码仓库
$ git push gitdemo master
Enumerating objects: 18, done.
Counting objects: 100% (18/18), done.
Delta compression using up to 4 threads
Compressing objects: 100% (12/12), done.
Writing objects: 100% (18/18), 1.34 KiB | 342.00 KiB/s, done.
Total 18 (delta 6), reused 0 (delta 0), pack-reused 0
remote: Resolving deltas: 100% (6/6), done.
To github.com:chenanddom/gitdemo.git
 * [new branch]      master -> master
# 切换到hot-fix分支到远程代码仓库 
$ git checkout hot-fix
Switched to branch 'hot-fix'

chendom@LAPTOP-FCVP44UK MINGW64 /e/book/gitdemo (hot-fix)
$ git push gitdemo hot-fix
Total 0 (delta 0), reused 0 (delta 0), pack-reused 0
remote:
remote: Create a pull request for 'hot-fix' on GitHub by visiting:
remote:      https://github.com/chenanddom/gitdemo/pull/new/hot-fix
remote:
To github.com:chenanddom/gitdemo.git
 * [new branch]      hot-fix -> hot-fix
```






























































































































