<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
  import echarts from "echarts"
  require('echarts/theme/macarons') // echarts theme
  require('echarts/lib/component/title');
  require('echarts/lib/component/toolbox');
  require('echarts/lib/component/tooltip');
  require('echarts/lib/component/grid');
  require('echarts/lib/component/legend');
  require('echarts/lib/chart/line');
  import resize from './mixins/resize'
  export default {
    name: 'GradualStackMap',
    mixins: [resize],
    props: {
      className: {
        type: String,
        default: 'chart'
      },
      width: {
        type: String,
        default: '100%'
      },
      height: {
        type: String,
        default: '300px'
      }
    },
    data() {
      return {
        chart: null,
      }
    },
    mounted() {
      this.$nextTick(() => {
        this.initChart()
      })
    },
    beforeDestroy() {
      if (!this.chart) {
        return false;
      }
      this.chart.dispose()
      this.chart = null
    },
    methods: {
      initChart() {
        this.chart = echarts.init(this.$el,"macarons");

        this.chart.setOption( {
          color: ['#80FFA5', '#00DDFF', '#37A2FF', '#FF0087', '#FFBF00'],
          title: {text: '周销量统计图' },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross',
              label: {
                backgroundColor: '#6a7985'
              }
            }
          },
          legend: {
            data: ['Line 1', 'Line 2', 'Line 3', 'Line 4', 'Line 5']
          },
          toolbox: {
            feature: {
              saveAsImage: {}
            }
          },
          grid: {
            left: '2%',
            right: '2%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: [
            {
              type: 'category',
              boundaryGap: false,
              data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
            }
          ],
          yAxis: [
            { type: 'value' }
          ],
          series: [
            {
              name: 'Line 1',
              type: 'line',
              stack: '总量',
              smooth: true,
              lineStyle: {
                width: 0
              },
              showSymbol: false,
              areaStyle: {
                opacity: 0.8,
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                  offset: 0,
                  color: 'rgba(128, 255, 165)'
                }, {
                  offset: 1,
                  color: 'rgba(1, 191, 236)'
                }])
              },
              emphasis: {
                focus: 'series'
              },
              data: [140, 232, 101, 264, 90, 340, 250]
            },
            {
              name: 'Line 2',
              type: 'line',
              stack: '总量',
              smooth: true,
              lineStyle: {
                width: 0
              },
              showSymbol: false,
              areaStyle: {
                opacity: 0.8,
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                  offset: 0,
                  color: 'rgba(0, 221, 255)'
                }, {
                  offset: 1,
                  color: 'rgba(77, 119, 255)'
                }])
              },
              emphasis: {
                focus: 'series'
              },
              data: [120, 282, 111, 234, 220, 340, 310]
            },
            {
              name: 'Line 3',
              type: 'line',
              stack: '总量',
              smooth: true,
              lineStyle: {
                width: 0
              },
              showSymbol: false,
              areaStyle: {
                opacity: 0.8,
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                  offset: 0,
                  color: 'rgba(55, 162, 255)'
                }, {
                  offset: 1,
                  color: 'rgba(116, 21, 219)'
                }])
              },
              emphasis: {
                focus: 'series'
              },
              data: [320, 132, 201, 334, 190, 130, 220]
            },
            {
              name: 'Line 4',
              type: 'line',
              stack: '总量',
              smooth: true,
              lineStyle: {
                width: 0
              },
              showSymbol: false,
              areaStyle: {
                opacity: 0.8,
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                  offset: 0,
                  color: 'rgba(255, 0, 135)'
                }, {
                  offset: 1,
                  color: 'rgba(135, 0, 157)'
                }])
              },
              emphasis: {
                focus: 'series'
              },
              data: [220, 402, 231, 134, 190, 230, 120]
            },
            {
              name: 'Line 5',
              type: 'line',
              stack: '总量',
              smooth: true,
              lineStyle: {
                width: 0
              },
              showSymbol: false,
              label: {
                show: true,
                position: 'top'
              },
              areaStyle: {
                opacity: 0.8,
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                  offset: 0,
                  color: 'rgba(255, 191, 0)'
                }, {
                  offset: 1,
                  color: 'rgba(224, 62, 76)'
                }])
              },
              emphasis: {
                focus: 'series'
              },
              data: [220, 302, 181, 234, 210, 290, 150]
            }
          ]
        })
      }
    }
  }
</script>

<style scoped>

</style>
