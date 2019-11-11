<script>
  import {Bar, mixins} from 'vue-chartjs'

  const {reactiveProp} = mixins;

  export default {
    extends: Bar,
    mixins: [reactiveProp],
    props: {
      options: {
        type: Object,
        required: true
      },
    },
    watch: {
      options() {
        this.renderChart(this.chartData, this.options);
      }
    },
    mounted() {
      // this.chartData is created in the mixin
      this.renderChart(this.chartData, this.options)
    }
  }
</script>

<style scoped>

</style>