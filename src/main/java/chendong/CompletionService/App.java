package chendong.CompletionService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		main();
	}

	private static void main() {

		List<Bean> listBean = new ArrayList<Bean>();
		int zrateNum = 5;// 启动几个线程任务
		ExecutorService threadPool2 = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());// 获取到cpu数量,代表同时执行的线程数
		List<Future<Bean>> futures = new ArrayList<Future<Bean>>(zrateNum);
		CompletionService<Bean> completionService = new ExecutorCompletionService<Bean>(threadPool2);
		
		for (int i = 0; i < zrateNum; i++) {
			futures.add(completionService.submit(new ThreadBean(i)));
		}
		try {
            for (int t = 0; t < futures.size(); t++) {
                Future<Bean> result = completionService.poll(3000, TimeUnit.MILLISECONDS);
                if (result == null) {
                    //So lets cancel the first futures we find that havent completed
                    for (Future future : futures) {
                        if (future.isDone()) {
                            continue;
                        }
                        else {
                            future.cancel(true);
                            break;
                        }
                    }
                    continue;
                }
                else {
                    try {
                        if (result.isDone() && !result.isCancelled() && result.get()!=null) {
                        	listBean.add(result.get());
                        }
                        else {
                            continue;
                        }
                    }
                    catch (ExecutionException ee) {
                        ee.printStackTrace(System.out);
                    }
                }
            }
        }
        catch (InterruptedException ie) {
        }
        finally {
            //Cancel by interrupting any existing tasks currently running in Executor Service
            for (Future<Bean> f : futures) {
                f.cancel(true);
            }
            threadPool2.shutdown();
        }
		
		
		print(listBean);
	
	}

	private static void print(List<Bean> listBean) {
		for (int i = 0; i < listBean.size(); i++) {
			Bean bean = listBean.get(i);
			System.out.println(bean.getThreadNum()+"-->"+bean.getRandom());
		}
	}
}
