<template>
  <div class="app-container">
    <slot/>
  </div>
</template>

<script>
export default {
  name: 'LayoutContainer'
}
</script>

<style scoped>
  /** 全局大盒子 */
  .app-container {
    min-height: calc(100vh - 84px);
  //background-color: #EEEEE0 !important;
    background-color: #f0f2f5 !important;
  }
  .app-container {
    padding: 20px;
  }
</style>
