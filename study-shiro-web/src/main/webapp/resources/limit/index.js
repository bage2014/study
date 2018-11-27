
function init(){
	var resources = $("#oper-btn").attr("resources");
	resources = resources.split(",");
	var data = "resources=" + JSON.stringify(resources);
	$.ajax({
		url : "/limit",
		data : data,
		success : function(res) {
			console.log(res);
		}
	});
}