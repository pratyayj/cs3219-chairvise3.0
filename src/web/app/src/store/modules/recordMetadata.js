/* eslint-disable */
import axios from 'axios';
import {deepCopy} from "@/common/utility"

export default {
    state: {
        recordMetadataForm: {
            conferenceName: ''
        }
    },
    mutations: {
        setRecordMetadataForm(state, payload) {
            state.recordMetadataForm = payload;
        },

        setRecordMetadataFormField(state, {field, value}) {
            state.recordMetadataForm[field] = value;
        }
    }
}