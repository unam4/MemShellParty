package com.reajason.javaweb.memshell;

import com.reajason.javaweb.memshell.jetty.behinder.BehinderListener;
import com.reajason.javaweb.memshell.jetty.command.CommandListener;
import com.reajason.javaweb.memshell.jetty.godzilla.GodzillaListener;
import com.reajason.javaweb.memshell.jetty.injector.JettyFilterInjector;
import com.reajason.javaweb.memshell.jetty.injector.JettyListenerInjector;
import com.reajason.javaweb.memshell.jetty.injector.JettyServletInjector;
import com.reajason.javaweb.memshell.shelltool.behinder.BehinderFilter;
import com.reajason.javaweb.memshell.shelltool.behinder.BehinderServlet;
import com.reajason.javaweb.memshell.shelltool.command.CommandFilter;
import com.reajason.javaweb.memshell.shelltool.command.CommandServlet;
import com.reajason.javaweb.memshell.shelltool.godzilla.GodzillaFilter;
import com.reajason.javaweb.memshell.shelltool.godzilla.GodzillaServlet;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

import static com.reajason.javaweb.memshell.config.Constants.*;

/**
 * @author ReaJason
 * @since 2024/12/7
 */
public class JettyShell extends AbstractShell {

    @Override
    protected Map<String, Pair<Class<?>, Class<?>>> getCommandShellMap() {
        return Map.of(
                SERVLET, Pair.of(CommandServlet.class, JettyServletInjector.class),
                JAKARTA_SERVLET, Pair.of(CommandServlet.class, JettyServletInjector.class),
                FILTER, Pair.of(CommandFilter.class, JettyFilterInjector.class),
                JAKARTA_FILTER, Pair.of(CommandFilter.class, JettyFilterInjector.class),
                LISTENER, Pair.of(CommandListener.class, JettyListenerInjector.class),
                JAKARTA_LISTENER, Pair.of(CommandListener.class, JettyListenerInjector.class)
        );
    }

    @Override
    protected Map<String, Pair<Class<?>, Class<?>>> getGodzillaShellMap() {
        return Map.of(
                SERVLET, Pair.of(GodzillaServlet.class, JettyServletInjector.class),
                JAKARTA_SERVLET, Pair.of(GodzillaServlet.class, JettyServletInjector.class),
                FILTER, Pair.of(GodzillaFilter.class, JettyFilterInjector.class),
                JAKARTA_FILTER, Pair.of(GodzillaFilter.class, JettyFilterInjector.class),
                LISTENER, Pair.of(GodzillaListener.class, JettyListenerInjector.class),
                JAKARTA_LISTENER, Pair.of(GodzillaListener.class, JettyListenerInjector.class)
        );
    }

    @Override
    protected Map<String, Pair<Class<?>, Class<?>>> getBehinderShellMap() {
        return Map.of(
                SERVLET, Pair.of(BehinderServlet.class, JettyServletInjector.class),
                JAKARTA_SERVLET, Pair.of(BehinderServlet.class, JettyServletInjector.class),
                FILTER, Pair.of(BehinderFilter.class, JettyFilterInjector.class),
                JAKARTA_FILTER, Pair.of(BehinderFilter.class, JettyFilterInjector.class),
                LISTENER, Pair.of(BehinderListener.class, JettyListenerInjector.class),
                JAKARTA_LISTENER, Pair.of(BehinderListener.class, JettyListenerInjector.class)
        );
    }
}
