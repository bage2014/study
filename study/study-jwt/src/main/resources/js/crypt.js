
/**
 * AES加密
 * @param data
 * @param key
 * @returns
 */
function encryptAes(data,key){
	 return CryptoJS.AES.encrypt(data, key);
}

/**
 * AES解密
 * @param encryptedData
 * @param key
 * @returns
 */
function decryptAes(encryptedData,key){
	 return CryptoJS.AES.decrypt(encryptedData, key);
}