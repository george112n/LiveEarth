
/**
 * @author 14walkerg
 * @date 8 Jan 2021
 * @time 17:49:01
 */
package LiveWeather;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TestyBoy
{

	public static void main(String[] args)
	{
		int iWeatherCode;
		LocalDateTime dSunrise = LocalDateTime.parse("2021-01-08T08:01:41");
		LocalDateTime dSunset = LocalDateTime.parse("2021-01-08T16:08:26");
		System.out.println();
		LocalTime dNow = LocalTime.parse("00:00:00");
		long l;
		for (int i = 0 ; i < 1440 ; i++)
		{
			dNow = dNow.plusMinutes(1);
			System.out.println("Time: "+dNow.toString());
			l = updateTimeSeasonal(dSunrise, dSunset, dNow);
		}
	//	l = updateTimeSeasonal(dSunrise, dSunset, dNow);
	}
	public static long updateTimeSeasonal(LocalDateTime sunrise, LocalDateTime sunset, LocalTime dNow)
	{
		long lSeasonalTime;
		long lSeasonalNightTime;
		long militaryTime;
		final float mcSunLight = 14076F;
		final float mcSunRise = 22967F;
		float fractionOfDaylightComplete;
		float fractionOfNightlightComplete;
		float fSunset = sunset.getHour()*60 + sunset.getMinute();
		float fSunrise = sunrise.getHour()*60 + sunrise.getMinute();
		
		//Daylight in minutes
		float fDaylight = 60*(sunset.getHour() - sunrise.getHour()) + sunset.getMinute() - sunrise.getMinute();
		
		float fMinutesAfterSunrise = ((dNow.getHour()-sunrise.getHour())*60 + (dNow.getMinute())-sunrise.getMinute());
		
		//Work out the minecraft ticks count
		fractionOfDaylightComplete = fMinutesAfterSunrise/fDaylight;
		lSeasonalTime = (long) (mcSunRise + fractionOfDaylightComplete * mcSunLight);
		
		if (fMinutesAfterSunrise < 0) //Sun not risen
		{
			if (lSeasonalTime < 22000)
			{
				float fMinutesOfNightAlg = fSunrise - 937 *(fDaylight/mcSunLight);
			//	System.out.println("Mins btween sunset + nghtalg: "+fMinutesOfNightAlg);
				fractionOfNightlightComplete = ((float)(dNow.getHour()*60 + dNow.getMinute())/ (fMinutesOfNightAlg));
				
			//	System.out.println(fractionOfNightlightComplete);
				lSeasonalNightTime = (long) (18000 + fractionOfNightlightComplete*4000);
				lSeasonalTime = lSeasonalNightTime;
			}
			else
			{
				
			}
		}
		else if (fMinutesAfterSunrise < fDaylight) //Sun still up
		{
			//Delt with
		}
		
		else //Sun has set but is before midnight
		{
			lSeasonalTime = lSeasonalTime - 24000;
			
			//If the seasonal time goes over 14000, use night algorith
			if (!(lSeasonalTime < 14000))
			{
				//Minutes of the day that the night algorithm begins
				float fMinutesOfNightAlg = fSunset + 957 *(fDaylight/mcSunLight);
				
				//Fration of night alg to midnight that is done
				fractionOfNightlightComplete = ((float)(dNow.getHour()*60 + dNow.getMinute())-(fMinutesOfNightAlg))/ (1440 - (fMinutesOfNightAlg));
				
				//Ticks
				lSeasonalNightTime = (long) (14000 + fractionOfNightlightComplete*4000);
				lSeasonalTime = lSeasonalNightTime;
			}
		}
		
	//	player.setPlayerTime(lSeasonalTime, false);
		
		lSeasonalTime = lSeasonalTime % 24000;
		System.out.println(lSeasonalTime);
		
		militaryTime = LocalTime.now().getHour()*100 + LocalTime.now().getMinute();
		return militaryTime;
	}
}
//End Class

//Created by Bluecarpet in London