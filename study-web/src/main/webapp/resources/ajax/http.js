
var Http = function(){
    return {
        baseUrl: '',
        jsonGet: function(url,json,callback,headers) {
            this.request(url,json,callback,headers,'GET');
        },
        jsonPost: function(url,json,callback,headers) {
            this.request(url,json,callback,headers,'POST');
        },
        get: function(url,paramPairs,callback,headers) {
            this.request(url,paramPairs,callback,headers,'GET');
        },
        post: function(url,paramPairs,callback,headers) {
            this.request(url,paramPairs,callback,headers,'POST');
        },
        request: function(url,paramPairs,callback,headers,method) {
        	if(false){
        		this.jqueryAjax(url,paramPairs,callback,headers,method);
        	} else {
        		// 原生ajax 
        		this.ajax(url,paramPairs,callback,headers,method);
        	}
            
        },
        ajax: function(url,paramPairs,callback,headers,method) {
        	
        	var datas = this.pairsToString(paramPairs);

            var xhr = null;
            if (window.XMLHttpRequest) {
                xhr = new XMLHttpRequest
            } else {
                xhr = new ActiveXObject('Microsoft.XMLHttp')
            }
            if (method == 'POST') {
            	xhr.open(method, url, true);
                xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded');
                xhr.send(datas);
            } else { // GET
                xhr.open(method, url + datas, true);
                xhr.send();
            }

            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4) {
                    if (xhr.status == 200) {
                        callback(xhr.responseText);
                    }
                }
            }
        },
        
        jqueryAjax: function(url,paramPairs,callback,headers,method) {
            
        },

        pairsToString: function(paramPairs) {
        	var params = "";
            if(paramPairs){
            	for(var index in paramPairs){
            		var pair = paramPairs[index];
            		if(params == ""){
            			params = params + "?";
            		} else {
            			params = params + "&";
            		}
            		// 获取第一个属性
            		for(var key in pair){
            		    params += key + "=" + pair[key];
            			break;
            		}
            	}
            }
            return params;
        },

        
    };
}();

