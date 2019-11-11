<template>
  <div>
    <h4>Presentations created by me</h4>
    <el-menu
      v-loading="isLoading"
      :default-active="$route.path"
      router
    >
      <li key="__NEW__">
        <el-menu-item :index="'/analyze/__NEW__'">
          <i class="el-icon-edit-outline" />
          <span slot="title">
            New
          </span>
        </el-menu-item>
      </li>
      <li
        v-for="presentation in presentations"
        :key="presentation.id"
      >
        <el-menu-item :index="`/analyze/${presentation.id}`">
          <i class="el-icon-document" />
          <span slot="title">
            {{ presentation.name }}
          </span>
        </el-menu-item>
      </li>
    </el-menu>
  </div>
</template>

<script>
  export default {
    name: 'ListOfPresentation',
    data() {
      return {}
    },
    computed: {
      isLoading() {
        return this.$store.state.presentation.presentationListStatus.isLoading
          || this.$store.state.presentation.presentationFormStatus.isLoading
          || this.$store.state.section.sectionListStatus.isLoading
          || this.$store.state.section.sectionList.some(s => s.status.isLoading)
      },
      presentations() {
        return this.$store.state.presentation.presentationList
      },
      isError() {
        return this.$store.state.presentation.presentationListStatus.isApiError
      },
    },
    watch: {
      'isError'() {
        if (!this.isError) {
          return
        }
        this.$notify.error({
          title: 'Presentation list API request fail',
          message: this.$store.state.presentation.presentationListStatus.apiErrorMsg,
          duration: 0
        });
      }
    },
    mounted() {
      this.$store.dispatch('getPresentationList')
    }
  }
</script>
