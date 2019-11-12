<template>
  <div>
    <div class="middle">
      <!--can change to a better logo next time-->
      <img
        alt="ChairVisE logo"
        src="@/assets/logo.png"
      >
      <h2>Welcome to Conference Data Visualization Management System</h2>
    </div>
    <el-row :gutter="20">
      <el-col
        class="column-card"
        :sm="8"
      >
        <el-card>
          <h1>Visualize</h1>
          conference submission statistics, such as author and submission data.
        </el-card>
      </el-col>
      <el-col
        class="column-card"
        :sm="8"
      >
        <el-card>
          <h1>Import</h1>
          from multiple systems, such as EasyChair or SoftConf.
        </el-card>
      </el-col>
      <el-col
        class="column-card"
        :sm="8"
      >
        <el-card>
          <h1>Share</h1>
          and export your visualisation with others.
        </el-card>
      </el-col>
    </el-row>
    <el-row
      class="middle"
      :span="24"
    >
      To begin, please 
      <el-button @click="login">
        Login
      </el-button>
    </el-row>
  </div>
</template>

<script>
  import AuthRedirects from '@/mixins/AuthRedirects';

  export default {
    name: 'WelcomeMessage',
    mixins: [AuthRedirects]
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  p {
    font-size: 1.1em;
  }

  img {
    width: 10%;
  }

  .column-card {
    padding: 10px;
  }

  .column-card h1 {
    font-weight: lighter;
    font-size: 1.1em;
  }

  .middle {
    text-align: center;
  }
</style>
