<template>
  <canvas
    class="canvasStyle"
    width="1300"
    height="480"
  />
</template>

<script>
  import renderWordCloudNative from 'wordcloud';
  import {deepCopy} from "@/common/utility"

  export default {
    name: "WordCloud",

    props: {
      data: {
        type: Array,
        required: true
      },
    },

    computed: {
      weightFactor() {
        // magic number
        return 100.0 / this.data.reduce((previous, current) => {
          return Math.max(previous, current[1])
        }, 0);
      }
    },
    watch: {
      data() {
        this.renderWordCloud()
      },
    },
    mounted() {
      this.renderWordCloud()
    },
    updated() {
      this.renderWordCloud()
    },

    methods: {
      renderWordCloud() {
        renderWordCloudNative(this.$el, {
          list: deepCopy(this.data),
          weightFactor: this.weightFactor,
          rotateRatio: 0
        })
      }
    }
  }
</script>

<style scoped>
  .canvasStyle {
    width: 100%;
    height: 300px;
  }
</style>