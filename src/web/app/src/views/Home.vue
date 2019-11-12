<template>
  <div>
    <el-container>
      <el-main>
        <WelcomeMessage v-if="!isLogin" />
        <Dashboard v-else />
      </el-main>
    </el-container>
  </div>
</template>

<script>
  // @ is an alias to /src
  import WelcomeMessage from '@/components/homePageDetail/WelcomeMessage.vue'
  import Dashboard from '@/components/homePageDetail/Dashboard.vue'

  export default {
    name: 'Home',
    components: {
      WelcomeMessage,
      Dashboard
    },
    computed: {
      isLogin() {
        return this.$store.state.userInfo.isLogin
      },
    }
  }
</script>
<style scoped>
  .el-aside {
    width: 250px;
  }

  .el-main {
    margin: 30px;
  }
</style>