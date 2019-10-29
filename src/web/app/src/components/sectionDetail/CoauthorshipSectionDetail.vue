<template>
  <basic-section-detail
    :section-detail="sectionDetail"
    :presentation-id="presentationId"
    :has-data="hasData"
    :edit-form-selections-rule="editFormSelectionsRule"
    :edit-form-involved-records-rule="editFormInvolvedRecordsRule"
    :edit-form-filters-rule="editFormFiltersRule"
    :edit-form-groupers-rule="editFormGroupersRule"
    :extra-form-items-rules="extraFormItemsRules"
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
          <el-form-item
            v-if="slotProps.isInAdvancedMode"
            label="Colorful Bar"
            prop="extraData.isColorfulBar"
          >
              <el-switch
                v-model="slotProps.extraData.isColorfulBar"
                active-text="Colorful Bar"
                inactive-text="Single Color Bar"
              />
          </el-form-item>
          <el-form-item
            v-if="slotProps.isInAdvancedMode"
            label="Num of result to display"
            prop="extraData.numOfResultToDisplay"
          >
              <el-slider
                v-model="slotProps.extraData.numOfResultToDisplay"
                :min="5"
                :max="30"
              />
          </el-form-item>
          <template v-if="slotProps.isInAdvancedMode">
              <el-form-item
                v-for="(tooltip, index) in slotProps.extraData.fieldsShownInToolTips"
                :key="'tooltips' + index"
                :label="'Tooltips ' + index"
              >
                  <el-select
                    v-model="tooltip.field"
                    placeholder="Field"
                  >
                      <el-option
                        v-for="selection in slotProps.editForm.selections"
                        :key="selection.rename"
                        :label="selection.rename"
                        :value="selection.rename"
                      />
                  </el-select>&nbsp;
                  <el-input
                    v-model="tooltip.label"
                    placeholder="Label Name"
                    style="width: 150px"
                  />&nbsp;
                  <el-button
                    type="danger"
                    icon="el-icon-delete"
                    circle
                    @click="removeTooltip(slotProps.extraData.fieldsShownInToolTips, tooltip)"
                  />
              </el-form-item>
          </template>
          <el-form-item v-if="slotProps.isInAdvancedMode">
              <el-button
                type="success"
                plain
                @click="addTooltip(slotProps.extraData.fieldsShownInToolTips)"
              >
                  Add Tooltip
              </el-button>
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

                extraFormItemsRules: {
                    xAxisFieldName: [{
                        required: true,
                        message: 'There should be one field to map x axis',
                        trigger: 'blur',
                    }],
                    yAxisFieldName: [{
                        required: true,
                        message: 'There should be one field to map y axis',
                        trigger: 'blur',
                    }],
                },

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

            addTooltip(tooltips) {
                tooltips.push({
                    label: '',
                    field: '',
                })
            },

            removeTooltip(tooltips, tooltipToRemove) {
                let index = tooltips.indexOf(tooltipToRemove);
                tooltips.splice(index, 1)
            },
        }
    }
</script>

<style scoped>

</style>