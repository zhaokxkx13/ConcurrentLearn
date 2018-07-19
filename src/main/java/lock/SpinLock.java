package lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhaoguochen
 * Date: 2018-07-11
 * Time: 14:45
 *
 * 1.CAS操作需要硬件的配合；
 * 2.保证各个CPU的缓存（L1、L2、L3、跨CPU Socket、主存）的数据一致性，通讯开销很大，在多处理器系统上更严重；
 * 3.没法保证公平性，不保证等待进程/线程按照FIFO顺序获得锁。
 */


public class SpinLock {
    //当前拥有锁的线程
    AtomicReference<Thread> owner = new AtomicReference<>();

    public void lock() {
        Thread thread = Thread.currentThread();

        //自旋等待其它线程释放锁 ()
        while (owner.compareAndSet(null, thread)) ;

    }

    public void unlock() {

        Thread thread = Thread.currentThread();

        //线程拥有者释放线程
        owner.compareAndSet(thread, null);
    }

}
