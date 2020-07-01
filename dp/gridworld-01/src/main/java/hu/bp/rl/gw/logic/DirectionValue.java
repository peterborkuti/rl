package hu.bp.rl.gw.logic;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class DirectionValue {
	public final String sign;
	public final BigDecimal value;
}
