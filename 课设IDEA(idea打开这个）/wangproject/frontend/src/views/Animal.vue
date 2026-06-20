<template>
  <div>
    <!-- 搜索和操作栏 -->
    <el-card shadow="never" style="margin-bottom: 20px">
      <el-row :gutter="20" align="middle">
        <el-col :span="6">
          <el-input v-model="searchName" placeholder="请输入动物名称搜索" clearable @clear="loadData" @keyup.enter="handleSearch" />
        </el-col>
        <el-col :span="10">
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="loadData">刷新</el-button>
          <el-button type="warning" @click="handleExport">导出Excel</el-button>
        </el-col>
        <el-col :span="8" style="text-align: right">
          <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">批量删除({{ selectedIds.length }})</el-button>
          <el-button type="success" @click="handleAdd">新增动物</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <el-table :data="tableData" border stripe style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="45" align="center" />
        <el-table-column prop="id" label="ID" width="55" align="center" />
        <el-table-column label="照片" width="80" align="center">
          <template #default="scope">
            <el-image v-if="scope.row.photo" :src="scope.row.photo" style="width: 40px; height: 40px; border-radius: 4px;" fit="cover" :preview-src-list="[scope.row.photo]" />
            <span v-else style="color: #c0c4cc;">无</span>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" width="100" />
        <el-table-column prop="species" label="种类" width="80" />
        <el-table-column prop="breed" label="品种" min-width="120" />
        <el-table-column prop="gender" label="性别" width="60" align="center" />
        <el-table-column prop="age" label="年龄(月)" width="80" align="center" />
        <el-table-column prop="weight" label="体重(kg)" width="85" align="center" />
        <el-table-column prop="ownerName" label="主人" width="100" />
        <el-table-column prop="nextVaccineDate" label="疫苗到期" width="105" align="center">
          <template #default="scope">
            <el-tag v-if="scope.row.nextVaccineDate && new Date(scope.row.nextVaccineDate) < new Date()" type="danger" size="small">{{ scope.row.nextVaccineDate }}</el-tag>
            <span v-else>{{ scope.row.nextVaccineDate || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="symptom" label="症状" show-overflow-tooltip width="120" />
        <el-table-column label="操作" align="center" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 16px; display: flex; justify-content: flex-end;">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="loadData"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="550px">
      <el-form :model="form" label-width="90px" :rules="rules" ref="formRef">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入动物名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="种类" prop="species">
              <el-input v-model="form.species" placeholder="请输入种类（如：猫、狗、兔）" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="品种" prop="breed">
              <el-input v-model="form.breed" placeholder="请输入品种" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-radio-group v-model="form.gender">
                <el-radio label="公" /><el-radio label="母" /><el-radio label="未知" />
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="10">
            <el-form-item label="年龄(月)" prop="age">
              <el-input-number v-model="form.age" :min="0" :max="600" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="体重(kg)" prop="weight">
              <el-input-number v-model="form.weight" :min="0" :max="500" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="主人" prop="ownerName">
              <el-input v-model="form.ownerName" placeholder="主人姓名" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="照片" prop="photo">
          <el-upload
            class="avatar-uploader"
            action="/api/upload"
            :show-file-list="false"
            :on-success="handleUploadSuccess"
            :on-error="handleUploadError"
            :before-upload="beforeUpload"
            accept="image/*"
            name="file"
          >
            <img v-if="form.photo" :src="form.photo" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="最近疫苗日" prop="vaccineDate">
              <el-date-picker v-model="form.vaccineDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下次疫苗日" prop="nextVaccineDate">
              <el-date-picker v-model="form.nextVaccineDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="症状" prop="symptom">
          <el-input v-model="form.symptom" type="textarea" :rows="2" placeholder="请输入当前症状（可选）" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const searchName = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('新增动物')
const formRef = ref(null)
const form = ref({})
const selectedIds = ref([])

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const rules = {
  name: [{ required: true, message: '请输入动物名称', trigger: 'blur' }],
  species: [{ required: true, message: '请选择种类', trigger: 'change' }]
}

const resetForm = () => ({
  id: null, name: '', species: '', breed: '', gender: '未知',
  age: 0, weight: 0, ownerName: '', symptom: '', photo: '',
  vaccineDate: '', nextVaccineDate: ''
})

const loadData = async () => {
  try {
    const res = await request.get('/animal/page', {
      params: { pageNum: currentPage.value, pageSize: pageSize.value }
    })
    tableData.value = res.data.list
    total.value = res.data.total
  } catch (e) {
    console.error('加载数据失败', e)
  }
}

const handleSearch = async () => {
  currentPage.value = 1
  if (!searchName.value) return loadData()
  const res = await request.get('/animal/search/page', {
    params: { name: searchName.value, pageNum: currentPage.value, pageSize: pageSize.value }
  })
  tableData.value = res.data.list
  total.value = res.data.total
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  loadData()
}

const handleExport = async () => {
  const res = await request.get('/export/animals', { responseType: 'blob' })
  const url = window.URL.createObjectURL(new Blob([res]))
  const link = document.createElement('a')
  link.href = url
  link.setAttribute('download', '动物列表.xlsx')
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  ElMessage.success('导出成功')
}

const handleSelectionChange = (rows) => {
  selectedIds.value = rows.map(r => r.id)
}

const handleBatchDelete = () => {
  ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条记录吗？`, '批量删除', { type: 'warning' }).then(async () => {
    for (const id of selectedIds.value) {
      await request.delete(`/animal/${id}`)
    }
    ElMessage.success(`成功删除 ${selectedIds.value.length} 条记录`)
    loadData()
  })
}

const handleAdd = () => {
  form.value = resetForm()
  dialogTitle.value = '新增动物'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  const data = { ...row }
  if (data.age == null) data.age = 0
  if (data.weight == null) data.weight = 0
  if (!data.ownerName) data.ownerName = ''
  form.value = data
  dialogTitle.value = '编辑动物'
  dialogVisible.value = true
}

const handleUploadSuccess = (res) => {
  form.value.photo = res.url
  ElMessage.success('照片上传成功')
}

const handleUploadError = () => {
  ElMessage.error('照片上传失败')
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  if (!isImage) ElMessage.error('只能上传图片文件!')
  if (!isLt5M) ElMessage.error('图片大小不能超过 5MB!')
  return isImage && isLt5M
}

const handleSubmit = async () => {
  await formRef.value.validate()
  let ownerId = null
  if (form.value.ownerName) {
    const ownerRes = await request.get('/owner/search', { params: { name: form.value.ownerName } })
    const found = ownerRes.data.find(o => o.name === form.value.ownerName)
    if (found) {
      ownerId = found.id
    } else {
      const newOwnerRes = await request.post('/owner', { name: form.value.ownerName })
      ownerId = newOwnerRes.data?.id
    }
  }
  const submitData = { ...form.value, ownerId }
  delete submitData.ownerName
  if (form.value.id) {
    await request.put('/animal', submitData)
    ElMessage.success('修改成功')
  } else {
    await request.post('/animal', submitData)
    ElMessage.success('添加成功')
  }
  dialogVisible.value = false
  loadData()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该动物记录吗？', '提示', { type: 'warning' }).then(async () => {
    await request.delete(`/animal/${id}`)
    ElMessage.success('删除成功')
    loadData()
  })
}

onMounted(() => { loadData() })
</script>

<style scoped>
.avatar-uploader .avatar {
  width: 78px;
  height: 78px;
  display: block;
  border-radius: 6px;
  object-fit: cover;
}
.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: border-color 0.2s;
}
.avatar-uploader :deep(.el-upload:hover) {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 78px;
  height: 78px;
  text-align: center;
  line-height: 78px;
}
</style>
