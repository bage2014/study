module.exports = {
  root: true,
  env: {
    browser: true,
    es2021: true,
    'vue/setup-compiler-macros': true,
    node: true
  },
  extends: [
    'plugin:vue/vue3-essential',
    'eslint:recommended'
  ],
  parserOptions: {
    ecmaVersion: 2021,
    sourceType: 'module'
  },
  rules: {
    'vue/multi-word-component-names': 'off',
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'vue/no-unused-vars': 'warn',
    'vue/no-unused-components': 'warn',
    'no-unused-vars': ['warn', { argsIgnorePattern: '^_' }]
  }
};
