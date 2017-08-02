import java.math.BigDecimal;
import java.math.RoundingMode;

public class Smooth {

	public static double bestSmooth(double... d) {
	    double v1 = 0.0;
	    double v5 = 1.0;
	    while (true) {
	        double v3 = (v1 + v5) / 2;
	        if (v3 == v1 || v3 == v5) return v3;
	        double s1 = smooth(v1, d);
	        double s3 = smooth(v3, d);
	        double s5 = smooth(v5, d);
	        if (s5 >= s3 && s5 >= s1) {
	            v1 = v3;
	        } else if (s1 >= s3 && s1 >= s5) {
	            v5 = v3;
	        } else {
	            double v2 = (v1 + v3) / 2;
	            double v4 = (v3 + v5) / 2;
	            double s2 = smooth(v2, d);
	            double s4 = smooth(v4, d);
	            if (s4 >= s3 && s4 >= s5) {
	                v1 = v3;
	                v3 = v4;
	            } else if (s2 >= s3 && s2 >= s1) {
	                v5 = v3;
	                v3 = v2;
	            } else if (s3 >= s2 && s3 >= s4) {
	                v1 = v2;
	                v5 = v4;
	            }
	        }
	    }
	}
	public static double smooth(double a, double... d) {
		double soma = 0.0;
		double potencia = 1.0;
		for (int i = d.length - 1; i >= 0; i--) {
			soma += a * potencia * d[i];
			potencia *= 1.0 - a;
		}
		return soma;
	}

	private static boolean ordered(double a, double b, double c) {
		return (a <= b && b <= c) || (c <= b && b <= a);
	}

	public static double smoothRatio(double total, double... d) {
		double minA = 0.0;
		double maxA = 1.0;
		double smoothMin = smooth(minA, d);
		double smoothMax = smooth(maxA, d);
		if (!ordered(smoothMin, total, smoothMax))
			throw new IllegalArgumentException();

		while (true) {
			if (total == smoothMin)
				return minA;
			if (total == smoothMax)
				return maxA;
			double mid = minA + (maxA - minA) / 2;
			if (mid == minA)
				return Math.ceil(smoothMin);
			if (mid == maxA)
				return Math.ceil(smoothMax);
			double smoothMid = smooth(mid, d);
			if (ordered(smoothMid, total, smoothMax)) {
				minA = mid;
				smoothMin = smoothMid;
			} else if (ordered(smoothMid, total, smoothMin)) {
				maxA = mid;
				smoothMax = smoothMid;
			}
		}
	}

	public static void main(String[] args){
		System.out.println();
		
		
		
		
		Double p = BigDecimal.valueOf((bestSmooth(120,69,133,169,59)))
			    .setScale(3, RoundingMode.HALF_UP)
			    .doubleValue(); //333,69,133,169,59,82,133,136,124,86,165,333,45,166,343,248,24,44,44

	    System.out.println("Melhor taxa: " + p + " - resultado: " + Math.round(smooth(0.468,44,44,25,248,343,166,45,333,165,86,124,136,133,82,59,169,133,69,120)));
	    System.out.println();
	}

}
