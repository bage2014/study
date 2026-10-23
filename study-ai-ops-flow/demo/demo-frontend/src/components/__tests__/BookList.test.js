import { mount } from '@vue/test-utils'
import BookList from '../../views/BookList.vue'

describe('BookList', () => {
  it('renders title', () => {
    const wrapper = mount(BookList, {
      global: {
        stubs: {
          'el-table': true,
          'el-table-column': true,
          'el-dialog': true,
          'el-form': true,
          'el-form-item': true
        }
      }
    })
    expect(wrapper.find('h2').text()).toBe('图书列表')
  })
})
