<template>
  <basic-section-detail
    :section-detail="sectionDetail"
    :presentation-id="presentationId"
    :has-data="hasData"
    :edit-form-selections-rule="editFormSelectionsRule"
    :edit-form-involved-records-rule="editFormInvolvedRecordsRule"
    :edit-form-filters-rule="editFormFiltersRule"
    :edit-form-groupers-rule="editFormGroupersRule"
    @update-visualisation="updateVisualisation"
  >
      <ForceDirectedGraph
        :chart-data="chartData"
        :options="options"
      />

      <template
        slot="extraFormItems"
        slot-scope="slotProps"
      >
          <template v-if="slotProps.isInAdvancedMode">
          </template>
          <el-form-item v-if="slotProps.isInAdvancedMode">
          </el-form-item>
      </template>
  </basic-section-detail>
</template>

<script>
    import ForceDirectedGraph from '@/components/sectionDetail/chart/ForceDirectedGraph'
    import BasicSectionDetail from '@/components/sectionDetail/BasicSectionDetail.vue'

    export default {
        name: "CoauthorshipSectionDetail",

        components: {
            BasicSectionDetail,
            ForceDirectedGraph
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
                editFormSelectionsRule: [{
                    validator: (rule, value, callback) => {
                        if (value.expression.length === 0 || value.expression.rename === 0) {
                            return callback(new Error('Please specify all field for the selection'))
                        }
                        callback();
                    },
                    trigger: 'blur',
                }],
                editFormInvolvedRecordsRule: [{
                    validator: (rule, value, callback) => {
                        if (value.length < 1) {
                            return callback(new Error('There must be one record involved'))
                        }
                        callback();
                    },
                    trigger: 'change',
                }],
                editFormFiltersRule: [{
                    validator: (rule, value, callback) => {
                        if (value.field.length === 0 || value.comparator.length === 0 || value.value.length === 0) {
                            return callback(new Error('Please specify all fields'))
                        }
                        callback();
                    },
                    trigger: 'blur',
                }],
                editFormGroupersRule: [],

                labels: [],
                dataset: {},
                partialResult: [],
                options: {},
            }
        },

        computed: {
            hasData() {
                return true;
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
                this.labels = result;
                this.options = extraData;
            },
        }
    }
</script>

<style scoped>

</style>