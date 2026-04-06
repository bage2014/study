<template>
  <div class="min-h-screen bg-gray-50">
    <!-- Header -->
    <header class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <button @click="navigateTo('/home')" class="mr-4 text-gray-600 hover:text-gray-900">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
            </button>
            <h1 class="text-xl font-bold text-gray-900">家族树</h1>
          </div>
          <div class="flex items-center">
            <button @click="navigateTo('/family-management')" class="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 shadow-md hover:shadow-lg transition-all duration-200">
              家族管理
            </button>
          </div>
        </div>
      </div>
    </header>

    <!-- Main Content -->
    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <!-- Family Selector -->
      <div class="bg-white p-6 rounded-lg shadow mb-6">
        <h2 class="text-lg font-medium text-gray-900 mb-4">选择家族</h2>
        <div v-if="familyStore.loading" class="flex justify-center py-8">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-green-500"></div>
        </div>
        <div v-else-if="familyStore.families.length === 0" class="text-center py-8">
          <p class="text-gray-600">暂无家族数据</p>
          <button @click="navigateTo('/family-management')" class="mt-4 px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 shadow-md hover:shadow-lg transition-all duration-200">
            去创建家族
          </button>
        </div>
        <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div 
            v-for="family in familyStore.families" 
            :key="family.id" 
            @click="selectFamily(family)"
            :class="['border rounded-md p-4 cursor-pointer hover:shadow-md transition-all', selectedFamily?.id === family.id ? 'border-primary bg-blue-50' : 'border-gray-200']"
          >
            <div class="flex items-center">
              <div class="w-12 h-12 rounded-full bg-gray-200 flex items-center justify-center mr-3">
                <span class="text-lg font-bold text-gray-600">{{ family.name.charAt(0) }}</span>
              </div>
              <div class="flex-1">
                <h3 class="font-medium text-gray-900">{{ family.name }}</h3>
                <p class="text-sm text-gray-600">{{ family.description || '无描述' }}</p>
              </div>
              <div v-if="selectedFamily?.id === family.id" class="text-primary">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Selected Family Content -->
      <div v-if="selectedFamily" class="space-y-6">
        <!-- Family Info -->
        <div class="bg-white p-6 rounded-lg shadow">
          <div class="flex justify-between items-start">
            <div>
              <h2 class="text-2xl font-bold text-gray-900">{{ selectedFamily.name }}</h2>
              <p class="text-gray-600 mt-1">{{ selectedFamily.description || '无描述' }}</p>
              <p class="text-sm text-gray-500 mt-2">创建于: {{ formatDate(selectedFamily.createdAt) }}</p>
            </div>
            <button @click="navigateTo('/family-management')" class="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 shadow-md hover:shadow-lg transition-all duration-200">
              管理家族
            </button>
          </div>
        </div>

        <!-- Family Relationship Graph -->
        <div class="bg-white p-6 rounded-lg shadow">
          <h3 class="text-lg font-medium text-gray-900 mb-4">家族关系图</h3>
          <div v-if="familyMembers.length === 0 || familyRelationships.length === 0" class="text-center py-8">
            <p class="text-gray-600">暂无足够的成员和关系数据来显示家族关系图</p>
            <button @click="navigateTo('/family-management')" class="mt-4 px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 shadow-md hover:shadow-lg transition-all duration-200">
              去添加成员和关系
            </button>
          </div>
          <div v-else class="h-96 w-full border rounded-lg">
            <div ref="graphContainer" class="w-full h-full"></div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import { useFamilyStore } from '../stores/family'
import { useMemberStore } from '../stores/member'
import { useRelationshipStore } from '../stores/relationship'
import { useRouter } from 'vue-router'
import * as d3 from 'd3'

export default {
  name: 'FamilyTree',
  setup() {
    const router = useRouter()
    const familyStore = useFamilyStore()
    const memberStore = useMemberStore()
    const relationshipStore = useRelationshipStore()

    const selectedFamily = ref(null)
    const graphContainer = ref(null)

    const familyMembers = computed(() => {
      if (!selectedFamily.value) return []
      return memberStore.members.filter(member => member.familyId === selectedFamily.value.id)
    })

    const familyRelationships = computed(() => {
      if (!selectedFamily.value) return []
      return relationshipStore.relationships.filter(rel => {
        const member1 = familyMembers.value.find(m => m.id === rel.member1Id)
        const member2 = familyMembers.value.find(m => m.id === rel.member2Id)
        return member1 && member2
      })
    })

    const navigateTo = (path) => {
      router.push(path)
    }

    const formatDate = (date) => {
      if (!date) return '未知'
      return new Date(date).toLocaleDateString('zh-CN')
    }

    const selectFamily = (family) => {
      selectedFamily.value = family
      memberStore.fetchMembers(family.id)
      relationshipStore.fetchRelationships()
    }

    const drawFamilyGraph = async () => {
      if (!graphContainer.value || familyMembers.value.length === 0 || familyRelationships.value.length === 0) {
        return
      }

      await nextTick()

      // 清除现有图形
      d3.select(graphContainer.value).selectAll('*').remove()

      const width = graphContainer.value.clientWidth
      const height = graphContainer.value.clientHeight

      // 创建力导向图
      const simulation = d3.forceSimulation()
        .force('link', d3.forceLink().id(d => d.id).distance(100))
        .force('charge', d3.forceManyBody().strength(-300))
        .force('center', d3.forceCenter(width / 2, height / 2))

      // 准备数据
      const nodes = familyMembers.value.map(member => ({
        id: member.id,
        name: member.name,
        gender: member.gender
      }))

      const links = familyRelationships.value.map(relationship => ({
        source: relationship.member1Id,
        target: relationship.member2Id,
        type: relationship.relationshipType
      }))

      // 创建SVG
      const svg = d3.select(graphContainer.value)
        .append('svg')
        .attr('width', width)
        .attr('height', height)

      // 创建箭头
      svg.append('defs').append('marker')
        .attr('id', 'arrowhead')
        .attr('viewBox', '-0 -5 10 10')
        .attr('refX', 20)
        .attr('refY', 0)
        .attr('orient', 'auto')
        .attr('markerWidth', 6)
        .attr('markerHeight', 6)
        .attr('xoverflow', 'visible')
        .append('svg:path')
        .attr('d', 'M 0,-5 L 10 ,0 L 0,5')
        .attr('fill', '#999')
        .style('stroke', 'none')

      // 创建链接
      const link = svg.selectAll('.link')
        .data(links)
        .enter()
        .append('line')
        .attr('class', 'link')
        .attr('stroke', '#999')
        .attr('stroke-width', 2)
        .attr('marker-end', 'url(#arrowhead)')

      // 创建节点
      const node = svg.selectAll('.node')
        .data(nodes)
        .enter()
        .append('g')
        .attr('class', 'node')
        .call(d3.drag()
          .on('start', dragstarted)
          .on('drag', dragged)
          .on('end', dragended))

      // 添加节点圆圈
      node.append('circle')
        .attr('r', 20)
        .attr('fill', d => d.gender === 'male' ? '#64b5f6' : '#f48fb1')

      // 添加节点文本
      node.append('text')
        .attr('dy', 3)
        .attr('text-anchor', 'middle')
        .text(d => d.name)
        .attr('fill', '#333')
        .attr('font-size', '12px')

      // 添加关系标签
      link.append('text')
        .attr('class', 'link-label')
        .attr('font-size', '10px')
        .attr('fill', '#666')

      // 更新力导向图
      simulation
        .nodes(nodes)
        .on('tick', ticked)

      simulation.force('link')
        .links(links)

      function ticked() {
        link
          .attr('x1', d => d.source.x)
          .attr('y1', d => d.source.y)
          .attr('x2', d => d.target.x)
          .attr('y2', d => d.target.y)

        link.selectAll('text')
          .attr('x', d => (d.source.x + d.target.x) / 2)
          .attr('y', d => (d.source.y + d.target.y) / 2 - 10)
          .text(d => d.type)

        node
          .attr('transform', d => `translate(${d.x},${d.y})`)
      }

      function dragstarted(event, d) {
        if (!event.active) simulation.alphaTarget(0.3).restart()
        d.fx = d.x
        d.fy = d.y
      }

      function dragged(event, d) {
        d.fx = event.x
        d.fy = event.y
      }

      function dragended(event, d) {
        if (!event.active) simulation.alphaTarget(0)
        d.fx = null
        d.fy = null
      }
    }

    // 监听家族成员和关系变化，重新绘制图形
    watch([familyMembers, familyRelationships], () => {
      drawFamilyGraph()
    }, { deep: true })

    // 监听选择的家族变化，重新绘制图形
    watch(selectedFamily, (newFamily) => {
      if (newFamily) {
        setTimeout(() => {
          drawFamilyGraph()
        }, 100)
      }
    })

    onMounted(() => {
      familyStore.fetchFamilies()
    })

    return {
      familyStore,
      memberStore,
      relationshipStore,
      selectedFamily,
      familyMembers,
      familyRelationships,
      graphContainer,
      navigateTo,
      formatDate,
      selectFamily
    }
  }
}
</script>