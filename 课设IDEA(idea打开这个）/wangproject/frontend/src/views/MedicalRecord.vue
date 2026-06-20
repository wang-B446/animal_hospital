<template>
  <div>
    <!-- 搜索和操作栏 -->
    <el-card shadow="never" style="margin-bottom: 20px">
      <el-row :gutter="20" align="middle">
        <el-col :span="6">
          <el-input v-model="searchKeyword" placeholder="请输入症状或动物名称搜索" clearable @clear="loadData" @keyup.enter="handleSearch" />
        </el-col>
        <el-col :span="10">
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="loadData">刷新</el-button>
          <el-button type="warning" @click="handleExport">导出Excel</el-button>
        </el-col>
        <el-col :span="8" style="text-align: right">
          <el-button type="danger" :disabled="selectedIds.length === 0" @click="handleBatchDelete">批量删除({{ selectedIds.length }})</el-button>
          <el-button type="success" @click="handleAdd">新增就诊记录</el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <el-table :data="tableData" border stripe style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="45" align="center" />
        <el-table-column prop="id" label="ID" width="55" align="center" />
        <el-table-column prop="animalName" label="动物" width="100" />
        <el-table-column prop="ownerName" label="主人" width="100">
          <template #default="scope">{{ scope.row.animalName ? (scope.row.ownerName || '-') : '-' }}</template>
        </el-table-column>
        <el-table-column prop="doctorName" label="医生" width="90" />
        <el-table-column prop="visitDate" label="就诊日期" width="110" align="center" />
        <el-table-column prop="symptom" label="症状" width="150" show-overflow-tooltip />
        <el-table-column prop="diagnosis" label="诊断" width="150" show-overflow-tooltip />
        <el-table-column prop="treatment" label="治疗方案" show-overflow-tooltip min-width="150" />
        <el-table-column prop="cost" label="费用(元)" width="90" align="center" />
        <el-table-column prop="paymentStatus" label="缴费状态" width="95" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.paymentStatus === '已缴费' ? 'success' : 'danger'" size="small">
              {{ scope.row.paymentStatus || '未缴费' }}
            </el-tag>
            <el-button v-if="scope.row.paymentStatus !== '已缴费'" size="small" type="primary" link style="margin-left: 4px;" @click="handlePay(scope.row)">缴费</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="scope">
            <div style="display: flex; justify-content: center; gap: 4px;">
              <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button size="small" type="success" @click="handlePrint(scope.row)">打印</el-button>
              <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
            </div>
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
            <el-form-item label="动物" prop="animalId">
              <el-select v-model="form.animalId" placeholder="请选择动物" style="width: 100%">
                <el-option v-for="a in animalList" :key="a.id" :label="a.name" :value="a.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="医生" prop="doctorId">
              <el-select v-model="form.doctorId" placeholder="请选择医生" style="width: 100%">
                <el-option v-for="d in doctorList" :key="d.id" :label="d.name" :value="d.id" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="就诊日期" prop="visitDate">
              <el-date-picker v-model="form.visitDate" type="date" value-format="YYYY-MM-DD" placeholder="请选择日期" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="费用(元)" prop="cost">
              <el-input-number v-model="form.cost" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="症状" prop="symptom">
          <el-input v-model="form.symptom" type="textarea" :rows="2" placeholder="请输入症状" />
        </el-form-item>
        <el-form-item label="诊断" prop="diagnosis">
          <el-input v-model="form.diagnosis" type="textarea" :rows="2" placeholder="请输入诊断" />
        </el-form-item>
        <el-form-item label="治疗方案" prop="treatment">
          <el-input v-model="form.treatment" type="textarea" :rows="2" placeholder="请输入治疗方案" />
        </el-form-item>
        <el-form-item label="缴费状态" prop="paymentStatus">
          <el-radio-group v-model="form.paymentStatus">
            <el-radio label="未缴费" /><el-radio label="已缴费" />
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 打印区域（隐藏） -->
    <div ref="printRef" style="display: none;">
      <div style="padding: 40px; font-family: SimSun, serif;">
        <h2 style="text-align: center; margin-bottom: 30px;">动物医院就诊单据</h2>
        <table style="width: 100%; border-collapse: collapse; font-size: 14px;">
          <tr><td style="border:1px solid #333; padding:8px; width:120px;">动物名称：</td><td style="border:1px solid #333; padding:8px;">{{ printData.animalName }}</td></tr>
          <tr><td style="border:1px solid #333; padding:8px;">主人：</td><td style="border:1px solid #333; padding:8px;">{{ printData.ownerName }}</td></tr>
          <tr><td style="border:1px solid #333; padding:8px;">医生：</td><td style="border:1px solid #333; padding:8px;">{{ printData.doctorName }}</td></tr>
          <tr><td style="border:1px solid #333; padding:8px;">就诊日期：</td><td style="border:1px solid #333; padding:8px;">{{ printData.visitDate }}</td></tr>
          <tr><td style="border:1px solid #333; padding:8px;">症状：</td><td style="border:1px solid #333; padding:8px;">{{ printData.symptom }}</td></tr>
          <tr><td style="border:1px solid #333; padding:8px;">诊断：</td><td style="border:1px solid #333; padding:8px;">{{ printData.diagnosis }}</td></tr>
          <tr><td style="border:1px solid #333; padding:8px;">治疗方案：</td><td style="border:1px solid #333; padding:8px;">{{ printData.treatment }}</td></tr>
          <tr><td style="border:1px solid #333; padding:8px;">费用(元)：</td><td style="border:1px solid #333; padding:8px; font-weight:bold;">{{ printData.cost }}</td></tr>
          <tr><td style="border:1px solid #333; padding:8px;">缴费状态：</td><td style="border:1px solid #333; padding:8px;">{{ printData.paymentStatus || '未缴费' }}</td></tr>
        </table>
        <p style="margin-top: 30px; text-align: right;">打印时间：{{ new Date().toLocaleString() }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const animalList = ref([])
const doctorList = ref([])
const searchKeyword = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('新增就诊记录')
const formRef = ref(null)
const form = ref({})
const selectedIds = ref([])
const printRef = ref(null)
const printData = reactive({})

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const rules = {
  animalId: [{ required: true, message: '请选择动物', trigger: 'change' }],
  doctorId: [{ required: true, message: '请选择医生', trigger: 'change' }],
  visitDate: [{ required: true, message: '请选择就诊日期', trigger: 'change' }]
}

const resetForm = () => ({
  id: null, animalId: null, doctorId: null, visitDate: '',
  symptom: '', diagnosis: '', treatment: '', cost: 0, paymentStatus: '未缴费'
})

const loadData = async () => {
  const res = await request.get('/medical-record/page', {
    params: { pageNum: currentPage.value, pageSize: pageSize.value }
  })
  tableData.value = res.data.list
  total.value = res.data.total
}

const handleSearch = async () => {
  currentPage.value = 1
  if (!searchKeyword.value) return loadData()
  const res = await request.get('/medical-record/search/page', {
    params: { keyword: searchKeyword.value, pageNum: currentPage.value, pageSize: pageSize.value }
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
  const res = await request.get('/export/records', { responseType: 'blob' })
  const url = window.URL.createObjectURL(new Blob([res]))
  const link = document.createElement('a')
  link.href = url
  link.setAttribute('download', '就诊记录.xlsx')
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  ElMessage.success('导出成功')
}

const handleSelectionChange = (rows) => { selectedIds.value = rows.map(r => r.id) }

const handleBatchDelete = () => {
  ElMessageBox.confirm(`确定删除选中的 ${selectedIds.value.length} 条记录吗？`, '批量删除', { type: 'warning' }).then(async () => {
    for (const id of selectedIds.value) await request.delete(`/medical-record/${id}`)
    ElMessage.success(`成功删除 ${selectedIds.value.length} 条记录`)
    loadData()
  })
}

const loadSelectData = async () => {
  const [animalRes, doctorRes] = await Promise.all([request.get('/animal'), request.get('/doctor')])
  animalList.value = animalRes.data
  doctorList.value = doctorRes.value
}

const handleAdd = () => { form.value = resetForm(); dialogTitle.value = '新增就诊记录'; dialogVisible.value = true }
const handleEdit = (row) => { form.value = { ...row }; dialogTitle.value = '编辑就诊记录'; dialogVisible.value = true }

const handleSubmit = async () => {
  await formRef.value.validate()
  if (!form.value.paymentStatus) form.value.paymentStatus = '未缴费'
  if (form.value.id) {
    await request.put('/medical-record', form.value)
    ElMessage.success('修改成功')
  } else {
    await request.post('/medical-record', form.value)
    ElMessage.success('添加成功')
  }
  dialogVisible.value = false
  loadData()
}

const handlePay = async (row) => {
  await request.put('/medical-record', { ...row, paymentStatus: '已缴费' })
  ElMessage.success('缴费成功')
  loadData()
}

const handlePrint = (row) => {
  Object.assign(printData, row)
  // 获取主人名
  const animal = animalList.value.find(a => a.id === row.animalId)
  if (animal) printData.ownerName = animal.ownerName
  setTimeout(() => {
    const content = printRef.value.innerHTML
    const win = window.open('', '_blank')
    win.document.write(`<html><head><title>就诊单据</title><style>body{font-family:SimSun,serif;padding:20px;} table{width:100%;border-collapse:collapse;font-size:14px} td{border:1px solid #333;padding:8px}</style></head><body>${content}<script>window.print();window.close();<\/script></body></html>`)
    win.document.close()
  }, 50)
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该就诊记录吗？', '提示', { type: 'warning' }).then(async () => {
    await request.delete(`/medical-record/${id}`)
    ElMessage.success('删除成功')
    loadData()
  })
}

onMounted(() => { loadData(); loadSelectData() })
</script>
