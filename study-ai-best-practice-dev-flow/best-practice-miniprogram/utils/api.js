const BASE_URL = 'http://localhost:8080/api'

function request(url, method = 'GET', data = {}, header = {}) {
  const token = wx.getStorageSync('token')
  
  const defaultHeader = {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
  
  if (token) {
    defaultHeader['Authorization'] = `Bearer ${token}`
  }
  
  return new Promise((resolve, reject) => {
    wx.request({
      url: `${BASE_URL}${url}`,
      method: method,
      data: data,
      header: { ...defaultHeader, ...header },
      success: (res) => {
        if (res.statusCode === 200) {
          if (res.data.code === 200) {
            resolve(res.data.data)
          } else {
            wx.showToast({
              title: res.data.message || '请求失败',
              icon: 'none'
            })
            reject(new Error(res.data.message))
          }
        } else if (res.statusCode === 401) {
          wx.removeStorageSync('token')
          wx.showToast({
            title: '登录失效，请重新登录',
            icon: 'none'
          })
          setTimeout(() => {
            wx.navigateTo({ url: '/pages/login/login' })
          }, 1500)
          reject(new Error('未授权'))
        } else {
          wx.showToast({
            title: '服务器错误',
            icon: 'none'
          })
          reject(new Error('服务器错误'))
        }
      },
      fail: (err) => {
        wx.showToast({
          title: '网络请求失败',
          icon: 'none'
        })
        reject(err)
      }
    })
  })
}

const api = {
  login: (data) => request('/users/login', 'POST', data),
  register: (data) => request('/users/register', 'POST', data),
  
  family: {
    list: () => request('/family'),
    create: (data) => request('/family', 'POST', data),
    update: (id, data) => request(`/family/${id}`, 'PUT', data),
    delete: (id) => request(`/family/${id}`, 'DELETE'),
    getById: (id) => request(`/family/${id}`)
  },
  
  member: {
    list: (familyId) => request(`/members?familyId=${familyId}`),
    create: (data) => request('/members', 'POST', data),
    update: (id, data) => request(`/members/${id}`, 'PUT', data),
    delete: (id) => request(`/members/${id}`, 'DELETE'),
    getById: (id) => request(`/members/${id}`)
  }
}

module.exports = api