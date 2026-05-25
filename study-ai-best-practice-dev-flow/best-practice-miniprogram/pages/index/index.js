const app = getApp()

Page({
  data: {
    userInfo: null
  },

  onLoad: function (options) {
    this.loadUserInfo()
  },

  loadUserInfo: function () {
    const userInfo = wx.getStorageSync('userInfo')
    if (userInfo) {
      this.setData({
        userInfo: JSON.parse(userInfo)
      })
    }
  },

  goToFamily: function () {
    wx.switchTab({
      url: '/pages/family/family'
    })
  },

  goToMembers: function () {
    wx.switchTab({
      url: '/pages/members/members'
    })
  },

  showAbout: function () {
    wx.showModal({
      title: '关于家族族谱',
      content: '家族族谱系统是一款帮助您记录和管理家族信息的应用。支持家族档案管理、成员管理、家族树展示等功能。',
      showCancel: false
    })
  },

  handleLogout: function () {
    wx.showModal({
      title: '确认退出',
      content: '确定要退出登录吗？',
      success: (res) => {
        if (res.confirm) {
          wx.removeStorageSync('token')
          wx.removeStorageSync('userInfo')
          app.globalData.token = ''
          app.globalData.userInfo = null
          this.setData({
            userInfo: null
          })
          wx.showToast({
            title: '退出成功',
            icon: 'success'
          })
        }
      }
    })
  }
})