package com.reajason.javaweb.memsell;

import com.reajason.javaweb.config.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author ReaJason
 * @since 2024/12/7
 */
public abstract class AbstractShell {

    /**
     * 获取当前支持的内存马功能列表
     *
     * @return supported tool lists
     */
    public abstract List<ShellTool> getSupportedShellTools();


    /**
     * 获取内存马功能所支持的注入类型列表
     *
     * @param tool 内存马功能
     * @return shellTypes
     */
    public List<String> getSupportedShellTypes(ShellTool tool) {
        return switch (tool) {
            case Godzilla -> getGodzillaShellMap().keySet().stream().toList();
            case Command -> getCommandShellMap().keySet().stream().toList();
            default -> Collections.emptyList();
        };
    }

    /**
     * 检查 shellConfig 的配置是否有效
     *
     * @param shellConfig 内存马生成配置
     * @return valid
     */
    public boolean isValid(ShellConfig shellConfig) {
        List<ShellTool> supportedShellTools = getSupportedShellTools();
        if (!supportedShellTools.contains(shellConfig.getShellTool())) {
            return false;
        }

        List<String> supportedShellTypes = getSupportedShellTypes(shellConfig.getShellTool());
        return supportedShellTypes.contains(shellConfig.getShellType());
    }

    public GenerateResult generate(ShellConfig shellConfig, InjectorConfig injectorConfig, ShellToolConfig shellToolConfig) {
        Pair<Class<?>, Class<?>> shellInjectorPair = getShellInjectorPair(shellConfig.getShellTool(), shellConfig.getShellType());
        Class<?> shellClass = shellInjectorPair.getLeft();
        Class<?> injectorClass = shellInjectorPair.getRight();

        shellToolConfig.setClazz(shellClass);

        byte[] shellBytes = generateShellBytes(shellConfig, shellToolConfig);

        injectorConfig = injectorConfig
                .toBuilder()
                .injectorClass(injectorClass)
                .shellClassName(shellToolConfig.getClassName())
                .shellClassBytes(shellBytes).build();

        byte[] injectorBytes = InjectorGenerator.generate(shellConfig, injectorConfig);

        return GenerateResult.builder()
                .shellConfig(shellConfig)
                .shellToolConfig(shellToolConfig)
                .injectorConfig(injectorConfig)
                .shellClassName(shellToolConfig.getClassName())
                .shellBytes(shellBytes)
                .injectorClassName(injectorConfig.getInjectorClassName())
                .injectorBytes(injectorBytes)
                .build();
    }

    /**
     * 获取 Godzilla 注入生成类 Map
     *
     * @return shellType -> shellClass,injectorClass
     */
    protected Map<String, Pair<Class<?>, Class<?>>> getGodzillaShellMap() {
        return Collections.emptyMap();
    }

    /**
     * 获取 Command 注入生成类 Map
     *
     * @return shellType -> shellClass,injectorClass
     */
    protected Map<String, Pair<Class<?>, Class<?>>> getCommandShellMap() {
        return Collections.emptyMap();
    }

    private Pair<Class<?>, Class<?>> getShellInjectorPair(ShellTool shellTool, String shellType) {
        Map<String, Pair<Class<?>, Class<?>>> shellMap = switch (shellTool) {
            case Godzilla -> getGodzillaShellMap();
            case Command -> getCommandShellMap();
            default -> Collections.emptyMap();
        };
        return shellMap.get(shellType);
    }

    private byte[] generateShellBytes(ShellConfig shellConfig, ShellToolConfig shellToolConfig) {
        return switch (shellConfig.getShellTool()) {
            case Godzilla -> GodzillaGenerator.generate(shellConfig, (GodzillaConfig) shellToolConfig);
            case Command -> CommandGenerator.generate(shellConfig, (CommandConfig) shellToolConfig);
            default -> throw new UnsupportedOperationException("Unknown shell tool: " + shellConfig.getShellTool());
        };
    }
}