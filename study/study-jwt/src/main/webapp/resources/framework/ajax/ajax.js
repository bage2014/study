
//首先备份下jquery的ajax方法
var _ajax=$.ajax;
//重写jquery的ajax方法
$.ajax=function(opt){
	
	//扩展增强处理
	var _opt = $.extend(opt,{
		headers : {
				'Authorization' : '12306'
		}
	});
	
	return _ajax(_opt);
};
