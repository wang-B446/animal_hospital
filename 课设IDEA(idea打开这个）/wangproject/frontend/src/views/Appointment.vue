<template>
  <div>
    <!-- 搜索和操作栏 -->
    <el-card shadow="never" style="margin-bottom: 20px">
      <el-row :gutter="20" align="middle">
        <el-col :span="6">
          <el-select v-model="searchStatus" placeholder="筛选状态" clearable @change="loadData">
            <el-option label="待确认" value="待确认" />
            <el-option label="已确认" value="已确认" />
            <el-option label="已取消" value="已取消" />
            <el-option label="已完成" value="已完成" />
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-button @click="loadData">刷新</el-button>
        </el-col>
        <el-col :span="14" style="text-align: right">
          <el-button type="success" @click="handleAdd">新增预约</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column prop="animalName" label="动物" width="120" />
        <el-table-column prop="ownerName" label="主人" width="120" />
        <el-table-column prop="doctorName" label="医生" width="120" />
        <el-table-column prop="appointmentDate" label="预约日期" width="120" align="center" />
        <el-table-column prop="appointmentTime" label="预约时间" width="100" align="center" />
        <el-table-column prop="reason" label="预约原因" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="statusType(scope.row.status)" size="small">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
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
      <el-form :model="form" label-width="90px" :rules="rules" ref="formRef">
        <el-form-item label="动物" prop="animalName">
          <el-input v-model="form.animalName" placeholder="请输入动物名称" />
        </el-form-item>
        <el-form-item label="种类" prop="species">
          <el-input v-model="form.species" placeholder="请输入动物种类（如：猫、狗、兔）" />
        </el-form-item>
        <el-form-item label="主人" prop="ownerName">
          <el-input v-model="form.ownerName" placeholder="请输入主人姓名" />
        </el-form-item>
        <el-form-item label="医生" prop="doctorId">
          <el-select v-model="form.doctorId" placeholder="请选择医生" style="width: 100%">
            <el-option v-for="d in doctorList" :key="d.id" :label="d.name" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="预约日期" prop="appointmentDate">
          <el-date-picker v-model="form.appointmentDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择日期" style="width: 100%" />
        </el-form-item>
        <el-form-item label="预约时间" prop="appointmentTime">
          <el-time-select v-model="form.appointmentTime" start="08:00" step="00:30" end="18:00" placeholder="选择时间" style="width: 100%" />
        </el-form-item>
        <el-form-item label="预约原因" prop="reason">
          <el-input v-model="form.reason" type="textarea" :rows="2" placeholder="请输入预约原因" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="待确认" value="待确认" />
            <el-option label="已确认" value="已确认" />
            <el-option label="已取消" value="已取消" />
            <el-option label="已完成" value="已完成" />
          </el-select>
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
const animalList = ref([])
const doctorList = ref([])
const searchStatus = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('新增预约')
const formRef = ref(null)
const form = ref({})

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const rules = {
  animalName: [{ required: true, message: '请输入动物名称', trigger: 'blur' }],
  doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }],
  appointmentDate: [{ required: true, message: '请选择预约日期', trigger: 'change' }],
  appointmentTime: [{ required: true, message: '请选择预约时间', trigger: 'change' }]
}

const statusType = (status) => {
  const map = { '待确认': 'warning', '已确认': '', '已取消': 'info', '已完成': 'success' }
  return map[status] || 'info'
}

const resetForm = () => ({
  id: null,
  animalName: '',
  species: '',
  ownerName: '',
  doctorId: null,
  appointmentDate: '',
  appointmentTime: '',
  reason: '',
  status: '待确认'
})

const loadData = async () => {
  const res = await request.get('/appointment/page', {
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

const loadSelectData = async () => {
  const [animalRes, doctorRes] = await Promise.all([
    request.get('/animal'),
    request.get('/doctor')
  ])
  animalList.value = animalRes.data
  doctorList.value = doctorRes.data
}

const handleAdd = () => {
  form.value = resetForm()
  dialogTitle.value = '新增预约'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  form.value = { ...row }
  dialogTitle.value = '编辑预约'
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  if (form.value.id) {
    await request.put('/appointment', form.value)
    ElMessage.success('修改成功')
  } else {
    await request.post('/appointment', form.value)
    ElMessage.success('添加成功')
  }
  dialogVisible.value = false
  loadData()
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该预约吗？', '提示', { type: 'warning' }).then(async () => {
    await request.delete(`/appointment/${id}`)
    ElMessage.success('删除成功')
    loadData()
  })
}

onMounted(() => {
  loadData()
  loadSelectData()
})
</script>
