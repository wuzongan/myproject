package com.kunlun.poker.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.domain.Simplifiable;

public class DataUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(DataUtil.class);

	public static float getWithPrecision(float number, int precision) {
		int multi = (int) Math.pow(10, precision);
		return ((float) Math.round(number * multi)) / multi;
	}

	public static int readShort(byte[] bytes, int from) {
		byte ch1 = bytes[from];
		byte ch2 = bytes[from + 1];
	    return (ch1 << 8) | (ch2 & 0x00ff);
	}

	public static int readShort(byte[] bytes) {
		return readShort(bytes, 0);
	}

	public static void writeShort(int value, byte[] bytes, int from) {
		bytes[from] = (byte) (value >> 8);
		bytes[from + 1] = (byte) (value & 0x00ff);
	}

	public static byte[] writeShort(int value) {
		byte[] bytes = new byte[2];
		writeShort(value, bytes, 0);

		return bytes;
	}

	@SuppressWarnings("unchecked")
	public static <T> void simplifyAll(Simplifiable<?>[] simplifiables,
			T[] simplifieds) {
		int length = Math.min(simplifiables.length, simplifieds.length);
		for (int i = 0; i < length; i++) {
			Simplifiable<?> simplifiable = simplifiables[i];
			simplifieds[i] = simplifiable == null ? null : (T) simplifiables[i]
					.simplify();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> void simplifyAll(
			Collection<? extends Simplifiable<?>> simplifiables, T[] simplifieds) {
		int i = 0;
		for (Simplifiable<?> simplifiable : simplifiables) {
			simplifieds[i] = simplifiable == null ? null : (T) simplifiable
					.simplify();
			i++;
		}
	}

	public static void main(String[] args) {
		//System.out.println(readShort(new byte[]{0, -55}));
	}

	public static Map<?, ?> createMap(Object[] keyValues) {
		Map<Object, Object> map = new HashMap<>();
		for (int i = 0; i < keyValues.length;) {
			map.put(keyValues[i++], keyValues[i++]);
		}

		return map;
	}

	/**
	 * 一般消息发送布尔类型转换时使用
	 * 
	 * @param state
	 * @return
	 */
	public static int booleanToInt(boolean state) {
		return state ? 1 : 0;
	}

	/**
	 * 一般消息发送布尔类型转换时使用
	 * 
	 * @param state
	 * @return
	 */
	public static boolean intToBoolean(int state) {
		return state == 1;
	}

	public static byte[] object2ByteArray(Object obj) {
		if (obj == null) {
			return null;
		}
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(bos)) {
			oos.writeObject(obj);
			return bos.toByteArray();
		} catch (IOException ex) {
			logger.error(ex.getMessage(), ex);
		}
		return null;
	}

	public static int getInt(Number value, int defaultValue)
	{
		return value == null ? defaultValue : value.intValue();
	}
	
	public static Object byteArray2Object(byte[] buffer) {
		if ((buffer == null) || (buffer.length == 0)) {
			return null;
		}
		try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
				ObjectInputStream ois = new ObjectInputStream(bais)) {
			return ois.readObject();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return null;
		}
	}
	
	/**
	 * 
	 * @param id 获取一级目录
	 * @return
	 */
	public static String firstFolderAddress(int id) {
		//id % 16;
		return Integer.toString(id & 0x000000f);
	}
	
	/**
	 * @param id 获取二级目录名称
	 * @return
	 */
	public static String secondFolderAddress(int id){
		// (id / 16) % 16
		return Integer.toString((id & 0x000000f0) >> 4);
	}
	
	/**
	 * 生成目录
	 * @param firstFold
	 * @param secondFold
	 * @return
	 */
	public static String setUploadPath(String prefix, String firstFold, String secondFold){
		return prefix + firstFold + File.separator + secondFold;
	}
}
