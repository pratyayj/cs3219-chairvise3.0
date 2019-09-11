<template>
  <div>
    <div>
      <el-container>
        <el-aside
          v-if="isLogin"
          width="250px"
        >
          <list-of-presentation />
        </el-aside>
        <el-main>
          <presentation-brief :id="id" />
          <section-list-panel :presentation-id="id" />
        </el-main>
      </el-container>
    </div>
  </div>
</template>

<script>
  import 'chartjs-plugin-datalabels';
  import ListOfPresentation from '@/components/ListOfPresentation.vue'
  import PresentationBrief from "@/components/PresentationBrief.vue";
  import SectionListPanel from "@/components/SectionListPanel.vue";

  export default {
    name: 'Analyze',
    components: {
      PresentationBrief,
      ListOfPresentation,
      SectionListPanel,
    },
    props: {
      id: {
        type: String,
        required: true
      }
    },
    computed: {
      isLogin() {
        return this.$store.state.userInfo.isLogin
      },
      isAppLoading() {
        return this.$store.state.isPageLoading
      }
    }
  }
</script>