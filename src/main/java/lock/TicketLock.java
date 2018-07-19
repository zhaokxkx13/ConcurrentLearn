package lock;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: zhaoguochen
 * Date: 2018-07-11
 * Time: 14:54
 */

/**
 * Ticket Lock 虽然解决了公平性的问题，但是多处理器系统上，每个进程/线程占用的处理器都在读写同一个变量serviceNum ，
 * 每次读写操作都必须在多个处理器缓存之间进行缓存同步，这会导致繁重的系统总线和内存的流量，大大降低系统整体的性能。
 */
public class TicketLock {
    private AtomicInteger serviceNum = new AtomicInteger();
    private AtomicInteger ticketNum = new AtomicInteger();

    /**
     * 自旋之后返回获得的票
     *
     * @return
     */
    public int lock() {
        int myTicket = ticketNum.incrementAndGet();

        while (myTicket != serviceNum.get()) ;

        return myTicket;
    }

    public void unlock(int myTicket) {
        //下一个服务号
        int next = myTicket + 1;

        //如果当前服务号符合期望，则替换为下一个服务号
        serviceNum.compareAndSet(myTicket, next);
    }
}
