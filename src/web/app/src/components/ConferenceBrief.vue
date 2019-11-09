<template>
    <el-alert v-if="isNewConference && !isLogin" title="Please login to create new conference" type="error" show-icon
              class="errorMsg"/>
    <el-form v-else label-position="right" ref="conferenceForm" label-width="120px" :rules="rules"
             :model="conferenceForm" v-loading="isLoading">
        <el-alert v-if="isError" :title="apiErrorMsg" type="error" show-icon class="errorMsg"/>
        <el-form-item label="Conference Name" :prop=" isInEditMode ? 'name' : ''">
            <div v-if="!isInEditMode">{{ conferenceForm.conferenceName }}</div>
            <el-input v-model="conferenceFormName" v-if="isInEditMode"/>
        </el-form-item>
        <el-form-item label="Creator" v-if="!isNewConference">
            <el-tag>Created by {{ conferenceForm.creatorIdentifier }}</el-tag>
        </el-form-item>
        <el-form-item label="Year">
            <div v-if="!isInEditMode" id="conference-year">{{ conferenceForm.conferenceYear }}</div>
            <el-input v-model="conferenceFormYear" v-if="isInEditMode"/>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="changeEditMode(true)" v-if="!isInEditMode && isConferenceEditable">Edit
            </el-button>
            <el-button type="primary" @click="addConference()" v-if="isInEditMode">Save</el-button>
            <el-button type="info" @click="changeEditMode(false)" v-if="isInEditMode && !isNewConference">Cancel</el-button>
            <el-button type="danger" v-if="!isNewConference && isLogin && isConferenceEditable"
                       @click="deleteConference()">Delete
            </el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    import {ID_NEW_CONFERENCE} from "@/common/const";

    export default {
        name: "ConferenceBrief",
        props: {
            id: String
        },
        mounted: function() {
            this.updateConferenceForm()
        },
        watch: {
            // when id changes run the method
            'id'() {
                this.updateConferenceForm()
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
                        {required: true, message: 'There must be a conference year.', trigger: 'blur'},
                        {min: 4, message: 'The length should be 4 characters.', trigger: 'blur'},
                        {max: 4, message: 'The length should be 4 characters.', trigger: 'blur'}
                    ]
                }
            }
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
                                if (this.isError()) {
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