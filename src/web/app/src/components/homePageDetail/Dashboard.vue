<template>
  <el-row :gutter="20">
    <div class="header">
      <h2>Welcome, {{ userName }}.</h2>
      Below is a list of conferences and presentations you have in ChairVisE.
    </div>
    <el-col
      class="column-card"
      :sm="12"
    >
      <el-card>
        <div slot="header">
          <span class="header">Conferences</span>
          <router-link to="/conferenceList">
            <el-button
              style="float: right; padding: 3px"
              type="primary"
              plain
            >
              <i class="el-icon-plus" /> New Conference
            </el-button>
          </router-link>
        </div>
        <ul
          v-if="hasConferences"
          class="unstyled"
        >
          <li
            v-for="conference in conferences"
            :key="conference.id"
          >
            <router-link :to="`/conferenceList/${conference.id}`">
              {{ conference.conferenceName }}
            </router-link>
          </li>
        </ul>
        <div v-else>
          You currently do not have any conferences. <br><br>
          Create a new one using the "New conference" button above.
        </div>
      </el-card>
    </el-col>
    <el-col
      class="column-card"
      :sm="12"
    >
      <el-card>
        <div slot="header">
          <span class="header">Presentations</span>
          <router-link to="/analyze">
            <el-button
              v-if="hasConferences"
              style="float: right; padding: 3px"
              type="primary"
              plain
            >
              <i class="el-icon-plus" /> New Presentation
            </el-button>
          </router-link>
        </div>
        <ul
          v-if="hasPresentations"
          class="unstyled"
        >
          <li
            v-for="presentation in presentations"
            :key="presentation.id"
          >
            <router-link :to="`/analyze/${presentation.id}`">
              {{ presentation.name }}
            </router-link>
          </li>
        </ul>
        <div v-else-if="hasConferences">
          You currently do not have any presentations. <br><br>
          Create a new one using the "New Presentation" button above.
        </div>
        <div v-else>
          You need to create a conference before creating presentations. <br><br>
          Create a new conference using the "New Conference" button on the left.
        </div>
      </el-card>
    </el-col>
    <!-- <el-col
      class="column-card"
      :sm="8"
    >
      <el-card>
        <h1>Share</h1>
        and export your visualisation with others.
      </el-card>
    </el-col> -->
  </el-row>
</template>

<script>
export default {
    name: 'Dashboard',
    computed: {
        userName() {
          return this.$store.state.userInfo.userNickname;
        },
        conferences() {
            return this.$store.state.conference.conferenceList
        },
        hasConferences() {
          return this.$store.state.conference.conferenceList.length > 0;
        },
        presentations() {
            return this.$store.state.presentation.presentationList
        },
        hasPresentations() {
          return this.$store.state.presentation.presentationList.length > 0;
        },
    },
    created() {
        this.$store.dispatch('getConferenceList');
        this.$store.dispatch('getPresentationList');
    },
}
</script>

<style>
h2 {
  font-weight: lighter;
}

.header {
  padding-bottom: 20px;
}

.column-card {
    padding: 10px;
  }

.column-card .header {
    font-weight: lighter;
    font-size: 1.1em;
}

ul.unstyled {
    list-style-type: none;
    padding-left: 0;
}
</style>