<template>
    <div>
        <div>
            <el-container>
                <el-aside width="250px" v-if="isLogin">
                    <list-of-conference/>
                </el-aside>
                <el-main>
                    <conference-brief :id="id"/>
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
        props: {
            id: String
        },
        computed: {
            isLogin() {
                return this.$store.state.userInfo.isLogin
            },
            isAppLoading() {
                return this.$store.state.isPageLoading
            }
        },
        components: {
            ConferenceBrief,
            ListOfConference
        }
    }
</script>

<style scoped>

</style>