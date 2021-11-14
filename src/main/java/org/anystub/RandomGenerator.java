package org.anystub;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.*;
import java.util.Random;
import java.util.logging.Logger;

public class RandomGenerator {

    private static final Logger log = Logger.getLogger(Base.class.getName());
    private static final Random random = new Random();

    private static final String[] javaTypes = {"int", "double"};

    public static <R> R g(Class<R> genClass) {
        if (genClass.equals(int.class)) {
            Object o = gInt();
            return (R) o;
        }
        if (genClass.equals(byte.class)) {
            Object o = (byte) gInt();
            return (R) o;
        }
        if (genClass.equals(double.class)) {
            Object o = gDouble();
            return (R) o;
        }
        if (genClass.equals(String.class)) {
            return (R) gString();
        }
        if (genClass.equals(Integer.class)) {
            return (R) (Object) gInt();
        }
        if (genClass.equals(Double.class)) {
            return (R) (Double) gDouble();
        }

        if(genClass.isPrimitive()) {
            return null;
        }

        if (genClass.isEnum()) {
           return gEnum(genClass);
        }

        if (genClass.isArray()) {
            R r;
            try {
                r = ObjectMapperFactory.get().readValue("[]", genClass);
            } catch (JsonProcessingException e) {
                r = null;
            }
            return r;
        }
        if (genClass.equals(LocalDateTime.class)) {
            return (R) gDateTime();
        }
        if (genClass.equals(LocalDate.class)) {
            return (R) gDate();
        }

        if (genClass.equals(OffsetDateTime.class)) {
            return (R) gOffsetDateTime();
        }

        if(genClass.equals(OffsetTime.class)) {
            return (R) gOffsetTime();
        }
        if(genClass.equals(ZonedDateTime.class)) {
            return (R) gZonedDateTime();
        }

        try {
            R r = ObjectMapperFactory.get().readValue("{}", genClass);
            populate(r);
            return r;
        } catch (JacksonException ex) {
            log.finest(() -> String.format("cannot build empty object for %s", genClass.getName()));
        }

        try {
            R r = ObjectMapperFactory.get().readValue("[]", genClass);
            populate(r);
            return r;
        } catch (JacksonException ex) {
            log.finest(() -> String.format("cannot build empty object for %s", genClass.getName()));
        }

        return null;
    }

    public static <R> void populate(R a) {
        Class<?> aClass = a.getClass();
        for (Method m : aClass.getMethods()) {
            if (!m.getName().startsWith("set")) {
                continue;
            }
            if (m.getParameterTypes().length != 1) {
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

    public static void s() {
        RandomGenerator.g(Integer.class);
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
                .plusDays(random.nextInt(100) - 200)
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
            log.finest(()->String.format("cannot get instance of enum %s", genClass.getName()));
        }
        return null;
    }

}
