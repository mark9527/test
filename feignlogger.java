package com.hsbc.rbwm.digital.amh.base.feign;
import java.io.IOException;import java.util.HashMap;import java.util.Map;import java.util.Optional;import java.util.UUID;
import feign.Logger;import feign.Request;import feign.Response;
public class FeignLogger extends Logger {    private static final String START_ARROW = "--->";    private static final String END_ARROW = "<---";    private static final String ERROR_END = "---> END";    private static final String JSON = "json=";    private static String newLine = System.getProperty("line.separator");    private final org.slf4j.Logger log;    private final org.slf4j.event.Level logLevel;    private final StringBuilder errorLogger = new StringBuilder();    private Map<String, StringBuilder> builders = new HashMap<String, StringBuilder>();
    public FeignLogger(final String newLine, final org.slf4j.Logger log) {        this(log);        FeignLogger.newLine = newLine;    }
    public FeignLogger(final org.slf4j.Logger log) {        this.log = log;        this.logLevel = org.slf4j.event.Level.INFO;    }
    public FeignLogger(final org.slf4j.Logger log, final org.slf4j.event.Level logLevel) {        this.log = log;        this.logLevel = logLevel;    }
    protected void log(final String configKey, final String format, final Object... args) {        // System.out.println("111111111111111111111111111111111111111111111111111111111111");        // if (this.builders != null && this.builders.size() > 0) {        // Set<String> set = this.builders.keySet();        // for (String s : set) {        // System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmm" + s);        // System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmm" +        // this.builders.get(s));        // }        // }        if (format == null || format.isEmpty()) {            return;        }        Optional<StringBuilder> builderOptional = this.getLogBuilder(configKey);        if (builderOptional.isPresent()) {            this.addLog(format, (StringBuilder) builderOptional.get(), args);        } else {            this.logError(format, args);        }    }
    protected void logRequest(final String configKey, final Logger.Level logLevel, final Request request) {        String config = this.setUpConfigKey(configKey);        super.logRequest(config, logLevel, request);        this.writeLog(config);    }
    protected Response logAndRebufferResponse(final String configKey, final Logger.Level logLevel, final Response response,        final long elapsedTime) throws IOException {        String config = this.setUpConfigKey(configKey);        Response logResponse = super.logAndRebufferResponse(config, logLevel, response, elapsedTime);        this.writeLog(config);        return logResponse;    }
    private String setUpConfigKey(final String configKey) {        String uuid = UUID.randomUUID().toString();        String config = configKey + "--" + uuid;        this.builders.put(config, new StringBuilder());        return config;    }
    private void writeLog(final String configKey) {        Optional<StringBuilder> messageBuilderOptional = this.getLogBuilder(configKey);        if (messageBuilderOptional.isPresent()) {            StringBuilder messageBuilder = (StringBuilder) messageBuilderOptional.get();            this.writeLog(messageBuilder);            this.builders.remove(configKey);        }    }
    private void writeLog(final StringBuilder builder) {        String message = builder.toString();        switch (this.logLevel) {        case TRACE: {            this.log.trace(message);            break;        }        case DEBUG: {            this.log.debug(message);            break;        }        case WARN: {            this.log.warn(message);            break;        }        case ERROR: {            this.log.error(message);            break;        }        default: {            this.log.info(message);        }        }    }
    private Optional<StringBuilder> getLogBuilder(final String configKey) {        return Optional.ofNullable((StringBuilder) this.builders.get(configKey));    }
    private String formatHeader(final String original) {        String[] originalParts = original.split(":", 2);        if (originalParts.length == 2) {            return String.format("%s=\"%s\"", originalParts[0], originalParts[1].trim());        }        return original;    }
    private boolean isStartOrEnd(final String message) {        String arrow = message.substring(0, 4);        return FeignLogger.START_ARROW.equals(arrow) || FeignLogger.END_ARROW.equals(arrow);    }
    private void logError(final String format, final Object... args) {        String message = this.addLog(format, this.errorLogger, args);        if (message.contains(FeignLogger.ERROR_END)) {            this.writeLog(this.errorLogger);            this.errorLogger.setLength(0);        }    }
    private String addLog(final String format, final StringBuilder builder, final Object... args) {        String formattedMessage = String.format(format, args);        String message = formattedMessage.replace(FeignLogger.newLine, "").trim();        if ("{".equals(message.substring(0, 1))) {            builder.append(FeignLogger.JSON).append(message);        } else if (this.isStartOrEnd(message)) {            builder.append(message);        } else {            builder.append(this.formatHeader(message));        }        builder.append(" ");        return message;    }
}
