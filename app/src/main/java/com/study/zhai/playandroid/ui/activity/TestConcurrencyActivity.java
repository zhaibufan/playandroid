package com.study.zhai.playandroid.ui.activity;

import android.view.View;

import com.study.zhai.playandroid.R;
import com.study.zhai.playandroid.base.BaseActivity;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试Java并发的原子性、可见性、有序性
 *
 * 结论：volatile大多情况下只能保证其可见性，atomic包下的类也只能保证原子性，只有synchronized可以保证原子性、可见性、有序性
 */
public class TestConcurrencyActivity extends BaseActivity {

    private static volatile int count = 0;
    private static AtomicInteger atomicCount = new AtomicInteger(0);
    private static int synchronizedCount = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_test_concurrency;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        ThreadLocal<String> s = new ThreadLocal<>();
    }

    public void test(View view) {
        //  分别调用，打印结果
//         volatileCount();
//          atomicCount();
        synchronizedCount();
    }

    private static void volatileCount() {
        for (int i = 0; i < 50; i++) {
            Executors.newFixedThreadPool(5).execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 虽然使用volatile关键字修饰int变量，但是对于多线程的环境下，也很难保证没问题，所以一般用来修饰标志位
                    System.out.println("volatile count: " +  Thread.currentThread().getName() + "  " + ++count);
                }
            });
        }
    }
    //    打印结果：有重复数据，且顺序错乱。表示数据操作不是原子的，线程之间也不是有序的
    //    volatile count: 1
    //    volatile count: 5
    //    volatile count: 4
    //    volatile count: 3
    //    volatile count: 1
    //    volatile count: 2
    //    volatile count: 6
    //    volatile count: 7
    //    volatile count: 8
    //    volatile count: 9


    private static void atomicCount() {
        for (int i = 0; i < 10; i++) {
            Executors.newFixedThreadPool(3).execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 通过使用Atomic包中的原子类保证数据操作是原子的（数据没有重复，表示是原子操作），但是不能保障有序性
                    System.out.println("atomic count: " + atomicCount.incrementAndGet());
                }
            });
        }
    }
    //    打印结果：虽然顺序错乱，但是数据没有重复，也就说明保障了数据的操作是原子的，但是线程间不是有序的
    //    atomic count: 1
    //    atomic count: 2
    //    atomic count: 3
    //    atomic count: 5
    //    atomic count: 8
    //    atomic count: 9
    //    atomic count: 10
    //    atomic count: 4
    //    atomic count: 7
    //    atomic count: 6


    private static void synchronizedCount() {
        for (int i = 0; i < 50; i++) {
            Executors.newFixedThreadPool(5).execute(new Runnable() {
                @Override
                public void run() {
                    synchronized (TestConcurrencyActivity.class) {  // 通过synchronized关键字来保证线程之间的有序性
                        System.out.println("synchronized count: " + ++synchronizedCount);
                    }
                }
            });
        }
    }
    //    打印结果：没有重复数据，也没有错乱现象，说明数据操作是原子的，同时线程操作也是顺序的。同时也说明，有序性可以保障数据操作的原子性
    //    synchronized count: 1
    //    synchronized count: 2
    //    synchronized count: 3
    //    synchronized count: 4
    //    synchronized count: 5
    //    synchronized count: 6
    //    synchronized count: 7
    //    synchronized count: 8
    //    synchronized count: 9
    //    synchronized count: 10
}
