package ag;

import java.util.Scanner;
import java.util.Vector;

/*@author ahmed */
public class AG {

    public static boolean end(Vector<process> p) {
        boolean end = true;
        for (process o : p) {
            if (o.burst > 0) {
                end = false;
                break;
            }
        }
        return end;
    }

    public static void main(String[] args) {
        int process_number;
        Scanner s = new Scanner(System.in);
        System.out.print("Process Number= ");
        process_number = s.nextInt();
        Vector<process> processes = new Vector<process>();
        for (int i = 0; i < process_number; i++) {
            process p = new process();
            System.out.print("Name: ");
            p.name = s.next();
            System.out.print("Burst time= ");
            p.burst = s.nextInt();
            System.out.print("Arrival time= ");
            p.arrival = s.nextInt();
            System.out.print("Quantume= ");
            p.quantum = s.nextInt();
            processes.add(p);
        }
        Vector<process> queue = new Vector<process>();
        process processed = null;
        int q = 0;
        Vector<Object> pr = new Vector<Object>();
        for (int i = 0; true; i++) {
            for (process p : processes) {
                if (p.arrival == i && processed == null) {
                    pr.add(i);
                    processed = p;
                    q = processed.quantum;
                } else if (p.arrival == i) {
                    queue.add(p);
                }
            }
            if (processed == null) {
                continue;
            }
/*************************************************************/
            for (process queue1 : queue) {
                queue1.wait++;
                queue1.ta++;
            }
            processed.ta++;
            processed.burst--;
            q--;
            pr.add(processed.name);
/*************************************************************/
            if (processed.burst == 0) {
                processed.quantum = 0;
                if (queue.size() > 0) {
                    pr.add(i + 1);
                    int b = queue.get(0).burst, ind = 0;
                    for (int j = 1; j < queue.size(); j++) {
                        if (queue.get(j).burst < b) {
                            b = queue.get(j).burst;
                            ind = j;
                        }
                    }
                    processed = queue.get(ind);
                    queue.remove(ind);
                    q = processed.quantum;
                } else {
                    processed = null;
                }
                for (process p : processes) {
                    System.out.print(p.quantum + " ");
                }
                System.out.println();
                if (end(processes)) {
                    pr.add(i + 1);
                    break;
                }
                continue;

            }
/****************************************************************/
            if (q == 0) {
                pr.add(i + 1);
                processed.quantum += 1;
                if (queue.size() > 0) {
                    int b = queue.get(0).burst, ind = 0;
                    for (int j = 1; j < queue.size(); j++) {
                        if (queue.get(j).burst < b) {
                            b = queue.get(j).burst;
                            ind = j;
                        }
                    }

                    queue.add(processed);
                    processed = queue.get(ind);
                    queue.remove(ind);
                }

                q = processed.quantum;
                for (process p : processes) {
                    System.out.print(p.quantum + " ");
                }
                System.out.println();
                continue;
            }
/***************************************************************/
            if (q <= processed.quantum / 2) {
                int b = processed.burst, ind = -1;
                for (int j = 0; j < queue.size(); j++) {
                    if (queue.get(j).burst < b) {
                        b = queue.get(j).burst;
                        ind = j;
                    }
                }
                if (ind != -1) {
                    pr.add(i + 1);
                    processed.quantum += q;
                    queue.add(processed);
                    processed = queue.get(ind);
                    queue.remove(ind);
                    q = processed.quantum;
                    for (process p : processes) {
                        System.out.print(p.quantum + " ");
                    }
                    System.out.println();
                }
            }

        }
        int wait = 0, ta = 0;
        System.out.println("Wait Time");
        for (process p : processes) {
            System.out.println(p.name + " " + p.wait);
            wait += p.wait;
        }
        System.out.println("Turnaround Time");
        for (process p : processes) {
            System.out.println(p.name + " " + p.ta);
            ta += p.ta;
        }
        System.out.println("Avarage Waiting Time=" + ((double) wait / processes.size()));
        System.out.println("Avarage Turnaround Time=" + ((double) ta / processes.size()));
        System.out.println("Order");
        for (int i = 0; i < pr.size(); i++) {
            if (i != pr.size() - 1 && pr.get(i) == pr.get(i + 1)) {
                continue;
            } else {
                System.out.print(pr.get(i) + " ");
            }
        }

    }

}
