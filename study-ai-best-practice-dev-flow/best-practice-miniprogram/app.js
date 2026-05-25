App({
  onLaunch: function () {
    console.log('家族族谱小程序启动')
    
    const token = wx.getStorageSync('token')
    if (token) {
      this.globalData.token = token
    }
  },

  onShow: function () {
    console.log('小程序显示')
  },

  onHide: function () {
    console.log('小程序隐藏')
  },

  globalData: {
    token: '',
    userInfo: null,
    currentFamily: null
  }
})