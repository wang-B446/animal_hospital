<template>
  <div>
    <el-card shadow="never">
      <template #header>
        <span>操作日志</span>
      </template>
      <el-table :data="tableData" border stripe style="width: 100%">
        <el-table-column prop="id" label="序号" width="60" align="center" />
        <el-table-column prop="username" label="操作人" width="100" />
        <el-table-column label="操作描述">
          <template #default="scope">
            {{ formatOperation(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column prop="operation" label="结果" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.operation === '成功' ? 'success' : 'danger'" size="small">{{ scope.row.operation }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="时间" width="180" />
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
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'

const tableData = ref([])

// 分页
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 模块名称映射（技术名称 → 中文）
const moduleMap = {
  'user': '用户管理', 'User': '用户管理',
  'animal': '动物档案', 'Animal': '动物档案',
  'owner': '主人信息', 'Owner': '主人信息',
  'doctor': '医生管理', 'Doctor': '医生管理',
  'appointment': '预约管理', 'Appointment': '预约管理',
  'medicalRecord': '就诊记录', 'MedicalRecord': '就诊记录',
  'operationLog': '操作日志', 'OperationLog': '操作日志'
}

// 方法名称映射
const methodMap = {
  'login': '登录', 'Login': '登录',
  'add': '新增', 'Add': '新增',
  'update': '修改', 'Update': '修改',
  'delete': '删除', 'Delete': '删除',
  'save': '保存', 'Save': '保存',
  'query': '查询', 'Query': '查询',
  'getList': '查询列表',
  'getById': '查看详情'
}

// 格式化操作描述，让医生能看懂
function formatOperation(row) {
  const module = moduleMap[row.module] || row.module || ''
  const method = methodMap[row.method] || row.method || ''

  let desc = ''
  if (module && method) {
    desc = `${module} - ${method}`
  } else if (row.operation) {
    desc = `执行了${row.operation === '成功' ? '' : '未成功的'}操作`
  } else {
    desc = '系统操作'
  }

  // 如果有参数，尝试提取关键信息
  if (row.params) {
    try {
      const params = typeof row.params === 'string' ? JSON.parse(row.params) : row.params
      if (params.name || params.username) {
        const target = params.name || params.username
        desc += `（${target}）`
      }
    } catch {}
  }

  return desc
}

onMounted(async () => {
  loadData()
})

const loadData = async () => {
  try {
    const res = await request.get('/log/page', {
      params: { pageNum: currentPage.value, pageSize: pageSize.value }
    })
    tableData.value = res.data.list || []
    total.value = res.data.total || 0
  } catch {}
}

const handleSizeChange = (val) => {
  pageSize.value = val
  currentPage.value = 1
  loadData()
}
</script>
