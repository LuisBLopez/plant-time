package com.example.planttime.ui.security

import android.content.Context
import android.preference.PreferenceManager
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class AES {

    //Code based on: https://gist.github.com/reuniware/1015e933ecd75224a8dcd54d6822960e
    fun encrypt(context: Context, strToEncrypt: String): ByteArray {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        val plainText = strToEncrypt.toByteArray(Charsets.UTF_8)
        if(!sharedPref.contains("secret_key") && !sharedPref.contains("initialization_vector")){
            val keygen = KeyGenerator.getInstance("AES")
            keygen.init(256)
            val key = keygen.generateKey()
            saveSecretKey(context, key)
            cipher.init(Cipher.ENCRYPT_MODE, key)
            val cipherText = cipher.doFinal(plainText)
            saveInitializationVector(context, cipher.iv)
            val sb = StringBuilder()
            for (b in cipherText) {
                sb.append(b.toChar())
            }
            return cipherText
        }
        val key: SecretKey = getSavedSecretKey(context)
        val ivSpec = IvParameterSpec(getSavedInitializationVector(context))
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
        return cipher.doFinal(plainText)
    }

    fun decrypt(context:Context, dataToDecrypt: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        val ivSpec = IvParameterSpec(getSavedInitializationVector(context))
        cipher.init(Cipher.DECRYPT_MODE, getSavedSecretKey(context), ivSpec)
        return cipher.doFinal(dataToDecrypt)
    }

    private fun saveSecretKey(context:Context, secretKey: SecretKey) {
        val baos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(baos)
        oos.writeObject(secretKey)
        val strToSave = String(android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT))
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.putString("secret_key", strToSave)
        editor.apply()
    }

    private fun getSavedSecretKey(context: Context): SecretKey {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val strSecretKey = sharedPref.getString("secret_key", "")
        val bytes = android.util.Base64.decode(strSecretKey, android.util.Base64.DEFAULT)
        val ois = ObjectInputStream(ByteArrayInputStream(bytes))
        return ois.readObject() as SecretKey
    }

    private fun saveInitializationVector(context: Context, initializationVector: ByteArray) {
        val baos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(baos)
        oos.writeObject(initializationVector)
        val strToSave = String(android.util.Base64.encode(baos.toByteArray(), android.util.Base64.DEFAULT))
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.putString("initialization_vector", strToSave)
        editor.apply()
    }

    private fun getSavedInitializationVector(context: Context) : ByteArray {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val strInitializationVector = sharedPref.getString("initialization_vector", "")
        val bytes = android.util.Base64.decode(strInitializationVector, android.util.Base64.DEFAULT)
        val ois = ObjectInputStream(ByteArrayInputStream(bytes))
        return ois.readObject() as ByteArray
    }

}