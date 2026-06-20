<template>
  <div>
    <el-card shadow="never">
      <template #header>
        <div style="display: flex; align-items: center;">
          <el-icon style="margin-right: 8px; font-size: 20px; color: #409EFF;">
            <svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg">
              <path fill="currentColor" d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372zM480 352h92c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8h-92c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8zm0 320h92c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8h-92c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8zm-168-160h92c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8h-92c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8zm336 0h92c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8h-92c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8z"/>
            </svg>
          </el-icon>
          <span style="font-size: 16px; font-weight: bold;">AI智能诊疗建议</span>
        </div>
      </template>

      <el-alert
        title="温馨提示"
        type="info"
        :closable="false"
        show-icon
        style="margin-bottom: 20px;"
      >
        <template #default>
          本功能由DeepSeek AI驱动，可根据动物症状提供初步的医疗建议供参考，但不能替代专业兽医诊断。如遇紧急情况请及时就医。
        </template>
      </el-alert>

      <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="动物名称" prop="animalName">
              <el-input v-model="form.animalName" placeholder="请输入动物名称（可选）" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="动物种类" prop="animalType">
              <el-select v-model="form.animalType" placeholder="请选择动物种类（可选）" style="width: 100%">
                <el-option label="狗" value="狗" />
                <el-option label="猫" value="猫" />
                <el-option label="兔子" value="兔子" />
                <el-option label="鸟类" value="鸟类" />
                <el-option label="仓鼠" value="仓鼠" />
                <el-option label="其他" value="其他" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="症状描述" prop="symptom">
          <el-input
            v-model="form.symptom"
            type="textarea"
            :rows="4"
            placeholder="请详细描述动物的症状，例如：精神不振、食欲下降、呕吐、腹泻、咳嗽等..."
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleGetAdvice">
            <el-icon v-if="!loading" style="margin-right: 4px;">
              <svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z"/></svg>
            </el-icon>
            {{ loading ? 'AI正在分析中...' : '获取AI建议' }}
          </el-button>
          <el-button type="success" @click="handleOpenAnimalDialog">
            <el-icon style="margin-right: 4px;">
              <svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M192 192h128v128H192V192zm256 0h128v128H448V192zm256 0h128v128H704V192zM192 448h128v128H192V448zm256 0h128v128H448V448zm256 0h128v128H704V448zM192 704h128v128H192V704zm256 0h128v128H448V704zm256 0h128v128H704V704z"/></svg>
            </el-icon>
            导入已有动物
          </el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 选择动物对话框 -->
    <el-dialog v-model="animalDialogVisible" title="选择动物" width="600px" center>
      <el-input
        v-model="animalSearchKeyword"
        placeholder="搜索动物名称..."
        clearable
        style="margin-bottom: 16px;"
      >
        <template #prefix>
          <el-icon><svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M909.6 854.5L649.9 594.8C690.2 542.7 712 479 712 412c0-165.7-134.3-300-300-300S112 246.3 112 412s134.3 300 300 300c67 0 130.7-21.8 182.8-62.1l259.7 259.7c3.2 3.2 8.4 3.2 11.6 0l43.5-43.5c3.2-3.1 3.2-8.3 0-11.6zM412 632c-121.5 0-220-98.5-220-220s98.5-220 220-220 220 98.5 220 220-98.5 220-220 220z"/></svg></el-icon>
        </template>
      </el-input>
      <el-table :data="filteredAnimalList" border stripe highlight-current-row @current-change="handleSelectAnimal" max-height="400">
        <el-table-column prop="name" label="名称" width="120" />
        <el-table-column prop="species" label="种类" width="100" />
        <el-table-column prop="breed" label="品种" width="120" />
        <el-table-column prop="gender" label="性别" width="80" align="center" />
        <el-table-column prop="age" label="年龄" width="80" align="center">
          <template #default="scope">{{ scope.row.age }}岁</template>
        </el-table-column>
        <el-table-column prop="symptom" label="症状" show-overflow-tooltip />
        <el-table-column prop="ownerName" label="主人" show-overflow-tooltip />
      </el-table>
      <template #footer>
        <el-button @click="animalDialogVisible = false">取消</el-button>
        <el-button type="primary" :disabled="!selectedAnimal" @click="handleImportAnimal">确认导入</el-button>
      </template>
    </el-dialog>

    <!-- AI建议结果 -->
    <el-card shadow="never" v-if="advice" style="margin-top: 20px;">
      <template #header>
        <div style="display: flex; align-items: center; justify-content: space-between;">
          <span style="font-size: 15px; font-weight: bold; color: #409EFF;">AI诊断建议</span>
          <el-tag type="success" size="small">DeepSeek</el-tag>
        </div>
      </template>
      <div class="advice-content" v-html="formattedAdvice"></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const formRef = ref(null)
const loading = ref(false)
const advice = ref('')

// 动物选择对话框相关
const animalDialogVisible = ref(false)
const animalList = ref([])
const animalSearchKeyword = ref('')
const selectedAnimal = ref(null)

const form = ref({
  animalName: '',
  animalType: '',
  symptom: ''
})

const rules = {
  symptom: [{ required: true, message: '请输入症状描述', trigger: 'blur' }]
}

// 过滤后的动物列表
const filteredAnimalList = computed(() => {
  if (!animalSearchKeyword.value) return animalList.value
  const keyword = animalSearchKeyword.value.toLowerCase()
  return animalList.value.filter(a =>
    (a.name && a.name.toLowerCase().includes(keyword)) ||
    (a.species && a.species.toLowerCase().includes(keyword)) ||
    (a.ownerName && a.ownerName.toLowerCase().includes(keyword))
  )
})

// 加载动物列表
const loadAnimalList = async () => {
  try {
    const res = await request.get('/animal')
    animalList.value = res.data || []
  } catch (error) {
    console.error('加载动物列表失败', error)
  }
}

// 打开动物选择对话框
const handleOpenAnimalDialog = async () => {
  if (animalList.value.length === 0) {
    await loadAnimalList()
  }
  animalSearchKeyword.value = ''
  selectedAnimal.value = null
  animalDialogVisible.value = true
}

// 选择动物行
const handleSelectAnimal = (row) => {
  selectedAnimal.value = row
}

// 确认导入选中的动物
const handleImportAnimal = () => {
  if (!selectedAnimal.value) return
  form.value.animalName = selectedAnimal.value.name || ''
  form.value.animalType = selectedAnimal.value.species || ''
  form.value.symptom = selectedAnimal.value.symptom || ''
  animalDialogVisible.value = false
  ElMessage.success(`已导入：${selectedAnimal.value.name}`)
}

const formattedAdvice = computed(() => {
  if (!advice.value) return ''
  return advice.value
    .replace(/\n/g, '<br>')
    .replace(/(\d+\.\s)/g, '<strong>$1</strong>')
    .replace(/【(.*?)】/g, '<span style="color:#409EFF;font-weight:bold;">【$1】</span>')
})

const handleGetAdvice = async () => {
  await formRef.value.validate()
  loading.value = true
  advice.value = ''

  try {
    const res = await request.post('/ai-advice', form.value)
    if (res.code === 200) {
      advice.value = res.data
      ElMessage.success('AI建议获取成功')
    } else {
      ElMessage.error(res.message || '获取失败')
    }
  } catch (error) {
    ElMessage.error('请求失败，请检查网络连接')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  form.value = {
    animalName: '',
    animalType: '',
    symptom: ''
  }
  advice.value = ''
  formRef.value?.resetFields()
}
</script>

<style scoped>
.advice-content {
  line-height: 1.8;
  color: #333;
  font-size: 14px;
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 6px;
}
</style>
