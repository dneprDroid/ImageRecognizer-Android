package neural.imagerecognizer.app.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.*;

public class ThreadManager {

    private static ThreadManager instance;
    private ExecutorService pool;

    private ThreadManager() {
        pool = Executors.newCachedThreadPool();
    }

    public static synchronized ThreadManager getInstance() {
        if (instance == null) instance = new ThreadManager();
        return instance;
    }

    public void execute(Runnable r) {
        pool.execute(r);
    }

    public <T> void execute(final Executor<T> executor) {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final T data = executor.onExecute();
                    if (data != null)
                        runOnMainThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    executor.onCallback(data);
                                } catch (Exception e) {
                                    Tool.log("main thread error");
                                }
                            }
                        });
                } catch (final Exception e) {
                    Runnable onError = new Runnable() {
                        @Override
                        public void run() {
                            e.printStackTrace();
                            executor.onError(e);
                        }
                    };
                    runOnMainThread(onError);
                }
            }
        });
    }

    private void runOnMainThread(Runnable r) {
        new Handler(Looper.getMainLooper()).post(r);
    }

    public void end() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(5, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        instance = null;
    }

    public interface Executor<T> {
        @Nullable
        T onExecute() throws Exception;

        @MainThread
        void onCallback(@NonNull T data);

        @MainThread
        void onError(Exception e);
    }
}