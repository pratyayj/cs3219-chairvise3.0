<template>
  <div>
    <el-row
      v-if="isNewPresentation"
      class="addRowRightAlign"
    >
      <el-alert
        title="Please create presentation before adding sections"
        type="info"
        show-icon
      />
    </el-row>
    <div
      v-if="!isNewPresentation"
      v-loading="isLoadingDBMetaData || isLoadingSectionList"
    >
      <el-row
        v-if="isLogin && isPresentationEditable"
        class="addRowRightAlign"
      >
        <el-select
          v-model="selectedNewSection"
          placeholder="Please select a section to add"
          style="width: 300px"
          filterable
        >
          <el-option-group
            v-for="group in predefinedSections"
            :key="group.label"
            :label="group.label"
          >
            <el-option
              v-for="item in group.options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-option-group>
        </el-select>
        <el-button
          class="addButtonLeft"
          type="success"
          @click="addNewSection"
        >
          Add New Section
        </el-button>
      </el-row>
      <br>
      <el-alert
        v-if="isSectionListApiError"
        :title="sectionListApiErrorMsg"
        type="error"
        show-icon
      />
      <abstract-section-detail
        v-for="section in sectionList"
        :key="section.id"
        class="presentation-section"
        :section-detail="section"
        :presentation-id="presentationId"
      />
    </div>
  </div>
</template>

<script>
  import AbstractSectionDetail from "@/components/AbstractSectionDetail.vue"
  import {ID_NEW_PRESENTATION} from "@/common/const";
  import PredefinedQueries from "@/store/data/predefinedQueries"

  export default {
    components: {
      AbstractSectionDetail
    },
    props: {
      presentationId: {
        type: String,
        required: true
      }
    },
    data() {
      return {
        selectedNewSection: '',
      }
    },
    computed: {
      isLogin() {
        return this.$store.state.userInfo.isLogin
      },

      isPresentationEditable() {
        return this.$store.state.presentation.isPresentationEditable;
      },

      predefinedSections() {
        let sectionOptionsGroup = {};
        // grouping the predefined queries
        for (let key in PredefinedQueries) {
          if (!Object.prototype.hasOwnProperty.call(PredefinedQueries, key)) {
            continue;
          }
          let groupName = PredefinedQueries[key].group;
          if (sectionOptionsGroup[groupName] === undefined) {
            sectionOptionsGroup[groupName] = [];
          }
          sectionOptionsGroup[groupName].push({
            value: key,
            label: PredefinedQueries[key].name,
          })
        }

        // generate to format that element ui requires
        let sectionOptions = [];
        for (let groupName in sectionOptionsGroup) {
          if (!Object.prototype.hasOwnProperty.call(sectionOptionsGroup, groupName)) {
            continue;
          }
          sectionOptions.push({
            label: groupName,
            options: sectionOptionsGroup[groupName]
          })
        }
        return sectionOptions;
      },

      isNewPresentation() {
        return this.presentationId === ID_NEW_PRESENTATION
      },

      sectionList() {
        return this.$store.state.section.sectionList
      },
      isLoadingSectionList() {
        return this.$store.state.section.sectionListStatus.isLoading
      },
      isSectionListApiError() {
        return this.$store.state.section.sectionListStatus.isApiError
      },
      sectionListApiErrorMsg() {
        return this.$store.state.section.sectionListStatus.apiErrorMsg
      },
      isLoadingDBMetaData() {
        return this.$store.state.dbMetaData.entitiesStatus.isLoading
      }
    },
    watch: {
      presentationId: 'fetchSectionList'
    },
    mounted() {
      this.fetchSectionList();
      this.$store.dispatch('fetchDBMetaDataEntities');
    },
    methods: {
      fetchSectionList() {
        if (this.isNewPresentation) {
          this.$store.commit('clearSectionList');
        } else {
          this.$store.dispatch('fetchSectionList', this.presentationId)
        }
      },

      addNewSection() {
        if (this.selectedNewSection.length === 0) {
          return;
        }
        this.$store.dispatch('addSectionDetail', {
          presentationId: this.presentationId,
          selectedNewSection: this.selectedNewSection,
          dataSet: this.$store.state.userInfo.userEmail,
        }).then(() => {
          this.selectedNewSection = ''
        })
      }
    }
  }
</script>

<style scoped>
  .addButtonLeft {
    margin-left: 10px;
  }

  .addRowRightAlign {
    text-align: right;
  }
</style>