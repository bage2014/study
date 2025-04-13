# study-vue-Mac #


Mac 搭建vue环境

https://www.jianshu.com/p/a056e41833a7



## 一键安装

```
# Download and install nvm:
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.2/install.sh | bash

# in lieu of restarting the shell
\. "$HOME/.nvm/nvm.sh"

# Download and install Node.js:
nvm install 22

# Verify the Node.js version:
node -v # Should print "v22.14.0".
nvm current # Should print "v22.14.0".

# Verify npm version:
npm -v # Should print "10.9.2".

```




## 安装HomeBrew

https://docs.brew.sh



安装

```bash
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```



验证

```undefined
brew -v
```



异常处理

```undefined
==> Homebrew has enabled anonymous aggregate formulae and cask analytics.
Read the analytics documentation (and how to opt-out) here:
  https://docs.brew.sh/Analytics
No analytics data has been sent yet (nor will any be during this install run).

==> Homebrew is run entirely by unpaid volunteers. Please consider donating:
  https://github.com/Homebrew/brew#donations

==> Next steps:
- Run these two commands in your terminal to add Homebrew to your PATH:
    echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> /Users/bage/.zprofile
    eval "$(/opt/homebrew/bin/brew shellenv)"
- Run brew help to get started
- Further documentation:
    https://docs.brew.sh
```



补充安装

```undefined
echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> /Users/bage/.zprofile
   
eval "$(/opt/homebrew/bin/brew shellenv)"
```



## 安装 node

安装

```undefined
brew install nodejs
```

验证

```undefined
node -v
```





## 安装 vue

安装

```undefined
npm install --global vue-cli

npm install --global vue-cli@3.0.1

npm config set registry https://registry.npm.taobao.org

```

验证

```undefined
vue -V
```

适当更新版本

```undefined
npm install -g npm@8.10.0
```



## 安装 yarn

安装

```undefined
brew install yarn

yarn global add vue-cli

```

设置淘宝镜像

```
yarn config set registry http://registry.npm.taobao.org/


```

验证

```undefined
yarn -V
```



