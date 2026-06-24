<template>
  <header class="header">
    <div class="header-inner">
      <span class="logo">GrayFlow 管理台</span>
      <nav class="nav">
        <RouterLink to="/">首页</RouterLink>
        <RouterLink to="/logs">日志记录</RouterLink>
        <RouterLink to="/replay">回放记录</RouterLink>
      </nav>
    </div>
  </header>
</template>

<style scoped>
.header { background: #1a1a2e; color: white; padding: 0 24px; }
.header-inner { max-width: 1200px; margin: 0 auto; display: flex; align-items: center; justify-content: space-between; height: 56px; }
.logo { font-size: 18px; font-weight: 600; }
.nav { display: flex; gap: 24px; }
.nav a { color: rgba(255,255,255,0.75); text-decoration: none; font-size: 14px; padding: 4px 0; transition: color 0.2s; }
.nav a:hover, .nav a.router-link-exact-active { color: white; border-bottom: 2px solid #4a9eff; }
</style>