package com.lowdragmc.lowdraglib.utils;

import com.lowdragmc.lowdraglib.LDLib;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.annotation.Nullable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/EnumHelper.class */
public class EnumHelper {
    private static Object reflectionFactory;
    private static Method newConstructorAccessor;
    private static Method newInstance;
    private static Method newFieldAccessor;
    private static Method fieldAccessorSet;

    static {
        reflectionFactory = null;
        newConstructorAccessor = null;
        newInstance = null;
        newFieldAccessor = null;
        fieldAccessorSet = null;
        try {
            Method getReflectionFactory = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("getReflectionFactory", new Class[0]);
            reflectionFactory = getReflectionFactory.invoke(null, new Object[0]);
            newConstructorAccessor = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("newConstructorAccessor", Constructor.class);
            newInstance = Class.forName("sun.reflect.ConstructorAccessor").getDeclaredMethod("newInstance", Object[].class);
            newFieldAccessor = Class.forName("sun.reflect.ReflectionFactory").getDeclaredMethod("newFieldAccessor", Field.class, Boolean.TYPE);
            fieldAccessorSet = Class.forName("sun.reflect.FieldAccessor").getDeclaredMethod("set", Object.class, Object.class);
        } catch (Exception e) {
            LDLib.LOGGER.error("Error setting up EnumHelper.", e);
        }
    }

    @Nullable
    public static <T extends Enum<?>> T addEnum(Class<T> enumType, String enumName, Class<?>[] paramTypes, Object... paramValues) {
        Field valuesField = null;
        Field[] fields = enumType.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            if (name.equals("$VALUES") || name.equals("ENUM$VALUES")) {
                valuesField = field;
                break;
            }
        }
        if (valuesField == null) {
            String valueType = String.format("[L%s;", enumType.getName().replace('.', '/'));
            int length = fields.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Field field2 = fields[i];
                if ((field2.getModifiers() & 4121) != 4121 || !field2.getType().getName().replace('.', '/').equals(valueType)) {
                    i++;
                } else {
                    valuesField = field2;
                    break;
                }
            }
        }
        if (valuesField == null) {
            return null;
        }
        valuesField.setAccessible(true);
        try {
            Enum[] enumArr = (Enum[]) valuesField.get(enumType);
            T newValue = (T) makeEnum(enumType, enumName, enumArr.length, paramTypes, paramValues);
            setFailsafeFieldValue(valuesField, null, org.apache.commons.lang3.ArrayUtils.add(enumArr, newValue));
            cleanEnumCache(enumType);
            return newValue;
        } catch (Exception e) {
            LDLib.LOGGER.error("Error adding enum with EnumHelper.", e);
            throw new RuntimeException(e);
        }
    }

    private static <T extends Enum<?>> T makeEnum(Class<T> enumClass, @Nullable String value, int ordinal, Class<?>[] additionalTypes, @Nullable Object[] additionalValues) throws Exception {
        int additionalParamsCount = additionalValues == null ? 0 : additionalValues.length;
        Object[] params = new Object[additionalParamsCount + 2];
        params[0] = value;
        params[1] = Integer.valueOf(ordinal);
        if (additionalValues != null) {
            System.arraycopy(additionalValues, 0, params, 2, additionalValues.length);
        }
        return enumClass.cast(newInstance.invoke(getConstructorAccessor(enumClass, additionalTypes), params));
    }

    private static Object getConstructorAccessor(Class<?> enumClass, Class<?>[] additionalParameterTypes) throws Exception {
        Class<?>[] parameterTypes = new Class[additionalParameterTypes.length + 2];
        parameterTypes[0] = String.class;
        parameterTypes[1] = Integer.TYPE;
        System.arraycopy(additionalParameterTypes, 0, parameterTypes, 2, additionalParameterTypes.length);
        return newConstructorAccessor.invoke(reflectionFactory, enumClass.getDeclaredConstructor(parameterTypes));
    }

    public static void setFailsafeFieldValue(Field field, @Nullable Object target, @Nullable Object value) throws Exception {
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & (-17));
        Object fieldAccessor = newFieldAccessor.invoke(reflectionFactory, field, false);
        fieldAccessorSet.invoke(fieldAccessor, target, value);
    }

    private static void cleanEnumCache(Class<?> enumClass) throws Exception {
        blankField(enumClass, "enumConstantDirectory");
        blankField(enumClass, "enumConstants");
        blankField(enumClass, "enumVars");
    }

    private static void blankField(Class<?> enumClass, String fieldName) throws Exception {
        Field[] declaredFields;
        for (Field field : Class.class.getDeclaredFields()) {
            if (field.getName().contains(fieldName)) {
                field.setAccessible(true);
                setFailsafeFieldValue(field, enumClass, null);
                return;
            }
        }
    }
}
