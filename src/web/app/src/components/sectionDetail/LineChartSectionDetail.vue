<template>
  <basic-section-detail
    :section-detail="sectionDetail"
    :presentation-id="presentationId"
    :has-data="hasData"
    :extra-form-items-rules="{}"
    @update-visualisation="updateVisualisation"
  >
    <line-chart
      :chart-data="chartData"
      :options="options"
    />

    <template
      slot="extraFormItems"
      slot-scope="slotProps"
    >
      <el-form-item
        v-if="slotProps.isInAdvancedMode"
        label="xAxis Field Name"
        prop="extraData.xAxisFieldName"
      >
        <el-select
          v-model="slotProps.extraData.xAxisFieldName"
          placeholder="xAxisFieldName"
        >
          <el-option
            v-for="selection in slotProps.editForm.selections"
            :key="selection.rename"
            :label="selection.rename"
            :value="selection.rename"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        v-if="slotProps.isInAdvancedMode"
        label="yAxis Field Name"
        prop="extraData.yAxisFieldName"
      >
        <el-select
          v-model="slotProps.extraData.yAxisFieldName"
          placeholder="yAxisFieldName"
        >
          <el-option
            v-for="selection in slotProps.editForm.selections"
            :key="selection.rename"
            :label="selection.rename"
            :value="selection.rename"
          />
        </el-select>
      </el-form-item>
      <el-form-item
        v-if="slotProps.isInAdvancedMode"
        label="Legend Label Name"
        prop="extraData.dataSetLabel"
      >
        <el-input
          v-model="slotProps.extraData.dataSetLabel"
          placeholder="Label Name"
        />
      </el-form-item>
    </template>
  </basic-section-detail>
</template>

<script>
  import LineChart from '@/components/sectionDetail/chart/LineChart.vue'
  import BasicSectionDetail from '@/components/sectionDetail/BasicSectionDetail.vue'
  import {generateBorderColor, generateBackgroundColor} from '@/common/color'

  export default {
    name: "LineChartSectionDetail",

    components: {
      BasicSectionDetail,
      LineChart
    },

    props: {
      sectionDetail: {
        type: Object,
        required: true
      },
      presentationId: {
        type: String,
        required: true
      }
    },

    data() {
      return {
        labels: [],
        dataset: {},
        options: {},
      }
    },

    computed: {
      hasData() {
        return this.labels.length !== 0;
      },

      chartData() {
        return {
          labels: this.labels,
          datasets: [this.dataset]
        }
      }
    },

    methods: {
      updateVisualisation({result, extraData}) {
        // process x axis
        this.labels = result.map(record => record[extraData.xAxisFieldName]);

        // process y axis
        this.dataset = {
          borderWidth: 1,
          label: extraData.dataSetLabel,
          data: result.map(record => record[extraData.yAxisFieldName]),
          backgroundColor: generateBackgroundColor(2)[1],
          borderColor: generateBorderColor(2)[1],
        };

        this.options = {
          scales: {
            yAxes: [{
              ticks: {
                beginAtZero: false,
              },
              gridLines: {
                display: true
              }
            }],
            xAxes: [{
              ticks: {
                autoSkip: false
              },
              gridLines: {
                display: false
              },
            }]
          },
          legend: {
            display: true,
            position: 'bottom'
          },
          layout: {
            padding: {
              top: 30,
            }
          },
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            datalabels: {
              anchor: 'end',
              align: 'end'
            }
          }
        }
      }
    }
  }
</script>

<style scoped>

</style>