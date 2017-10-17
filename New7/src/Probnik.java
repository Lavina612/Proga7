import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Probnik {
    public static void main (String [] args) {
        ZonedDateTime zdt = ZonedDateTime.now();
        System.out.println(zdt);
        zdt = zdt.withZoneSameInstant(ZoneId.of("Canada/Central"));
        String str = zdt.toString();
      //  zdt = ZonedDateTime.parse(str);
      //  ZonedDateTime z = ZonedDateTime.parse("Ghb");
        System.out.println(zdt);
        Locale loc = new Locale("ru","RU");
        ResourceBundle mess = ResourceBundle.getBundle("texts/123_ru", loc);
        System.out.println(mess.getString("greeting"));
        System.out.println(mess.getString("message"));
        System.out.println(mess.getString("farewell"));
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.KOREA);
        String result = format.format(499.99);
        System.out.println(result);
        FormatStyle style1 = FormatStyle.MEDIUM;
        DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedDateTime(style1, style1);
        System.out.println(dtf);
    }
}
