const api = require('../../utils/api.js')

Page({
  data: {
    members: [],
    familyOptions: [],
    selectedFamily: null,
    showModal: false,
    isEdit: false,
    editId: null,
    formData: {
      name: '',
      gender: 'MALE',
      birthDate: '',
      remark: ''
    }
  },

  onLoad: function (options) {
    this.loadFamilies()
  },

  onShow: function () {
    this.loadMembers()
  },

  loadFamilies: function () {
    api.family.list()
      .then((data) => {
        const families = data || []
        this.setData({
          familyOptions: families
        })
        
        const storedFamilyId = wx.getStorageSync('currentFamilyId')
        if (storedFamilyId) {
          const selected = families.find(f => f.id === parseInt(storedFamilyId))
          if (selected) {
            this.setData({
              selectedFamily: selected
            })
            this.loadMembers()
          }
        } else if (families.length > 0) {
          this.setData({
            selectedFamily: families[0]
          })
          this.loadMembers()
        }
      })
      .catch((err) => {
        console.error('加载家族列表失败', err)
      })
  },

  loadMembers: function () {
    if (!this.data.selectedFamily) return
    
    wx.showLoading({ title: '加载中...' })
    api.member.list(this.data.selectedFamily.id)
      .then((data) => {
        this.setData({
          members: data || []
        })
      })
      .catch((err) => {
        console.error('加载成员列表失败', err)
      })
      .finally(() => {
        wx.hideLoading()
      })
  },

  onFamilyChange: function (e) {
    const index = e.detail.value
    const family = this.data.familyOptions[index]
    this.setData({
      selectedFamily: family
    })
    wx.setStorageSync('currentFamilyId', family.id)
    this.loadMembers()
  },

  showCreateModal: function () {
    if (!this.data.selectedFamily) {
      wx.showToast({
        title: '请先选择家族',
        icon: 'none'
      })
      return
    }
    
    this.setData({
      showModal: true,
      isEdit: false,
      editId: null,
      formData: {
        name: '',
        gender: 'MALE',
        birthDate: '',
        remark: ''
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
        gender: item.gender || 'MALE',
        birthDate: item.birthDate || '',
        remark: item.remark || ''
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

  setGender: function (e) {
    const gender = e.currentTarget.dataset.gender
    this.setData({
      'formData.gender': gender
    })
  },

  onDateChange: function (e) {
    this.setData({
      'formData.birthDate': e.detail.value
    })
  },

  onRemarkInput: function (e) {
    this.setData({
      'formData.remark': e.detail.value
    })
  },

  saveMember: function () {
    const { name, gender, birthDate, remark } = this.data.formData
    
    if (!name.trim()) {
      wx.showToast({
        title: '请输入成员姓名',
        icon: 'none'
      })
      return
    }

    wx.showLoading({ title: '保存中...' })
    
    const data = {
      name: name.trim(),
      gender: gender,
      birthDate: birthDate,
      remark: remark.trim(),
      familyId: this.data.selectedFamily.id
    }

    if (this.data.isEdit) {
      api.member.update(this.data.editId, data)
        .then(() => {
          wx.showToast({
            title: '修改成功',
            icon: 'success'
          })
          this.closeModal()
          this.loadMembers()
        })
        .catch((err) => {
          console.error('修改成员失败', err)
        })
        .finally(() => {
          wx.hideLoading()
        })
    } else {
      api.member.create(data)
        .then(() => {
          wx.showToast({
            title: '添加成功',
            icon: 'success'
          })
          this.closeModal()
          this.loadMembers()
        })
        .catch((err) => {
          console.error('添加成员失败', err)
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
      content: `确定要删除成员「${name}」吗？`,
      success: (res) => {
        if (res.confirm) {
          wx.showLoading({ title: '删除中...' })
          api.member.delete(id)
            .then(() => {
              wx.showToast({
                title: '删除成功',
                icon: 'success'
              })
              this.loadMembers()
            })
            .catch((err) => {
              console.error('删除成员失败', err)
            })
            .finally(() => {
              wx.hideLoading()
            })
        }
      }
    })
  }
})