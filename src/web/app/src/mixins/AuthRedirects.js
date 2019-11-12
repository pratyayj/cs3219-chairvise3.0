export default {
    methods: {
        login() {
            // enter full screen loading and wait browser to redirect to google login page
            this.$data.isFullscreenLoading = true;
            window.location.href = this.$store.state.userInfo.loginUrl
        },
        logout() {
            // enter full screen loading and wait browser to redirect to google login page
            this.$data.isFullscreenLoading = true;
            window.location.href = this.$store.state.userInfo.logoutUrl
        }
    }
}
