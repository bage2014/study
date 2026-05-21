export const TEST_CONFIG = {
  baseURL: 'http://localhost:5173',
  
  paths: {
    login: '/login',
    family: '/family',
    members: '/members',
    relationships: '/relationships',
    familyTree: '/family-tree',
    events: '/events',
    media: '/media',
    track: '/track',
    ai: '/ai'
  },
  
  selectors: {
    usernameInput: 'input[placeholder="用户名"]',
    passwordInput: 'input[placeholder="密码"]',
    emailInput: 'input[placeholder="邮箱"]',
    submitBtn: '.submit-btn',
    tabBtn: '.tab-btn',
    searchBar: '.search-bar',
    elTable: '.el-table',
    elDialog: '.el-dialog',
    elButton: '.el-button',
    elButtonDanger: '.el-button--danger'
  },
  
  timeout: {
    short: 500,
    medium: 1000,
    long: 2000,
    extraLong: 10000
  }
}

export const getFullUrl = (path: string): string => {
  return `${TEST_CONFIG.baseURL}${path}`
}

export const getPath = (key: keyof typeof TEST_CONFIG.paths): string => {
  return TEST_CONFIG.paths[key]
}
