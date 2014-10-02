package com.forside.android;

import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;

import com.forside.android.xml.NavPointMap;

public class DecryptionWorker {
	private final Context context;
	private String aesDecrytekey = null;
	byte fullkey[] = null;
	byte firsthalfkey[] = new byte[16];
	byte secondhalfkey[] = new byte[16];
//	Cipher cipher = null;
	NavPointMap navMap = NavPointMap.getInstance();
	
	public DecryptionWorker(Context context) {
		this.context = context;
	}

	/**
	 * Getting the decryption key from the root.json
	 * 
	 * @return
	 */
	private byte[] getAESDecrytKey() {
		byte[] mdbytes = null;
		try {
			String strLine = "";
			InputStream rsfile = null;
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			int by = 256;
			if (navMap.getAppType().equalsIgnoreCase(NavPointMap.APP_FULL)) {
				rsfile = Uniberg.getInstance().getAssets().open("root.json");
			} else {
				// root_keycheck.json will be created by build script
				// dynamically, it will be used only for encrypted key check for
				// sample apps

				rsfile = Uniberg.getInstance().getAssets()
						.open("root_keycheck.json");
			}
			byte[] buffer = new byte[by];
			long read = 0;
			long offset = by;
			int size;
			while (read < offset) {
				size = (int) (((offset - read) >= by) ? by : (offset - read));
				rsfile.read(buffer, 0, size);
				read += size;
			}
			rsfile.close();
			strLine = new String(buffer);
			md.update(strLine.getBytes());
			mdbytes = md.digest();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return mdbytes;
	}
	
	public String DecrptyPage(String htmlFile ) {
		try {

			if (aesDecrytekey == null) {
				Init();
			}
			/*
			 * byte key[] = { 0x66, 0x66, 0x66, 0x66, 0x66, 0x66, 0x66, 0x66,
			 * 0x66, 0x66, 0x66, 0x66, 0x66, 0x66, 0x66, 0x66 };
			 */
			InputStream fin = context.getAssets().open(htmlFile);
			byte[] buffer = new byte[fin.available()];
			fin.read(buffer);
			fin.close();
			//Create cipher each time to support multi-thread decription
			Cipher cipher = CreateCipher();
			byte[] decrypted = cipher.doFinal(buffer);
			String str = new String(decrypted);
			return str;

		} catch (java.security.InvalidAlgorithmParameterException e) {
		} catch (javax.crypto.NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (java.security.InvalidKeyException e) {
			e.printStackTrace();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	private void Init() throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		fullkey = getAESDecrytKey();

		aesDecrytekey = fullkey.toString();
		int k = 16;
		for (int i = 0; i < 16; i++) {
			// firsthalfkey is the secret key byte
			// secindhalfkey is the initalizing vector
			firsthalfkey[i] = fullkey[i];
			secondhalfkey[i] = fullkey[k];
			k++;
		}
		CreateCipher();
	}

	private Cipher CreateCipher() throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException {
		SecretKeySpec skeySpec = new SecretKeySpec(firsthalfkey, "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/noPadding");

		/*
		 * byte[] tmpIV = { 0x00, (byte) 0x00, 0x00, (byte) 0x00, 0x00,
		 * (byte) 0x00, (byte) 0x00, (byte) 0x00, 0x00, 0x00, (byte)
		 * 0x00, 0x00, 0x00, (byte) 0x00, (byte) 0x00, 0x00 };
		 */

		AlgorithmParameterSpec paramSpec = new IvParameterSpec(
				secondhalfkey);

		// cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, paramSpec);
		return cipher;
	}
}
