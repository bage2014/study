
$("#current_user").html(getCurrentUser());

/**
 * 通过ajax从后台请求数据
 * @returns
 */
function getAjaxData() {

	var param = "";
	$.ajax({
		url : "me",
		//type : 'POST',
		data : param,
		success : function(res) {
			alert(res);
		}
	});
}

function logout() {

	var param = "";
	$.ajax({
		url : "logout",
		//type : 'POST',
		data : param,
		success : function(res) {
			if ("success" == res) {
				location.href = "../";
			}
		}
	});
}
