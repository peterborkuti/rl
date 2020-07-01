package hu.bp.rl.gw;

import hu.bp.rl.gw.logic.DynamicProgramming;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class Main {
	public static void main(String[] args) {
		DynamicProgramming dp = new DynamicProgramming(50, new BigDecimal(0.7D));

		BigDecimal delta = new BigDecimal(1000);
		BigDecimal stop = new BigDecimal("0.000000000000000000000000001");
		int i = 0;
		while (delta.compareTo(stop) == 1) {
			delta = dp.improvePolicy();
			i++;
			System.out.println(i + "|" +  delta);
		}
		System.out.println( i + "|" + delta + "************************");
		System.out.println(dp);
	}
}
