package com.ifarm.util;

import java.lang.reflect.Field;

public class ReflectTraverse {
	public static void traverseObject(Object object) {
		Field[] fields = object.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			try {
				fields[i].setAccessible(true);
				System.out.println(fields[i].getName() + ":" + fields[i].get(object));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
