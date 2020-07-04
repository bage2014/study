
var Http = function(){
    return {
        baseUrl: '',
        get: function(url,data,callBack) {
            return this.request(url,data,callback,‘POST’);
        },
        post: function(url,data,callBack) {
            return this.request(url,data,callback,‘GET’);
        },
        request: function(url,data,callback,method) {
            var xhr = null;
            if (window.XMLHttpRequest) {
                xhr = new XMLHttpRequest
            } else {
                xhr = new ActiveXObject('Microsoft.XMLHttp')
            }
            if (method == 'GET') {
                xhr.open(method, url+'?'+data, true);
                xhr.send();
            } else if (method == 'POST') {
                xhr.open(method, url, true);
                xhr.setRequestHeader('Content-type','application/x-www-form-urlencoded')
                xhr.send(data);
            }

            xhr.onreadystatechange = function () {
                if (xhr.readyState == 4) {
                    if (xhr.status == 200) {
                        callback(xhr.responseText);
                    }
                }
            }
        }
    };
}();

