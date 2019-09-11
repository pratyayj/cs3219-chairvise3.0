<template>
  <el-alert
    v-if="isNewPresentation && !isLogin"
    title="Please login to create new presentation"
    type="error"
    show-icon
    class="errorMsg"
  />
  <el-form
    v-else
    ref="presentationForm"
    v-loading="isLoading"
    label-position="right"
    label-width="120px"
    :rules="rules"
    :model="presentationForm"
  >
    <el-alert
      v-if="isError"
      :title="apiErrorMsg"
      type="error"
      show-icon
      class="errorMsg"
    />
    <el-form-item
      label="Name"
      :prop=" isInEditMode ? 'name' : ''"
    >
      <div v-if="!isInEditMode">
        {{ presentationForm.name }}
      </div>
      <el-input
        v-if="isInEditMode"
        v-model="presentationFormName"
      />
    </el-form-item>
    <el-form-item
      v-if="!isNewPresentation"
      label="Access Control"
    >
      <el-tag>Created by {{ presentationForm.creatorIdentifier }}</el-tag>
      <el-button
        v-if="isLogin && isPresentationEditable"
        type="success"
        size="small"
        class="share_button_left_margin"
        icon="el-icon-view"
        @click="openAccessControlPanel()"
      >
        SHARE
      </el-button>
    </el-form-item>
    <el-dialog
      title="Share with other users:"
      :visible.sync="isAccessControlDialogVisible"
      width="70%"
      :close-on-click-modal="false"
    >
      <access-control-panel :presentation-id="id" />
    </el-dialog>
    <el-form-item label="Description">
      <div
        v-if="!isInEditMode"
        id="presentation-description"
      >
        {{ presentationForm.description }}
      </div>
      <el-input
        v-if="isInEditMode"
        v-model="presentationFormDescription"
      />
    </el-form-item>
    <el-form-item>
      <el-button
        v-if="!isInEditMode && !isNewPresentation"
        type="primary"
        @click="downloadPDF()"
      >
        Download as PDF
      </el-button>
      <el-button
        v-if="!isInEditMode && isPresentationEditable"
        type="primary"
        @click="changeEditMode(true)"
      >
        Edit
      </el-button>
      <el-button
        v-if="isInEditMode"
        type="primary"
        @click="addPresentation()"
      >
        Save
      </el-button>
      <el-button
        v-if="isInEditMode && !isNewPresentation"
        type="info"
        @click="changeEditMode(false)"
      >
        Cancel
      </el-button>
      <el-button
        v-if="!isNewPresentation && isLogin && isPresentationEditable"
        type="danger"
        @click="deletePresentation()"
      >
        Delete
      </el-button>
    </el-form-item>
  </el-form>
</template>

<script>
  import AccessControlPanel from '@/components/AccessControlPanel'
  import {download} from "@/store/helpers/pdfDownloader"
  import {AccessLevel, ID_NEW_PRESENTATION, SPECIAL_IDENTIFIER_PUBLIC} from "@/common/const";
  import {deepCopy} from "@/common/utility";

  export default {
    name: 'PresentationBrief',

    components: {
      AccessControlPanel
    },
    props: {
      id: {
        type: String,
        required: true
      }
    },
    data() {
      return {
        isEditing: false,
        isAccessControlDialogVisible: false,
        rules: {
          name: [
            {required: true, message: 'Please enter presentation name', trigger: 'blur'},
            {min: 3, message: 'The length should be more than 3 character', trigger: 'blur'}
          ]
        }
      }
    },
    computed: {
      isLogin() {
        return this.$store.state.userInfo.isLogin
      },
      isPresentationEditable() {
        return this.$store.state.presentation.isPresentationEditable;
      },

      presentationForm() {
        return {
          name: this.presentationFormName,
          creatorIdentifier: this.presentationFormCreatorIdentifier,
          description: this.presentationFormDescription,
        }
      },
      presentationFormName: {
        get() {
          return this.$store.state.presentation.presentationForm.name
        },
        set(value) {
          this.$store.commit('setPresentationFormField', {
            field: 'name',
            value
          })
        },
      },
      presentationFormCreatorIdentifier() {
        return this.$store.state.presentation.presentationForm.creatorIdentifier
      },
      presentationFormDescription: {
        get() {
          return this.$store.state.presentation.presentationForm.description
        },
        set(value) {
          this.$store.commit('setPresentationFormField', {
            field: 'description',
            value
          })
        },
      },
      isNewPresentation() {
        return this.id === ID_NEW_PRESENTATION
      },
      isInEditMode() {
        return this.isEditing || this.isNewPresentation
      },
      isLoading() {
        return this.$store.state.presentation.presentationFormStatus.isLoading
      },
      isError() {
        return this.$store.state.presentation.presentationFormStatus.isApiError
      },
      apiErrorMsg() {
        return this.$store.state.presentation.presentationFormStatus.apiErrorMsg
      }
    },
    watch: {
      'id'() {
        this.updatePresentationForm()
      },
    },
    mounted() {
      this.updatePresentationForm()
    },
    methods: {
      changeEditMode(isEditing) {
        if (isEditing === false) {
          this.updatePresentationForm();
        }
        this.isEditing = isEditing;
      },

      openAccessControlPanel() {
        this.isAccessControlDialogVisible = true;
      },

      addPresentation() {
        this.$refs['presentationForm'].validate((valid) => {
          if (!valid) {
            return
          }
          this.$refs['presentationForm'].clearValidate();
          if (this.isNewPresentation) {
            // add
            this.$store.dispatch('savePresentation')
              .then(() => {
                if (this.isError) {
                  return
                }
                // redirect to the newly added presentation
                this.$router.push({
                  name: 'analyze',
                  params: {
                    id: this.$store.state.presentation.presentationForm.id
                  }
                });
              });
          } else {
            // edit
            this.$store.dispatch('updatePresentation')
              .then(() => {
                if (this.isError) {
                  return
                }
                this.isEditing = false
              })
          }
        });
      },
      deletePresentation() {
        this.$store.dispatch('deletePresentation', this.id)
          .then(() => {
            if (this.isError) {
              return
            }
            this.$router.replace({
              name: 'analyze',
              params: {
                id: ID_NEW_PRESENTATION
              }
            });
            this.isEditing = false;
          })
      },
      updatePresentationForm() {
        if (this.$refs['presentationForm']) {
          this.$refs['presentationForm'].clearValidate();
        }
        this.$store.commit('resetPresentationForm');
        if (this.id !== ID_NEW_PRESENTATION) {
          this.$store.dispatch('getPresentation', this.id)
            .then(() => {
              // check writable or not
              this.$store.dispatch('fetchAccessControlList', this.id)
                .then(() => {
                  let currentUser = this.$store.state.userInfo.userEmail;
                  let accessControlList = this.$store.state.accessControl.accessControlList;
                  let isPresentationEditable =
                    currentUser === this.presentationFormCreatorIdentifier
                    || accessControlList.some(acl => acl.userIdentifier === currentUser && acl.accessLevel === AccessLevel.CAN_WRITE)
                    || accessControlList.some(acl => acl.userIdentifier === SPECIAL_IDENTIFIER_PUBLIC && acl.accessLevel === AccessLevel.CAN_WRITE);
                  this.$store.commit('setIsPresentationEditable', isPresentationEditable)
                })
            })
        }
      },
      downloadPDF() {
        let vm = this;
        let wasPresentationEditable = deepCopy(vm.isPresentationEditable);
        vm.$store.commit('setIsPresentationEditable', false);
        vm.$store.commit('setPageLoadingStatus', true);

        this.$nextTick(() => {
          download(vm.presentationFormName).then(() => {
            vm.$store.commit('setIsPresentationEditable', wasPresentationEditable);
            vm.$store.commit('setPageLoadingStatus', false);
          });
        });
      }
    },
  }
</script>

<style scoped>
  .share_button_left_margin {
    margin-left: 10px;
  }

  .errorMsg {
    margin-bottom: 18px;
  }
</style>
