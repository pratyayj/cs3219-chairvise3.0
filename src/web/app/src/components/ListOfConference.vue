<template>
  <div>
    <h4>Conferences created by me</h4>
    <el-menu
      v-loading="isLoading"
      :default-active="$route.path"
      router
    >
      <li key="__NEW__">
        <el-menu-item :index="'/conferenceList/__NEW__'">
          <i class="el-icon-edit-outline" />
          <span slot="title">
            New Conference
          </span>
        </el-menu-item>
      </li>

      <li
        v-for="conference in conferences"
        :key="conference.id"
      >
        <el-menu-item :index="`/conferenceList/${conference.id}`">
          <i class="el-icon-document" />
          <span slot="title">
            {{ conference.conferenceName }}
          </span>
        </el-menu-item>
      </li>
    </el-menu>
  </div>
</template>

<script>
    export default {
        name: "ListOfConference",
        data() {
            return {}
        },
        computed: {
            isLoading() {
                return this.$store.state.conference.conferenceListStatus.isLoading
                    || this.$store.state.conference.conferenceFormStatus.isLoading
            },
            conferences() {
                return this.$store.state.conference.conferenceList
            },
            isError() {
                return this.$store.state.conference.conferenceListStatus.isApiError
            }
        },
        watch: {
            'isError'() {
                if(!this.isError) {
                    return
                }
                this.$notify.error({
                    title: 'Conference list API request fail',
                    message: this.$store.state.conference.conferenceListStatus.apiErrorMsg,
                    duration: 0
                });
            }
        },
        mounted() {
            this.$store.dispatch('getConferenceList')
        }
    }
</script>

<style scoped>

</style>