public class Main {

    public static void main(String[] args) {
        Service service = new Service();
        ThreadA a = new ThreadA(service);
        ThreadB b = new ThreadB(service);
        ThreadC c = new ThreadC(service);
        a.start();
        b.start();
        c.start();
    }
}

class ThreadA extends Thread {
    private Service service;

    public ThreadA(Service service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.printA();
    }
}

class ThreadB extends Thread {
    private Service service;

    public ThreadB(Service service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.printB();
    }
}

class ThreadC extends Thread {
    private Service service;

    public ThreadC(Service service) {
        super();
        this.service = service;
    }

    @Override
    public void run() {
        service.printC();
    }
}

class Service {
    public static final Object lock = new Object();
    private static Integer c = 0;
    private Integer d = 0;
    private Object l = new Object();

    public void printA() {
        synchronized (lock) {
            try {
                System.out.println(Thread.currentThread().getName() + "进入A");
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getName() + "离开A");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void printB() {
        synchronized (c) {
            try {
                System.out.println(Thread.currentThread().getName() + "进入B");
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getName() + "离开B");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    synchronized public void printC() {
        try {
            System.out.println(Thread.currentThread().getName() + "进入C");
            Thread.sleep(3000);
            System.out.println(Thread.currentThread().getName() + "离开C");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
