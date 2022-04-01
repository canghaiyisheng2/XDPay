import * as echarts from 'echarts';

var option;
option = {
  xAxis: {
    type: 'category',
    data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  },
  yAxis: {
    type: 'value'
  },
  series: [
    {
      data: [150, 230, 224, 218, 135, 147, 260],
      type: 'line'
    }
  ]
};

export function mountChart(id){
    var chartDom = document.getElementById(id);
    var myChart = echarts.init(chartDom);
    myChart.setOption(option);
}