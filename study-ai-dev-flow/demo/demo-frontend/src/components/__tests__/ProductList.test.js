import { mount } from '@vue/test-utils'
import ProductList from '../../views/ProductList.vue'

describe('ProductList', () => {
  it('renders title', () => {
    const wrapper = mount(ProductList)
    expect(wrapper.find('h2').text()).toBe('商品列表')
  })
})