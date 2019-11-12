<template>
  <div>
    <div>
      <el-container>
        <el-aside
          v-if="isLogin"
          width="250px"
        >
          <list-of-conference />
        </el-aside>
        <el-main>
          <conference-brief :id="id" />
        </el-main>
      </el-container>
    </div>
  </div>
</template>

<script>
    import ListOfConference from '@/components/ListOfConference';
    import ConferenceBrief from "@/components/ConferenceBrief";

    export default {
        name: "ConferenceList",
        components: {
            ConferenceBrief,
            ListOfConference
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

<style scoped>

</style>