package com.example.cupofjoe.comms.context;

public class ContextHolder implements Runnable{
    private static final ThreadLocal<Context> threadLocal = new ThreadLocal<>();
    private final Context context;

    public ContextHolder(Context context) {
        this.context = context;
    }

    public static void set(Context context) {
        threadLocal.set(context);
    }

    public static void unset() {
        threadLocal.remove();
    }

    public static Context get() {
        return threadLocal.get();
    }

    @Override
    public void run() {
        set(context);
    }
}
