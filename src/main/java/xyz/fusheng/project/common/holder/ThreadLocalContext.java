package xyz.fusheng.project.common.holder;

import lombok.Data;

/**
 * @FileName: ThreadContextHolder
 * @Author: code-fusheng
 * @Date: 2022/3/31 22:16
 * @Version: 1.0
 * @Description:
 */

@Data
public class ThreadLocalContext {

    // 测试标记
    private String mark = "test";

    // 本地线程变量
    private static ThreadLocal<ThreadLocalContext> threadLocal = new ThreadLocal<>();

    public static ThreadLocalContext get() {
        if (threadLocal.get() == null) {
            ThreadLocalContext threadLocalContext = new ThreadLocalContext();
            threadLocal.set(threadLocalContext);
        }
        ThreadLocalContext threadLocalContext = threadLocal.get();
        return threadLocalContext;
    }

    public void removeAll() {
        this.mark = null;
        threadLocal.remove();
    }

}
