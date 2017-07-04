package application;

public class Combinations {
	
	public static int[][] combinations(int n, int k) {//subsets of size k from [0,n-1]
	    int c = (int) binomial(n, k);
	    int[][] res = new int[c][Math.max(0, k)];
	    int[] ind = k < 0 ? null : new int[k];
	    for (int i = 0; i < k; ++i) { ind[i] = i; }
	    for (int i = 0; i < c; ++i) {
	        for (int j = 0; j < k; ++j) {
	            res[i][j] = ind[j];
	        }
	        int x = ind.length - 1;
	        boolean loop;
	        do {
	            loop = false;
	            ind[x] = ind[x] + 1;
	            if (ind[x] > n - (k - x)) {
	                x--;
	                loop = x >= 0;
	            } else {
	                for (int x1 = x + 1; x1 < ind.length; ++x1) {
	                    ind[x1] = ind[x1 - 1] + 1;
	                }
	            }
	        } while (loop);
	    }
	    return res;
	}
	
	public static long binomial(int n, int k) {
	    if (k < 0 || k > n) return 0;
	    if (k > n - k) {    // take advantage of symmetry
	        k = n - k;
	    }
	    long c = 1;
	    for (int i = 1; i < k+1; ++i) {
	        c = c * (n - (k - i));
	        c = c / i;
	    }
	    return c;
	}
}
