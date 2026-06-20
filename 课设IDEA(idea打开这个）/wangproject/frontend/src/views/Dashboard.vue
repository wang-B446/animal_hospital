<template>
  <div>
    <!-- 统计卡片 -->
    <el-row :gutter="20" style="margin-bottom: 20px">
      <el-col :span="4" v-for="item in statCards" :key="item.title">
        <el-card shadow="hover" :body-style="{ padding: '20px' }" class="stat-card">
          <div class="stat-card-content">
            <div class="stat-card-icon" :style="{ background: item.color }">
              <span>{{ item.icon }}</span>
            </div>
            <div class="stat-card-info">
              <div class="stat-card-value">{{ item.value }}</div>
              <div class="stat-card-title">{{ item.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="never">
          <template #header><span>动物种类分布</span></template>
          <div ref="speciesChartRef" style="height: 350px"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <template #header><span>月度就诊趋势</span></template>
          <div ref="monthChartRef" style="height: 350px"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 疫苗到期提醒 -->
    <el-card shadow="never" style="margin-top: 20px">
      <template #header><span>疫苗即将到期提醒</span></template>
      <el-table :data="vaccineAlerts" border stripe size="small" v-if="vaccineAlerts.length > 0">
        <el-table-column prop="name" label="动物名称" width="120" />
        <el-table-column prop="species" label="种类" width="80" />
        <el-table-column prop="ownerName" label="主人" width="120" />
        <el-table-column prop="nextVaccineDate" label="下次疫苗日期" width="140" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag type="danger" v-if="isExpired(scope.row.nextVaccineDate)">已过期</el-tag>
            <el-tag type="warning" v-else-if="isNearExpiry(scope.row.nextVaccineDate)">即将到期</el-tag>
            <el-tag type="success" v-else>正常</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-else description="暂无疫苗提醒数据（请在动物管理中设置疫苗日期）" />
    </el-card>

    <!-- 最近操作日志 -->
    <el-card shadow="never" style="margin-top: 20px">
      <template #header>
        <span>最近操作日志</span>
        <el-button size="small" style="float: right" @click="$router.push('/log')">查看全部</el-button>
      </template>
      <el-table :data="logs.slice(0, 10)" border stripe size="small">
        <el-table-column prop="module" label="模块" width="100" />
        <el-table-column prop="method" label="操作" width="100" />
        <el-table-column prop="operation" label="结果" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.operation === '成功' ? 'success' : 'danger'" size="small">{{ scope.row.operation }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="时间" width="180" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import request from '../utils/request'
import * as echarts from 'echarts'

const speciesChartRef = ref(null)
const monthChartRef = ref(null)
const vaccineAlerts = ref([])
const logs = ref([])
const statCards = reactive([
  { title: '动物总数', value: 0, icon: '🐾', color: '#409EFF' },
  { title: '主人总数', value: 0, icon: '👤', color: '#67C23A' },
  { title: '医生总数', value: 0, icon: '⚕️', color: '#E6A23C' },
  { title: '就诊记录', value: 0, icon: '📋', color: '#F56C6C' },
  { title: '总收入(元)', value: 0, icon: '💰', color: '#909399' },
  { title: '今日就诊', value: 0, icon: '📅', color: '#9B59B6' }
])

const isExpired = (date) => {
  if (!date) return false
  return new Date(date) < new Date()
}

const isNearExpiry = (date) => {
  if (!date) return false
  const diff = (new Date(date) - new Date()) / (1000 * 60 * 60 * 24)
  return diff >= 0 && diff <= 30
}

const loadStatData = async () => {
  const res = await request.get('/stat')
  const data = res.data
  statCards[0].value = data.animalCount
  statCards[1].value = data.ownerCount
  statCards[2].value = data.doctorCount
  statCards[3].value = data.recordCount
  statCards[4].value = data.totalIncome
  statCards[5].value = data.todayRecordCount

  // 种类饼图
  const speciesChart = echarts.init(speciesChartRef.value)
  speciesChart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
    legend: { bottom: 0 },
    series: [{
      type: 'pie',
      radius: ['40%', '65%'],
      avoidLabelOverlap: true,
      data: data.speciesStats.map(s => ({ name: s.name, value: s.value }))
    }]
  })

  // 月度趋势图
  const monthChart = echarts.init(monthChartRef.value)
  monthChart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['就诊次数', '收入'], bottom: 0 },
    xAxis: { type: 'category', data: data.monthStats.map(m => m.month) },
    yAxis: [
      { type: 'value', name: '次数' },
      { type: 'value', name: '收入(元)' }
    ],
    series: [
      {
        name: '就诊次数', type: 'bar', data: data.monthStats.map(m => m.count),
        itemStyle: { color: '#409EFF' }
      },
      {
        name: '收入', type: 'line', yAxisIndex: 1,
        data: data.monthStats.map(m => m.income || 0),
        itemStyle: { color: '#67C23A' }
      }
    ]
  })

  window.addEventListener('resize', () => {
    speciesChart.resize()
    monthChart.resize()
  })
}

const loadOtherData = async () => {
  try {
    const animalRes = await request.get('/animal')
    vaccineAlerts.value = animalRes.data.filter(a => a.nextVaccineDate)
  } catch {}
  try {
    const logRes = await request.get('/log')
    logs.value = logRes.data || []
  } catch {}
}

onMounted(() => {
  loadStatData()
  loadOtherData()
})
</script>

<style scoped>
.stat-card-content {
  display: flex;
  align-items: center;
}
.stat-card-icon {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 16px;
  flex-shrink: 0;
}
.stat-card-info {
  flex: 1;
}
.stat-card-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
}
.stat-card-title {
  font-size: 13px;
  color: #909399;
  margin-top: 4px;
}
</style>
