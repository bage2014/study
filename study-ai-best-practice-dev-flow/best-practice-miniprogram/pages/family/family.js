const api = require('../../utils/api.js')

Page({
  data: {
    families: [],
    showModal: false,
    isEdit: false,
    editId: null,
    formData: {
      name: '',
      description: ''
    }
  },

  onLoad: function (options) {
    this.loadFamilies()
  },

  onShow: function () {
    this.loadFamilies()
  },

  loadFamilies: function () {
    wx.showLoading({ title: '加载中...' })
    api.family.list()
      .then((data) => {
        this.setData({
          families: data || []
        })
      })
      .catch((err) => {
        console.error('加载家族列表失败', err)
      })
      .finally(() => {
        wx.hideLoading()
      })
  },

  showCreateModal: function () {
    this.setData({
      showModal: true,
      isEdit: false,
      editId: null,
      formData: {
        name: '',
        description: ''
      }
    })
  },

  showEditModal: function (e) {
    const item = e.currentTarget.dataset.item
    this.setData({
      showModal: true,
      isEdit: true,
      editId: item.id,
      formData: {
        name: item.name,
        description: item.description || ''
      }
    })
  },

  closeModal: function () {
    this.setData({
      showModal: false
    })
  },

  onNameInput: function (e) {
    this.setData({
      'formData.name': e.detail.value
    })
  },

  onDescInput: function (e) {
    this.setData({
      'formData.description': e.detail.value
    })
  },

  saveFamily: function () {
    const { name, description } = this.data.formData
    
    if (!name.trim()) {
      wx.showToast({
        title: '请输入家族名称',
        icon: 'none'
      })
      return
    }

    wx.showLoading({ title: '保存中...' })
    
    const data = {
      name: name.trim(),
      description: description.trim()
    }

    if (this.data.isEdit) {
      api.family.update(this.data.editId, data)
        .then(() => {
          wx.showToast({
            title: '修改成功',
            icon: 'success'
          })
          this.closeModal()
          this.loadFamilies()
        })
        .catch((err) => {
          console.error('修改家族失败', err)
        })
        .finally(() => {
          wx.hideLoading()
        })
    } else {
      api.family.create(data)
        .then(() => {
          wx.showToast({
            title: '创建成功',
            icon: 'success'
          })
          this.closeModal()
          this.loadFamilies()
        })
        .catch((err) => {
          console.error('创建家族失败', err)
        })
        .finally(() => {
          wx.hideLoading()
        })
    }
  },

  handleDelete: function (e) {
    const id = e.currentTarget.dataset.id
    const name = e.currentTarget.dataset.name
    
    wx.showModal({
      title: '确认删除',
      content: `确定要删除家族「${name}」吗？`,
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '删除中...' })
          api.family.delete(id)
            .then(() => {
              wx.showToast({
                title: '删除成功',
                icon: 'success'
              })
              this.loadFamilies()
            })
            .catch((err) => {
              console.error('删除家族失败', err)
            })
            .finally(() => {
              wx.hideLoading()
            })
        }
      }
    })
  },

  selectFamily: function (e) {
    const id = e.currentTarget.dataset.id
    wx.setStorageSync('currentFamilyId', id)
    wx.showToast({
      title: '已选择该家族',
      icon: 'success'
    })
  }
})