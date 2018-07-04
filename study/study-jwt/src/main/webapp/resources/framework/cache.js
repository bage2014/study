
function setStore(key, value) {

	if (!window.localStorage) {
		alert("浏览器支持localstorage");
		return false;
	} else {
		var storage = window.localStorage;
		//写入a字段
		storage[key] = value;
		//写入b字段
		//storage.a=1;
		//写入c字段
		//storage.setItem("c",3);
	}
}

function getExpirationDate() {
	return getStore("_expiration_date");
}

function setExpirationDate(date) {
	setStore("_expiration_date", date);
}
function getServerDate() {
	return getStore("_server_date");
}

function setServerDate(date) {
	setStore("_server_date", date);
}

function getStore(key) {

	if (!window.localStorage) {
		alert("浏览器支持localstorage");
		return false;
	} else {
		var storage = window.localStorage;
		return storage[key];
	}
	return "";
}

function setCurrentUser(user) {
	setStore("_current_user", user);
}

function getCurrentUser() {
	return getStore("_current_user");
}

function getToken() {
	var key = getCurrentUser();
	return getStore(key);
}

function setToken(token) {
	var key = getCurrentUser();
	return setStore(key, token);
}
