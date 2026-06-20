<template>
  <!-- 登录/注册页面：全屏显示，无侧边栏 -->
  <div v-if="isAuthPage" class="auth-page">
    <router-view />
  </div>

  <!-- 主页面：侧边栏 + 头部 + 内容区 -->
  <el-container v-else style="height: 100vh">
    <el-aside width="220px" style="background-color: #304156">
      <div class="logo">动物医院管理系统</div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/dashboard">
          <el-icon><svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M128 128h768v768H128z"/></svg></el-icon>
          <span>数据仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/animal">
          <el-icon><svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M640 192h128v128H640V192zm0 256h128v128H640V448zm-256-256h128v128H384V192zm0 256h128v128H384V448zM192 192h128v128H192V192zm0 256h128v128H192V448zm448 256h128v128H640V704zm-256 0h128v128H384V704zM192 704h128v128H192V704z"/></svg></el-icon>
          <span>动物管理</span>
        </el-menu-item>
        <el-menu-item index="/owner">
          <el-icon><svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M512 512a192 192 0 1 0 0-384 192 192 0 0 0 0 384zm0 64a256 256 0 1 1 0-512 256 256 0 0 1 0 512zm320 320v-64a256 256 0 0 0-256-256H448a256 256 0 0 0-256 256v64a32 32 0 0 0 32 32h576a32 32 0 0 0 32-32z"/></svg></el-icon>
          <span>主人管理</span>
        </el-menu-item>
        <el-menu-item index="/doctor">
          <el-icon><svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M512 512a192 192 0 1 0 0-384 192 192 0 0 0 0 384zm0 64a256 256 0 1 1 0-512 256 256 0 0 1 0 512zM288 832h448a32 32 0 0 1 0 64H288a32 32 0 0 1 0-64z"/></svg></el-icon>
          <span>医生管理</span>
        </el-menu-item>
        <el-menu-item index="/appointment">
          <el-icon><svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M680 764c0 17.7-14.3 32-32 32H376c-17.7 0-32-14.3-32-32s14.3-32 32-32h272c17.7 0 32 14.3 32 32zM512 220c-141.4 0-256 114.6-256 256s114.6 256 256 256 256-114.6 256-256-114.6-256-256-256zm0 448c-106 0-192-86-192-192s86-192 192-192 192 86 192 192-86 192-192 192z"/></svg></el-icon>
          <span>预约管理</span>
        </el-menu-item>
        <el-menu-item index="/medical-record">
          <el-icon><svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M640 384H384v256h256V384zm64 0v256h128V384H704zm-384 0H192v256h128V384zm384-64V192H320v128h384zM320 640v128h384V640H320zM192 640v128h128V640H192zm448 0v128h128V640H704zM192 320h128V192H192v128z"/></svg></el-icon>
          <span>就诊记录</span>
        </el-menu-item>
        <el-menu-item index="/ai-advice">
          <el-icon><svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M512 64C264.6 64 64 264.6 64 512s200.6 448 448 448 448-200.6 448-448S759.4 64 512 64zm0 820c-205.4 0-372-166.6-372-372s166.6-372 372-372 372 166.6 372 372-166.6 372-372 372z"/></svg></el-icon>
          <span>AI小建议</span>
        </el-menu-item>
        <el-menu-item index="/log">
          <el-icon><svg viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg"><path fill="currentColor" d="M832 256H704V192c0-35.3-28.7-64-64-64H384c-35.3 0-64 28.7-64 64v64H192c-35.3 0-64 28.7-64 64v512c0 35.3 28.7 64 64 64h640c35.3 0 64-28.7 64-64V320c0-35.3-28.7-64-64-64zM384 192h256v64H384v-64z"/></svg></el-icon>
          <span>操作日志</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background-color: #fff; border-bottom: 1px solid #e6e6e6; display: flex; align-items: center; justify-content: space-between;">
        <span style="font-size: 18px; font-weight: bold; color: #333;">
          {{ $route.meta.title || '动物医院动物管理系统' }}
        </span>
        <div class="user-info">
          <span>{{ userInfo?.username }}</span>
          <el-tag :type="userInfo?.role === 'admin' ? '' : 'success'" size="small" style="margin-left: 8px;">
            {{ userInfo?.role === 'admin' ? '管理员' : '医生' }}
          </el-tag>
          <el-button type="danger" size="small" plain style="margin-left: 16px;" @click="handleLogout">退出登录</el-button>
        </div>
      </el-header>
      <el-main style="background-color: #f0f2f5; padding: 20px;">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userInfo = ref(null)

// 判断是否是登录/注册页面
const isAuthPage = computed(() => {
  return route.path === '/login' || route.path === '/register'
})

onMounted(() => {
  const token = localStorage.getItem('token')
  if (token) {
    try {
      userInfo.value = JSON.parse(token)
    } catch (e) {}
  }
})

const handleLogout = () => {
  localStorage.removeItem('token')
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  font-family: 'Microsoft YaHei', sans-serif;
}
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #263445;
}
.el-aside {
  overflow-x: hidden;
}
.user-info {
  display: flex;
  align-items: center;
  font-size: 14px;
  color: #606266;
}
.auth-page {
  width: 100%;
  height: 100vh;
}
</style>
