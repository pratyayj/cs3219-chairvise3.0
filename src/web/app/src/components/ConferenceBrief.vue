<template>
  <el-alert
    v-if="isNewConference && !isLogin"
    title="Please login to create new conference"
    type="error"
    show-icon
    class="errorMsg"
  />
  <el-form
    v-else
    ref="conferenceForm"
    v-loading="isLoading"
    label-position="right"
    label-width="120px"
    :model="conferenceForm"
  >
    <el-alert
      v-if="isError"
      :title="apiErrorMsg"
      type="error"
      show-icon
      class="errorMsg"
    />
    <el-form-item
      label="Conf. Name"
      :prop=" isInEditMode ? 'conferenceName' : ''"
      :rules="rules.conferenceName"
    >
      <div v-if="!isInEditMode">
        {{ conferenceForm.conferenceName }}
      </div>
      <el-input
        v-if="isInEditMode"
        v-model="conferenceFormName"
      />
    </el-form-item>
    <el-form-item
      v-if="!isNewConference"
      label="Creator"
    >
      <el-tag>Created by {{ conferenceForm.creatorIdentifier }}</el-tag>
    </el-form-item>
    <el-form-item
      label="Year"
      :prop=" isInEditMode ? 'conferenceYear' : ''"
      :rules="rules.conferenceYear"
    >
      <div
        v-if="!isInEditMode"
        id="conference-year"
      >
        {{ conferenceForm.conferenceYear }}
      </div>
      <el-select
        v-else
        v-model="conferenceFormYear"
        placeholder="Conference Year"
        filterable
        default-first-option
      >
        <el-option
          v-for="year in years"
          :key="year"
          :value="year"
        />
      </el-select>
    </el-form-item>
    <div v-if="!isInEditMode">
      <el-form-item label="Existing Records">
        <ul>
          <li
            v-for="conferenceRecord in conferenceRecords"
            id="conferenceRecords"
            :key="conferenceRecord.recordType"
          >
            {{ conferenceRecord.recordType }}
          </li>
        </ul>
      </el-form-item>
    </div>
    <el-form-item>
      <el-button
        v-if="!isInEditMode && isConferenceEditable"
        type="primary"
        @click="changeEditMode(true)"
      >
        Edit
      </el-button>
      <el-button
        v-if="isInEditMode"
        type="primary"
        @click="addConference()"
      >
        Save
      </el-button>
      <el-button
        v-if="isInEditMode && !isNewConference"
        type="info"
        @click="changeEditMode(false)"
      >
        Cancel
      </el-button>
      <el-button
        v-if="!isNewConference && isLogin && isConferenceEditable"
        type="danger"
        @click="deleteConference()"
      >
        Delete
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script>
    import {ID_NEW_CONFERENCE} from "@/common/const";

    export default {
        name: "ConferenceBrief",
        props: {
          id: {
            type: String,
            required: true
          }
        },
        data() {
            return {
                isEditing: false,
                rules: {
                    conferenceName: [
                        {required: true, message: 'There must be a conference name.', trigger: 'blur'},
                        {min: 3, message: 'The length should be more than 3 characters.', trigger: 'blur'}
                    ],
                    conferenceYear: [
                        {required: true, message: 'There should be a year, or you will forget too.', trigger: 'blur'}
                    ]
                }
            }
        },
        computed: {
            isLogin() {
                return this.$store.state.userInfo.isLogin
            },
            isConferenceEditable() {
              return this.$store.state.conference.isConferenceEditable;
            },
            conferenceForm() {
                return {
                    conferenceName: this.conferenceFormName,
                    conferenceYear: this.conferenceFormYear,
                    creatorIdentifier: this.conferenceFormCreatorIdentifier
                }
            },
            conferenceFormName: {
                get() {
                    return this.$store.state.conference.conferenceForm.conferenceName;
                },
                set(value) {
                    this.$store.commit('setConferenceFormField', {
                        field: 'conferenceName',
                        value
                    })
                }
            },
            conferenceFormYear: {
                get() {
                    return this.$store.state.conference.conferenceForm.conferenceYear;
                },
                set(value) {
                    this.$store.commit('setConferenceFormField', {
                        field: 'conferenceYear',
                        value
                    })
                }
            },
            conferenceFormCreatorIdentifier() {
                return this.$store.state.conference.conferenceForm.creatorIdentifier
            },
            isNewConference() {
                return this.id === ID_NEW_CONFERENCE
            },
            isInEditMode() {
                return this.isEditing || this.isNewConference
            },
            isLoading() {
                return this.$store.state.conference.conferenceFormStatus.isLoading
            },
            isError() {
                return this.$store.state.conference.conferenceFormStatus.isApiError
            },
            apiErrorMsg() {
                return this.$store.state.conference.conferenceFormStatus.apiErrorMsg
            },
            years () {
                const year = new Date().getFullYear() + 10;
                return Array.from({length: year - 1900}, (value, index) => 1901 + index)
            },
            conferenceRecords: function() {
                return this.$store.state.conferenceRecord.conferenceRecordList;
            }
        },
        watch: {
            // when id changes run the method
            'id'() {
                this.updateConferenceForm();
                this.$store.dispatch('getConferenceRecordList', this.id);
            }
        },
        mounted: function() {
            this.updateConferenceForm()
        },
        methods: {
            changeEditMode(isEditing) {
                if (isEditing === false) {
                    this.updateConferenceForm();
                }
                this.isEditing = isEditing;
            },

            addConference() {
                this.$refs['conferenceForm'].validate((valid) => {
                    if (!valid) {
                        return
                    }
                    this.$refs['conferenceForm'].clearValidate();
                    if (this.isNewConference) {
                        this.$store.dispatch('saveConference')
                            .then(() => {
                                if (this.isError) {
                                    return
                                }
                                this.$router.push({
                                    name: 'conferenceList',
                                    params: {
                                        id: this.$store.state.conference.conferenceForm.id
                                    }
                                });
                            });
                    } else {
                        this.$store.dispatch('updateConference')
                            .then(() => {
                                if (this.isError) {
                                    return
                                }
                                this.isEditing = false
                            })
                    }
                });
            },
            deleteConference() {
                this.$store.dispatch('deleteConference', this.id)
                    .then(() => {
                        if (this.isError) {
                            return
                        }
                        this.$router.replace({
                            name: 'conferenceList',
                            params: {
                                id: ID_NEW_CONFERENCE
                            }
                        });
                        this.isEditing = false;
                    })
            },
            updateConferenceForm() {
                if (this.$refs['conferenceForm']) {
                    this.$refs['conferenceForm'].clearValidate();
                }
                this.$store.commit('resetConferenceForm');
                if (this.id !== ID_NEW_CONFERENCE) {
                    this.$store.dispatch('getConference', this.id).then(() => {
                        this.$store.commit('setIsConferenceEditable', true)
                    })
                }
            }
        }
    }
</script>

<style scoped>

</style>