package com.ityu.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class BeanUtils {
    public BeanUtils() {
    }

    private static Method getMethod(Object bean, String propertyName) {
        StringBuilder sb = (new StringBuilder("get")).append(Character.toUpperCase(propertyName.charAt(0)));
        if (propertyName.length() > 1) {
            sb.append(propertyName.substring(1));
        }

        String getterName = sb.toString();
        Method[] arr$ = bean.getClass().getMethods();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Method m = arr$[i$];
            if (getterName.equals(m.getName()) && m.getParameterTypes().length == 0) {
                return m;
            }
        }

        return null;
    }

    private static Field getField(Object bean, String propertyName) {
        Field[] arr$ = bean.getClass().getDeclaredFields();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Field f = arr$[i$];
            if (propertyName.equals(f.getName())) {
                return f;
            }
        }

        return null;
    }

    private static void validateArgs(Object bean, String propertyName) {
        if (bean == null) {
            throw new IllegalArgumentException("bean is null");
        } else if (propertyName == null) {
            throw new IllegalArgumentException("propertyName is null");
        } else if (propertyName.trim().length() == 0) {
            throw new IllegalArgumentException("propertyName is empty");
        }
    }

    public static Object getBeanProperty(Object bean, String propertyName) {
        validateArgs(bean, propertyName);
        Method getter = getMethod(bean, propertyName);
        if (getter != null) {
            try {
                return getter.invoke(bean);
            } catch (Exception var6) {
            }
        }

        Field field = getField(bean, propertyName);
        if (field != null) {
            try {
                field.setAccessible(true);
                return field.get(bean);
            } catch (Exception var5) {
            }
        }

        return null;
    }

    public static long getLongBeanProperty(Object bean, String propertyName) throws NoSuchFieldException {
        validateArgs(bean, propertyName);
        Object o = getBeanProperty(bean, propertyName);
        if (o == null) {
            throw new NoSuchFieldException(propertyName);
        } else if (!(o instanceof Number)) {
            throw new IllegalArgumentException(propertyName + " not an Number");
        } else {
            return ((Number)o).longValue();
        }
    }
}
