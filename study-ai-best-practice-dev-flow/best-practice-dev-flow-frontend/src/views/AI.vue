<script setup lang="ts">
import { ref } from 'vue'
import { useFamilyStore } from '@/stores/family'
import { aiAPI } from '@/api'
import Sidebar from '@/components/Sidebar.vue'

const familyStore = useFamilyStore()

const predictionResult = ref<any>(null)
const storyResult = ref<any>(null)
const loadingPrediction = ref(false)
const loadingStory = ref(false)

const storyTypeOptions = [
  { value: 'general', label: '综合故事' },
  { value: 'origin', label: '家族起源' },
  { value: 'timeline', label: '家族时间线' },
  { value: 'achievements', label: '家族成就' }
]

const selectedStoryType = ref('general')

async function predictRelationships() {
  if (!familyStore.currentFamily) return
  
  loadingPrediction.value = true
  try {
    predictionResult.value = await aiAPI.predictRelationships(familyStore.currentFamily.id)
  } catch (e) {
    console.error('Prediction error:', e)
  } finally {
    loadingPrediction.value = false
  }
}

async function generateStory() {
  if (!familyStore.currentFamily) return
  
  loadingStory.value = true
  try {
    storyResult.value = await aiAPI.generateStory(familyStore.currentFamily.id, selectedStoryType.value)
  } catch (e) {
    console.error('Story generation error:', e)
  } finally {
    loadingStory.value = false
  }
}
</script>

<template>
  <div class="page-layout">
    <Sidebar />
    
    <main class="main-content">
      <div class="page-header">
        <h1>AI 功能</h1>
      </div>

      <div class="card ai-section">
        <h2 class="section-title">
          <el-icon>Sparkles</el-icon>
          关系预测
        </h2>
        <p class="section-desc">基于家族成员的年龄和性别信息，AI 自动推断成员之间可能的关系</p>
        
        <el-button 
          type="primary" 
          :loading="loadingPrediction"
          @click="predictRelationships"
          :disabled="!familyStore.currentFamily"
        >
          开始预测
        </el-button>

        <div v-if="predictionResult" class="prediction-result">
          <h3>预测结果</h3>
          
          <div v-if="predictionResult.missingDataSuggestions && predictionResult.missingDataSuggestions.length > 0" class="suggestions">
            <h4>数据完善建议：</h4>
            <ul>
              <li v-for="(suggestion, index) in predictionResult.missingDataSuggestions" :key="index">
                {{ suggestion }}
              </li>
            </ul>
          </div>

          <div v-if="predictionResult.predictedRelationships && predictionResult.predictedRelationships.length > 0">
            <h4>预测的关系：</h4>
            <div class="relationship-list">
              <div 
                v-for="(rel, index) in predictionResult.predictedRelationships" 
                :key="index" 
                class="relationship-item"
              >
                <div class="relation-info">
                  <span class="member-name">{{ rel.member1.name }}</span>
                  <span class="relation-type">{{ rel.relationship }}</span>
                  <span class="member-name">{{ rel.member2.name }}</span>
                </div>
                <div class="confidence">
                  置信度: {{ Math.round(rel.confidence * 100) }}%
                </div>
                <div class="reasoning">
                  <strong>推理依据：</strong>
                  <ul>
                    <li v-for="(reason, idx) in rel.reasoning" :key="idx">{{ reason }}</li>
                  </ul>
                </div>
                <div class="ai-comment">{{ rel.aiComment }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="card ai-section">
        <h2 class="section-title">
          <el-icon>BookOpen</el-icon>
          家族故事生成
        </h2>
        <p class="section-desc">根据家族成员信息，AI 自动生成家族故事</p>
        
        <div class="story-type-selector">
          <el-select v-model="selectedStoryType">
            <el-option v-for="opt in storyTypeOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
          </el-select>
        </div>

        <el-button 
          type="primary" 
          :loading="loadingStory"
          @click="generateStory"
          :disabled="!familyStore.currentFamily"
        >
          生成故事
        </el-button>

        <div v-if="storyResult" class="story-result">
          <h3>{{ storyResult.title }}</h3>
          <div class="story-content">{{ storyResult.content }}</div>
          <div class="ai-comment">{{ storyResult.aiComment }}</div>
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
  margin-bottom: 20px;
}

.ai-section {
  position: relative;
  overflow: hidden;
}

.ai-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
}

.section-title {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 18px;
  margin-bottom: 10px;
  color: #333;
}

.section-desc {
  color: #999;
  font-size: 14px;
  margin-bottom: 20px;
}

.story-type-selector {
  margin-bottom: 20px;
  width: 200px;
}

.prediction-result, .story-result {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.prediction-result h3, .story-result h3 {
  font-size: 16px;
  margin-bottom: 16px;
  color: #333;
}

.suggestions {
  background: #fff7e6;
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 16px;
}

.suggestions h4 {
  font-size: 14px;
  margin-bottom: 8px;
  color: #d48806;
}

.suggestions ul {
  margin: 0;
  padding-left: 20px;
}

.suggestions li {
  color: #666;
  font-size: 14px;
}

.relationship-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.relationship-item {
  background: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
}

.relation-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.member-name {
  font-weight: bold;
  color: #333;
}

.relation-type {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 14px;
}

.confidence {
  color: #67c23a;
  font-weight: bold;
  margin-bottom: 8px;
}

.reasoning {
  color: #666;
  font-size: 14px;
  margin-bottom: 8px;
}

.reasoning ul {
  margin: 4px 0 0 20px;
  padding: 0;
}

.reasoning li {
  margin-bottom: 4px;
}

.ai-comment {
  background: #e8f4fd;
  border-left: 4px solid #409eff;
  padding: 12px;
  font-size: 14px;
  color: #666;
}

.story-content {
  white-space: pre-wrap;
  font-size: 16px;
  line-height: 1.8;
  color: #333;
  margin-bottom: 16px;
}
</style>