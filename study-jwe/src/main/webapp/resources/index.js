
var text = document.getElementById("plaintext").innerHTML;
//JWS signing 
// (alg, spHeader, spPayload, key, pass) {
sJWS = KJUR.jws.JWS.sign(null, {"alg":"HS256", "cty":"JWT"}, {"age": 21}, {"utf8": "password"});
console.log(text);

document.getElementById("ciphertext").innerHTML = sJWS;
// JWS validation
isValid = KJUR.jws.JWS.verify(sJWS, {"utf8": "password"});
console.log(isValid);
// JWT validation
isValid = KJUR.jws.JWS.verifyJWT(sJWS, {"utf8": "password"}, {
  alg: ['HS256', 'HS384'],
  iss: ['http://foo.com']
});

temp = KJUR.jws.JWS.parse(sJWS) ;
console.log(JSON.stringify(temp));