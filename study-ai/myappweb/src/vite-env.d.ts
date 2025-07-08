declare module '*.vue' {
  import { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

// 添加BMap类型声明
declare global {
  interface Window {
    BMap: any;
  }
}