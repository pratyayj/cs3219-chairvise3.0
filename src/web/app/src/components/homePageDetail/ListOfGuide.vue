<template>
  <div>
    <h4><i class="el-icon-document" /> List of Information Available</h4>
    <el-menu
      default-active="general-guide"
      @select="scrollToId($event)"
    >
      <el-menu-item index="general-guide">
        <i class="el-icon-question" />
        <span>
          General User Guide
        </span>
      </el-menu-item>
      <el-menu-item index="features-available">
        <i class="el-icon-question" />
        <span>
          Features Supported
        </span>
      </el-menu-item>
      <el-menu-item index="import-guide">
        <i class="el-icon-question" />
        <span>
          How to Import Data
        </span>
      </el-menu-item>
      <el-menu-item index="add-guide">
        <i class="el-icon-question" />
        <span>
          How to Add Presentation
        </span>
      </el-menu-item>
      <el-menu-item index="share-guide">
        <i class="el-icon-question" />
        <span>
          How to Share Presentation
        </span>
      </el-menu-item>
      <el-menu-item index="add-conference">
        <i class="el-icon-question"></i>
        <span>
          How to Add Conference
        </span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
  export default {
    name: 'ListOfGuide',
    methods: {
      scrollToId(id) {
        document.getElementById(id).scrollIntoView();
      }
    }
  }
</script>
