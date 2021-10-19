<template>
  <div class="animeContent" @click="toRouter">
    <div class="stagger-visualizer"></div>
  </div>
</template>

<script>
  import { lockScreen } from '@/utils/anime'
  export default {
    name: 'lockScreen',
    mounted() {
      this.$nextTick(() => {
        lockScreen();
      })
    },
    methods: {
      toRouter() {
        this.$router.push({path: "/index"})
      }
    }
  }
</script>

<style lang="scss">
  .animeContent{
    position: absolute;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    align-items: center;
    width: 100%;
    min-height: 100vh;
    background-color:#141019;
    background: radial-gradient(at 50% -20%, #908392, #0d060e) fixed;
    overflow: hidden;
    .stagger-visualizer {
      display: flex;
      /*flex-wrap: wrap;*/
      justify-content: center;
      align-items: center;
      width: 450px;
      height: 450px;
    }
    .stagger-visualizer .animeDiv {
      /*position: absolute;*/
      width: 64px;
      height: 128px;
      /* border: 1px solid #373535; */
      border: 2px solid radial-gradient(at 50% -20%,#908392,#0d060e);
      box-sizing: inherit;
      background-color: #141019;
      background: radial-gradient(at 20% -50%,#0d060e, #908392);
    }
  }
</style>
