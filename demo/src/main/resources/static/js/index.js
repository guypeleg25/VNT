$.ajax({
       url: 'cpu',
       success: function(result){
       var period = JSON.parse(result).period;
       var avgCpu = JSON.parse(result).avgCpu;
       drawLineChart(period,avgCpu)
       }
})

function drawLineChart(period , avgCpu){
Highcharts.chart('container', {
	    chart: {
	        type: 'line',
	        width: 500
	    },

	    title: {
	        text: 'Line chart'
	    },

	    xAxis: {
	        categories: period
	    },

	    tooltip: {
	        formatter: function() {
	        	  return '<strong>'+this.x+': </strong>'+ this.y;
	        }
	    },
	    series: [{
	        data: avgCpu
	    }]
	});
}