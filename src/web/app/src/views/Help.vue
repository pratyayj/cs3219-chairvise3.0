<template>
  <div>
    <div>
      <el-container>
        <el-aside>
          <ListOfGuide />
        </el-aside>
        <el-main>
          <FeatureGuide v-once />
          <DetailedFeatureGuide v-once />
        </el-main>
      </el-container>
    </div>
  </div>
</template>

<script>
  // @ is an alias to /src
  import FeatureGuide from '@/components/homePageDetail/FeatureGuide.vue'
  import DetailedFeatureGuide from '@/components/homePageDetail/DetailedFeatureGuide.vue'
  import ListOfGuide from '@/components/homePageDetail/ListOfGuide.vue'

  export default {
    name: 'Help',
    components: {
      FeatureGuide,
      DetailedFeatureGuide,
      ListOfGuide
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