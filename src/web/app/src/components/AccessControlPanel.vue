<template>
  <div v-loading="isAccessControlPanelLoading">
    <h4>Shareable Link</h4>
    <el-input
      :value="currentUrl"
      @focus="$event.target.select()"
    >
      <template slot="prepend">
        Any one with the link
      </template>
      <template slot="append">
        <el-select
          :value="publicAccessLevel"
          style="width: 150px"
          @change="modifyPublicAccessControl($event)"
        >
          <el-option
            label="Cannot Access"
            value="OFF"
          />
          <el-option
            label="Can View"
            value="CAN_READ"
          />
          <el-option
            label="Can Edit"
            value="CAN_WRITE"
          />
        </el-select>
      </template>
    </el-input>
    <h4>Specific Access Control</h4>
    <el-alert
      v-if="isAccessControlListApiError"
      :title="accessControlListApiErrorMsg"
      type="error"
      show-icon
      class="errorAlert"
    />
    <el-table
      :data="accessControlList"
      style="width: 100%"
      empty-text="No Access Control for this Presentation!"
    >
      <el-table-column
        prop="userIdentifier"
        label="Email"
      />
      <el-table-column
        label="Access Level"
      >
        <template slot-scope="scope">
          <el-select
            :value="scope.row.accessLevel"
            placeholder="Select the permission"
            @change="updateAccessControl(scope.row, $event)"
          >
            <el-option
              label="View"
              value="CAN_READ"
            />
            <el-option
              label="Edit"
              value="CAN_WRITE"
            />
          </el-select>&nbsp;
          <el-button
            type="danger"
            icon="el-icon-delete"
            circle
            @click="deleteAccessControl(scope.row)"
          />
        </template>
      </el-table-column>
    </el-table>
    <h4>Add New Access Control</h4>
    <el-alert
      v-if="isAccessControlFormApiError"
      :title="accessControlFormApiErrorMsg"
      type="error"
      show-icon
      class="errorAlert"
    />
    <el-form
      ref="accessControlForm"
      label-position="left"
      label-width="120px"
      :model="accessControlForm"
      :rules="accessControlFormRule"
    >
      <el-form-item
        label="Email address"
        prop="userIdentifier"
      >
        <el-input
          v-model="accessControlFormUserIdentifier"
          placeholder="Email of the user to share"
        />
      </el-form-item>
      <el-form-item
        label="Permissions"
        prop="accessLevel"
      >
        <el-select
          v-model="accessControlFormAccessLevel"
          placeholder="Permission the user will have"
          style="width: 100%"
        >
          <el-option
            label="View"
            value="CAN_READ"
          />
          <el-option
            label="Edit"
            value="CAN_WRITE"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          @click="addAccessControl()"
        >
          Add
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
    import {ID_NEW_PRESENTATION, SPECIAL_IDENTIFIER_PUBLIC} from "@/common/const";
    import emailjs from 'emailjs-com';

    export default {
        name: "AccessControlPanel",
        props: {
            presentationId: {
                type: String,
                required: true
            }
        },
        data() {
            return {
                accessControlFormRule: {
                    userIdentifier: [
                        {required: true, message: 'Please enter the email', trigger: 'blur'},
                        {type: 'email', message: 'Please enter a valid email', trigger: ['blur']},
                        {
                            validator: (rule, value, callback) => {
                                if (this.accessControlList.some(ele => ele.userIdentifier === value)) {
                                    callback(new Error('There is existent access control for the user'));
                                } else {
                                    callback();
                                }
                            },
                            trigger: 'blur'
                        }
                    ],
                    accessLevel: [
                        {required: true, message: 'Please give an access level', trigger: 'blur'},
                    ]
                },
                currentUrl: '',
            }
        },
        computed: {
            isAccessControlPanelLoading() {
                return this.$store.state.accessControl.accessControlListStatus.isLoading
                    || this.$store.state.accessControl.accessControlFormStatus.isLoading
            },
            isAccessControlListApiError() {
                return this.$store.state.accessControl.accessControlListStatus.isApiError
            },
            accessControlListApiErrorMsg() {
                return this.$store.state.accessControl.accessControlListStatus.apiErrorMsg
            },
            isAccessControlFormApiError() {
                return this.$store.state.accessControl.accessControlFormStatus.isApiError
            },
            accessControlFormApiErrorMsg() {
                return this.$store.state.accessControl.accessControlFormStatus.apiErrorMsg
            },
            accessControlList() {
                // filter out public access control in the ACL
                return this.$store.state.accessControl.accessControlList
                    .filter(ac => ac.userIdentifier !== SPECIAL_IDENTIFIER_PUBLIC)
            },
            publicAccessLevel() {
                let publicAccessLevelControl =
                    this.$store.state.accessControl.accessControlList.find(ac => ac.userIdentifier === SPECIAL_IDENTIFIER_PUBLIC);
                if (publicAccessLevelControl === undefined) {
                    return 'OFF'
                }
                return publicAccessLevelControl.accessLevel
            },
            accessControlForm() {
                return {
                    userIdentifier: this.accessControlFormUserIdentifier,
                    accessLevel: this.accessControlFormAccessLevel,
                }
            },
            accessControlFormUserIdentifier: {
                get() {
                    return this.$store.state.accessControl.accessControlForm.userIdentifier
                },
                set(value) {
                    this.$store.commit('setAccessControlFormField', {
                        field: 'userIdentifier',
                        value
                    })
                },
            },
            accessControlFormAccessLevel: {
                get() {
                    return this.$store.state.accessControl.accessControlForm.accessLevel
                },
                set(value) {
                    this.$store.commit('setAccessControlFormField', {
                        field: 'accessLevel',
                        value
                    })
                },
            },
        },
        watch: {
            'presentationId': {
                immediate: true,
                handler: 'fetchAccessControlList'
            }
        },
        beforeUpdate() {
            this.currentUrl = window.location.href;
        },
        methods: {
            modifyPublicAccessControl(accessLevel) {
                let publicAccessControl =
                    this.$store.state.accessControl.accessControlList.find(ac => ac.userIdentifier === SPECIAL_IDENTIFIER_PUBLIC);
                // delete
                if (accessLevel === 'OFF' && publicAccessControl !== undefined) {
                    this.$store.dispatch('deleteAccessControl',
                        {
                            presentationId: this.presentationId,
                            id: publicAccessControl.id,
                        }
                    );
                    return;
                }
                if (publicAccessControl === undefined) {
                    // create if not exist
                    this.$store.dispatch('addAccessControl', {
                        presentationId: this.presentationId,
                        userIdentifier: SPECIAL_IDENTIFIER_PUBLIC,
                        accessLevel
                    })
                } else {
                    // update if exist
                    this.$store.dispatch('updateAccessControl',
                        {
                            presentationId: this.presentationId,
                            id: publicAccessControl.id,
                            accessLevel
                        }
                    );
                }
            },
            fetchAccessControlList() {
                if (this.presentationId === ID_NEW_PRESENTATION) {
                    return;
                }
                this.$store.dispatch('fetchAccessControlList', this.presentationId)
            },
            updateAccessControl({id}, $event) {
                this.$store.dispatch('updateAccessControl',
                    {
                        presentationId: this.presentationId,
                        id,
                        accessLevel: $event
                    }
                );

                // send email if user updated access control.
                console.log(this);
                var accessControlList = this.$store.state.accessControl.accessControlList;
                var creatorName = this.$store.state.userInfo.userEmail;
                var toMail;
                for (var i = 0; i < accessControlList.length; i++) {
                    if (accessControlList[i].id == id) {
                        toMail = accessControlList[i].userIdentifier;
                    }
                }
                var editLevel;
                if ($event === "CAN_WRITE") {
                    editLevel = "can edit";
                } else if ($event === "CAN_READ") {
                    editLevel = "can view";
                }
                var homePage = "http://localhost:4040/home";
                var sharedLink = "http://localhost:4040/analyze/" + this.presentationId;
                var presentationName = this.$store.state.presentation.presentationForm.name;
                var templateParams = {
                    "to_mail": toMail,
                    "from_name": "ChairVisE3.0 Team",
                    "to_name": toMail.split("@")[0].toString(),
                    "message_html": "User "+ creatorName + " modified your access level from a shared" +
                        " presentation("+presentationName+"). " +
                        "Now, you " + editLevel + " this presentation at " + sharedLink,
                    "home_page": homePage
                };

                var serviceId = "default_service";
                var templateId = "modify_access_level";
                emailjs.send(serviceId, templateId, templateParams, 'user_9GYYhuAzRpxnPndS8ZloS').then(() => {
                    console.log('UPDATE ACCESS CONTROL EMAIL SENT SUCCESS!');
                }, () => {
                    console.log('UPDATE ACCESS CONTROL EMAIL SEND FAILED...');
                });
            },
            deleteAccessControl({id}) {

                // send email if access deleted.
                var accessControlList = this.$store.state.accessControl.accessControlList;
                var creatorName = this.$store.state.userInfo.userEmail;
                var toMail;
                for (var i = 0; i < accessControlList.length; i++) {
                    if (accessControlList[i].id == id) {
                        toMail = accessControlList[i].userIdentifier;
                    }
                }
                var homePage = "http://localhost:4040/home";
                var sharedLink = "http://localhost:4040/analyze/" + this.presentationId;
                var presentationName = this.$store.state.presentation.presentationForm.name;
                var templateParams = {
                    "to_mail": toMail,
                    "from_name": "ChairVisE3.0 Team",
                    "to_name": toMail.split("@")[0].toString(),
                    "message_html": "User " +creatorName + " removed your access from a shared " +
                        "presentation("+presentationName+"). " +
                        "Now, you can't edit or view the presentation at " + sharedLink + ". " +
                        "Please contact the organizer of the presentation if you have any question.",
                    "home_page": homePage
                };
                var serviceId = "default_service";
                var templateId = "modify_access_level";
                emailjs.send(serviceId, templateId, templateParams, 'user_9GYYhuAzRpxnPndS8ZloS').then(() => {
                    console.log('DELETE ACCESS EMAIL SENT SUCCESSFULLY!');
                }, () => {
                    console.log('DELETE ACCESS EMAIL SEND FAILED...');
                });

                this.$store.dispatch('deleteAccessControl',
                    {
                        presentationId: this.presentationId,
                        id,
                    }
                );
            },
            addAccessControl() {
                this.$refs['accessControlForm'].validate((valid) => {
                    if (!valid) {
                        return
                    }
                    this.$store.dispatch('addAccessControl', {
                        presentationId: this.presentationId,
                        userIdentifier: this.accessControlFormUserIdentifier,
                        accessLevel: this.accessControlFormAccessLevel
                    })
                        .then(() => {
                            this.accessControlFormUserIdentifier = '';
                            this.accessControlFormAccessLevel = '';
                            this.$refs['accessControlForm'].resetFields();
                        });
                });
                var toMail = this.accessControlFormUserIdentifier;
                var editLevel;
                if (this.accessControlFormAccessLevel === "CAN_WRITE") {
                    editLevel = "can edit";
                } else if (this.accessControlFormAccessLevel === "CAN_READ") {
                    editLevel = "can view";
                }

                // send email if user created access.
                // console.log(this);
                var homePage = "http://localhost:4040/home";
                var sharedLink = "http://localhost:4040/analyze/" + this.presentationId;
                var presentationName = this.$store.state.presentation.presentationForm.name;
                var creatorName = this.$store.state.userInfo.userEmail;

                var templateParams = {
                    "to_mail": toMail,
                    "from_name": "ChairVisE3.0 Team",
                    "to_name": toMail.split("@")[0].toString(),
                    "message_html": "User "+creatorName+" shared a presentation ("+presentationName+") to you. Now, " +
                        "you " + editLevel + " this presentation at " + sharedLink,
                    "home_page": homePage
                };

                var serviceId = "default_service";
                var templateId = "template_F4kfWhTf";
                emailjs.send(serviceId, templateId, templateParams, 'user_9GYYhuAzRpxnPndS8ZloS').then(() => {
                    console.log('ADD ACCESS EMAIL SENT SUCCESSFULLY!');
                }, () => {
                    console.log('ADD ACCESS EMAIL SEND FAILED...');
                });
            },
        }
    }
</script>

<style scoped>
    .errorAlert {
        margin-bottom: 15px;
    }
</style>