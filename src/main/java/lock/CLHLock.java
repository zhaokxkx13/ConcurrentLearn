package lock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhaoguochen
 * Date: 2018-07-11
 * Time: 15:37
 */
public class CLHLock {

    public static class CLHNode {
        private boolean isLocked = true;
    }

    private volatile CLHNode tail;
    private static final AtomicReferenceFieldUpdater<CLHLock, CLHNode> UPDATER = AtomicReferenceFieldUpdater
            .newUpdater(CLHLock.class, CLHNode.class, "tail");

    public void lock(CLHNode currentThreadCLHNode) {
        CLHNode preNode = UPDATER.getAndSet(this, currentThreadCLHNode);

        if (preNode != null) {
            while (preNode.isLocked) ;
        }
    }

    public void unlock(CLHNode currentThreadCLHNode) {
        // 如果队列里只有当前线程，则释放对当前线程的引用（for GC）
        if (!UPDATER.compareAndSet(this, currentThreadCLHNode, null)) {
            // 还有后续线程
            currentThreadCLHNode.isLocked = false;// 改变状态，让后续线程结束自旋
        }
    }
}
