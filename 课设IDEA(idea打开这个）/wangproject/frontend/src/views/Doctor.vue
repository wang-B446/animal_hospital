<template>
  <div>
    <!-- 搜索和操作栏 -->
    <el-card shadow="never" style="margin-bottom: 20px">
      <el-row :gutter="20" align="middle">
        <el-col :span="6">
          <el-input v-model="searchName" placeholder="请输入姓名搜索" clearable @clear="loadData" @keyup.enter="handleSearch" />
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="loadData">刷新</el-button>
        </el-col>
        <el-col :span="14" style="text-align: right">
          <el-button type="success" @click="handleAdd">新增医生</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="title" label="职称" width="140" />
        <el-table-column prop="specialty" label="专长" min-width="180" />
        <el-table-column prop="phone" label="电话" width="160" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="180" align="center">
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="form" label-width="80px" :rules="rules" ref="formRef">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="职称" prop="title">
          <el-select v-model="form.title" placeholder="请选择职称" style="width: 100%">
            <el-option label="主任医师" value="主任医师" />
            <el-option label="副主任医师" value="副主任医师" />
            <el-option label="主治医师" value="主治医师" />
            <el-option label="住院医师" value="住院医师" />
          </el-select>
        </el-form-item>
        <el-form-item label="专长" prop="specialty">
          <el-input v-model="form.specialty" placeholder="请输入专长" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话" />
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
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const searchName = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('新增医生')
const formRef = ref(null)
const form = ref({})

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }]
}

const resetForm = () => ({
  id: null,
  name: '',
  title: '',
  specialty: '',
  phone: ''
})

const loadData = async () => {
  const res = await request.get('/doctor/page', {
    params: { pageNum: currentPage.value, pageSize: pageSize.value }
  })
  tableData.value = res.data.list
  total.value = res.data.total
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  loadData()
}

const handleSearch = async () => {
  if (!searchName.value) return loadData()
  const res = await request.get('/doctor/search/page', {
    params: { name: searchName.value, pageNum: 1, pageSize: pageSize.value }
  })
  tableData.value = res.data.list
  total.value = res.data.total
}

const handleAdd = () => {
  form.value = resetForm()
  dialogTitle.value = '新增医生'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.value = { ...row }
  dialogTitle.value = '编辑医生'
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  if (form.value.id) {
    await request.put('/doctor', form.value)
    ElMessage.success('修改成功')
  } else {
    await request.post('/doctor', form.value)
    ElMessage.success('添加成功')
  }
  dialogVisible.value = false
  loadData()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该医生记录吗？', '提示', { type: 'warning' }).then(async () => {
    await request.delete(`/doctor/${id}`)
    ElMessage.success('删除成功')
    loadData()
  })
}

onMounted(() => {
  loadData()
})
</script>
