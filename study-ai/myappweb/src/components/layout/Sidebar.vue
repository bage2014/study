<template>
  <aside class="sidebar">
    <nav class="sidebar-nav">
      <router-link to="/dashboard" class="nav-item" active-class="active">
        <span class="nav-icon">📊</span>
        <span>{{ $t('common.dashboard') }}</span>
      </router-link>
      <router-link to="/profile" class="nav-item" active-class="active">
        <span class="nav-icon">👤</span>
        <span>{{ $t('common.profile') }}</span>
      </router-link>
    </nav>
  </aside>
</template>

<script setup>
</script>

<style scoped>
.sidebar {
  width: 250px;
  background-color: var(--surface-color);
  border-right: 1px solid var(--border-color);
  padding: 1rem 0;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 0.75rem 1.5rem;
  text-decoration: none;
  color: var(--text-color);
  transition: background-color 0.3s;
  gap: 0.75rem;
}

.nav-item:hover {
  background-color: var(--border-color);
}

.nav-item.active {
  background-color: var(--primary-color);
  color: white;
}

.nav-icon {
  font-size: 1.2rem;
}
</style>