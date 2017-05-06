package ProgramsPorblems;
import static java.lang.Thread.currentThread; 
import java.util.concurrent.TimeUnit; 
/** * Java Program to demonstrate how to pause a thread in Java. *
 *  There is no pause method, but there are multiple way to pause * 
 *  a thread for short period of time. Two popular way is either *
 *   by using Thread.sleep() method or TimeUnit.sleep() method. * * @author java67 */
public class ThreadPauseDemo 
{
	public static void main(String args[]) throws InterruptedException 
	{ 
Game game = new Game(); 
Thread t1 = new Thread(game, "T1"); 
t1.start(); 
//Now, let's stop our Game thread 
System.out.println(currentThread().getName() + " is stopping game thread"); 
game.stop(); 
//Let's wait to see game thread stopped
 TimeUnit.MILLISECONDS.sleep(200);
 System.out.println(currentThread().getName() + " is finished now");
 } 
}
 class Game implements Runnable{ 
private volatile boolean isStopped = false;
 public void run() {
 while(!isStopped)
{ System.out.println("Game thread is running....."); 
System.out.println("Game thread is now going to pause"); 
try { Thread.sleep(200); } 
catch (InterruptedException e)
 {
 e.printStackTrace();
 } 

System.out.println("Game thread is now resumed ..");
 } 
System.out.println("Game thread is stopped....");
 } public void stop(){ 
isStopped = true; 
}
 }
