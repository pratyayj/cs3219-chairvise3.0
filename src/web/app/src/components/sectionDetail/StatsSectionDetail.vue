<template>
  <basic-section-detail
    :section-detail="sectionDetail"
    :presentation-id="presentationId"
    :has-data="hasData"
    :extra-form-items-rules="{}"
    @update-visualisation="updateVisualisation"
  >
    <el-table
      :data="tableData"
      style="width: 100%"
    >
      <el-table-column
        prop="type"
        label="Type"
      />
      <el-table-column
        prop="value"
        label="Value"
      />
    </el-table>
    <template
      slot="extraFormItems"
      slot-scope="slotProps"
    >
      <el-form-item
        v-if="slotProps.isInAdvancedMode"
        label="Type"
      >
        <el-select
          v-model="slotProps.extraData.types"
          placeholder="type"
          multiple
        >
          <el-option
            label="Min"
            value="min"
          />
          <el-option
            label="Max"
            value="max"
          />
          <el-option
            label="Sum"
            value="sum"
          />
          <el-option
            label="Average"
            value="avg"
          />
          <el-option
            label="Median"
            value="median"
          />
          <el-option
            label="Standard Deviation"
            value="std"
          />
        </el-select>
      </el-form-item>
    </template>
  </basic-section-detail>
</template>

<script>
  import {max, mean, median, min, standardDeviation, sum} from 'simple-statistics'
  import BasicSectionDetail from '@/components/sectionDetail/BasicSectionDetail.vue'

  export default {
    name: "StatsSectionDetail",

    components: {
      BasicSectionDetail,
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
        tableData: []
      }
    },

    computed: {
      hasData() {
        return this.tableData.length !== 0;
      }
    },

    methods: {
      updateVisualisation({result, selections, extraData}) {
        this.tableData = [];
        if (result.length === 0) {
          return
        }
        let data = result.map(r => r[selections[0].rename]);

        extraData.types.forEach(t => {
          switch (t) {
            case 'min':
              this.tableData.push({
                type: 'Min',
                value: min(data),
              });
              break;
            case 'max':
              this.tableData.push({
                type: 'Max',
                value: max(data),
              });
              break;
            case 'sum':
              this.tableData.push({
                type: 'Sum',
                value: sum(data),
              });
              break;
            case 'avg':
              this.tableData.push({
                type: 'Average',
                value: mean(data).toFixed(2),
              });
              break;
            case 'median':
              this.tableData.push({
                type: 'Median',
                value: median(data),
              });
              break;
            case 'std':
              this.tableData.push({
                type: 'Standard Deviation',
                value: standardDeviation(data).toFixed(2),
              });
              break;
          }
        })
      }
    }
  }
</script>

<style scoped>

</style>