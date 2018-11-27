package com.bage.study.cryption.aes;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 实现参考：https://github.com/tozny/java-aes-crypto/blob/master/aes-crypto/src/main/java/com/tozny/crypto/android/AesCbcWithIntegrity.java
 * 
 * @author luruihua
 *
 */
public class MyAES {

	private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final String CIPHER = "AES";
	private static final int AES_KEY_LENGTH_BITS = 128;
	private static final int IV_LENGTH_BYTES = 16;
	private static final int PBE_ITERATION_COUNT = 10000;
	private static final int PBE_SALT_LENGTH_BITS = AES_KEY_LENGTH_BITS; // same size as key output
	private static final String PBE_ALGORITHM = "PBKDF2WithHmacSHA1";

	// default for testing
	static final AtomicBoolean prngFixed = new AtomicBoolean(false);

	private static final String HMAC_ALGORITHM = "HmacSHA256";
	private static final int HMAC_KEY_LENGTH_BITS = 256;

	public static void main(String[] args) {

		try {

			// Generate new key
			MyAES.SecretKeys keys = MyAES.generateKey();
			System.out.println(keys);
			// Encrypt
			MyAES.CipherTextIvMac cipherTextIvMac = MyAES.encrypt("fsjgsjgjdgjdhgjngsjghsjgh", keys);
			// store or send to server
			String cipherTextString = cipherTextIvMac.toString();

			System.out.println(cipherTextString);

			// Decrypt
			// Use the constructor to re-create the CipherTextIvMac class from the string:
			CipherTextIvMac cipherTextIvMacDecrypt = new CipherTextIvMac(cipherTextString);
			String plainText = MyAES.decryptString(cipherTextIvMacDecrypt, keys);
			System.out.println(plainText);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Converts the given AES/HMAC keys into a base64 encoded string suitable for
	 * storage. Sister function of keys.
	 *
	 * @param keys
	 *            The combined aes and hmac keys
	 * @return a base 64 encoded AES string and hmac key as base64(aesKey) :
	 *         base64(hmacKey)
	 */
	public static String keyString(SecretKeys keys) {
		return keys.toString();
	}

	/**
	 * An aes key derived from a base64 encoded key. This does not generate the key.
	 * It's not random or a PBE key.
	 *
	 * @param keysStr
	 *            a base64 encoded AES key / hmac key as base64(aesKey) :
	 *            base64(hmacKey).
	 * @return an AES and HMAC key set suitable for other functions.
	 */
	public static SecretKeys keys(String keysStr) throws InvalidKeyException {
		String[] keysArr = keysStr.split(":");

		if (keysArr.length != 2) {
			throw new IllegalArgumentException("Cannot parse aesKey:hmacKey");

		} else {
			byte[] confidentialityKey = Base64.getDecoder().decode(keysArr[0]);
			if (confidentialityKey.length != AES_KEY_LENGTH_BITS / 8) {
				throw new InvalidKeyException("Base64 decoded key is not " + AES_KEY_LENGTH_BITS + " bytes");
			}
			byte[] integrityKey = Base64.getDecoder().decode(keysArr[1]);
			if (integrityKey.length != HMAC_KEY_LENGTH_BITS / 8) {
				throw new InvalidKeyException("Base64 decoded key is not " + HMAC_KEY_LENGTH_BITS + " bytes");
			}

			return new SecretKeys(new SecretKeySpec(confidentialityKey, 0, confidentialityKey.length, CIPHER),
					new SecretKeySpec(integrityKey, HMAC_ALGORITHM));
		}
	}

	/**
	 * A function that generates random AES and HMAC keys and prints out exceptions
	 * but doesn't throw them since none should be encountered. If they are
	 * encountered, the return value is null.
	 *
	 * @return The AES and HMAC keys.
	 * @throws GeneralSecurityException
	 *             if AES is not implemented on this system, or a suitable RNG is
	 *             not available
	 */
	public static SecretKeys generateKey() throws GeneralSecurityException {
		KeyGenerator keyGen = KeyGenerator.getInstance(CIPHER);
		// No need to provide a SecureRandom or set a seed since that will
		// happen automatically.
		keyGen.init(AES_KEY_LENGTH_BITS);
		SecretKey confidentialityKey = keyGen.generateKey();

		// Now make the HMAC key
		byte[] integrityKeyBytes = randomBytes(HMAC_KEY_LENGTH_BITS / 8);// to get bytes
		SecretKey integrityKey = new SecretKeySpec(integrityKeyBytes, HMAC_ALGORITHM);

		return new SecretKeys(confidentialityKey, integrityKey);
	}

	/**
	 * A function that generates password-based AES and HMAC keys. It prints out
	 * exceptions but doesn't throw them since none should be encountered. If they
	 * are encountered, the return value is null.
	 *
	 * @param password
	 *            The password to derive the keys from.
	 * @return The AES and HMAC keys.
	 * @throws GeneralSecurityException
	 *             if AES is not implemented on this system, or a suitable RNG is
	 *             not available
	 */
	public static SecretKeys generateKeyFromPassword(String password, byte[] salt) throws GeneralSecurityException {
		// Get enough random bytes for both the AES key and the HMAC key:
		KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT,
				AES_KEY_LENGTH_BITS + HMAC_KEY_LENGTH_BITS);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBE_ALGORITHM);
		byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();

		// Split the random bytes into two parts:
		byte[] confidentialityKeyBytes = copyOfRange(keyBytes, 0, AES_KEY_LENGTH_BITS / 8);
		byte[] integrityKeyBytes = copyOfRange(keyBytes, AES_KEY_LENGTH_BITS / 8,
				AES_KEY_LENGTH_BITS / 8 + HMAC_KEY_LENGTH_BITS / 8);

		// Generate the AES key
		SecretKey confidentialityKey = new SecretKeySpec(confidentialityKeyBytes, CIPHER);

		// Generate the HMAC key
		SecretKey integrityKey = new SecretKeySpec(integrityKeyBytes, HMAC_ALGORITHM);

		return new SecretKeys(confidentialityKey, integrityKey);
	}

	/**
	 * A function that generates password-based AES and HMAC keys. See
	 * generateKeyFromPassword.
	 * 
	 * @param password
	 *            The password to derive the AES/HMAC keys from
	 * @param salt
	 *            A string version of the salt; base64 encoded.
	 * @return The AES and HMAC keys.
	 * @throws GeneralSecurityException
	 */
	public static SecretKeys generateKeyFromPassword(String password, String salt) throws GeneralSecurityException {
		return generateKeyFromPassword(password, Base64.getDecoder().decode(salt));
	}

	/**
	 * Generates a random salt.
	 * 
	 * @return The random salt suitable for generateKeyFromPassword.
	 */
	public static byte[] generateSalt() throws GeneralSecurityException {
		return randomBytes(PBE_SALT_LENGTH_BITS);
	}

	/**
	 * Converts the given salt into a base64 encoded string suitable for storage.
	 *
	 * @param salt
	 * @return a base 64 encoded salt string suitable to pass into
	 *         generateKeyFromPassword.
	 */
	public static String saltString(byte[] salt) {
		return Base64.getEncoder().encodeToString(salt);
	}

	/**
	 * Creates a random Initialization Vector (IV) of IV_LENGTH_BYTES.
	 *
	 * @return The byte array of this IV
	 * @throws GeneralSecurityException
	 *             if a suitable RNG is not available
	 */
	public static byte[] generateIv() throws GeneralSecurityException {
		return randomBytes(IV_LENGTH_BYTES);
	}

	private static byte[] randomBytes(int length) throws GeneralSecurityException {
		SecureRandom random = new SecureRandom();
		byte[] b = new byte[length];
		random.nextBytes(b);
		return b;
	}

	/*
	 * ----------------------------------------------------------------- Encryption
	 * -----------------------------------------------------------------
	 */

	/**
	 * Generates a random IV and encrypts this plain text with the given key. Then
	 * attaches a hashed MAC, which is contained in the CipherTextIvMac class.
	 *
	 * @param plaintext
	 *            The text that will be encrypted, which will be serialized with
	 *            UTF-8
	 * @param secretKeys
	 *            The AES and HMAC keys with which to encrypt
	 * @return a tuple of the IV, ciphertext, mac
	 * @throws GeneralSecurityException
	 *             if AES is not implemented on this system
	 * @throws UnsupportedEncodingException
	 *             if UTF-8 is not supported in this system
	 */
	public static CipherTextIvMac encrypt(String plaintext, SecretKeys secretKeys)
			throws UnsupportedEncodingException, GeneralSecurityException {
		return encrypt(plaintext, secretKeys, "UTF-8");
	}

	/**
	 * Generates a random IV and encrypts this plain text with the given key. Then
	 * attaches a hashed MAC, which is contained in the CipherTextIvMac class.
	 *
	 * @param plaintext
	 *            The bytes that will be encrypted
	 * @param secretKeys
	 *            The AES and HMAC keys with which to encrypt
	 * @return a tuple of the IV, ciphertext, mac
	 * @throws GeneralSecurityException
	 *             if AES is not implemented on this system
	 * @throws UnsupportedEncodingException
	 *             if the specified encoding is invalid
	 */
	public static CipherTextIvMac encrypt(String plaintext, SecretKeys secretKeys, String encoding)
			throws UnsupportedEncodingException, GeneralSecurityException {
		return encrypt(plaintext.getBytes(encoding), secretKeys);
	}

	/**
	 * Generates a random IV and encrypts this plain text with the given key. Then
	 * attaches a hashed MAC, which is contained in the CipherTextIvMac class.
	 *
	 * @param plaintext
	 *            The text that will be encrypted
	 * @param secretKeys
	 *            The combined AES and HMAC keys with which to encrypt
	 * @return a tuple of the IV, ciphertext, mac
	 * @throws GeneralSecurityException
	 *             if AES is not implemented on this system
	 */
	public static CipherTextIvMac encrypt(byte[] plaintext, SecretKeys secretKeys) throws GeneralSecurityException {
		byte[] iv = generateIv();
		Cipher aesCipherForEncryption = Cipher.getInstance(CIPHER_TRANSFORMATION);
		aesCipherForEncryption.init(Cipher.ENCRYPT_MODE, secretKeys.getConfidentialityKey(), new IvParameterSpec(iv));

		/*
		 * Now we get back the IV that will actually be used. Some Android versions do
		 * funny stuff w/ the IV, so this is to work around bugs:
		 */
		iv = aesCipherForEncryption.getIV();
		byte[] byteCipherText = aesCipherForEncryption.doFinal(plaintext);
		byte[] ivCipherConcat = CipherTextIvMac.ivCipherConcat(iv, byteCipherText);

		byte[] integrityMac = generateMac(ivCipherConcat, secretKeys.getIntegrityKey());
		return new CipherTextIvMac(byteCipherText, iv, integrityMac);
	}

	/*
	 * ----------------------------------------------------------------- Decryption
	 * -----------------------------------------------------------------
	 */

	/**
	 * AES CBC decrypt.
	 *
	 * @param civ
	 *            The cipher text, IV, and mac
	 * @param secretKeys
	 *            The AES and HMAC keys
	 * @param encoding
	 *            The string encoding to use to decode the bytes after decryption
	 * @return A string derived from the decrypted bytes (not base64 encoded)
	 * @throws GeneralSecurityException
	 *             if AES is not implemented on this system
	 * @throws UnsupportedEncodingException
	 *             if the encoding is unsupported
	 */
	public static String decryptString(CipherTextIvMac civ, SecretKeys secretKeys, String encoding)
			throws UnsupportedEncodingException, GeneralSecurityException {
		return new String(decrypt(civ, secretKeys), encoding);
	}

	/**
	 * AES CBC decrypt.
	 *
	 * @param civ
	 *            The cipher text, IV, and mac
	 * @param secretKeys
	 *            The AES and HMAC keys
	 * @return A string derived from the decrypted bytes, which are interpreted as a
	 *         UTF-8 String
	 * @throws GeneralSecurityException
	 *             if AES is not implemented on this system
	 * @throws UnsupportedEncodingException
	 *             if UTF-8 is not supported
	 */
	public static String decryptString(CipherTextIvMac civ, SecretKeys secretKeys)
			throws UnsupportedEncodingException, GeneralSecurityException {
		return decryptString(civ, secretKeys, "UTF-8");
	}

	/**
	 * AES CBC decrypt.
	 *
	 * @param civ
	 *            the cipher text, iv, and mac
	 * @param secretKeys
	 *            the AES and HMAC keys
	 * @return The raw decrypted bytes
	 * @throws GeneralSecurityException
	 *             if MACs don't match or AES is not implemented
	 */
	public static byte[] decrypt(CipherTextIvMac civ, SecretKeys secretKeys) throws GeneralSecurityException {

		byte[] ivCipherConcat = CipherTextIvMac.ivCipherConcat(civ.getIv(), civ.getCipherText());
		byte[] computedMac = generateMac(ivCipherConcat, secretKeys.getIntegrityKey());
		if (constantTimeEq(computedMac, civ.getMac())) {
			Cipher aesCipherForDecryption = Cipher.getInstance(CIPHER_TRANSFORMATION);
			aesCipherForDecryption.init(Cipher.DECRYPT_MODE, secretKeys.getConfidentialityKey(),
					new IvParameterSpec(civ.getIv()));
			return aesCipherForDecryption.doFinal(civ.getCipherText());
		} else {
			throw new GeneralSecurityException("MAC stored in civ does not match computed MAC.");
		}
	}

	/*
	 * ----------------------------------------------------------------- Helper Code
	 * -----------------------------------------------------------------
	 */

	/**
	 * Generate the mac based on HMAC_ALGORITHM
	 * 
	 * @param integrityKey
	 *            The key used for hmac
	 * @param byteCipherText
	 *            the cipher text
	 * @return A byte array of the HMAC for the given key and ciphertext
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 */
	public static byte[] generateMac(byte[] byteCipherText, SecretKey integrityKey)
			throws NoSuchAlgorithmException, InvalidKeyException {
		// Now compute the mac for later integrity checking
		Mac sha256_HMAC = Mac.getInstance(HMAC_ALGORITHM);
		sha256_HMAC.init(integrityKey);
		return sha256_HMAC.doFinal(byteCipherText);
	}

	/**
	 * Holder class that has both the secret AES key for encryption
	 * (confidentiality) and the secret HMAC key for integrity.
	 */

	public static class SecretKeys {
		private SecretKey confidentialityKey;
		private SecretKey integrityKey;

		/**
		 * Construct the secret keys container.
		 * 
		 * @param confidentialityKeyIn
		 *            The AES key
		 * @param integrityKeyIn
		 *            the HMAC key
		 */
		public SecretKeys(SecretKey confidentialityKeyIn, SecretKey integrityKeyIn) {
			setConfidentialityKey(confidentialityKeyIn);
			setIntegrityKey(integrityKeyIn);
		}

		public SecretKey getConfidentialityKey() {
			return confidentialityKey;
		}

		public void setConfidentialityKey(SecretKey confidentialityKey) {
			this.confidentialityKey = confidentialityKey;
		}

		public SecretKey getIntegrityKey() {
			return integrityKey;
		}

		public void setIntegrityKey(SecretKey integrityKey) {
			this.integrityKey = integrityKey;
		}

		/**
		 * Encodes the two keys as a string
		 * 
		 * @return base64(confidentialityKey):base64(integrityKey)
		 */
		@Override
		public String toString() {
			return Base64.getEncoder().encodeToString(getConfidentialityKey().getEncoded()) + ":"
					+ Base64.getEncoder().encodeToString(getIntegrityKey().getEncoded());
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + confidentialityKey.hashCode();
			result = prime * result + integrityKey.hashCode();
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SecretKeys other = (SecretKeys) obj;
			if (!integrityKey.equals(other.integrityKey))
				return false;
			if (!confidentialityKey.equals(other.confidentialityKey))
				return false;
			return true;
		}
	}

	/**
	 * Simple constant-time equality of two byte arrays. Used for security to avoid
	 * timing attacks.
	 * 
	 * @param a
	 * @param b
	 * @return true iff the arrays are exactly equal.
	 */
	public static boolean constantTimeEq(byte[] a, byte[] b) {
		if (a.length != b.length) {
			return false;
		}
		int result = 0;
		for (int i = 0; i < a.length; i++) {
			result |= a[i] ^ b[i];
		}
		return result == 0;
	}

	/**
	 * Holder class that allows us to bundle ciphertext and IV together.
	 */
	public static class CipherTextIvMac {
		private final byte[] cipherText;
		private final byte[] iv;
		private final byte[] mac;

		public byte[] getCipherText() {
			return cipherText;
		}

		public byte[] getIv() {
			return iv;
		}

		public byte[] getMac() {
			return mac;
		}

		/**
		 * Construct a new bundle of ciphertext and IV.
		 * 
		 * @param c
		 *            The ciphertext
		 * @param i
		 *            The IV
		 * @param h
		 *            The mac
		 */
		public CipherTextIvMac(byte[] c, byte[] i, byte[] h) {
			cipherText = new byte[c.length];
			System.arraycopy(c, 0, cipherText, 0, c.length);
			iv = new byte[i.length];
			System.arraycopy(i, 0, iv, 0, i.length);
			mac = new byte[h.length];
			System.arraycopy(h, 0, mac, 0, h.length);
		}

		/**
		 * Constructs a new bundle of ciphertext and IV from a string of the format
		 * <code>base64(iv):base64(ciphertext)</code>.
		 *
		 * @param base64IvAndCiphertext
		 *            A string of the format <code>iv:ciphertext</code> The IV and
		 *            ciphertext must each be base64-encoded.
		 */
		public CipherTextIvMac(String base64IvAndCiphertext) {
			String[] civArray = base64IvAndCiphertext.split(":");
			if (civArray.length != 3) {
				throw new IllegalArgumentException("Cannot parse iv:ciphertext:mac");
			} else {
				iv = Base64.getDecoder().decode(civArray[0]);
				mac = Base64.getDecoder().decode(civArray[1]);
				cipherText = Base64.getDecoder().decode(civArray[2]);
			}
		}

		/**
		 * Concatinate the IV to the cipherText using array copy. This is used e.g.
		 * before computing mac.
		 * 
		 * @param iv
		 *            The IV to prepend
		 * @param cipherText
		 *            the cipherText to append
		 * @return iv:cipherText, a new byte array.
		 */
		public static byte[] ivCipherConcat(byte[] iv, byte[] cipherText) {
			byte[] combined = new byte[iv.length + cipherText.length];
			System.arraycopy(iv, 0, combined, 0, iv.length);
			System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);
			return combined;
		}

		/**
		 * Encodes this ciphertext, IV, mac as a string.
		 *
		 * @return base64(iv) : base64(mac) : base64(ciphertext). The iv and mac go
		 *         first because they're fixed length.
		 */
		@Override
		public String toString() {
			String ivString = Base64.getEncoder().encodeToString(iv);
			String cipherTextString = Base64.getEncoder().encodeToString(cipherText);
			String macString = Base64.getEncoder().encodeToString(mac);
			return String.format(ivString + ":" + macString + ":" + cipherTextString);
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(cipherText);
			result = prime * result + Arrays.hashCode(iv);
			result = prime * result + Arrays.hashCode(mac);
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CipherTextIvMac other = (CipherTextIvMac) obj;
			if (!Arrays.equals(cipherText, other.cipherText))
				return false;
			if (!Arrays.equals(iv, other.iv))
				return false;
			if (!Arrays.equals(mac, other.mac))
				return false;
			return true;
		}
	}

	/**
	 * Copy the elements from the start to the end
	 *
	 * @param from
	 *            the source
	 * @param start
	 *            the start index to copy
	 * @param end
	 *            the end index to finish
	 * @return the new buffer
	 */
	private static byte[] copyOfRange(byte[] from, int start, int end) {
		int length = end - start;
		byte[] result = new byte[length];
		System.arraycopy(from, start, result, 0, length);
		return result;
	}

}
