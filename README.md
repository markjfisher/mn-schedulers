# micronaut scheduler settings demo

This repo is to test executors settings in application.yml to limit IO scheduler.

# Setup

Run the application:

    ./gradlew :app:run

Run apache bench test with the following:

    $ ab -r -c 10 -n 50 http://127.0.0.1:8080/api/hello

The reported output looks like:

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

However, the nThreads value should limit the pool to a fixed size of 5:

    micronaut:
      executors:
        io:
          type: fixed
          nThreads: 5

as per https://docs.micronaut.io/latest/guide/index.html#reactiveServer

but logging shows `RxCachedThreadScheduler-6` to `RxCachedThreadScheduler-10`, expanding to meet the
concurrency setting in the Apache Bench command `"-c <concurrency>"`.

This and running with concurrency 1000, for 50000 requests (`"-c 1000 -n 50000"`) raises the thread count to over 1000, showing it doesn't
appear to be bounded.

Setting the value for `micronaut.server.netty.worker.threads` does work (nioEventLoopGroup thread reported
in the output increases to the set value), illustrating the application.yml file is at least being read.

Additionally, after running the above, I also tried adding the following

      n-threads: 5
      number-of-threads: 5

to the io executor similar to the table at https://docs.micronaut.io/latest/guide/index.html#scheduling

However, this didn't change the number of threads used.
