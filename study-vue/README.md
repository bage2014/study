# study-vue #
Vue 学习笔记
vue 显示 markdown 内容 [https://github.com/miaolz123/vue-markdown](https://github.com/miaolz123/vue-markdown)
代码高亮 [https://github.com/highlightjs/highlight.js](https://github.com/highlightjs/highlight.js)
## 环境搭建 ##

### 安装nodejs  ###
下载地址 [https://nodejs.org/en/](https://nodejs.org/en/)
常规软件安装过程，此处略

### 安装vue-cli  ###
    npm install -g vue-cli

### 创建hello-vue项目 ###
    vue init webpack hello-vue

### 下载依赖 ###
    npm install

### 部署运行 ###
    npm run dev


## 学习笔记 ##

### 起步 ###

#### 声明式渲染 ####

{{ message }}模板渲染

    <div id="app">
      {{ message }}
    </div>

v-bind 指令

    <div id="app-2">
      <span v-bind:title="message">
    鼠标悬停几秒钟查看此处动态绑定的提示信息！
      </span>
    </div>

#### 条件与循环 ####

v-if 控制切换

    <div id="app-3">
      <p v-if="seen">现在你看到我了</p>
    </div>

-for 循环

<div id="app-4">
  <ol>
<li v-for="todo in todos">
  {{ todo.text }}
</li>
  </ol>
</div>

#### 处理用户输入 ####

v-on 指令

    <div id="app-5">
      <p>{{ message }}</p>
      <button v-on:click="reverseMessage">逆转消息</button>
    </div>

v-model 指令

    <div id="app-6">
      <p>{{ message }}</p>
      <input v-model="message">
    </div>


#### 组件化应用构建 ####
// TODO