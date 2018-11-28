# micronaut scheduler settings demo

This repo is to test executors settings in application.yml to limit IO scheduler.

# Update: Fix

The fix was to specify the executor to use, the micronaut config does not update
the `Schedulers.io()` pool, but its own version:

    class Controller(@Named(TaskExecutors.IO) private val executionService: ExecutorService) {

        ...
        return Maybe
                .just(id)
                .subscribeOn(Schedulers.from(executionService))


# Setup

Run the application:

    ./gradlew :app:run

Run apache bench test with the following:

    $ ab -r -c 10 -n 50 http://127.0.0.1:8080/api/hello

## Output with specified micronaut pool

    08:42:11.503 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.509 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.509 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.510 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.510 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.510 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.510 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.510 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.510 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.510 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.511 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.511 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.511 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.511 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.511 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.511 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.511 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.511 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.511 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:11.512 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    08:42:12.011 [pool-1-thread-5] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-5
    08:42:12.011 [pool-1-thread-11] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-11
    08:42:12.011 [pool-1-thread-13] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-13
    08:42:12.011 [pool-1-thread-10] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-10
    08:42:12.011 [pool-1-thread-6] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-6
    08:42:12.011 [pool-1-thread-12] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-12
    08:42:12.011 [pool-1-thread-7] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-7
    08:42:12.011 [pool-1-thread-4] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-4
    08:42:12.011 [pool-1-thread-9] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-9
    08:42:12.011 [pool-1-thread-8] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-8
    08:42:12.534 [pool-1-thread-13] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-13
    08:42:12.534 [pool-1-thread-10] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-10
    08:42:12.534 [pool-1-thread-6] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-6
    08:42:12.534 [pool-1-thread-9] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-9
    08:42:12.534 [pool-1-thread-5] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-5
    08:42:12.534 [pool-1-thread-4] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-4
    08:42:12.534 [pool-1-thread-8] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-8
    08:42:12.534 [pool-1-thread-7] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-7
    08:42:12.534 [pool-1-thread-12] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-12
    08:42:12.534 [pool-1-thread-11] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: pool-1-thread-11

Correctly limiting the number of threads according to the nThreads value in the configuration (here, from 4-13).

## Output when running with unbounded pool

    11:20:17.907 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    11:20:17.908 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    11:20:17.908 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    11:20:17.908 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    11:20:17.908 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    11:20:17.908 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    11:20:17.908 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    11:20:17.908 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    11:20:17.908 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    11:20:17.908 [nioEventLoopGroup-1-2] INFO  mn.Controller - getId: on thread nioEventLoopGroup-1-2
    11:20:18.408 [RxCachedThreadScheduler-1] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: RxCachedThreadScheduler-1
    11:20:18.408 [RxCachedThreadScheduler-5] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: RxCachedThreadScheduler-5
    11:20:18.408 [RxCachedThreadScheduler-8] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: RxCachedThreadScheduler-8
    11:20:18.408 [RxCachedThreadScheduler-9] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: RxCachedThreadScheduler-9
    11:20:18.408 [RxCachedThreadScheduler-3] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: RxCachedThreadScheduler-3
    11:20:18.408 [RxCachedThreadScheduler-6] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: RxCachedThreadScheduler-6
    11:20:18.408 [RxCachedThreadScheduler-4] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: RxCachedThreadScheduler-4
    11:20:18.409 [RxCachedThreadScheduler-7] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: RxCachedThreadScheduler-7
    11:20:18.408 [RxCachedThreadScheduler-2] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: RxCachedThreadScheduler-2
    11:20:18.409 [RxCachedThreadScheduler-10] INFO  mn.Controller - doOnEvent called success: hello, throwable: null, thread: RxCachedThreadScheduler-10
