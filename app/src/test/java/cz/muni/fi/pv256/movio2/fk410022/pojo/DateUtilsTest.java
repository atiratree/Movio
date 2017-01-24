package cz.muni.fi.pv256.movio2.fk410022.pojo;

import android.support.v4.util.Pair;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.muni.fi.pv256.movio2.fk410022.network.MovieDbClient;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {Calendar.class, DateUtils.class})
public class DateUtilsTest {
    private static final Calendar now = Calendar.getInstance();

    @Before
    public void init() throws Exception {
        now.set(2017, Calendar.APRIL, 5);
        mockCalendar();
    }

    @Test
    public void convertToReadableString() throws Exception {
        Assert.assertEquals(DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(now.getTime()),
                DateUtils.convertToReadableString(now.getTime()));
    }

    @Test
    public void convertToDate() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));

        Assert.assertEquals(calendar.getTime(), DateUtils.convertToDate("2017-04-05"));
    }

    @Test
    public void convertToString() throws Exception {
        Assert.assertEquals("2017-04-05", DateUtils.convertToString(now.getTime()));
    }

    @Test
    public void getCurrentYearAsInt() throws Exception {
        Assert.assertEquals(now.get(Calendar.YEAR), DateUtils.getCurrentYearAsInt());
    }

    @Test
    public void getCurrentYear() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(now.get(Calendar.YEAR), Calendar.JANUARY, 1);
        Assert.assertEquals(calendar.getTime(), DateUtils.getCurrentYear());
    }

    @Test
    public void getToday() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
        Assert.assertEquals(calendar.getTime(), DateUtils.getToday());
    }

    @Test
    public void getNewMoviesMonthsBack() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE));
        calendar.add(Calendar.MONTH, -MovieDbClient.NEW_MOVIES_MONTHS_BACK);

        Assert.assertEquals(calendar.getTime(), DateUtils.getNewMoviesMonthsBack());
    }

    @Test
    public void getCurrentYearInterval() throws Exception {
        Calendar from = Calendar.getInstance();
        from.clear();
        from.set(now.get(Calendar.YEAR), Calendar.JANUARY, 1);

        Calendar to = (Calendar) from.clone();
        to.set(Calendar.YEAR, now.get(Calendar.YEAR) + 1);
        Assert.assertEquals(new Pair<>(from.getTime().getTime(), to.getTime().getTime()), DateUtils.getCurrentYearInterval());
    }

    private void mockCalendar() throws InterruptedException {
        PowerMockito.mockStatic(Calendar.class);
        PowerMockito.when(Calendar.getInstance()).thenAnswer(new Answer<Calendar>() {
            @Override
            public Calendar answer(InvocationOnMock invocation) throws Throwable {
                return getCalendarInstance();
            }
        });
        Thread.sleep(100);
    }

    private Calendar getCalendarInstance() {
        return (Calendar) now.clone();
    }
}
