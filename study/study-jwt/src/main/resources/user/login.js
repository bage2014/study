
/**
 * 加密
 * @param data
 * @param key
 * @returns
 */
function encrypt(data,key){
	 return CryptoJS.AES.encrypt(data, key);
}

/**
 * 解密
 * @param encryptedData
 * @param key
 * @returns
 */
function decrypt(encryptedData,key){
	 return CryptoJS.AES.decrypt(encryptedData, key);
}