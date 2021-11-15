package org.anystub;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.*;
import java.util.Random;
import java.util.logging.Logger;

public class RandomGenerator {

    private static final Logger log = Logger.getLogger(RandomGenerator.class.getName());
    private static final Random random = new Random();

    private RandomGenerator() {
    }

    public static <R> R g(Class<R> genClass) {


        if (genClass.isPrimitive()) {
            return gPrimitive(genClass);
        }

        R r;
        r = gBasic(genClass);
        if (r != null) {
            return r;
        }

        r = gTime(genClass);
        if (r != null) {
            return r;
        }

        if (genClass.isArray()) {
            try {
                r = ObjectMapperFactory.get().readValue("[]", genClass);
            } catch (JsonProcessingException e) {
                log.finest(() -> String.format("cannot build empty array for %s", genClass.getName()));
            }
            return r;
        }


        try {
            r = ObjectMapperFactory.get().readValue("{}", genClass);
            populate(r);
            return r;
        } catch (JacksonException ex) {
            log.finest(() -> String.format("cannot build empty object for %s", genClass.getName()));
        }

        try {
            // attempt to create an empty list
            r = ObjectMapperFactory.get().readValue("[]", genClass);
            return r;
        } catch (JacksonException ex) {
            log.finest(() -> String.format("cannot build an empty list for %s", genClass.getName()));
        }

        return null;
    }

    public static <R> R gPrimitive(Class<R> genClass) {
        if (genClass.equals(int.class)) {
            Object o = gInt();
            return (R) o;
        }
        if (genClass.equals(byte.class)) {
            Object o = (byte) gInt();
            return (R) o;
        }
        if (genClass.equals(char.class)) {
            Object o = (char) gInt();
            return (R) o;
        }
        if (genClass.equals(boolean.class)) {
            Object o = random.nextBoolean();
            return (R) o;
        }
        if (genClass.equals(double.class)) {
            Object o = gDouble();
            return (R) o;
        }
        if (genClass.equals(short.class)) {
            Object o = (short) gInt();
            return (R) o;
        }
        if (genClass.equals(long.class)) {
            Object o = (long) gInt();
            return (R) o;
        }
        if (genClass.equals(float.class)) {
            Object o = (float) gDouble();
            return (R) o;
        }

        return null;
    }

    public static <R> R gBasic(Class<R> genClass) {
        if (genClass.equals(String.class)) {
            return (R) gString();
        }
        if (genClass.equals(Integer.class)) {
            return (R) (Object) gInt();
        }
        if (genClass.equals(Double.class)) {
            return (R) (Double) gDouble();
        }
        if (genClass.equals(Long.class)) {
            return (R) (Long) (long) gInt();
        }
        if (genClass.equals(Float.class)) {
            return (R) (Float) (float) gDouble();
        }
        if (genClass.equals(Byte.class)) {
            return (R) (Byte) (byte) gInt();
        }
        if (genClass.equals(byte[].class)) {
            byte[] n = new byte[random.nextInt(10) + 10];
            random.nextBytes(n);
            return (R) n;
        }
        if (genClass.isEnum()) {
            return gEnum(genClass);
        }
        return null;
    }

    public static <R> R gTime(Class<R> genClass) {
        if (genClass.equals(LocalDateTime.class)) {
            return (R) gDateTime();
        }
        if (genClass.equals(LocalDate.class)) {
            return (R) gDate();
        }

        if (genClass.equals(OffsetDateTime.class)) {
            return (R) gOffsetDateTime();
        }

        if (genClass.equals(OffsetTime.class)) {
            return (R) gOffsetTime();
        }
        if (genClass.equals(ZonedDateTime.class)) {
            return (R) gZonedDateTime();
        }
        return null;
    }

    public static <R> void populate(R a) {
        Class<?> aClass = a.getClass();
        for (Method m : aClass.getMethods()) {
            if (!m.getName().startsWith("set") ||
                    m.getParameterTypes().length != 1) {
                continue;
            }
            Object g = g(m.getParameterTypes()[0]);
            try {
                m.invoke(a, g);
            } catch (IllegalAccessException | InvocationTargetException e) {
                log.finest(() -> String.format("cannot set generated value with %s", m.getName()));
            }
        }
    }

    public static String gString() {
        String abc = "ABCDEFG abcdef 1234567890";
        StringBuilder sb = new StringBuilder();
        int len = random.nextInt(100) + 3;
        for (int i = 0; i < len; i++) {
            int p = random.nextInt(abc.length());
            sb.append(abc.charAt(p));
        }
        return sb.toString();
    }

    public static double gDouble() {

        return random.nextDouble() * 100;
    }

    public static int gInt() {
        return random.nextInt();
    }

    public static LocalDate gDate() {
        return gDateTime().toLocalDate();
    }

    public static LocalDateTime gDateTime() {
        return gOffsetDateTime().toLocalDateTime();
    }

    public static OffsetDateTime gOffsetDateTime() {
        return OffsetDateTime.now()
                .plusDays(random.nextInt(100) - 200L)
                .plusHours(random.nextInt(3))
                .plusMinutes(random.nextInt(10))
                .plusSeconds(random.nextInt(60));
    }

    public static OffsetTime gOffsetTime() {
        return gOffsetDateTime().toOffsetTime();
    }

    public static ZonedDateTime gZonedDateTime() {
        return gOffsetDateTime().toZonedDateTime();
    }

    public static <R> R gEnum(Class<R> genClass) {
        if (!genClass.isEnum()) {
            return null;
        }
        try {
            Method values;
            values = genClass.getDeclaredMethod("values");
            Object invoke = values.invoke(genClass);
            Object[] instances = (Object[]) invoke;
            return (R) instances[random.nextInt(instances.length)];
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.finest(() -> String.format("cannot get instance of enum %s", genClass.getName()));
        }
        return null;
    }

    public static <R> R g(TypeReference<R> returnType) {
        String typeName = returnType.getType().getTypeName();
        R r = null;
        r = gPrimitive(typeName);
        if (r != null) {
            return r;
        }
        r = gBasic(typeName);
        if (r != null) {
            return r;
        }
        r = gTime(typeName);

        try {
            r = ObjectMapperFactory.get().readValue("{}", returnType);
            populate(r);
            return r;
        } catch (JacksonException ex) {
            log.finest(() -> String.format("cannot build empty object for %s", typeName));
        }

        try {
            // attempt to create an empty list
            r = ObjectMapperFactory.get().readValue("[]", returnType);
            return r;
        } catch (JacksonException ex) {
            log.finest(() -> String.format("cannot build an empty list for %s", typeName));
        }

        return r;

    }

    public static <R> R gPrimitive(String genClass) {
        if (genClass.equals("int")) {
            Object o = gInt();
            return (R) o;
        }
        if (genClass.equals("byte")) {
            Object o = (byte) gInt();
            return (R) o;
        }
        if (genClass.equals("char")) {
            Object o = (char) gInt();
            return (R) o;
        }
        if (genClass.equals("boolean")) {
            Object o = random.nextBoolean();
            return (R) o;
        }
        if (genClass.equals("double")) {
            Object o = gDouble();
            return (R) o;
        }
        if (genClass.equals("short")) {
            Object o = (short) gInt();
            return (R) o;
        }
        if (genClass.equals("long")) {
            Object o = (long) gInt();
            return (R) o;
        }
        if (genClass.equals("float")) {
            Object o = (float) gDouble();
            return (R) o;
        }

        return null;
    }

    public static <R> R gBasic(String genClass) {
        if (genClass.equals(String.class.getName())) {
            return (R) gString();
        }
        if (genClass.equals(Integer.class.getName())) {
            return (R) (Object) gInt();
        }
        if (genClass.equals(Double.class.getName())) {
            return (R) (Double) gDouble();
        }
        if (genClass.equals(Long.class.getName())) {
            return (R) (Long) (long) gInt();
        }
        if (genClass.equals(Float.class.getName())) {
            return (R) (Float) (float) gDouble();
        }
        if (genClass.equals(Byte.class.getName())) {
            return (R) (Byte) (byte) gInt();
        }
        if (genClass.equals(byte[].class.getName())) {
            byte[] n = new byte[random.nextInt(10) + 10];
            random.nextBytes(n);
            return (R) n;
        }
//        if (genClass.isEnum()) {
//            return gEnum(genClass);
//        }
        return null;
    }

    public static <R> R gTime(String genClass) {
        if (genClass.equals(LocalDateTime.class.getName())) {
            return (R) gDateTime();
        }
        if (genClass.equals(LocalDate.class.getName())) {
            return (R) gDate();
        }

        if (genClass.equals(OffsetDateTime.class.getName())) {
            return (R) gOffsetDateTime();
        }

        if (genClass.equals(OffsetTime.class.getName())) {
            return (R) gOffsetTime();
        }
        if (genClass.equals(ZonedDateTime.class.getName())) {
            return (R) gZonedDateTime();
        }
        return null;
    }

}
