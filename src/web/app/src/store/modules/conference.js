import axios from 'axios';
import {deepCopy} from "@/common/utility"

export default {
    state: {
        conferenceList: [],
        conferenceListStatus: {
            isLoading: true,
            isApiError: false,
            apiErrorMsg: '',
        },
        conferenceForm: {
            id: '',
            conferenceName: '',
            conferenceYear: '',
            creatorIdentifier: '',
        },
        conferenceFormStatus: {
            isLoading: false,
            isApiError: false,
            apiErrorMsg: '',
        },
        isConferenceEditable: false
    },
    mutations: {
        setConferenceListLoading(state, payload) {
            if (payload) {
                state.conferenceListStatus.isApiError = false;
            }
            state.conferenceListStatus.isLoading = payload;
        },

        setConferenceListApiError(state, payload) {
            state.conferenceListStatus.isApiError = true;
            state.conferenceListStatus.apiErrorMsg = payload;
        },

        setConferenceList(state, payload) {
            state.conferenceList = payload;
        },

        addToConferenceList(state, payload) {
            state.conferenceList.push(payload)
        },

        deleteFromConferenceList(state, payload) {
            const index = state.conferenceList.findIndex(conference => conference.id === payload);
            state.conferenceList.splice(index, 1)
        },

        updateConferenceListWith(state, payload) {
            state.conferenceList = state.conferenceList.map(conference => {
                // if find conference with payload's id then use payload
                if (conference.id === payload.id) {
                    return payload
                }
                // else just return the current conference
                return conference
            });
        },

        setConferenceFormLoading(state, isLoading) {
            state.conferenceFormStatus.isLoading = isLoading;
        },

        setConferenceFormApiError(state, msg) {
            state.conferenceFormStatus.isApiError = true;
            state.conferenceFormStatus.apiErrorMsg = msg;
        },

        setConferenceForm(state, payload) {
            state.conferenceForm = payload;
        },

        setConferenceFormField(state, {field, value}) {
            state.conferenceForm[field] = value;
        },

        resetConferenceForm(state) {
            state.conferenceForm.id = '';
            state.conferenceForm.conferenceName = '';
            state.conferenceForm.conferenceYear = '';
            state.conferenceForm.creatorIdentifier = '';
            state.conferenceFormStatus.isApiError = false;
            state.conferenceFormStatus.isLoading = false;
            state.conferenceFormStatus.apiErrorMsg = '';
        },

        setIsConferenceEditable(state, isConferenceEditable) {
            state.isConferenceEditable = isConferenceEditable;
        },

    },
    actions: {
        async getConferenceList({commit}) {
            commit('setConferenceListLoading', true);
            axios.get('/api/conferences')
                .then(response => {
                    commit('setConferenceList', response.data)
                })
                .catch(e => {
                    commit('setConferenceListApiError', e.toString())
                })
                .finally(() => {
                    commit('setConferenceListLoading', false);
                })
        },

        async getConference({commit}, conferenceId) {
            commit('setConferenceFormLoading', true);
            await axios.get(`/api/conferences/${conferenceId}`)
                .then(response => {
                    commit('setConferenceForm', response.data)
                })
                .catch(e => {
                    commit('setConferenceFormApiError', e.toString());
                })
                .finally(() => {
                    commit('setConferenceFormLoading', false);
                });
        },

        async saveConference({commit, state}) {
            commit('setConferenceFormLoading', true);
            await axios.post('/api/conferences', state.conferenceForm)
                .then(response => {
                    commit('addToConferenceList', deepCopy(response.data));
                    commit('setConferenceForm', deepCopy(response.data))
                })
                .catch(e => {
                    commit('setConferenceFormApiError', e.toString());
                })
                .finally(() => {
                    commit('setConferenceFormLoading', false);
                })
        },

        async updateConference({commit, state}) {
            commit('setConferenceFormLoading', true);
            await axios.put('/api/conferences/' + state.conferenceForm.id, state.conferenceForm)
                .then(response => {
                    commit('updateConferenceListWith', response.data)
                })
                .catch(e => {
                    commit('setConferenceFormApiError', e.toString());
                })
                .finally(() => {
                    commit('setConferenceFormLoading', false);
                })
        },

        async deleteConference({commit}, payload) {
            commit('setConferenceFormLoading', true);
            await axios.delete('/api/conferences/' + payload)
                .then(() => {
                    commit('deleteFromConferenceList', payload);
                    commit('resetConferenceForm')
                })
                .catch(e => {
                    commit('setConferenceFormApiError', e.toString());
                })
                .finally(() => {
                    commit('setConferenceFormLoading', false);
                })
        }
    }
}