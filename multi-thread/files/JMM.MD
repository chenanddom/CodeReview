JMM数据的原子操作
* read(读取):从主内存读取数据
* load(载入):将主内存读取到的数据写入到工作内存
* use(使用):从工作内存读取数据来计算
* assign(赋值):将计算好的值重新赋值到工作内存中
* store(存储):将工作内存的数据写入主内存
* write(写入):将store过去的变量值赋值给主内存中的变量
* lock(锁定):将主内存变量加锁，标识为线程独占状态。
* unlock(解锁):将主内存变量解锁，解锁后其他的线程可以锁定变量


缓存一致性协议(MESI)
	多个CPU从主内存读取同一个数据到各自的高速缓存，当其中某个cpu修改了缓存里的树，该数据会马上同步会主内存，
	其他的cpu通过总线嗅探机制可以感知到数据变换从而将自己缓存里的数据失效

缓存加锁
	缓存的核心机制是基于缓存一致性协议来实现的，一个处理器的缓存回写到内存会导致其他处理器的缓存无效，
	IA-32和Intel 64处理器使用MESI实现缓存一致性协议




Volatile缓存可见性的实现原理
	底层实现主要是通过汇编lock前缀指令，它会锁定这块内存区域的缓存(缓存行锁定)并回写到主内存
	IA-32和Intel 64架构的软件开发者手册对lock指令的解释如下:
	1. 会将当前处理器缓存行的数据立即写会到系统内存
	2. 这个写回内存的操作会引起在其他CPU里缓存了该内存的地址的数据无效(MESI协议)
	3. 提供了内存屏障给你，是lock前后指令不能重排。
	
Java的程序的汇编代码查看如下：
-server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:CompileCommand=compileonly,*VolatileTest.prepareData
注意：需要安装hsdis-amd64.dll
			  hsdis-smd64.lib 		



指令重排序遵循的原则:
as-if-serial与happens-before
as-if-serial语义的意思是:不管怎么重排序(编译器和处理器维克提高并行度)，(单线程)程序的执行结果不能被改变。编译器、runtime和处理器都必须遵守as-if-serial语义。
为了遵守as-if-serial语义，编译器和处理器不回对存在数据依赖关系的操作做重排序，因为这种重排序会该百年执行结果。但是，如果操作之间不存在树依赖关系，这些擦欧总可以被编译器和处理器重排序。


JVM规范定义的内存屏障

| 屏障类型 | 指令示例 | 说明 |
| ---- | ---- | ---- |
| LoadLoad | Load1;LoadLoad;Load2 |保证load1的读取擦欧总在load2以及后续操作之后执行|
| StoreStore | Store1;StoreStore;Store |在store2及其后的写操作执行前，保证store的写操作已刷新到主内存 |
| LoadStore |Load1;LoadStore;Store2 | 在store2及其后的写操作执行前，保证load1读操作已经读取结束 |
| StoreLoad| Store1;StoreLoad;Load2 |保证store1的写操作已刷新到主内存之后，load2及其后的读操作才能执行 |



























