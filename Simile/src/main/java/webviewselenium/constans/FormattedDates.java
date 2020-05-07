package webviewselenium.constans;

import java.time.LocalDateTime;

/**
 * Class contains methods that allow to get formatted date/time/datetime.
 * All possible format should stored in one place - it will facilitate reuse.
 */
public class FormattedDates {

	public static String getFormattedCurrentDatetime() {
		return LocalDateTime.now().getDayOfMonth() + "-" + LocalDateTime.now().getMonthValue() + "-"
				+ LocalDateTime.now().getYear() + "_" + LocalDateTime.now().getHour() + "-"
				+ LocalDateTime.now().getMinute() + "-" + LocalDateTime.now().getSecond();
	}
}
