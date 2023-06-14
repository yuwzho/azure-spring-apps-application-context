package com.azure.springapps.reloadproperties.configuration;

import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

@Slf4j
public class FileWatcher implements InitializingBean, DisposableBean {

    private Thread watcherThread = null;
    private final String path;
    private final Consumer<List<Path>> action;

    private AtomicBoolean running = new AtomicBoolean(false);

    public FileWatcher(String path, Consumer<List<Path>> action) {
        this.path = path;
        this.action = action;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        watcherThread = new Thread(() -> {
            try {
                WatchService watcher = FileSystems.getDefault().newWatchService();
                Path dir = Path.of(path);
                dir.register(watcher, ENTRY_MODIFY);
                running.set(true);
                WatchKey key;
                while (running.get() && (key = watcher.take()) != null) {
                    action.accept(key.pollEvents()
                        .stream()
                        .filter(x -> x.kind() != OVERFLOW)
                        .map(this::getChangedFile).collect(Collectors.toList()));

                    boolean valid = key.reset();
                    if (!valid) {
                        break;
                    }
                }
            } catch (IOException | InterruptedException e) {
                log.error("Error when listen event: {}", e.getMessage(), e);
            }
        });
        watcherThread.start();
    }

    private Path getChangedFile(WatchEvent<?> event) {
        WatchEvent.Kind<?> kind = event.kind();
        @SuppressWarnings("unchecked")
        WatchEvent<Path> ev = (WatchEvent<Path>) event;
        return ev.context();
    }

    @Override
    public void destroy() throws Exception {
        running.set(false);
        watcherThread.interrupt();
    }

}
