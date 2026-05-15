<script setup lang="ts">
import { ref, onMounted, watch, h } from 'vue'
import { useFamilyStore } from '@/stores/family'
import { memberAPI, relationshipAPI } from '@/api'
import Sidebar from '@/components/Sidebar.vue'

const familyStore = useFamilyStore()
const members = ref<any[]>([])
const relationships = ref<any[]>([])
const treeData = ref<any[]>([])

async function loadData() {
  if (familyStore.currentFamily) {
    members.value = await memberAPI.getByFamily(familyStore.currentFamily.id) as unknown as any[]
    relationships.value = await relationshipAPI.getByFamily(familyStore.currentFamily.id) as unknown as any[]
    buildTree()
  }
}

function buildTree() {
  const memberMap = new Map(members.value.map(m => [m.id, m]))
  const childrenMap = new Map<number, any[]>()
  
  members.value.forEach(member => {
    childrenMap.set(member.id, [])
  })

  const parentChildRels = relationships.value.filter(r => r.relationshipType === 'PARENT_CHILD')
  
  parentChildRels.forEach(rel => {
    const parentId = rel.member1Id
    const childId = rel.member2Id
    if (childrenMap.has(parentId)) {
      childrenMap.get(parentId)?.push(childId)
    }
  })

  const rootMembers = members.value.filter(m => {
    return !parentChildRels.some(r => r.member2Id === m.id)
  })

  function buildNode(memberId: number): any {
    const member = memberMap.get(memberId)
    const children = (childrenMap.get(memberId) || []).map(childId => buildNode(childId))
    
    return {
      id: memberId,
      name: member?.name || '未知',
      gender: member?.gender,
      children
    }
  }

  treeData.value = rootMembers.map(m => buildNode(m.id))
}

function getGenderIcon(gender: string) {
  return gender === 'MALE' ? '👨' : '👩'
}

function renderTree(props: { node: any }) {
  const { node } = props
  
  const children = node.children?.map((child: any) => 
    h('div', { class: 'tree-children' }, [
      h('div', { class: 'child-connector' }),
      renderTree({ node: child })
    ])
  ) || []

  return h('div', { class: 'tree-node-container' }, [
    h('div', { class: 'tree-node' }, [
      h('span', { class: 'gender-icon' }, node.gender === 'MALE' ? '👨' : '👩'),
      h('span', { class: 'node-name' }, node.name)
    ]),
    ...children
  ])
}

watch(() => familyStore.currentFamily, async () => {
  await loadData()
})

onMounted(async () => {
  await loadData()
})
</script>

<template>
  <div class="page-layout">
    <Sidebar />
    
    <main class="main-content">
      <div class="page-header">
        <h1>家族树</h1>
      </div>

      <div class="card tree-container">
        <div v-if="treeData.length === 0" class="empty-state">
          <el-icon size="64" class="empty-icon">Users</el-icon>
          <p>暂无家族成员数据</p>
          <p class="hint">请先添加家族成员并建立关系</p>
        </div>
        
        <div v-else class="tree">
          <div v-for="root in treeData" :key="root.id" class="tree-root">
            <div class="tree-node">
              <span class="gender-icon">{{ getGenderIcon(root.gender) }}</span>
              <span class="node-name">{{ root.name }}</span>
            </div>
            <div v-if="root.children.length > 0" class="tree-children">
              <template v-for="child in root.children" :key="child.id">
                <div class="child-connector"></div>
                <component :is="renderTree" :node="child" />
              </template>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
.page-layout {
  display: flex;
  min-height: 100vh;
}

.main-content {
  flex: 1;
  padding: 20px;
  background: #f5f5f5;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  font-size: 24px;
  margin: 0;
}

.card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.tree-container {
  min-height: 500px;
}

.empty-state {
  text-align: center;
  padding: 100px 0;
}

.empty-icon {
  color: #ccc;
  margin-bottom: 20px;
}

.hint {
  color: #999;
  font-size: 14px;
}

.tree {
  display: flex;
  justify-content: center;
  padding: 40px;
}

.tree-root {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.tree-node {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 16px 24px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  color: white;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.gender-icon {
  font-size: 32px;
  margin-bottom: 8px;
}

.node-name {
  font-weight: bold;
}

.tree-children {
  display: flex;
  gap: 40px;
  margin-top: 30px;
}

.tree-node-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.child-connector {
  width: 2px;
  height: 20px;
  background: #ddd;
  margin: 0 auto;
}
</style>