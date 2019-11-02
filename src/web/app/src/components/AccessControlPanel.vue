<template>
    <div v-loading="isAccessControlPanelLoading">
        <h4>Shareable Link</h4>
        <el-input
                :value="currentUrl"
                @focus="$event.target.select()">
            <template slot="prepend">Any one with the link</template>
            <template slot="append">
                <el-select :value="publicAccessLevel" @change="modifyPublicAccessControl($event)" style="width: 150px">
                    <el-option label="Cannot Access" value="OFF"></el-option>
                    <el-option label="Can View" value="CAN_READ"></el-option>
                    <el-option label="Can Edit" value="CAN_WRITE"></el-option>
                </el-select>
            </template>
        </el-input>
        <h4>Specific Access Control</h4>
        <el-alert v-if="isAccessControlListApiError" :title="accessControlListApiErrorMsg" type="error" show-icon
                  class="errorAlert"/>
        <el-table
                :data="accessControlList"
                style="width: 100%" emptyText="No Access Control for this Presentation!">
            <el-table-column
                    prop="userIdentifier"
                    label="Email">
            </el-table-column>
            <el-table-column
                    label="Access Level">
                <template slot-scope="scope">
                    <el-select :value="scope.row.accessLevel" placeholder="Select the permission"
                               @change="updateAccessControl(scope.row, $event)">
                        <el-option label="View" value="CAN_READ"></el-option>
                        <el-option label="Edit" value="CAN_WRITE"></el-option>
                    </el-select>&nbsp;
                    <el-button
                            type="danger"
                            @click="deleteAccessControl(scope.row)" icon="el-icon-delete" circle></el-button>
                </template>
            </el-table-column>
        </el-table>
        <h4>Add New Access Control</h4>
        <el-alert v-if="isAccessControlFormApiError" :title="accessControlFormApiErrorMsg" type="error" show-icon
                  class="errorAlert"/>
        <el-form ref="accessControlForm" label-position="left" label-width="120px" :model="accessControlForm"
                 :rules="accessControlFormRule">
            <el-form-item label="Email address" prop="userIdentifier">
                <el-input v-model="accessControlFormUserIdentifier" placeholder="Email of the user to share"></el-input>
            </el-form-item>
            <el-form-item label="Permissions" prop="accessLevel">
                <el-select v-model="accessControlFormAccessLevel" placeholder="Permission the user will have"
                           style="width: 100%">
                    <el-option label="View" value="CAN_READ"></el-option>
                    <el-option label="Edit" value="CAN_WRITE"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="addAccessControl()">Add</el-button>
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

        watch: {
            'presentationId': {
                immediate: true,
                handler: 'fetchAccessControlList'
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

        beforeUpdate() {
            this.currentUrl = window.location.href;
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

        methods: {
            modifyPublicAccessControl(accessLevel) {
                let publicAccessControl =
                    this.$store.state.accessControl.accessControlList.find(ac => ac.userIdentifier === SPECIAL_IDENTIFIER_PUBLIC);

                // delete
                if (accessLevel === 'OFF' && publicAccessControl !== undefined) {
                    console.log("delete presentation id: " + this.presentationId);
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
                    console.log("create presentation id: " + this.presentationId);
                    this.$store.dispatch('addAccessControl', {

                        presentationId: this.presentationId,
                        userIdentifier: SPECIAL_IDENTIFIER_PUBLIC,
                        accessLevel
                    })
                } else {
                    // update if exist
                    console.log("update presentation id: " + this.presentationId);
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
                console.log("fetch presentation id: " + this.presentationId);
                if (this.presentationId === ID_NEW_PRESENTATION) {
                    return;
                }
                this.$store.dispatch('fetchAccessControlList', this.presentationId)
            },

            updateAccessControl({id}, $event) {
                console.log("update presentation id: " + this.presentationId);
                this.$store.dispatch('updateAccessControl',
                    {
                        presentationId: this.presentationId,
                        id,
                        accessLevel: $event
                    }
                );
            },
            deleteAccessControl({id}) {
                console.log("delete presentation id: " + this.presentationId);
                this.$store.dispatch('deleteAccessControl',
                    {
                        presentationId: this.presentationId,
                        id,
                    }
                );
            },
            addAccessControl() {
                console.log("add access control presentation id: " + this.presentationId);
                console.log("add access control presentation id: " + this.accessControlFormUserIdentifier);
                console.log("add access control presentation id: " + this.accessControlFormAccessLevel);
                var name = this.accessControlFormUserIdentifier;
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
                var template_params = {
                    "from_name": name,
                    "to_name": "don't_know_who",
                    "message_html": "This is the testing message"
                };

                var service_id = "default_service";
                var template_id = "template_F4kfWhTf";
                emailjs.send(service_id, template_id, template_params, 'user_9GYYhuAzRpxnPndS8ZloS').then((result) => {
                    console.log('SUCCESS!');
                }, (error) => {
                    console.log('FAILED...');
                });

            },

            sendEmail: (e) => {
                var name = this.accessControlFormUserIdentifier;
                var template_params = {
                    "from_name": name,
                    "to_name": e.target,
                    "message_html": "This is the testing message"
                };
                emailjs.sendForm('default_service', 'template_F4kfWhTf', template_params, 'user_9GYYhuAzRpxnPndS8ZloS')
                    .then((result) => {
                        console.log('SUCCESS!', response.status, response.text);
                    }, (error) => {
                        console.log('FAILED...', error);
                    });
            }
        }
    }
</script>

<style scoped>
    .errorAlert {
        margin-bottom: 15px;
    }
</style>