package com.reajason.javaweb.memsell.tomcat.godzilla;

import com.reajason.javaweb.config.ShellConfig;
import com.reajason.javaweb.config.GodzillaConfig;
import com.reajason.javaweb.memsell.GodzillaGenerator;
import com.reajason.javaweb.util.ClassUtils;
import com.reajason.javaweb.util.CommonUtil;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

/**
 * @author ReaJason
 * @since 2024/11/24
 */
class GodzillaTest {
    ShellConfig config = new ShellConfig();

    GodzillaConfig.GodzillaConfigBuilder<?, ?> shellConfigBuilder = GodzillaConfig.builder()
            .pass(CommonUtil.getRandomString(6))
            .key(CommonUtil.getRandomString(6))
            .headerName("User-Agent")
            .headerValue(CommonUtil.getRandomString(5));

    static Stream<Arguments> casesProvider() {
        return Stream.of(
                arguments(GodzillaFilter.class, "org.apache.utils.GodzillaFilter"),
                arguments(GodzillaListener.class, "org.apache.utils.GodzillaListener"),
                arguments(GodzillaValve.class, "org.apache.utils.GodzillaValve")

        );
    }

    @ParameterizedTest
    @MethodSource("casesProvider")
    void generate(Class<?> clazz, String className) {
        GodzillaConfig shellConfig = shellConfigBuilder
                .className(className)
                .clazz(clazz)
                .build();
        byte[] bytes = GodzillaGenerator.generate(config, shellConfig);
        Object obj = ClassUtils.newInstance(bytes);
        assertEquals(shellConfig.getClassName(), obj.getClass().getName());
        assertEquals(shellConfig.getPass(), ClassUtils.getFieldValue(obj, "pass"));
        assertEquals(shellConfig.getHeaderName(), ClassUtils.getFieldValue(obj, "headerName"));
        assertEquals(shellConfig.getHeaderValue(), ClassUtils.getFieldValue(obj, "headerValue"));
    }
}