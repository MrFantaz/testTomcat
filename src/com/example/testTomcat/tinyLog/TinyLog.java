package com.example.testTomcat.tinyLog;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.FileWriter;

public class TinyLog {
    static {
        Configurator.defaultConfig()
                .writer(new ConsoleWriter())
                .addWriter(new FileWriter("foo.bar"))
                .level(Level.TRACE)
                .activate();
    }

    public static void trace(String trace) {
        Logger.trace(trace);
    }

    public static void warn(String warn) {
        Logger.warn(warn);
    }

}
