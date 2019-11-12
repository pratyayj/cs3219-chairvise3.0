<template>
  <el-menu
    mode="horizontal"
    router
    :default-active="menuDefaultActive"
  >
    <el-menu-item index="/home">
      Home
    </el-menu-item>
    <el-menu-item
      index="/conferenceList"
      :disabled="!isLogin"
    >
      Conferences
    </el-menu-item>
    <el-menu-item
      index="/analyze"
      :disabled="!isLogin"
    >
      Presentations
    </el-menu-item>
    <el-menu-item
      index="/importData"
      :disabled="!isLogin"
    >
      Import Data
    </el-menu-item>
    <el-menu-item
      index="/help"
    >
      Help
    </el-menu-item>
    <el-menu-item
      v-if="isLogin"
      v-loading.fullscreen.lock="isFullscreenLoading"
      index="/logout"
      @click="logout"
    >
      <el-button
        type="success"
        plain
      >
        Logout ({{ userNickname }})
      </el-button>
    </el-menu-item>
    <el-menu-item
      v-if="!isLogin"
      v-loading.fullscreen.lock="isFullscreenLoading"
      index="/login"
      :disabled="isApiError"
      @click="login"
    >
      <el-button
        type="success"
        plain
        :disabled="isApiError"
      >
        Login
      </el-button>
    </el-menu-item>
  </el-menu>
</template>

<script>
  import AuthRedirects from '@/mixins/AuthRedirects';

  export default {
    name: 'MenuBar',
    mixins: [AuthRedirects],
    data() {
      return {
        isFullscreenLoading: false,
      }
    },
    computed: {
      menuDefaultActive() {
        // need to active the menu item if sub path is used. e.g. /analyze/new
        if (this.$route.path.includes('/analyze')) {
          return '/analyze'
        }

        if (this.$route.path.includes('/conferenceList')) {
            return '/conferenceList'
        }
        return this.$route.path
      },
      isLogin() {
        return this.$store.state.userInfo.isLogin
      },
      userNickname() {
        return this.$store.state.userInfo.userNickname
      },
      isApiError() {
        return this.$store.state.userInfo.isApiError
      }
    }
  }
</script>