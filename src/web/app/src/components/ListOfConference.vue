<template>
    <div>
        <h4>Conferences created by me</h4>
        <el-menu :default-active="$route.path" v-loading="isLoading" router>
            <li key="__NEW__">
                <el-menu-item :index="'/conferenceList/__NEW__'">
                    <i class="el-icon-edit-outline"></i>
                    <span slot="title">
                    New Conference
                    </span>
                </el-menu-item>
            </li>

            <li v-for="conference in conferences" :key="conference.id">
                <el-menu-item :index="`/conferenceList/${conference.id}`">
                    <i class="el-icon-document"></i>
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
        mounted() {
            this.$store.dispatch('getConferenceList')
        }
    }
</script>

<style scoped>

</style>